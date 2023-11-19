import org.apache.log4j.Layout;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;

import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
public class MemAppenderTest {
    private static final Logger LOGGER = Logger.getLogger(MemAppenderTest.class);
    private static final MemAppender memAppender = MemAppender.getInstance();

    @Before
    public void init(){
        LOGGER.addAppender(memAppender);
    }

    @AfterEach
    public void clean(){
        memAppender.resetNumber();
        memAppender.clear();
    }

    private void print(){
        LOGGER.fatal("fatal");
        LOGGER.error("error");
        LOGGER.warn("warn");
        LOGGER.info("info");
    }

    @Test
    public void testMemAppenderIsSingleton(){
        assertEquals(MemAppender.getInstance(),MemAppender.getInstance());
    }
    @Test
    public void testStoreLoggingEvents(){
        print();
        List<String> source = memAppender.getEventStrings();
        List<String> target= Arrays.asList( "main---MemAppenderTest---fatal","main---MemAppenderTest---error","main---MemAppenderTest---warn","main---MemAppenderTest---info");
        for (int i = 0; i < 4; i++) {
            assertEquals(source.get(i),target.get(i));
        }
    }
    @Test
    public void testMemAppenderStoreGetCurrentLogs(){
        List<LoggingEvent> list = memAppender.getCurrentLogs();
        try{
            list.set(0,null);
        }catch (Exception e){
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void testGetEventStrings(){
        print();
        List<String> list = memAppender.getEventStrings();
        try{
            list.set(0,"assign251_2");
        }catch (Exception e){
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    @Test
    public void test_log4j_maxSize(){
        memAppender.setLayout(MemAppender.DefaultMemLayout.getInstance());
        memAppender.setMaxSize(10);
        LOGGER.fatal("fatal");
        LOGGER.error("error");
        LOGGER.warn("warn");
        LOGGER.info("info");
        LOGGER.debug("debug");
        LOGGER.fatal("fatal");
        LOGGER.error("error");
        LOGGER.warn("warn");
        LOGGER.info("info");
        LOGGER.debug("debug");
        LOGGER.debug("debug");
        LOGGER.debug("debug");
        List<String> source =memAppender.getEventStrings();
        List<String> target=Arrays.asList("main---MemAppenderTest---warn","main---MemAppenderTest---info","main---MemAppenderTest---debug","main---MemAppenderTest---fatal","main---MemAppenderTest---error","main---MemAppenderTest---warn","main---MemAppenderTest---info","main---MemAppenderTest---debug","main---MemAppenderTest---debug","main---MemAppenderTest---debug");
        for (int i = 0; i < 10; i++) {
            assertEquals(target.get(i),source.get(i));
        }
        assertEquals(2,memAppender.getDiscardedLogCount());
    }



    @Test
    public void testNotSetLayout(){
        memAppender.setLayout(MemAppender.DefaultMemLayout.getInstance());
        print();
        List<String> source = memAppender.getEventStrings();
        List<String> target=Arrays.asList("main---MemAppenderTest---fatal","main---MemAppenderTest---error","main---MemAppenderTest---warn","main---MemAppenderTest---info");
        for (int i = 0; i < 4; i++) {
            assertEquals(target.get(i),source.get(i));
        }
        memAppender.printLogs();
        source = memAppender.getEventStrings();
        assertEquals("[]",source.toString());
    }

    @Test
    public void testSetLayout(){
        Layout layout = new SimpleLayout();
        memAppender.setLayout(layout);
        print();
        List<String> source = memAppender.getEventStrings();
        String[] list1 = new String[]{"FATAL - fatal\r\n", "ERROR - error\r\n", "WARN - warn\r\n", "INFO - info\r\n"};
        for (int i = 0; i < 4; i++) {
            assertEquals(list1[i],source.get(i));
        }
        memAppender.printLogs();
        source = memAppender.getEventStrings();
        assertEquals("[]",source.toString());
    }

    @Test
    public void test_log4j_Velocity(){
        VelocityLayout velocityLayout = new VelocityLayout(" [$p] $c $d: $m$n");
        memAppender.setLayout(velocityLayout);
        print();
        memAppender.printLogs();
    }
}

