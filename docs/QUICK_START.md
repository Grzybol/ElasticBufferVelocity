# ElasticBuffer Velocity - Quick Start Guide

This guide will help you quickly integrate with ElasticBuffer Velocity to send your plugin's logs to Elasticsearch.

## 5-Minute Integration

### Step 1: Add Dependency (Optional)

If you want compile-time access to ElasticBuffer classes, add it as a dependency:

**Maven:**
```xml
<dependency>
    <groupId>org.betterbox</groupId>
    <artifactId>elasticbuffer_velocity</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

### Step 2: Get LogBuffer Instance

Access the LogBuffer through your plugin:

```java
import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;

// You'll need to obtain the LogBuffer instance from ElasticBuffer plugin
// This typically happens through plugin messaging or dependency injection
private LogBuffer logBuffer;
```

### Step 3: Send Your First Log

```java
logBuffer.add(
    "Hello from my plugin!",           // message
    "INFO",                             // level: INFO, WARNING, ERROR, DEBUG
    "MyPlugin",                         // your plugin name
    System.currentTimeMillis(),         // timestamp
    "N/A",                              // transaction ID (optional)
    "N/A",                              // player name (optional)
    "N/A",                              // player UUID (optional)
    0.0                                 // numeric metric (optional)
);
```

## Common Use Cases

### Logging Player Join/Leave

```java
@Subscribe
public void onPlayerJoin(LoginEvent event) {
    String username = event.getPlayer().getUsername();
    String uuid = event.getPlayer().getUniqueId().toString();
    
    logBuffer.add(
        "Player joined: " + username,
        "INFO",
        "MyPlugin",
        System.currentTimeMillis(),
        "JOIN-" + uuid.substring(0, 8),
        username,
        uuid,
        1.0
    );
}
```

### Logging Errors

```java
try {
    // Your code
} catch (Exception e) {
    logBuffer.add(
        "Error: " + e.getMessage(),
        "ERROR",
        "MyPlugin",
        System.currentTimeMillis(),
        "ERR-" + System.currentTimeMillis(),
        "N/A",
        "N/A",
        0.0
    );
}
```

### Logging Metrics

```java
public void logServerPerformance(double tps) {
    logBuffer.add(
        "Server TPS: " + tps,
        tps < 18.0 ? "WARNING" : "INFO",
        "PerformanceMonitor",
        System.currentTimeMillis(),
        "PERF-TPS",
        "N/A",
        "N/A",
        tps
    );
}
```

## Configuration

ElasticBuffer automatically handles:
- ✅ Buffering logs
- ✅ Sending to Elasticsearch in batches
- ✅ Retrying failed requests
- ✅ Backing up to files if Elasticsearch is unavailable

You just need to configure Elasticsearch connection in `config.json`:

```json
{
  "webhookURL": "https://your-elasticsearch-server",
  "elasticsearch_port": 9200,
  "apiKey": "your-api-key",
  "index_pattern": "yourindex",
  "logBufferSize": 100
}
```

## Manual Flush

Force immediate sending of buffered logs:

```java
logBuffer.flush();
```

Or use the command in-game:
```
/elasticbufferv
```

## Parameters Explained

| Parameter | Required | Description | Example |
|-----------|----------|-------------|---------|
| message | ✅ | Your log message | "Player bought item" |
| level | ✅ | Log severity | INFO, WARNING, ERROR, DEBUG |
| pluginName | ✅ | Your plugin identifier | "EconomyPlugin" |
| timestamp | ✅ | When event occurred | `System.currentTimeMillis()` |
| transactionID | ❌ | Unique event ID | "TXN-12345" or "N/A" |
| playerName | ❌ | Player involved | Username or "N/A" |
| uuid | ❌ | Player UUID | UUID string or "N/A" |
| keyValue | ❌ | Numeric metric | Double value or 0.0 |

## What Happens to Your Logs?

1. Logs are added to an in-memory buffer
2. When buffer reaches `logBufferSize` (default 100), logs are sent to Elasticsearch
3. If Elasticsearch is unavailable, logs are saved to backup files
4. Logs can be queried and visualized in Kibana or other Elasticsearch tools

## Next Steps

- Read the [full API documentation](API.md) for advanced usage
- Set up [Elasticsearch index templates](API.md#index-mapping) for better performance
- Configure [SSL/TLS](API.md#ssl-configuration) for production
- Learn about [troubleshooting](API.md#troubleshooting) common issues

## Example Plugin Integration

```java
package com.example.myplugin;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;

@Plugin(id = "myplugin", name = "My Plugin", version = "1.0.0")
public class MyPlugin {
    private final ProxyServer server;
    private LogBuffer logBuffer;
    
    @Inject
    public MyPlugin(ProxyServer server) {
        this.server = server;
    }
    
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Get LogBuffer instance (implement based on your needs)
        // Option 1: Service injection
        // Option 2: Plugin messaging
        // Option 3: Shared static instance (not recommended)
        
        // Log initialization
        if (logBuffer != null) {
            logBuffer.add(
                "MyPlugin initialized successfully",
                "INFO",
                "MyPlugin",
                System.currentTimeMillis(),
                "INIT",
                "N/A",
                "N/A",
                1.0
            );
        }
    }
}
```

## Need Help?

- Check the [troubleshooting section](API.md#troubleshooting)
- Review plugin logs in `plugins/elasticbuffer_velocity/logs/`
- Check backup logs in `plugins/elasticbuffer_velocity/failed_logs/`
- Open an issue on GitHub

---

**Tip:** Start simple! Just log basic messages first, then gradually add transaction IDs, player context, and metrics as needed.
