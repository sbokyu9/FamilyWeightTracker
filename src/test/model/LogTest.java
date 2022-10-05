package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogTest {

    private Log testLog;

    @BeforeEach
    void runBefore() {
        testLog = new Log(10.2);
    }

    @Test
    void testConstructor() {
        LocalDate today = LocalDate.now();
        String to = today.toString();
        assertEquals(testLog.getDate(), to);
        assertEquals(testLog.getWeight(), 10.2);
    }

    @Test
    void testGetWeight() {
        assertEquals(testLog.getWeight(), 10.2);
    }

    @Test
    void testGetDate() {
        LocalDate today = LocalDate.now();
        String to = today.toString();
        assertEquals(testLog.getDate(), to);
    }

    @Test
    void updateDateTest() {
        Log log = new Log(10.0);
        log.updateDate("21-10-01");
        assertEquals("21-10-01", log.getDate());
    }

}
