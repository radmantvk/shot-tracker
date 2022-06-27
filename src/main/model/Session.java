package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

/**
 * represents a shooting session
 */

public class Session implements Writable {
    private static final String[] LOCATION_NAMES =
            new String[]{
                    "Free Throw", "Paint", "Right Low Post", "Left Low Post", "Right Elbow", "Left Elbow",
                    "Right Corner 3", "Right Wing 3", "Centre 3", "Left Wing 3", "Left Corner 3",
            };

    private final String name;
    private List<Location> locations;

    // EFFECTS: makes a new session with its given name and instantiates a location with every locationNames
    //          and adds them to locations;
    //          getLocationNames().length == getLocations().size() and for all i: locations.get(i) == locationNames[i]
    public Session(String name) {
        this.name = name;
        locations = new ArrayList<>();
        for (String ln : LOCATION_NAMES) {
            locations.add(new Location(ln));
        }
    }

    // getters
    public static String[] getLocationNames() {
        return LOCATION_NAMES;
    }

    // getters
    public int getTotalShots() {
        int totalShots = 0;
        for (Location location : locations) {
            totalShots += location.getShots();
        }
        return totalShots;
    }

    // getters
    public int getTotalMadeShots() {
        int totalMadeShots = 0;
        for (Location location : locations) {
            totalMadeShots += location.getMadeShots();
        }
        return totalMadeShots;
    }

    // EFFECTS: if no shots have been taken, return 0; otherwise, give a percentage of number of made shots from
    //          number of total shots
    public int getTotalAverage() {
        if (!(getTotalMadeShots() == 0)) {
            return (100 * getTotalMadeShots()) / getTotalShots();
        }
        return 0;
    }

    // REQUIRES: the last character of 3 point location names must be 3
    // EFFECTS: if a shot from the 3 point location has been taken returns the shot making percentage; otherwise 0
    public int get3PointAverage() {
        int countMade3s = 0;
        int countTotal3s = 0;
        for (Location location : locations) {
            if (location.getName().contains("3")) {
                countMade3s += location.getMadeShots();
                countTotal3s += location.getShots();
            }
        }
        if (countTotal3s == 0) {
            return 0;
        }
        return (100 * countMade3s) / countTotal3s;
    }

    // REQUIRES: the last character of 3 point location names must be 3
    // EFFECTS: returns the shot making percentage from the 2 pointer locations
    public int get2PointAverage() {
        int countMade2s = 0;
        int countTotal2s = 0;
        for (Location location : locations) {
            if (!location.getName().contains("3")) {
                countMade2s += location.getMadeShots();
                countTotal2s += location.getShots();
            }
        }
        if (countTotal2s == 0) {
            return 0;
        }
        return (100 * countMade2s) / countTotal2s;
    }

    // getters
    public String getName() {
        return name;
    }

    // getters
    public List<Location> getLocations() {
        return locations;
    }

    // setters
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    // Effects: gives a summary of places where at least a shot has been attempted in a consistent form:
    //          ****************         name          **************
    //          FreeThrow:
    //                      Shot Attempts: x , Made Shots: y , FG%: z
    //                      -----------------------------------------
    //           Paint:
    //                      Shot Attempts: x , Made Shots: y , FG%: z
    //                      -----------------------------------------
    //           etc.]
    public String toString() {

        String str = "                  < < <  " + name + "  > > >\n";
        str += "TOTAL: Shots: " + getTotalShots() + " , Makes: " + getTotalMadeShots()
                + " , 3 pointer: " + get3PointAverage() + "% , 2 pointer: " + get2PointAverage() + "% , FG%: "
                + getTotalAverage() + "%\n";
        for (Location location : locations) {
            if (location.getShots() != 0) {
                str += location.toString();
                str += "\n            - - - - - - - - - - - - - - -\n";
            }
        }
        return str;
    }

    // EFFECTS: converts this to a json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("locations", locationsToJson());
        return json;
    }

    // EFFECTS: converts the list of locations to a json array
    private JSONArray locationsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Location location : locations) {
            jsonArray.put(location.toJson());
        }
        return jsonArray;
    }

}
