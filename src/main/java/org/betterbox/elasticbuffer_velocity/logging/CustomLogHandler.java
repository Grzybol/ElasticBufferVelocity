package org.betterbox.elasticbuffer_velocity.logging;

import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Level;
public class CustomLogHandler extends Handler {
    private final LogBuffer logBuffer;

    public CustomLogHandler(LogBuffer logBuffer) {
        this.logBuffer = logBuffer;
    }

    @Override
    public void publish(LogRecord record) {
        if (record == null || record.getMessage() == null) {
            return;
        }

        String message = record.getMessage();
        //String level = String.valueOf(record.getLevel());
        String level = record.getLevel().getName();
        String pluginName = "Console";
        String transactionID = "N/A";
        String playerName = "N/A";
        String uuid = "N/A";

        // Przekazujemy logi konsoli do LogBuffer
        logBuffer.add(message, level, pluginName, System.currentTimeMillis(), transactionID, playerName, uuid, 0.0);
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
