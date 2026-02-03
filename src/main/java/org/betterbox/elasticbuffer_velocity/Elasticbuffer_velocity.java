package org.betterbox.elasticbuffer_velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.betterbox.elasticbuffer_velocity.commands.ElasticBufferCommand;
import org.betterbox.elasticbuffer_velocity.config.ConfigManager;
import org.betterbox.elasticbuffer_velocity.events.PlayerEventListener;
import org.betterbox.elasticbuffer_velocity.events.ProxyEventListener;
import org.betterbox.elasticbuffer_velocity.logging.*;
import org.betterbox.elasticbuffer_velocity.network.ElasticSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;



import java.nio.file.Path;
import java.util.logging.LogManager;

@Plugin(
        id = "elasticbuffer_velocity",
        name = "elasticbuffer_velocity",
        version = "1.0-SNAPSHOT"
        , description = "Minecraft Velocity Elasticsearch buffer plugin"
        , authors = {"Grzybol"}
)
public class Elasticbuffer_velocity {
    private final ProxyServer server;
    private Logger logger;
    private final ConfigManager configManager;
    private final PluginLogger pluginLogger;
    private LogBuffer logBuffer;
    private final ElasticSender elasticSender;

    @Inject
    public Elasticbuffer_velocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.configManager = new ConfigManager(dataDirectory);

        // 🔹 Tworzymy LogBuffer i PluginLogger
        this.logBuffer = new LogBuffer(configManager, null, dataDirectory);
        this.pluginLogger = new PluginLogger(logger, configManager, dataDirectory);
        this.elasticSender = new ElasticSender(configManager, pluginLogger);

        // Przekazujemy ElasticSender do LogBuffer
        this.logBuffer = new LogBuffer(configManager, elasticSender, dataDirectory);

        // PRZECHWYTYWANIE KONKRETNYCH LOGÓW Z SYSTEMU
        System.setOut(new ConsoleInterceptor(System.out, logBuffer));
        System.setErr(new ConsoleInterceptor(System.err, logBuffer));

        // ✅ **Przekierowanie JUL → SLF4J → nasz logBuffer**
        java.util.logging.LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        // 🔹 Rejestrujemy CustomLogHandler do przechwytywania logów Velocity
        java.util.logging.Logger rootLogger = java.util.logging.Logger.getLogger("");
        CustomLogHandler customLogHandler = new CustomLogHandler(logBuffer, pluginLogger);
        rootLogger.addHandler(customLogHandler);

        // 🔹 PRZECHWYTYWANIE LOGÓW SLF4J
        // Pobieramy główny SLF4J logger Velocity
        org.slf4j.Logger rootSlf4jLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);

        // Tworzymy nasz interceptor
        Slf4jInterceptor slf4jInterceptor = new Slf4jInterceptor(rootSlf4jLogger, logBuffer);



        pluginLogger.log(org.slf4j.event.Level.INFO, "SLF4J Log Interceptor registered!");
        Slf4jLogAdapter slf4jAdapter = new Slf4jLogAdapter(rootSlf4jLogger, msg -> {
            logBuffer.add(msg, "INFO", "Velocity", System.currentTimeMillis(), "N/A", "N/A", "N/A", 0.0);
        });

        pluginLogger.log(org.slf4j.event.Level.INFO, "SLF4J Log Adapter registered!");
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("ElasticBuffer has been enabled!");

        // Rejestracja eventów
        server.getEventManager().register(this, new PlayerEventListener(logBuffer, pluginLogger));
        server.getEventManager().register(this, new ProxyEventListener(logBuffer, pluginLogger));

        // Rejestracja komendy
        CommandManager commandManager = server.getCommandManager();
        commandManager.register("elasticbufferv", new ElasticBufferCommand(logBuffer, pluginLogger));

        pluginLogger.log(org.slf4j.event.Level.INFO, "ElasticBuffer is fully initialized!");
    }











    /*
    @Inject
    public Elasticbuffer_velocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.configManager = new ConfigManager(dataDirectory);
        // Najpierw inicjalizujemy LogBuffer
        this.logBuffer = new LogBuffer(configManager, null, dataDirectory);

        // Tworzymy PluginLogger i podpinamy do niego LogBuffer
        this.pluginLogger = new PluginLogger(logger, configManager, dataDirectory);

        // Teraz możemy poprawnie utworzyć ElasticSender, który używa PluginLoggera
        this.elasticSender = new ElasticSender(configManager, pluginLogger);

        // Na końcu poprawnie ustawiamy ElasticSender w LogBuffer
        this.logBuffer = new LogBuffer(configManager, elasticSender, dataDirectory);

    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("ElasticBuffer has been enabled!");
        // 🔹 Usunięcie wszystkich poprzednich JUL handlerów i przekierowanie do SLF4J
        java.util.logging.LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        pluginLogger.log(org.slf4j.event.Level.INFO, "Java Util Logging (JUL) fully redirected to SLF4J!");

        // 🔹 Pobieramy główny logger SLF4J
        org.slf4j.Logger rootSlf4jLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        pluginLogger.log(org.slf4j.event.Level.INFO, "Root SLF4J Logger Level: " + rootSlf4jLogger.isInfoEnabled());

        // Rejestracja eventów
        server.getEventManager().register(this, new PlayerEventListener(logBuffer, pluginLogger));
        server.getEventManager().register(this, new ProxyEventListener(logBuffer, pluginLogger));

        // Rejestracja komendy
        CommandManager commandManager = server.getCommandManager();
        commandManager.register("elasticbuffer", new ElasticBufferCommand(logBuffer, pluginLogger));

        // ✅ **Przekierowanie logów Java Logging (JUL) do SLF4J**
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        pluginLogger.log(org.slf4j.event.Level.INFO, "Java Util Logging (JUL) redirected to SLF4J!");

        // 🔹 Usuwamy domyślne handlery i rejestrujemy nasz CustomLogHandler
        java.util.logging.Logger rootLogger = java.util.logging.Logger.getLogger("");
        for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        CustomLogHandler customHandler = new CustomLogHandler(logBuffer, pluginLogger);
        rootLogger.addHandler(customHandler);
        pluginLogger.log(org.slf4j.event.Level.INFO, "CustomLogHandler registered globally!");



        pluginLogger.log(org.slf4j.event.Level.INFO, "SLF4J Log Adapter registered!");

        pluginLogger.log(org.slf4j.event.Level.INFO, "ElasticBuffer is fully initialized!");
    }

     */
}

