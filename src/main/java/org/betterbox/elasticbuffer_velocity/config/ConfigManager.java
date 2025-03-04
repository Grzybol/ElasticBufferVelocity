package org.betterbox.elasticbuffer_velocity.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File configFile;
    private ConfigData config;

    public ConfigManager(Path dataFolder) {
        this.configFile = new File(dataFolder.toFile(), "config.json");
        loadConfig();
    }

    public void loadConfig() {
        if (!configFile.exists()) {
            saveDefaultConfig();
        }
        try (FileReader reader = new FileReader(configFile)) {
            this.config = GSON.fromJson(reader, ConfigData.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDefaultConfig() {
        try {
            Files.createDirectories(configFile.getParentFile().toPath());
            this.config = new ConfigData();
            saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigData getConfig() {
        return config;
    }

    public static class ConfigData {
        public List<String> log_level = List.of("INFO", "WARNING", "ERROR", "DEBUG");
        public boolean local = false;
        public boolean authorization = true;
        public boolean checkCerts = false;
        public int elasticsearch_port = 9200;
        public String webhookURL = "https://100.96.1.18";
        public String apiKey = "YOUR_API_KEY";
        public String index_pattern = "betterbox";
        public String truststore_path = "pathToCertFromYourElasticServer";
        public String truststore_password = "yourPassword";
        public String serverName = "BetterBox";
        public double highMemoryUsageThreshold = 80.0;
        public double lowTPSThreshold = 18.0;
        public double highCpuUsageThreshold = 85.0;
        public double highDiskUsageThreshold = 90.0;
        public int monitoringIntervalTicks = 1200;
        public int logBufferSize = 100;
    }
}
