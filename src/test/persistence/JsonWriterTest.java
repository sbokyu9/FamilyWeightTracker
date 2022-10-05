package persistence;

import model.Member;
import model.exceptions.NegativeValueException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyFamily() {
        try {
            // WorkRoom wr = new WorkRoom("My work room");
            ArrayList<Member> family = new ArrayList<>();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyFamily.json");
            writer.open();
            writer.write(family);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyFamily.json");
            family = reader.read();
            assertEquals(0, family.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralFamily() {
        try {
            ArrayList<Member> family = new ArrayList<>();
            Member ethan = new Member("Ethan", 183);
            Member bryan = new Member("Bryan", 165);
            try {
                ethan.addWeightLog(10.0);
                ethan.addWeightLog(11.0);
                bryan.addWeightLog(10.0);
                bryan.addWeightLog(11.0);
            } catch (NegativeValueException e) {
                e.printStackTrace();
            }
            family.add(ethan);
            family.add(bryan);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralFamily.json");
            writer.open();
            writer.write(family);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralFamily.json");
            family = reader.read();
            assertEquals(2, family.size());
            ethan = family.get(0);
            bryan = family.get(1);

            assertEquals("Ethan", ethan.getName());
            assertEquals("Bryan", bryan.getName());
            assertEquals(183, ethan.getHeight());
            assertEquals(165, bryan.getHeight());
            assertEquals(10.0, ethan.getWeightLogs().get(0).getWeight());
            assertEquals(11.0, ethan.getWeightLogs().get(1).getWeight());
            assertEquals(10.0, bryan.getWeightLogs().get(0).getWeight());
            assertEquals(11.0, bryan.getWeightLogs().get(1).getWeight());

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }



}