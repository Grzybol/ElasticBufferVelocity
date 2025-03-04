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
import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;
import org.betterbox.elasticbuffer_velocity.logging.PluginLogger;
import org.betterbox.elasticbuffer_velocity.network.ElasticSender;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "elasticbuffer_velocity",
        name = "elasticbuffer_velocity",
        version = "1.0-SNAPSHOT"
        , description = "Minecraft Velocity Elasticsearch buffer plugin"
        , authors = {"Grzybol"}
)
public class Elasticbuffer_velocity {
    private final ProxyServer server;
    private final Logger logger;
    private final ConfigManager configManager;
    private final PluginLogger pluginLogger;
    private LogBuffer logBuffer;
    private final ElasticSender elasticSender;

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

        // Rejestracja eventów
        server.getEventManager().register(this, new PlayerEventListener(logBuffer, pluginLogger));
        server.getEventManager().register(this, new ProxyEventListener(logBuffer, pluginLogger));

        // Rejestracja komendy
        CommandManager commandManager = server.getCommandManager();
        commandManager.register("elasticbuffer", new ElasticBufferCommand(logBuffer, pluginLogger));

        pluginLogger.log(org.slf4j.event.Level.INFO, "ElasticBuffer is fully initialized!");
    }
}

