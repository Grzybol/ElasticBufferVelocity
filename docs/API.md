# ElasticBuffer Velocity - API Documentation

## Overview

ElasticBuffer Velocity is a Minecraft Velocity proxy plugin that buffers and sends logs to Elasticsearch. This plugin automatically captures logs from various sources including console output, SLF4J, and Java Util Logging (JUL), and forwards them to your Elasticsearch instance for centralized logging and analysis.

## Table of Contents

1. [Getting Started](#getting-started)
2. [Configuration](#configuration)
3. [Core API Components](#core-api-components)
4. [Using the LogBuffer API](#using-the-logbuffer-api)
5. [Log Entry Structure](#log-entry-structure)
6. [Integration Examples](#integration-examples)
7. [Elasticsearch Setup](#elasticsearch-setup)
8. [Troubleshooting](#troubleshooting)

---

## Getting Started

### Installation

1. Download the latest ElasticBuffer Velocity JAR file
2. Place it in your Velocity `plugins` folder
3. Start your Velocity proxy server
4. The plugin will generate a default `config.json` file in `plugins/elasticbuffer_velocity/`
5. Configure the plugin according to your Elasticsearch setup (see [Configuration](#configuration))
6. Restart the server or reload the plugin

### Quick Start

The plugin works automatically once configured. It will:
- Intercept all console logs
- Capture SLF4J and JUL logs
- Buffer logs in memory
- Send buffered logs to Elasticsearch when the buffer is full or on manual flush

---

## Configuration

The plugin uses a JSON configuration file located at `plugins/elasticbuffer_velocity/config.json`.

### Configuration Options

```json
{
  "log_level": ["INFO", "WARNING", "ERROR", "DEBUG"],
  "local": false,
  "authorization": true,
  "checkCerts": false,
  "elasticsearch_port": 9200,
  "webhookURL": "https://100.96.1.18",
  "apiKey": "YOUR_API_KEY",
  "index_pattern": "betterbox",
  "truststore_path": "pathToCertFromYourElasticServer",
  "truststore_password": "yourPassword",
  "serverName": "BetterBox",
  "highMemoryUsageThreshold": 80.0,
  "lowTPSThreshold": 18.0,
  "highCpuUsageThreshold": 85.0,
  "highDiskUsageThreshold": 90.0,
  "monitoringIntervalTicks": 1200,
  "logBufferSize": 100
}
```

### Configuration Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `log_level` | String[] | `["INFO", "WARNING", "ERROR", "DEBUG"]` | Log levels to capture |
| `local` | Boolean | `false` | Enable local mode (for testing) |
| `authorization` | Boolean | `true` | Use API key authorization |
| `checkCerts` | Boolean | `false` | Verify SSL certificates |
| `elasticsearch_port` | Integer | `9200` | Elasticsearch port |
| `webhookURL` | String | `"https://100.96.1.18"` | Elasticsearch server URL |
| `apiKey` | String | `"YOUR_API_KEY"` | Elasticsearch API key |
| `index_pattern` | String | `"betterbox"` | Elasticsearch index pattern |
| `truststore_path` | String | `"pathToCertFromYourElasticServer"` | Path to SSL truststore file |
| `truststore_password` | String | `"yourPassword"` | Truststore password |
| `serverName` | String | `"BetterBox"` | Server identifier in logs |
| `highMemoryUsageThreshold` | Double | `80.0` | Memory usage threshold (%) |
| `lowTPSThreshold` | Double | `18.0` | Low TPS threshold |
| `highCpuUsageThreshold` | Double | `85.0` | CPU usage threshold (%) |
| `highDiskUsageThreshold` | Double | `90.0` | Disk usage threshold (%) |
| `monitoringIntervalTicks` | Integer | `1200` | Monitoring interval in ticks |
| `logBufferSize` | Integer | `100` | Number of logs to buffer before sending |

---

## Core API Components

### LogBuffer

The `LogBuffer` class is the core component for managing and sending logs.

**Package:** `org.betterbox.elasticbuffer_velocity.logging.LogBuffer`

#### Methods

##### `add(String log, String level, String pluginName, long timestamp, String transactionID, String playerName, String uuid, double keyValue)`

Adds a log entry to the buffer.

**Parameters:**
- `log` (String): The log message
- `level` (String): Log level (INFO, WARNING, ERROR, DEBUG)
- `pluginName` (String): Name of the plugin generating the log
- `timestamp` (long): Unix timestamp in milliseconds
- `transactionID` (String): Transaction identifier (can be "N/A")
- `playerName` (String): Player name if applicable (can be "N/A")
- `uuid` (String): Player UUID if applicable (can be "N/A")
- `keyValue` (double): Numeric metric value

**Example:**
```java
logBuffer.add(
    "Player joined the server",
    "INFO",
    "MyPlugin",
    System.currentTimeMillis(),
    "TXN-12345",
    "PlayerName",
    "uuid-string",
    1.0
);
```

##### `flush()`

Manually flushes the buffer, sending all buffered logs to Elasticsearch immediately.

**Example:**
```java
logBuffer.flush();
```

##### `getBufferSize()`

Returns the current number of logs in the buffer.

**Returns:** `int` - Number of buffered logs

**Example:**
```java
int currentSize = logBuffer.getBufferSize();
```

##### `getAndClear()`

Gets all buffered logs and clears the buffer.

**Returns:** `List<LogEntry>` - List of buffered log entries

**Example:**
```java
List<LogEntry> logs = logBuffer.getAndClear();
```

---

### LogEntry

The `LogEntry` class represents a single log entry.

**Package:** `org.betterbox.elasticbuffer_velocity.logging.LogEntry`

#### Constructor

```java
public LogEntry(String message, String level, String pluginName, 
                long timestamp, String transactionID, String playerName, 
                String uuid, double keyValue)
```

#### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getMessage()` | String | Get log message |
| `getLevel()` | String | Get log level |
| `getPluginName()` | String | Get plugin name |
| `getTimestamp()` | long | Get timestamp |
| `getTransactionID()` | String | Get transaction ID |
| `getPlayerName()` | String | Get player name |
| `getUuid()` | String | Get player UUID |
| `getKeyValue()` | double | Get numeric metric |
| `toJson()` | String | Convert to JSON string |
| `toString()` | String | Get string representation |

---

### ElasticSender

The `ElasticSender` class handles sending logs to Elasticsearch.

**Package:** `org.betterbox.elasticbuffer_velocity.network.ElasticSender`

#### Methods

##### `sendLogs(List<LogEntry> logs)`

Sends a list of log entries to Elasticsearch using the bulk API.

**Parameters:**
- `logs` (List<LogEntry>): List of log entries to send

**Returns:** `boolean` - `true` if successful, `false` otherwise

**Example:**
```java
boolean success = elasticSender.sendLogs(logEntries);
if (!success) {
    // Logs will be saved to backup file
    logger.warn("Failed to send logs to Elasticsearch");
}
```

---

### PluginLogger

The `PluginLogger` class provides logging functionality with file output.

**Package:** `org.betterbox.elasticbuffer_velocity.logging.PluginLogger`

#### Methods

##### `log(Level level, String message)`

Logs a message both to file and to the Velocity console.

**Parameters:**
- `level` (org.slf4j.event.Level): Log level (INFO, WARN, ERROR, DEBUG, TRACE)
- `message` (String): Log message

**Example:**
```java
pluginLogger.log(Level.INFO, "Plugin initialized successfully");
pluginLogger.log(Level.ERROR, "An error occurred: " + errorMessage);
```

---

## Using the LogBuffer API

### Accessing the LogBuffer

To use the LogBuffer in your plugin, you need to access it from the main plugin instance. There are several approaches:

#### Option 1: Plugin Event Integration

Register event listeners that receive the LogBuffer instance:

```java
import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;

@Subscribe
public void onProxyInitialize(ProxyInitializeEvent event) {
    // Get LogBuffer instance (you'll need to implement a way to access it)
    LogBuffer logBuffer = getLogBufferFromElasticBufferPlugin();
    
    // Use the LogBuffer
    logBuffer.add(
        "My plugin initialized",
        "INFO",
        "MyCustomPlugin",
        System.currentTimeMillis(),
        "N/A",
        "N/A",
        "N/A",
        0.0
    );
}
```

#### Option 2: Direct Integration

Add ElasticBuffer as a dependency in your plugin and access its components:

```java
public class MyPlugin {
    private LogBuffer logBuffer;
    
    public void initializeLogging(LogBuffer elasticBuffer) {
        this.logBuffer = elasticBuffer;
    }
    
    public void logCustomEvent(String message) {
        logBuffer.add(
            message,
            "INFO",
            "MyPlugin",
            System.currentTimeMillis(),
            generateTransactionId(),
            null,
            null,
            0.0
        );
    }
}
```

---

## Log Entry Structure

### JSON Format

When sent to Elasticsearch, each log entry is formatted as NDJSON:

```json
{"index":{}}
{
  "timestamp": "1706918400000",
  "plugin": "ElasticBuffer",
  "transactionID": "TXN-12345",
  "level": "INFO",
  "message": "Player joined the server",
  "playerName": "Steve",
  "uuid": "069a79f4-44e9-4726-a5be-fca90e38aaf5",
  "serverName": "BetterBox",
  "keyValue": "1.00000"
}
```

### Field Descriptions

| Field | Type | Description |
|-------|------|-------------|
| `timestamp` | String (long) | Unix timestamp in milliseconds |
| `plugin` | String | Always "ElasticBuffer" for system logs |
| `transactionID` | String | Unique transaction identifier |
| `level` | String | Log level (INFO, WARNING, ERROR, DEBUG) |
| `message` | String | The log message (special characters sanitized) |
| `playerName` | String | Player name or "N/A" |
| `uuid` | String | Player UUID or "N/A" |
| `serverName` | String | Server name from configuration |
| `keyValue` | String (double) | Numeric metric value |

---

## Integration Examples

### Example 1: Logging Player Events

```java
import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;

public class PlayerEventHandler {
    private final LogBuffer logBuffer;
    
    public PlayerEventHandler(LogBuffer logBuffer) {
        this.logBuffer = logBuffer;
    }
    
    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        String playerName = event.getPlayer().getUsername();
        String uuid = event.getPlayer().getUniqueId().toString();
        
        logBuffer.add(
            "Player logged in: " + playerName,
            "INFO",
            "PlayerEvents",
            System.currentTimeMillis(),
            "LOGIN-" + uuid.substring(0, 8),
            playerName,
            uuid,
            1.0
        );
    }
}
```

### Example 2: Logging Performance Metrics

```java
import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;

public class PerformanceMonitor {
    private final LogBuffer logBuffer;
    
    public PerformanceMonitor(LogBuffer logBuffer) {
        this.logBuffer = logBuffer;
    }
    
    public void logMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        double usagePercent = (double) usedMemory / runtime.maxMemory() * 100;
        
        logBuffer.add(
            String.format("Memory usage: %.2f%%", usagePercent),
            "INFO",
            "PerformanceMonitor",
            System.currentTimeMillis(),
            "PERF-MEM",
            "N/A",
            "N/A",
            usagePercent
        );
    }
}
```

### Example 3: Custom Transaction Logging

```java
import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;
import java.util.UUID;

public class TransactionLogger {
    private final LogBuffer logBuffer;
    
    public TransactionLogger(LogBuffer logBuffer) {
        this.logBuffer = logBuffer;
    }
    
    public void logTransaction(String playerName, String uuid, 
                               String action, double amount) {
        String transactionId = "TXN-" + UUID.randomUUID().toString();
        
        logBuffer.add(
            String.format("Transaction: %s - Amount: %.2f", action, amount),
            "INFO",
            "EconomyPlugin",
            System.currentTimeMillis(),
            transactionId,
            playerName,
            uuid,
            amount
        );
    }
}
```

### Example 4: Error Logging with Context

```java
import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;
import org.slf4j.event.Level;

public class ErrorHandler {
    private final LogBuffer logBuffer;
    
    public ErrorHandler(LogBuffer logBuffer) {
        this.logBuffer = logBuffer;
    }
    
    public void handleError(Exception e, String context, String playerName) {
        String errorMessage = String.format(
            "Error in %s: %s - %s",
            context,
            e.getClass().getSimpleName(),
            e.getMessage()
        );
        
        logBuffer.add(
            errorMessage,
            "ERROR",
            "ErrorHandler",
            System.currentTimeMillis(),
            "ERR-" + System.currentTimeMillis(),
            playerName != null ? playerName : "N/A",
            "N/A",
            0.0
        );
        
        // Optionally flush immediately for critical errors
        logBuffer.flush();
    }
}
```

---

## Elasticsearch Setup

### Index Mapping

Create an index template for optimal log storage:

```json
PUT /_index_template/elasticbuffer_template
{
  "index_patterns": ["betterbox*"],
  "template": {
    "settings": {
      "number_of_shards": 1,
      "number_of_replicas": 1
    },
    "mappings": {
      "properties": {
        "timestamp": {
          "type": "date",
          "format": "epoch_millis"
        },
        "plugin": {
          "type": "keyword"
        },
        "transactionID": {
          "type": "keyword"
        },
        "level": {
          "type": "keyword"
        },
        "message": {
          "type": "text",
          "fields": {
            "keyword": {
              "type": "keyword",
              "ignore_above": 256
            }
          }
        },
        "playerName": {
          "type": "keyword"
        },
        "uuid": {
          "type": "keyword"
        },
        "serverName": {
          "type": "keyword"
        },
        "keyValue": {
          "type": "double"
        }
      }
    }
  }
}
```

### API Key Creation

Create an API key for the plugin:

```bash
POST /_security/api_key
{
  "name": "elasticbuffer-velocity",
  "role_descriptors": {
    "elasticbuffer-writer": {
      "cluster": ["monitor"],
      "index": [
        {
          "names": ["betterbox*"],
          "privileges": ["write", "create_index", "auto_configure"]
        }
      ]
    }
  }
}
```

Use the returned API key in your `config.json`.

### SSL Configuration

For production environments with SSL certificate verification:

1. Export your Elasticsearch certificate:
```bash
openssl s_client -connect your-elasticsearch:9200 -showcerts
```

2. Create a Java truststore:
```bash
keytool -import -alias elasticsearch -file elastic-cert.pem \
        -keystore truststore.jks -storepass yourPassword
```

3. Update your config:
```json
{
  "checkCerts": true,
  "truststore_path": "/path/to/truststore.jks",
  "truststore_password": "yourPassword"
}
```

---

## Troubleshooting

### Logs Not Appearing in Elasticsearch

1. **Check configuration**: Verify `webhookURL`, `elasticsearch_port`, and `apiKey` are correct
2. **Check network connectivity**: Ensure Velocity can reach Elasticsearch
3. **Review plugin logs**: Check `plugins/elasticbuffer_velocity/logs/` for errors
4. **Check failed logs**: Review `plugins/elasticbuffer_velocity/failed_logs/` for backup logs

### SSL Connection Issues

1. **Disable certificate checking** (for testing only):
```json
{
  "checkCerts": false
}
```

2. **Verify truststore**: Ensure the truststore path and password are correct
3. **Check Elasticsearch SSL configuration**: Verify Elasticsearch is configured for SSL

### Buffer Not Flushing

1. **Check buffer size**: Logs are sent when `logBufferSize` is reached
2. **Manual flush**: Use the `/elasticbufferv` command to manually flush logs
3. **Increase logging**: Ensure enough logs are being generated

### Performance Issues

1. **Increase buffer size**: Larger buffers reduce network calls
```json
{
  "logBufferSize": 500
}
```

2. **Reduce log levels**: Filter unnecessary logs
```json
{
  "log_level": ["WARNING", "ERROR"]
}
```

3. **Check Elasticsearch performance**: Ensure Elasticsearch can handle the ingestion rate

### Authorization Errors

1. **Verify API key**: Ensure the API key has write permissions
2. **Check authorization setting**:
```json
{
  "authorization": true,
  "apiKey": "your-valid-api-key"
}
```

---

## Command Reference

### `/elasticbufferv`

Manually flushes the log buffer to Elasticsearch.

**Permission:** Default (no special permission required)

**Usage:**
```
/elasticbufferv
```

**Output:**
```
[ElasticBuffer] Manually flushing log buffer...
[ElasticBuffer] X logs sent to Elasticsearch
```

---

## Best Practices

1. **Transaction IDs**: Use meaningful transaction IDs for tracking related events
2. **Buffer Size**: Set appropriate buffer size based on your logging volume
3. **Log Levels**: Use appropriate log levels (INFO for general events, ERROR for failures)
4. **Key Values**: Use keyValue field for numeric metrics that you want to aggregate
5. **Player Context**: Always include player information when applicable
6. **Error Handling**: Implement proper error handling and log failures appropriately
7. **Flush Strategy**: Don't flush too frequently to avoid performance issues

---

## Additional Resources

- [Elasticsearch Bulk API Documentation](https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-bulk.html)
- [Velocity Plugin Development](https://docs.papermc.io/velocity/dev/getting-started)
- [SLF4J Documentation](http://www.slf4j.org/manual.html)

---

## Support

For issues, feature requests, or contributions, please visit the GitHub repository:
https://github.com/Grzybol/ElasticBufferVelocity

---

**Version:** 1.0-SNAPSHOT  
**Last Updated:** 2026-02-03
