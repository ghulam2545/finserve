package com.ghulam.common.logger;

import org.springframework.beans.factory.annotation.Value;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public final class SimpleLogger {

    public static final String LOG_SEPARATOR = "───────: ";
    private static final Object LOG_LOCK = new Object();
    private static final StackWalker STACK_WALKER = StackWalker.getInstance();
    private static final AtomicInteger MAX_HEADER_LENGTH = new AtomicInteger(80);
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Value("${app.logs.print-date:false}")
    private boolean printDate;
    private final String name;

    private SimpleLogger(String name) {
        this.name = name != null ? name : "SimpleLogger";
    }

    public static SimpleLogger getLogger(Class<?> clazz) {
        return new SimpleLogger(clazz != null ? clazz.getName() : "Unknown");
    }

    public void info(String message, Object... arguments) {
        log(LogLevel.INFO, message, arguments);
    }

    public void error(String message, Object... arguments) {
        log(LogLevel.ERROR, message, arguments);
    }

    public void warn(String message, Object... arguments) {
        log(LogLevel.WARN, message, arguments);
    }

    private void log(LogLevel level, String pattern, Object... args) {
        Throwable error = null;
        if (args != null && args.length > 0
                && args[args.length - 1] instanceof Throwable t
                && countPlaceholders(pattern) < args.length) {
            error = t;
            args = Arrays.copyOf(args, args.length - 1);
        }

        String message = formatPattern(pattern, args);
        StackTraceElement caller = STACK_WALKER.walk(frames ->
                frames.dropWhile(frame -> frame.getClassName().equals(SimpleLogger.class.getName()))
                        .findFirst()
                        .map(StackWalker.StackFrame::toStackTraceElement)
                        .orElse(null));

        String location = caller != null ? caller.getMethodName() + ":" + caller.getLineNumber() : "";
        StringBuilder header = new StringBuilder();
        if (printDate)
            header.append(LocalDateTime.now().format(DATE_FORMATTER)).append(' ');

        header.append("[ ")
                .append(name)
                .append(" --- ")
                .append(location)
                .append(" ]");

        int width = MAX_HEADER_LENGTH.accumulateAndGet(
                header.length(), Math::max);
        StringBuilder output = new StringBuilder(header).repeat(" ", width - header.length())
                .append(LOG_SEPARATOR)
                .append(message);

        PrintStream out = level == LogLevel.ERROR ? System.err : System.out;
        synchronized (LOG_LOCK) {
            out.println(output);
            if (error != null) {
                error.printStackTrace(out);
            }
        }
    }

    private static String formatPattern(String pattern, Object... args) {
        if (pattern == null) return "null";
        if (args == null || args.length == 0) return pattern;

        StringBuilder result = new StringBuilder();
        int start = 0, i = 0, pos;

        while (i < args.length && (pos = pattern.indexOf("{}", start)) != -1) {
            result.append(pattern, start, pos).append(args[i++]);
            start = pos + 2;
        }
        result.append(pattern.substring(start));

        if (i < args.length) {
            result.append(" [");
            for (int j = i; j < args.length; j++)
                result.append(j > i ? ", " : "").append(args[j]);
            result.append(']');
        }

        return result.toString();
    }

    private static int countPlaceholders(String pattern) {
        int count = 0, index = 0;
        while (pattern != null && (index = pattern.indexOf("{}", index)) != -1) {
            count++;
            index += 2;
        }
        return count;
    }
}