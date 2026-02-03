# ElasticBuffer Velocity Documentation

Welcome to the ElasticBuffer Velocity documentation! This directory contains comprehensive guides for developers who want to integrate their plugins with ElasticBuffer to send logs to Elasticsearch.

## 📚 Documentation Index

### [Quick Start Guide](QUICK_START.md)
**Start here!** A 5-minute guide to get your plugin sending logs to Elasticsearch.
- Quick integration steps
- Common use cases with examples
- Basic configuration
- Troubleshooting tips

### [API Documentation](API.md)
**Complete reference** for developers integrating with ElasticBuffer.
- Detailed API reference
- All configuration options
- Core components documentation
- Integration examples
- Elasticsearch setup guide
- Troubleshooting guide

## 🎯 What is ElasticBuffer Velocity?

ElasticBuffer Velocity is a Minecraft Velocity proxy plugin that:
- **Buffers** logs from multiple sources (console, SLF4J, JUL)
- **Batches** logs for efficient network usage
- **Sends** logs to Elasticsearch for centralized logging
- **Backs up** logs locally if Elasticsearch is unavailable
- **Provides** an API for other plugins to report custom logs

## 🚀 Quick Example

```java
logBuffer.add(
    "Player completed quest",           // message
    "INFO",                             // level
    "QuestPlugin",                      // plugin name
    System.currentTimeMillis(),         // timestamp
    "QUEST-001",                        // transaction ID
    "Steve",                            // player name
    "069a79f4-44e9-4726-a5be-fca90e38aaf5", // player UUID
    100.0                               // reward amount
);
```

## 📖 Documentation Structure

```
docs/
├── README.md           # This file - documentation index
├── QUICK_START.md      # 5-minute integration guide
└── API.md              # Complete API reference
```

## 🔧 Key Features for Developers

### Automatic Log Capture
ElasticBuffer automatically captures:
- ✅ Console output (System.out, System.err)
- ✅ SLF4J logs
- ✅ Java Util Logging (JUL)
- ✅ Velocity plugin logs

### Manual Log Submission
Your plugin can also submit custom structured logs:
- Player events (join, leave, actions)
- Transaction logs
- Performance metrics
- Error reports
- Custom business events

### Flexible Configuration
- Configurable buffer size
- Multiple log levels
- SSL/TLS support
- API key authentication
- Automatic retry and backup

### Developer-Friendly API
- Simple method calls
- Type-safe parameters
- Optional fields with sensible defaults
- Manual flush capability

## 🎓 Learning Path

**For New Users:**
1. Start with [QUICK_START.md](QUICK_START.md)
2. Copy one of the examples
3. Test with a simple log message
4. Gradually add more context (player info, metrics)

**For Advanced Integration:**
1. Read the complete [API.md](API.md)
2. Review all integration examples
3. Set up proper Elasticsearch mappings
4. Configure SSL/TLS for production
5. Implement custom transaction tracking

## 💡 Common Use Cases

### Game Events
```java
// Player achievements, quests, milestones
logBuffer.add("Quest completed: Dragon Slayer", "INFO", "QuestPlugin", 
              System.currentTimeMillis(), "QUEST-" + questId, 
              playerName, uuid, questReward);
```

### Economy Transactions
```java
// Track all financial transactions
logBuffer.add("Purchase: Diamond Sword", "INFO", "EconomyPlugin",
              System.currentTimeMillis(), transactionId,
              playerName, uuid, purchaseAmount);
```

### Performance Monitoring
```java
// Track server performance metrics
logBuffer.add("Server TPS: " + tps, "WARNING", "Monitor",
              System.currentTimeMillis(), "PERF-TPS",
              "N/A", "N/A", tps);
```

### Security Events
```java
// Log security-related events
logBuffer.add("Failed login attempt", "WARNING", "Security",
              System.currentTimeMillis(), "SEC-" + attemptId,
              attemptedUsername, "N/A", attemptCount);
```

### Error Tracking
```java
// Centralized error logging
logBuffer.add("Database connection failed: " + error, "ERROR", "Database",
              System.currentTimeMillis(), "ERR-" + errorId,
              "N/A", "N/A", 0.0);
```

## 🔍 What You'll Find in Each Document

### QUICK_START.md Contains:
- ⚡ 5-minute integration guide
- 📝 Code examples for common scenarios
- ⚙️ Basic configuration
- 🎯 Minimal code to get started

### API.md Contains:
- 📚 Complete API reference
- 🔧 All configuration options explained
- 🏗️ Architecture and components
- 💼 Advanced integration patterns
- 🐘 Elasticsearch setup instructions
- 🔍 Comprehensive troubleshooting

## 🛠️ System Requirements

- Minecraft Velocity 3.4.0 or higher
- Java 17 or higher
- Elasticsearch 7.x or 8.x
- Network connectivity to Elasticsearch

## 📊 Elasticsearch Integration

Logs are sent to Elasticsearch in the following format:

```json
{
  "timestamp": "1706918400000",
  "plugin": "YourPlugin",
  "transactionID": "TXN-12345",
  "level": "INFO",
  "message": "Your log message",
  "playerName": "PlayerName",
  "uuid": "player-uuid",
  "serverName": "YourServer",
  "keyValue": "123.45"
}
```

This structure allows for:
- Time-series analysis
- Player behavior tracking
- Performance monitoring
- Transaction tracing
- Error rate monitoring

## 🤝 Support & Contribution

- **Issues:** Report bugs or request features on GitHub
- **Discussions:** Ask questions in GitHub Discussions
- **Contributing:** Pull requests welcome!

## 📝 License

ElasticBuffer Velocity is part of the BetterBox project.

## 🔗 Related Resources

- [Elasticsearch Documentation](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)
- [Velocity Plugin Development](https://docs.papermc.io/velocity/dev/getting-started)
- [Kibana for Log Visualization](https://www.elastic.co/guide/en/kibana/current/index.html)
- [SLF4J Documentation](http://www.slf4j.org/manual.html)

---

**Ready to get started?** Head over to [QUICK_START.md](QUICK_START.md)!

**Need detailed info?** Check out [API.md](API.md)!

**Version:** 1.0-SNAPSHOT  
**Last Updated:** 2026-02-03
