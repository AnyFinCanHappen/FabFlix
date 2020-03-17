package edu.uci.ics.sidneyjt.service.movies.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ServiceFormatter extends Formatter {
    private static final int MAX_BUF = 1000;

    public String format(LogRecord record) {
        StringBuffer buf = new StringBuffer(MAX_BUF);
        buf.append(calculateDate(record.getMillis()));
        buf.append("[" + record.getLevel() + "]");
        buf.append("[" + /*record.getSourceClassName() + "." +*/ record.getSourceMethodName() + "]  ");
        buf.append(record.getMessage() + "\n");
        return buf.toString();
    }

    private String calculateDate(long ms) {
        SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]");
        sdf.setTimeZone(TimeZone.getTimeZone("America/Los_Angeles"));
        Date date = new Date(ms);
        return sdf.format(date);
    }
}
