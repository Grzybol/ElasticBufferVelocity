package org.betterbox.elasticbuffer_velocity.logging;

import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.event.Level;

public class Slf4jInterceptor implements Logger {
    private final Logger originalLogger;
    private final LogBuffer logBuffer;

    public Slf4jInterceptor(Logger originalLogger, LogBuffer logBuffer) {
        this.originalLogger = originalLogger;
        this.logBuffer = logBuffer;
    }

    private void handleLog(Level level, String message) {
        System.out.println("[DEBUG] SLF4J Intercepted: " + level + " - " + message);
        logBuffer.add(message, level.name(), "Velocity", System.currentTimeMillis(), "N/A", "N/A", "N/A", 0.0);
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
    public void info(String s, Object o) {

    }

    @Override
    public void info(String s, Object o, Object o1) {

    }

    @Override
    public void warn(String msg) {
        handleLog(Level.WARN, msg);
        originalLogger.warn(msg);
    }

    @Override
    public void warn(String s, Object o) {

    }

    @Override
    public void error(String msg) {
        handleLog(Level.ERROR, msg);
        originalLogger.error(msg);
    }

    @Override
    public void error(String s, Object o) {

    }

    @Override
    public void error(String s, Object o, Object o1) {

    }

    @Override
    public void debug(String msg) {
        handleLog(Level.DEBUG, msg);
        originalLogger.debug(msg);
    }

    @Override
    public void debug(String s, Object o) {

    }

    @Override
    public void debug(String s, Object o, Object o1) {

    }

    @Override
    public void trace(String msg) {
        handleLog(Level.TRACE, msg);
        originalLogger.trace(msg);
    }

    @Override
    public void trace(String s, Object o) {

    }

    @Override
    public void trace(String s, Object o, Object o1) {

    }

    // Metody dla parametrów (nie wszystkie zaimplementowane)
    @Override
    public void info(String msg, Object... arguments) { originalLogger.info(msg, arguments); }

    @Override
    public void info(String s, Throwable throwable) {

    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return false;
    }

    @Override
    public void info(Marker marker, String s) {

    }

    @Override
    public void info(Marker marker, String s, Object o) {

    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void info(Marker marker, String s, Object... objects) {

    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public void warn(String msg, Object... arguments) { originalLogger.warn(msg, arguments); }

    @Override
    public void warn(String s, Object o, Object o1) {

    }

    @Override
    public void warn(String s, Throwable throwable) {

    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    @Override
    public void warn(Marker marker, String s) {

    }

    @Override
    public void warn(Marker marker, String s, Object o) {

    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {

    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public void error(String msg, Object... arguments) { originalLogger.error(msg, arguments); }

    @Override
    public void error(String s, Throwable throwable) {

    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    @Override
    public void error(Marker marker, String s) {

    }

    @Override
    public void error(Marker marker, String s, Object o) {

    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void error(Marker marker, String s, Object... objects) {

    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public void debug(String msg, Object... arguments) { originalLogger.debug(msg, arguments); }

    @Override
    public void debug(String s, Throwable throwable) {

    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return false;
    }

    @Override
    public void debug(Marker marker, String s) {

    }

    @Override
    public void debug(Marker marker, String s, Object o) {

    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {

    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public void trace(String msg, Object... arguments) { originalLogger.trace(msg, arguments); }

    @Override
    public void trace(String s, Throwable throwable) {

    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return false;
    }

    @Override
    public void trace(Marker marker, String s) {

    }

    @Override
    public void trace(Marker marker, String s, Object o) {

    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {

    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {

    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {

    }

    @Override
    public boolean isInfoEnabled() { return originalLogger.isInfoEnabled(); }
    @Override
    public boolean isWarnEnabled() { return originalLogger.isWarnEnabled(); }
    @Override
    public boolean isErrorEnabled() { return originalLogger.isErrorEnabled(); }
    @Override
    public boolean isDebugEnabled() { return originalLogger.isDebugEnabled(); }
    @Override
    public boolean isTraceEnabled() { return originalLogger.isTraceEnabled(); }

    // Reszta metod SLF4J do zaimplementowania, jeśli potrzebne...
}
