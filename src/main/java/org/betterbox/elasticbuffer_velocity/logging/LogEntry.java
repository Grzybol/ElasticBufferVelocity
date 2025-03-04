package org.betterbox.elasticbuffer_velocity.logging;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LogEntry {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String message;
    private final String level;
    private final String pluginName;
    private final long timestamp;
    private final String transactionID;
    private final String playerName;
    private final String uuid;
    private final double keyValue;

    public LogEntry(String message, String level, String pluginName, long timestamp, String transactionID, String playerName, String uuid, double keyValue) {
        this.message = message;
        this.level = level;
        this.pluginName = pluginName;
        this.timestamp = timestamp;
        this.transactionID = (transactionID != null) ? transactionID : "N/A";
        this.playerName = (playerName != null) ? playerName : "N/A";
        this.uuid = (uuid != null) ? uuid : "N/A";
        this.keyValue = keyValue;
    }

    public String getMessage() {
        return message;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getUuid() {
        return uuid;
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getLevel() {
        return level;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public double getKeyValue() {
        return keyValue;
    }

    public String toJson() {
        return GSON.toJson(this);
    }

    @Override
    public String toString() {
        return String.format("{\"level\": \"%s\", \"message\": \"%s\"}", level, message);
    }
}

