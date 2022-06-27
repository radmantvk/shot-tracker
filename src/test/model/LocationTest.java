package model;

import com.sun.org.apache.xpath.internal.operations.Bool;
import model.exception.NoShotsTakenException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LocationTest {
    private Location location;


    @BeforeEach
    public void setup() {
        location = new Location("Free Throw");
    }

    @Test
    public void testConstructor() {
        assertEquals("Free Throw", location.getName());
        assertEquals(0, location.getMadeShots());
        assertEquals(0, location.getShots());
        assertEquals(0, location.getListOfShots().size());
    }

    @Test
    public void testAddShotsMadeOnceAndLots() {
        location.addShot(true);

        assertEquals(1, location.getMadeShots());
        assertEquals(1, location.getShots());
        assertEquals(1, location.getListOfShots().size());
        assertEquals(true, location.getListOfShots().get(0));

        for (int i = 1; i < 10; i++) {
            location.addShot(true);
            assertEquals(i + 1, location.getMadeShots());
            assertEquals(i + 1, location.getShots());
            assertEquals(i + 1, location.getListOfShots().size());
            assertEquals(true, location.getListOfShots().get(i));
        }
    }

    @Test
    public void testAddShotsMissedOnceAndLots() {
        location.addShot(false);

        assertEquals(0, location.getMadeShots());
        assertEquals(1, location.getShots());
        assertEquals(1, location.getListOfShots().size());
        assertEquals(false, location.getListOfShots().get(0));

        for (int i = 1; i < 10; i++) {
            location.addShot(false);
            assertEquals(0, location.getMadeShots());
            assertEquals(i + 1, location.getShots());
            assertEquals(i + 1, location.getListOfShots().size());
            assertEquals(false, location.getListOfShots().get(i));
        }
    }

    @Test
    public void testAddShotsHighVolume() {
        for (int i = 0; i < 1000; i++) {
            location.addShot(true);
            location.addShot(false);
        }
    }

    @Test
    public void testRemoveShotNoShotsTakenException() {
        try {
            location.removeShot();
            fail("No exceptions thrown");
        } catch (NoShotsTakenException e) {
        }
        assertEquals(0, location.getListOfShots().size());
    }

    @Test
    public void testRemoveShotOnce() {
        location.addShot(true);
        try {
            location.removeShot();
        } catch (NoShotsTakenException e) {
            fail("Caught NoShotsTakenException");
        }
        assertEquals(0, location.getListOfShots().size());
    }

    @Test
    public void testRemoveShotMisses() {
        location.addShot(true);
        for (int i=0; i<5; i++) {
            location.addShot(false);
        }
        for (int i=0; i<5; i++) {
            try {
                location.removeShot();
            } catch (NoShotsTakenException e) {
                fail("Caught NoShotsTakenException");
            }
        }
        assertEquals(1, location.getListOfShots().size());
        assertEquals(true, location.getListOfShots().get(0));
    }

    @Test
    public void testRemoveShotMakes() {
        location.addShot(false);
        for (int i=0; i<5; i++) {
            location.addShot(true);
        }
        for (int i=0; i<5; i++) {
            try {
                location.removeShot();
            } catch (NoShotsTakenException e) {
                fail("Caught NoShotsTakenException");
            }
        }
        assertEquals(1, location.getListOfShots().size());
        assertEquals(false, location.getListOfShots().get(0));
    }

    @Test
    public void testGetAverageNoAddedShots() {
        int avg = location.getAverage();
        assertEquals(0, avg);
    }

    @Test
    public void testGetAverageNoMadeShots() {
        for (int i=0; i<5; i++) {
            location.addShot(false);
        }
        int avg = location.getAverage();
        assertEquals(0, avg);
    }

    @Test
    public void testGetAverageHalfMadeShots() {
        for (int i=0; i<5; i++) {
            location.addShot(false);
            location.addShot(true);
        }
        int avg = location.getAverage();
        assertEquals(50, avg);
    }

    @Test
    public void testGetAverageOnlyMadeShots() {
        for (int i=0; i<5; i++) {
            location.addShot(true);
        }
        int avg = location.getAverage();
        assertEquals(100, avg);
    }

    @Test
    public void testToString() {
        String toString = location.toString();
        String expectedString = "Free Throw:\n" + "            Shots: 0 , Makes: 0 , FG%: 0%";
        assertEquals(expectedString, toString);
    }
}
