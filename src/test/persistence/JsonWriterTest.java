package persistence;

import com.sun.org.apache.xpath.internal.operations.Bool;
import model.Location;
import model.Session;
import model.Sessions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.bind.SchemaOutputResolver;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest extends JsonTest{

    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    private Sessions sessions;
    private List<Session> listOfSession;
    private List<Boolean> listOfShots;

    @BeforeEach
    public void setup() {
        Session s1 = new Session("Test Session0");
        Session s2 = new Session("Test Session1");
        Session s3 = new Session("Test Session2");

        listOfShots = new ArrayList<>();
        for (int i=0; i<5; i++) {
            listOfShots.add(true);
            listOfShots.add(false);
        }

        for (Location location : s1.getLocations()) {
            location.addShot(true);
            location.addShot(false);
        }


        listOfSession = new ArrayList<>();
        listOfSession.add(s1);
        listOfSession.add(s2);
        listOfSession.add(s3);

        sessions = new Sessions(listOfSession);
        for (Session session : sessions.getSessions()) {
            for (Location location : session.getLocations()) {
                location.setListOfShots(listOfShots);

            }
        }
    }

    @Test
    void testWriterInvalidFile() {
        try {
            Session session = new Session("My Session");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException wasn't thrown");
        } catch (IOException e) {
            // pass
        }
    }



    @Test
    void testWriterEmptySessions() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(new Sessions(new ArrayList<>()));
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            Sessions testedSessions = reader.read();

            int counter = 0;
            for (Session testedSession : testedSessions.getSessions()) {
                checkSession(testedSession, "Test Session", (List<Location>) sessions.getSessions().get(counter));
                counter++;
            }

        } catch (IOException e) {
            fail("Caught IOException");
        }
    }


    @Test
    void testWriterGeneralSessions() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralWorkroom.json");
            writer.open();
            writer.write(sessions);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralWorkroom.json");
            Sessions testedSessions = reader.read();

            int counter = 0;
            assertEquals(sessions.getSessions().size(), testedSessions.getSessions().size());
            for (Session session : sessions.getSessions()) {
                checkSession(testedSessions.getSessions().get(counter), "Test Session" + Integer.toString(counter), session.getLocations());
                counter++;
            }

        } catch (IOException e) {
            fail("Caught IOException");
        }
    }
}
