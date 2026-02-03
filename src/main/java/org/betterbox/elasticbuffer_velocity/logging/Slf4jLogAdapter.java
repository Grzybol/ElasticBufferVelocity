package org.betterbox.elasticbuffer_velocity.logging;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;

import java.util.function.Consumer;

public class Slf4jLogAdapter implements Logger {
    private final Logger originalLogger;
    private final Consumer<String> logConsumer;

    public Slf4jLogAdapter(Logger originalLogger, Consumer<String> logConsumer) {
        this.originalLogger = originalLogger;
        this.logConsumer = logConsumer;
    }

    private void handleLog(Level level, String message) {
        System.out.println("[ElasticBuffer SLF4J] " + level + " - " + message);
        logConsumer.accept("[" + level + "] " + message);
    }

    @Override
    public String getName() {
        return originalLogger.getName();
    }

    @Override
    public void info(String msg) {
        handleLog(Level.INFO, msg);
        originalLogger.info(msg);
    }

    @Override
    public void warn(String msg) {
        handleLog(Level.WARN, msg);
        originalLogger.warn(msg);
    }

    @Override
    public void error(String msg) {
        handleLog(Level.ERROR, msg);
        originalLogger.error(msg);
    }

    @Override
    public void debug(String msg) {
        handleLog(Level.DEBUG, msg);
        originalLogger.debug(msg);
    }

    @Override
    public void trace(String msg) {
        handleLog(Level.TRACE, msg);
        originalLogger.trace(msg);
    }

    // Implementacja brakujących metod
    @Override
    public void trace(String msg, Object arg) { originalLogger.trace(msg, arg); }
    @Override
    public void debug(String msg, Object arg) { originalLogger.debug(msg, arg); }
    @Override
    public void info(String msg, Object arg) { originalLogger.info(msg, arg); }
    @Override
    public void warn(String msg, Object arg) { originalLogger.warn(msg, arg); }
    @Override
    public void error(String msg, Object arg) { originalLogger.error(msg, arg); }

    @Override
    public void trace(String msg, Object arg1, Object arg2) { originalLogger.trace(msg, arg1, arg2); }
    @Override
    public void debug(String msg, Object arg1, Object arg2) { originalLogger.debug(msg, arg1, arg2); }
    @Override
    public void info(String msg, Object arg1, Object arg2) { originalLogger.info(msg, arg1, arg2); }
    @Override
    public void warn(String msg, Object arg1, Object arg2) { originalLogger.warn(msg, arg1, arg2); }

    @Override
    public void warn(String s, Throwable throwable) {

    }

    @Override
    public void error(String msg, Object arg1, Object arg2) { originalLogger.error(msg, arg1, arg2); }

    @Override
    public void trace(String msg, Object... arguments) { originalLogger.trace(msg, arguments); }

    @Override
    public void trace(String s, Throwable throwable) {

    }

    @Override
    public void debug(String msg, Object... arguments) { originalLogger.debug(msg, arguments); }

    @Override
    public void debug(String s, Throwable throwable) {

    }

    @Override
    public void info(String msg, Object... arguments) { originalLogger.info(msg, arguments); }

    @Override
    public void info(String s, Throwable throwable) {

    }

    @Override
    public void warn(String msg, Object... arguments) { originalLogger.warn(msg, arguments); }
    @Override
    public void error(String msg, Object... arguments) { originalLogger.error(msg, arguments); }

    @Override
    public void error(String s, Throwable throwable) {

    }

    @Override
    public boolean isTraceEnabled() { return originalLogger.isTraceEnabled(); }
    @Override
    public boolean isDebugEnabled() { return originalLogger.isDebugEnabled(); }
    @Override
    public boolean isInfoEnabled() { return originalLogger.isInfoEnabled(); }
    @Override
    public boolean isWarnEnabled() { return originalLogger.isWarnEnabled(); }
    @Override
    public boolean isErrorEnabled() { return originalLogger.isErrorEnabled(); }

    @Override
    public void trace(Marker marker, String msg) { originalLogger.trace(marker, msg); }
    @Override
    public void debug(Marker marker, String msg) { originalLogger.debug(marker, msg); }
    @Override
    public void info(Marker marker, String msg) { originalLogger.info(marker, msg); }
    @Override
    public void warn(Marker marker, String msg) { originalLogger.warn(marker, msg); }
    @Override
    public void error(Marker marker, String msg) { originalLogger.error(marker, msg); }

    @Override
    public void trace(Marker marker, String msg, Object arg) { originalLogger.trace(marker, msg, arg); }
    @Override
    public void debug(Marker marker, String msg, Object arg) { originalLogger.debug(marker, msg, arg); }
    @Override
    public void info(Marker marker, String msg, Object arg) { originalLogger.info(marker, msg, arg); }
    @Override
    public void warn(Marker marker, String msg, Object arg) { originalLogger.warn(marker, msg, arg); }
    @Override
    public void error(Marker marker, String msg, Object arg) { originalLogger.error(marker, msg, arg); }

    @Override
    public void trace(Marker marker, String msg, Object arg1, Object arg2) { originalLogger.trace(marker, msg, arg1, arg2); }
    @Override
    public void debug(Marker marker, String msg, Object arg1, Object arg2) { originalLogger.debug(marker, msg, arg1, arg2); }
    @Override
    public void info(Marker marker, String msg, Object arg1, Object arg2) { originalLogger.info(marker, msg, arg1, arg2); }
    @Override
    public void warn(Marker marker, String msg, Object arg1, Object arg2) { originalLogger.warn(marker, msg, arg1, arg2); }
    @Override
    public void error(Marker marker, String msg, Object arg1, Object arg2) { originalLogger.error(marker, msg, arg1, arg2); }

    @Override
    public void trace(Marker marker, String msg, Object... arguments) { originalLogger.trace(marker, msg, arguments); }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public void debug(Marker marker, String msg, Object... arguments) { originalLogger.debug(marker, msg, arguments); }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public void info(Marker marker, String msg, Object... arguments) { originalLogger.info(marker, msg, arguments); }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public void warn(Marker marker, String msg, Object... arguments) { originalLogger.warn(marker, msg, arguments); }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public void error(Marker marker, String msg, Object... arguments) { originalLogger.error(marker, msg, arguments); }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public boolean isTraceEnabled(Marker marker) { return originalLogger.isTraceEnabled(marker); }
    @Override
    public boolean isDebugEnabled(Marker marker) { return originalLogger.isDebugEnabled(marker); }
    @Override
    public boolean isInfoEnabled(Marker marker) { return originalLogger.isInfoEnabled(marker); }
    @Override
    public boolean isWarnEnabled(Marker marker) { return originalLogger.isWarnEnabled(marker); }
    @Override
    public boolean isErrorEnabled(Marker marker) { return originalLogger.isErrorEnabled(marker); }
}