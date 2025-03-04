package org.betterbox.elasticbuffer_velocity.logging;

import org.betterbox.elasticbuffer_velocity.config.ConfigManager;
import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PluginLogger {
    private final Logger logger;
    private final File logFile;

    public PluginLogger(Logger logger, ConfigManager configManager, Path dataFolder) {
        this.logger = logger;

        // Tworzenie folderu logs, jeśli nie istnieje
        File logsFolder = new File(dataFolder.toFile(), "logs");
        if (!logsFolder.exists()) {
            logsFolder.mkdirs();
        }

        // Tworzenie pliku logu z datą
        String logFileName = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date()) + ".log";
        this.logFile = new File(logsFolder, logFileName);
    }

    public void log(Level level, String message) {
        // Logowanie do pliku
        writeToFile(level, message);

        // Logowanie do Velocity
        switch (level) {
            case INFO -> logger.info(message);
            case WARN -> logger.warn(message);
            case ERROR -> logger.error(message);
            case DEBUG -> logger.debug(message);
            default -> logger.trace(message);
        }
    }

    private void writeToFile(Level level, String message) {
        try (FileWriter writer = new FileWriter(logFile, true)) {
            String logEntry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                    " [" + level.name() + "] " + message + "\n";
            writer.write(logEntry);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
