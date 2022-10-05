package persistence;

import model.Member;
import model.exceptions.NegativeValueException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ArrayList<Member> family = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyFamily() {
        JsonReader reader = new JsonReader("./data/readerTestEmpty.json");
        try {
            ArrayList<Member> family = reader.read();
            assertEquals(0, family.size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }


    @Test
    void testReaderGeneralFamily() {
        JsonReader reader = new JsonReader("./data/readerTestFamily.json");
        try {
            ArrayList<Member> family = reader.read();
            assertEquals(2, family.size());
            Member ethan = family.get(0);
            assertEquals("Ethan", ethan.getName());
            assertEquals(183, ethan.getHeight());
            Member bryan = family.get(1);
            assertEquals("Bryan", bryan.getName());
            assertEquals(165, bryan.getHeight());

        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }








}