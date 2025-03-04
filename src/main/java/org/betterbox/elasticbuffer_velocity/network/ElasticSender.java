package org.betterbox.elasticbuffer_velocity.network;

import org.betterbox.elasticbuffer_velocity.config.ConfigManager;
import org.betterbox.elasticbuffer_velocity.logging.LogEntry;
import org.betterbox.elasticbuffer_velocity.logging.PluginLogger;
import org.slf4j.event.Level;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

public class ElasticSender {
    private final String elasticUrl;
    private final String apiKey;
    private final String indexPattern;
    private final boolean checkCerts;
    private final PluginLogger logger;
    private final ConfigManager configManager;

    public ElasticSender(ConfigManager configManager, PluginLogger logger) {
        this.elasticUrl = configManager.getConfig().webhookURL + ":" + configManager.getConfig().elasticsearch_port;
        this.apiKey = configManager.getConfig().apiKey;
        this.indexPattern = configManager.getConfig().index_pattern;
        this.checkCerts = configManager.getConfig().checkCerts;
        this.logger = logger;
        this.configManager = configManager;

        setupSSL();
    }

    private void setupSSL() {
        if (!checkCerts) {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() { return null; }
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                        }
                };
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                logger.log(Level.ERROR, "Error setting up SSL context: " + e.getMessage());
            }
        } else {
            try {
                KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                try (InputStream trustStoreIS = new FileInputStream(configManager.getConfig().truststore_path)) {
                    trustStore.load(trustStoreIS, configManager.getConfig().truststore_password.toCharArray());
                }
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(trustStore);
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            } catch (Exception e) {
                logger.log(Level.ERROR, "Failed to set SSL properties: " + e.getMessage());
            }
        }
    }

    public boolean sendLogs(List<LogEntry> logs) {
        try {
            logger.log(Level.INFO, "Preparing to send " + logs.size() + " log(s) to Elasticsearch...");

            URL url = new URL(elasticUrl + "/" + indexPattern + "/_bulk");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-ndjson; charset=UTF-8");
            if (configManager.getConfig().authorization) {
                connection.setRequestProperty("Authorization", "ApiKey " + apiKey);
            }
            connection.setDoOutput(true);

            StringBuilder bulkData = new StringBuilder();
            for (LogEntry log : logs) {
                bulkData.append(buildNdjsonChunk(log));
            }

            logger.log(Level.DEBUG, "NDJSON payload: \n" + bulkData);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = bulkData.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();

            logger.log(Level.INFO, "Elasticsearch response: " + responseCode + " - " + responseMessage);

            if (responseCode >= 200 && responseCode < 300) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "Error while sending logs: " + e.getMessage());
            return false;
        }
    }

    private String buildNdjsonChunk(LogEntry log) {
        return String.format("{\"index\":{}}\n{" +
                        "\"timestamp\":\"%s\"," +
                        "\"plugin\":\"ElasticBuffer\"," +
                        "\"transactionID\":\"%s\"," +
                        "\"level\":\"%s\"," +
                        "\"message\":\"%s\"," +
                        "\"playerName\":\"%s\"," +
                        "\"uuid\":\"%s\"," +
                        "\"serverName\":\"%s\"," +
                        "\"keyValue\":\"%.5f\"}" + "\n",
                log.getTimestamp(),
                log.getTransactionID(),
                log.getLevel(),
                sanitizeMessage(log.getMessage()),
                log.getPlayerName(),
                log.getUuid(),
                configManager.getConfig().serverName,
                log.getKeyValue()
        );
    }

    private String sanitizeMessage(String message) {
        return message.replace("\"", "");
    }
}
