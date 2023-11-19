import org.apache.log4j.Logger;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class JMXTest {
    public static void main(String[] args) throws Exception {
        Logger LOGGER2 = Logger.getLogger(JMXTest.class);
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        String domainName = ":type=MemAppender";
        ObjectName MemAppenderName = new ObjectName(domainName);
        server.registerMBean(MemAppender.getInstance(),MemAppenderName);
        Logger rootLogger = Logger.getRootLogger();
        MemAppender memAppender = MemAppender.getInstance();
        rootLogger.addAppender(memAppender);
        VelocityLayout velocityLayout = new VelocityLayout("$m");
        memAppender.setLayout(velocityLayout);
        memAppender.setMaxSize(1000);
        for (int i = 0; i < 1000000000; i++) {
            LOGGER2.debug("zjw");
        }
        rootLogger.removeAllAppenders();
    }
}
