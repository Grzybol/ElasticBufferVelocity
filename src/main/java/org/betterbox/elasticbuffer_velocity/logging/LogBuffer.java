package org.betterbox.elasticbuffer_velocity.logging;

import org.betterbox.elasticbuffer_velocity.config.ConfigManager;
import org.betterbox.elasticbuffer_velocity.network.ElasticSender;
import org.slf4j.event.Level;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
public class LogBuffer {
    private final List<LogEntry> buffer;
    private final int maxSize;
    private final ElasticSender elasticSender;
    private final File backupFile;

    public LogBuffer(ConfigManager configManager, ElasticSender elasticSender, Path dataFolder) {
        this.buffer = new LinkedList<>();
        this.maxSize = configManager.getConfig().logBufferSize;
        this.elasticSender = elasticSender;

        // Plik awaryjnego zapisu logów, jeśli nie można ich wysłać
        File logsFolder = new File(dataFolder.toFile(), "failed_logs");
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
        }
        String backupFileName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + "_failed.log";
        this.backupFile = new File(logsFolder, backupFileName);
    }

    public synchronized void add(String log, String level, String pluginName, long timestamp, String transactionID, String playerName, String uuid, double keyValue) {
        buffer.add(new LogEntry(log, level, pluginName, timestamp, transactionID, playerName, uuid, keyValue));
        if (buffer.size() >= maxSize) {
            flush();
        }
    }

    public synchronized List<LogEntry> getAndClear() {
        List<LogEntry> logsToSend = new LinkedList<>(buffer);
        buffer.clear();
        return logsToSend;
    }

    public void flush() {
        List<LogEntry> logsToSend = getAndClear();
        if (!logsToSend.isEmpty()) {
            boolean success = elasticSender.sendLogs(logsToSend);
            if (!success) {
                saveToBackup(logsToSend);
            }
        }
    }

    private void saveToBackup(List<LogEntry> logs) {
        try (FileWriter writer = new FileWriter(backupFile, true)) {
            for (LogEntry log : logs) {
                writer.write(log.toJson() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getBufferSize() {
        return buffer.size();
    }
}

