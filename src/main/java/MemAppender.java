import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MemAppender extends AppenderSkeleton implements MemAppenderMBean{
    private List<LoggingEvent> loglist;
    private Layout memLayout = DefaultMemLayout.getInstance();
    private long maxSize;
    private static long number = 0;
    private int point;
    public static volatile MemAppender instance = null;

    public static MemAppender getInstance(){
        if(instance == null){
            synchronized (MemAppender.class) {
                if(instance==null)
                    instance = new MemAppender();
            }
        }
        instance.clear();
        instance.resetNumber();
        instance.setLayout(DefaultMemLayout.getInstance());
        return instance;
    }
    private MemAppender() {
        this.maxSize=10;
        this.point=0;
        this.layout=DefaultMemLayout.getInstance();
        this.loglist=new LinkedList<>();
    }

    public void setLogList(List<LoggingEvent> loglist){
        this.loglist=loglist;
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if (point < maxSize){
            loglist.add(loggingEvent);
            point +=1;
        }
        else{
            number+=1;
            loglist.remove(0);
            loglist.add(loggingEvent);
        }
    }

    @Override
    public void close() {
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
    public List<LoggingEvent> getCurrentLogs(){
        return Collections.unmodifiableList(loglist);
    }

    public List<String> getEventStrings() {
        return this.getLogMessages();
    }

    public void setLayout(Layout layout){
        memLayout = layout;
    }

    public void printLogs(){
        loglist.forEach(logging->System.out.println(memLayout.format(logging)));
        loglist.clear();
        point = 0;
    }

    public void setMaxSize(long size){
        maxSize = size;
    }
    public void resetNumber(){
        number = 0;
    }

    @Override
    public List<String> getLogMessages() {
        return Collections.unmodifiableList(loglist.stream().map(logging->memLayout.format(logging)).collect(Collectors.toList()));
    }

    @Override
    public long getSize() {
        return loglist.size();
    }

    public long getDiscardedLogCount(){
        return number;
    }
    public void clear() {
        loglist.clear();
        point = 0;
    }

    public static class DefaultMemLayout extends Layout {
        public static volatile DefaultMemLayout instance = null;
        private static final String SPLIT="---";

        public static DefaultMemLayout getInstance(){
            if(instance == null){
                synchronized (DefaultMemLayout.class) {
                    instance = new DefaultMemLayout();
                }
            }
            return instance;
        }
        private DefaultMemLayout() {

        }
        @Override
        public String format(LoggingEvent loggingEvent) {
            return loggingEvent.getThreadName()+SPLIT+loggingEvent.getLoggerName()+SPLIT+loggingEvent.getLevel().toString().toLowerCase();
        }

        @Override
        public boolean ignoresThrowable() {
            return false;
        }

        @Override
        public void activateOptions() {

        }
    }

}
