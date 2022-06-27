package persistence;

import model.Location;
import model.Session;
import model.Sessions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {

    private Sessions sessions;
    List<Session> listOfSession;

    @BeforeEach
    public void setup() {
        Session s1 = new Session("Test Session0");
        Session s2 = new Session("Test Session1");
        Session s3 = new Session("Test Session2");

        ArrayList<Boolean> listOfShots = new ArrayList<>();
        for (int i=0; i<5; i++) {
            listOfShots.add(true);
            listOfShots.add(false);
        }
        s1.getLocations().get(0).setListOfShots(listOfShots);


        listOfSession = new ArrayList<>();
        listOfSession.add(s1);
        listOfSession.add(s2);
        listOfSession.add(s3);

        sessions = new Sessions(listOfSession);
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Sessions sessions = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptySessions() {
        JsonReader reader = new JsonReader("./data/testReaderEmptySessions.json");
        try {
            Sessions testSessions = reader.read();
            assertEquals(0, testSessions.getSessions().size());
            for (Session testSession : testSessions.getSessions()) {
                checkSession(testSession, "sessiosn", null);
            }
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralSessions() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralSessions.json");
        JsonWriter writer = new JsonWriter("./data/testReaderGeneralSessions.json");
        try {
            writer.open();
            writer.write(sessions);
            writer.close();
        } catch (FileNotFoundException e) {
            fail("Caught FileNotFoundException");
        }

        try {
            Sessions sessionsRead = reader.read();
            int counter = 0;
            for (Session testSession : sessionsRead.getSessions()) {
                List<Location> expectedLocations = sessions.getSessions().get(counter).getLocations();
                checkSession(testSession, "Test Session" + Integer.toString(counter), expectedLocations);
                counter++;
            }
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
