package persistence;

import model.Location;
import model.Session;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    //public class JsonTest {
    protected void checkSession(Session testSession, String name, List<Location> locations) {
        assertEquals(name, testSession.getName());
        int counter = 0;
        for (Location location : locations) {
            checkLocation(testSession.getLocations().get(counter), location);
            counter++;
        }
    }

    private void checkLocation(Location testLocation, Location location) {
        assertEquals(location.getName(), testLocation.getName());
        assertEquals(location.getShots(), testLocation.getShots());
        assertEquals(location.getMadeShots(), testLocation.getMadeShots());
        assertEquals(location.getListOfShots().size(), testLocation.getListOfShots().size());
        int counter = 0;
        for (Boolean shot : location.getListOfShots()) {
            assertEquals(shot, testLocation.getListOfShots().get(counter));
            counter++;
        }
    }


}
