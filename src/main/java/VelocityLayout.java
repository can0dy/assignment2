import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class VelocityLayout extends PatternLayout {
    private final String pattern;
    private static final DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
    public VelocityLayout(String s){
        pattern = s;
    }
    @Override
    public String format(LoggingEvent loggingEvent) {
        StringWriter stringWriter = new StringWriter();
        Velocity.init();
        VelocityContext velocityContext = new VelocityContext();
        velocityContext.put("c",loggingEvent.getLoggerName());
        velocityContext.put("d", LocalDateTime.now().format(formatter));
        velocityContext.put("m",loggingEvent.getMessage());
        velocityContext.put("p",loggingEvent.getLevel());
        velocityContext.put("t",loggingEvent.getThreadName());
        velocityContext.put("n","\n");
        Velocity.evaluate(velocityContext,stringWriter,"myString",pattern);
        return stringWriter.toString();
    }

    @Override
    public boolean ignoresThrowable() {
        return false;
    }

    @Override
    public void activateOptions() {

    }
}
