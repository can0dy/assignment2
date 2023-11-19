import java.util.List;

public interface MemAppenderMBean {
    List<String> getLogMessages();
    long getSize();
    long getDiscardedLogCount();
}
