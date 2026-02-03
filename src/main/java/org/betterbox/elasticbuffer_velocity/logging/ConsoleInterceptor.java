package org.betterbox.elasticbuffer_velocity.logging;

import java.io.PrintStream;

public class ConsoleInterceptor extends PrintStream {
    private final LogBuffer logBuffer;
    private final PrintStream original;

    public ConsoleInterceptor(PrintStream original, LogBuffer logBuffer) {
        super(original, true);
        this.original = original;
        this.logBuffer = logBuffer;
    }

    @Override
    public void println(String x) {
        original.println(x);  // Używamy ORYGINALNEGO strumienia, unikając zapętlenia
        logBuffer.add(x, "INFO", "Console", System.currentTimeMillis(), "N/A", "N/A", "N/A", 0.0);
    }
}
