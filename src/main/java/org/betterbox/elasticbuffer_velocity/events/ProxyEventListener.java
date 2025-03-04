package org.betterbox.elasticbuffer_velocity.events;

import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;
import org.betterbox.elasticbuffer_velocity.logging.PluginLogger;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.connection.PreLoginEvent;
import org.slf4j.event.Level;
import java.util.UUID;

public class ProxyEventListener {
    private final LogBuffer logBuffer;
    private final PluginLogger logger;

    public ProxyEventListener(LogBuffer logBuffer, PluginLogger logger) {
        this.logBuffer = logBuffer;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        String message = "Velocity proxy has started.";
        logBuffer.add(message, "INFO", "ElasticBuffer", System.currentTimeMillis(), UUID.randomUUID().toString(), "N/A", "N/A", 0.0);
        logger.log(Level.DEBUG, message);
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        String message = "Velocity proxy is shutting down.";
        logBuffer.add(message, "WARN", "ElasticBuffer", System.currentTimeMillis(), UUID.randomUUID().toString(), "N/A", "N/A", 0.0);
        logger.log(Level.DEBUG, message);
    }

    @Subscribe
    public void onPlayerPreLogin(PreLoginEvent event) {
        String message = "Pre-login attempt by: " + event.getUsername();
        logBuffer.add(message, "INFO", "ElasticBuffer", System.currentTimeMillis(), UUID.randomUUID().toString(), event.getUsername(), "N/A", 0.0);
        logger.log(Level.DEBUG, message);
    }

    @Subscribe
    public void onPlayerPostLogin(PostLoginEvent event) {
        String message = "Player " + event.getPlayer().getUsername() + " has successfully logged in through proxy.";
        logBuffer.add(message, "INFO", "ElasticBuffer", System.currentTimeMillis(), UUID.randomUUID().toString(), event.getPlayer().getUsername(), event.getPlayer().getUniqueId().toString(), 0.0);
        logger.log(Level.DEBUG, message);
    }
}


