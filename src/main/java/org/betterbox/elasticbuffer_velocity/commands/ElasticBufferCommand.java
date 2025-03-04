package org.betterbox.elasticbuffer_velocity.commands;

import com.velocitypowered.api.command.CommandSource;
import org.betterbox.elasticbuffer_velocity.logging.LogBuffer;
import org.betterbox.elasticbuffer_velocity.logging.PluginLogger;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.slf4j.event.Level;

import java.util.List;

public class ElasticBufferCommand implements SimpleCommand {
    private final LogBuffer logBuffer;
    private final PluginLogger logger;

    public ElasticBufferCommand(LogBuffer logBuffer, PluginLogger logger) {
        this.logBuffer = logBuffer;
        this.logger = logger;
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (args.length == 0) {
            source.sendMessage(Component.text("Usage: /elasticbuffer <flush|status>"));
            return;
        }

        String subcommand = args[0].toLowerCase();
        switch (subcommand) {
            case "flush":
                logBuffer.flush();
                source.sendMessage(Component.text("Log buffer flushed to Elasticsearch."));
                logger.log(Level.INFO, "Log buffer manually flushed by command.");
                break;
            case "status":
                source.sendMessage(Component.text("Log buffer size: " + logBuffer.getBufferSize()));
                break;
            default:
                source.sendMessage(Component.text("Unknown command. Usage: /elasticbuffer <flush|status>"));
                break;
        }
    }
}

