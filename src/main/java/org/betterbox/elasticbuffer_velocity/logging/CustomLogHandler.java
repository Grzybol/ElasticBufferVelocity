package org.betterbox.elasticbuffer_velocity.logging;

import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;
import org.slf4j.event.Level;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class CustomLogHandler extends Handler {
    private final LogBuffer logBuffer;
    private final PluginLogger logger;

    public CustomLogHandler(LogBuffer logBuffer, PluginLogger logger) {
        this.logBuffer = logBuffer;
        this.logger = logger;
    }

    @Override
    public void publish(LogRecord record) {
        if (record == null || record.getMessage() == null) {
            return;
        }

        try {
            String message = record.getMessage();
            //String level = String.valueOf(record.getLevel());
            String level = record.getLevel().getName();
            String pluginName = "Console";
            String transactionID = "N/A";
            String playerName = "N/A";
            String uuid = "N/A";
            // DEBUG: Logi przechwycone przez CustomLogHandler
            System.out.println("[ElasticBuffer_Velocity] Intercepted log: " + level + " - " + message);
            logger.log(Level.INFO, "Intercepted console log: " + level + " - " + message);

            // Przekazujemy logi konsoli do LogBuffer
            logBuffer.add(message, level, pluginName, System.currentTimeMillis(), transactionID, playerName, uuid, 0.0);
        }catch (Exception e) {
            logger.log(Level.ERROR, "Error while processing log record: " + e.getMessage());
        }
    }

    @Override
    public void flush() {
        // Opcjonalnie możemy dodać wymuszanie flushowania logów
        logBuffer.flush();
    }

    @Override
    public void close() throws SecurityException {
        // Można dodać logikę zwalniania zasobów, jeśli jest to wymagane
    }
}
