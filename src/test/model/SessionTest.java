package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionTest {
    private Session session;
    @BeforeEach
    public void setup() {
        session = new Session("testSession");
    }

    @Test
    public void testConstructor() {
        assertEquals("testSession", session.getName());
        assertEquals(session.getLocations().size(), session.getLocationNames().length);
        for (int i=0 ; i < session.getLocationNames().length; i++) {
            assertEquals(session.getLocations().get(i).getName() , session.getLocationNames()[i]);
        }
    }

    @Test
    public void testGetTotalShotsNone() {
        int totalShots = session.getTotalShots();
        assertEquals(totalShots, 0);
    }

    @Test
    public void TestGetTotalShotsMultiple() {
        for (int i = 0; i < session.getLocations().size() ; i++) {
            session.getLocations().get(i).addShot(true);
            session.getLocations().get(i).addShot(true);
        }
        int totalShots = session.getTotalShots();
        assertEquals(totalShots, 2 * session.getLocations().size());
    }

    @Test
    public void TestGetTotalMadeShotsNone() {
        assertEquals(session.getTotalMadeShots(), 0);
    }

    @Test
    public void TestGetTotalMadeShotsMultiple() {
        for (int i = 0; i < session.getLocations().size() ; i++) {
            session.getLocations().get(i).addShot(true);
            session.getLocations().get(i).addShot(true);
            session.getLocations().get(i).addShot(false);
        }
        int totalMadeShots = session.getTotalMadeShots();
        assertEquals(totalMadeShots, 2 * session.getLocations().size());
    }

    @Test
    public void testGetTotalAverageNoShotAttempts() {
        assertEquals(session.getTotalAverage(), 0);
    }

    @Test
    public void testGetTotalAverageHalfMadeShots() {
        for (int i = 0; i < session.getLocations().size() ; i++) {
            session.getLocations().get(i).addShot(true);
            session.getLocations().get(i).addShot(false);
        }
        int average = session.getTotalAverage();
        assertEquals(average, 50);
    }

    @Test
    public void testGetTotalAverageMultipleMadeShots() {
        for (int i = 0; i < session.getLocations().size() ; i++) {
            session.getLocations().get(i).addShot(true);
            session.getLocations().get(i).addShot(true);
        }
        int average = session.getTotalAverage();
        assertEquals(average, 100);
    }

    @Test
    public void testGet3PointAverageNoShots() {
        assertEquals(0, session.get3PointAverage());
    }

    @Test
    public void testGet3PointAverageOnlyMisses() {
        for (Location location : session.getLocations()) {
            location.addShot(false);
        }
        assertEquals(0, session.get3PointAverage());
    }

    @Test
    public void testGet3PointAverageHalfHalf() {
        for (Location location : session.getLocations()) {
            location.addShot(true);
            location.addShot(false);
        }
        assertEquals(50, session.get3PointAverage());
    }

    @Test
    public void testGet3PointAverageOnlyMakes() {
        for (Location location : session.getLocations()) {
            location.addShot(true);
        }
        assertEquals(100, session.get3PointAverage());
    }

    @Test
    public void testGet2PointAverageNoShots() {
        assertEquals(0, session.get2PointAverage());
    }

    @Test
    public void testGet2PointAverageOnlyMisses() {
        for (Location location : session.getLocations()) {
        location.addShot(false);
    }
        assertEquals(0, session.get2PointAverage());
    }

    @Test
    public void testGet2PointAverageHalfHalf() {
        for (Location location : session.getLocations()) {
            location.addShot(true);
            location.addShot(false);
        }
        assertEquals(50, session.get2PointAverage());
    }

    @Test
    public void testGet2PointAverageOnlyMakes() {
        for (Location location : session.getLocations()) {
            location.addShot(true);
        }
        assertEquals(100, session.get2PointAverage());
    }

    @Test
    public void testToStringNoShotsTaken() {
        String str =  "                  < < <  testSession  > > >\n" +
                "TOTAL: Shots: 0 , Makes: 0 , 3 pointer: 0% , 2 pointer: 0% , FG%: 0%\n";
        assertEquals(str, session.toString());

    }

    @Test
    public void testToStringFewShotsTaken() {
        for (Location location : session.getLocations()) {
            location.addShot(true);
        }
        String str = "                  < < <  testSession  > > >\n" +
                "TOTAL: Shots: 11 , Makes: 11 , 3 pointer: 100% , 2 pointer: 100% , FG%: 100%\n" +
                "Free Throw:\n" +
                "            Shots: 1 , Makes: 1 , FG%: 100%\n" +
                "            - - - - - - - - - - - - - - -\n" +
                "Paint:\n" +
                "            Shots: 1 , Makes: 1 , FG%: 100%\n" +
                "            - - - - - - - - - - - - - - -\n" +
                "Right Low Post:\n" +
                "            Shots: 1 , Makes: 1 , FG%: 100%\n" +
                "            - - - - - - - - - - - - - - -\n" +
                "Left Low Post:\n" +
                "            Shots: 1 , Makes: 1 , FG%: 100%\n" +
                "            - - - - - - - - - - - - - - -\n" +
                "Right Elbow:\n" +
                "            Shots: 1 , Makes: 1 , FG%: 100%\n" +
                "            - - - - - - - - - - - - - - -\n" +
                "Left Elbow:\n" +
                "            Shots: 1 , Makes: 1 , FG%: 100%\n" +
                "            - - - - - - - - - - - - - - -\n" +
                "Right Corner 3:\n" +
                "            Shots: 1 , Makes: 1 , FG%: 100%\n" +
                "            - - - - - - - - - - - - - - -\n" +
                "Right Wing 3:\n" +
                "            Shots: 1 , Makes: 1 , FG%: 100%\n" +
                "            - - - - - - - - - - - - - - -\n" +
                "Centre 3:\n" +
                "            Shots: 1 , Makes: 1 , FG%: 100%\n" +
                "            - - - - - - - - - - - - - - -\n" +
                "Left Wing 3:\n" +
                "            Shots: 1 , Makes: 1 , FG%: 100%\n" +
                "            - - - - - - - - - - - - - - -\n" +
                "Left Corner 3:\n" +
                "            Shots: 1 , Makes: 1 , FG%: 100%\n" +
                "            - - - - - - - - - - - - - - -\n";
        assertEquals(str, session.toString());

    }




}
