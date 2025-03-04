package org.betterbox.elasticbuffer_velocity.events;

import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;
import org.betterbox.elasticbuffer_velocity.logging.PluginLogger;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import org.slf4j.event.Level;
import java.util.UUID;
public class PlayerEventListener {
    private final LogBuffer logBuffer;
    private final PluginLogger logger;

    public PlayerEventListener(LogBuffer logBuffer, PluginLogger logger) {
        this.logBuffer = logBuffer;
        this.logger = logger;
    }

    @Subscribe
    public void onPlayerLogin(LoginEvent event) {
        Player player = event.getPlayer();
        String message = "Player " + player.getUsername() + " has logged in.";
        logBuffer.add(message, "INFO", "ElasticBuffer", System.currentTimeMillis(), UUID.randomUUID().toString(), player.getUsername(), player.getUniqueId().toString(), 0.0);
        logger.log(Level.DEBUG, message);
    }

    @Subscribe
    public void onPlayerDisconnect(DisconnectEvent event) {
        Player player = event.getPlayer();
        String message = "Player " + player.getUsername() + " has disconnected.";
        logBuffer.add(message, "INFO", "ElasticBuffer", System.currentTimeMillis(), UUID.randomUUID().toString(), player.getUsername(), player.getUniqueId().toString(), 0.0);
        logger.log(Level.DEBUG, message);
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = "Chat: [" + player.getUsername() + "] " + event.getMessage();
        logBuffer.add(message, "INFO", "ElasticBuffer", System.currentTimeMillis(), UUID.randomUUID().toString(), player.getUsername(), player.getUniqueId().toString(), 0.0);
        logger.log(Level.DEBUG, message);
    }
}

