package net.snurkle.t962interface;

import java.io.OutputStream;
import java.util.Date;
import java.util.logging.*;

public class Log {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    private static Logger logger = Logger.getLogger(Log.class.getName());

    public static void setup() {
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);

        WaveConsoleHandler handler = new WaveConsoleHandler();
        handler.setFormatter(new WaveLogFormatter());
        handler.setLevel(Level.INFO);
        logger.addHandler(handler);
    }

    private static String formatMessage(Class c, String msg) {
        return "[" + Thread.currentThread().getName() + "]" + " [" + c.getSimpleName() + "]: " + msg;
    }

    public static void severe(Class c, String msg) {
        logger.severe(formatMessage(c, msg));
    }
    public static void error(Class c, String msg) {
        logger.severe(formatMessage(c, msg));
    }
    public static void warning(Class c, String msg) {
        logger.warning(formatMessage(c, msg));
    }
    public static void info(Class c, String msg) {
        logger.info(formatMessage(c, msg));
    }
    public static void config(Class c, String msg) {
        logger.config(formatMessage(c, msg));
    }
    public static void debug(Class c, String msg) {logger.fine(formatMessage(c, msg));}
    public static void verbose(Class c, String msg) {logger.finer(formatMessage(c, msg));}
    public static void trace(Class c, String msg) {logger.finest(formatMessage(c, msg));}
}

class WaveLogFormatter extends Formatter {
    private String format = "[%1$tF %1$tT] [%2$-7s] %3$s %n";
    private String errorFormat = Log.ANSI_RED + "[%1$tF %1$tT] [%2$-7s] %3$s %n" + Log.ANSI_RESET;

    @Override
    public String format(LogRecord record) {
        if (record.getLevel() == Level.SEVERE || record.getLevel() == Level.WARNING) {
            return String.format(errorFormat,
                new Date(record.getMillis()),
                record.getLevel().getLocalizedName(),
                record.getMessage()
            );
        } else {
            return String.format(format,
                new Date(record.getMillis()),
                record.getLevel().getLocalizedName(),
                record.getMessage()
            );
        }
    }
}

class WaveConsoleHandler extends ConsoleHandler {
    OutputStream os;

    WaveConsoleHandler() {
        this.os = System.out;
    }

    WaveConsoleHandler(OutputStream os) {
        this.os = os;
    }
    protected void setOutputStream(OutputStream out) throws SecurityException {
        if (this.os == null) {
            super.setOutputStream(System.out);
        } else {
            super.setOutputStream(this.os);
        }
    }
}
