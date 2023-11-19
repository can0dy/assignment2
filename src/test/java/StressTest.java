import com.google.inject.internal.util.Lists;
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;
@RunWith(Parameterized.class)
public class StressTest {
    private static final Logger LOGGER = Logger.getLogger(StressTest.class);

    private final Integer type;

    public StressTest(Integer type){
        this.type=type;
    }

    @Parameterized.Parameters
    public static List<Integer> prepareData() {
        return Lists.newArrayList(0,1,2,4);
    }

    @Test
    public void parameterizedTest(){
        switch (type){
            case 0:{
                memAppenderWithArrayListTest();
                memAppenderWithLinkedListTest();
                break;
            }
            case 1:{
                consoleAppenderTest();
                fileAppenderTest();
                break;
            }
            case 2:{
                patternLayoutTest();
                velocityLayoutTest();
                break;
            }
            case 3:{
                beforeMaxSizeTest();
                afterMaxSizeTest();
            }
        }
    }

    private void listTest(List<LoggingEvent> list,String message){
        Logger rootLogger = Logger.getRootLogger();
        MemAppender memAppender = MemAppender.getInstance();
        memAppender.setLogList(list);
        rootLogger.addAppender(memAppender);
        memAppender.setMaxSize(1000);
        print(message);
        rootLogger.removeAllAppenders();
    }

    @Test
    public void memAppenderWithArrayListTest(){
        listTest(new ArrayList<>(),"memAppenderWithArrayListTest");
    }

    @Test
    public void memAppenderWithLinkedListTest(){
        listTest(new LinkedList<>(),"memAppenderWithLinkedListTest");
    }

    @Test
    public void consoleAppenderTest(){
        Logger rootLogger = Logger.getRootLogger();
        ConsoleAppender consoleAppender = new ConsoleAppender(MemAppender.DefaultMemLayout.getInstance());
        rootLogger.addAppender(consoleAppender);
        print("consoleAppenderTest");
        consoleAppender.activateOptions();
        rootLogger.removeAllAppenders();
    }
    @Test
    public void fileAppenderTest(){
        Logger rootLogger = Logger.getRootLogger();
        FileAppender fileAppender = new FileAppender();
        fileAppender.setFile("assign251_2.log");
        fileAppender.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
        fileAppender.setThreshold(Level.DEBUG);
        fileAppender.setAppend(true);
        fileAppender.activateOptions();
        rootLogger.addAppender(fileAppender);
        print("fileAppenderTest");
        rootLogger.removeAllAppenders();
    }
    @Test
    public void patternLayoutTest(){
        Logger rootLogger = Logger.getRootLogger();
        MemAppender memAppender = MemAppender.getInstance();
        rootLogger.addAppender(memAppender);
        PatternLayout patternLayout = new PatternLayout();
        memAppender.setLayout(patternLayout);
        memAppender.setMaxSize(1000);
        print("patternLayoutTest");
        rootLogger.removeAllAppenders();
    }
    @Test
    public void velocityLayoutTest(){
        Logger rootLogger = Logger.getRootLogger();
        MemAppender memAppender = MemAppender.getInstance();
        rootLogger.addAppender(memAppender);
        VelocityLayout velocityLayout = new VelocityLayout("$m");
        memAppender.setLayout(velocityLayout);
        memAppender.setMaxSize(1000);
        print("velocityLayoutTest");
        rootLogger.removeAllAppenders();
    }
    @Test
    public void beforeMaxSizeTest(){
        Logger rootLogger = Logger.getRootLogger();
        MemAppender memAppender = MemAppender.getInstance();
        rootLogger.addAppender(memAppender);
        VelocityLayout velocityLayout = new VelocityLayout("$m");
        memAppender.setLayout(velocityLayout);
        memAppender.setMaxSize(1000);
        print("beforeMaxSizeTest");
        rootLogger.removeAllAppenders();
    }
    @Test
    public void afterMaxSizeTest(){
        Logger rootLogger = Logger.getRootLogger();
        MemAppender memAppender = MemAppender.getInstance();
        rootLogger.addAppender(memAppender);
        VelocityLayout velocityLayout = new VelocityLayout("$m");
        memAppender.setLayout(velocityLayout);
        memAppender.setMaxSize(1000);
        print("afterMaxSizeTest");
        rootLogger.removeAllAppenders();
    }

    private static void print(String str){
        IntStream.range(0,1000).boxed().forEach(integer -> LOGGER.debug(str));
    }
}
