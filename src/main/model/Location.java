package model;

import model.exception.NoShotsTakenException;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;
/**
 * represents one of the shooting locations on the court
 * //Invariant: listOfShots.size() = shots >= madeShots >= 0
 */

public class Location implements Writable {

    private static final Boolean MADE = true;

    private String name;
    private int shots;
    private int madeShots;
    private List<Boolean> listOfShots;

    // Effects: starts a new shooting location with the given name
    public Location(String name) {
        this.name = name;
        listOfShots = new ArrayList<>();
//        checkClassInvariant();
    }

    // MODIFIES: this
    // EFFECTS: adds 1 to the number of total shots and add MADE or MISSED to list of shots; if shot is made, adds 1
    //          to number of made shots
    public void addShot(Boolean isMade) {
        shots++;
        listOfShots.add(isMade);
        if (isMade == MADE) {
            madeShots++;
        }
//        checkClassInvariant();
    }

    // MODIFIES: this
    // EFFECTS: throws NoShotsTakenException if shots = 0
    //          otherwise, removes the last shot from the list, subtract 1 from totalShots, and subtract 1 from
    //          madeShots if shot was made
    public void removeShot() throws NoShotsTakenException {
        if (shots == 0) {
            throw new NoShotsTakenException();
        }
        shots--;
        boolean lastShot = listOfShots.get(listOfShots.size() - 1);
        if (lastShot == MADE) {
            madeShots--;
        }
        listOfShots.remove(listOfShots.size() - 1);
//        checkClassInvariant();
    }

    // EFFECTS: if no shots have been taken, return 0; otherwise, give a percentage of number of made shots from
    //          number of total shots
    public int getAverage() {
        if (shots == 0) {
            return 0;
        }
        return (madeShots * 100) / shots;
    }

    // Effects: gives a summary of this in a consistent form:
    //          name
    //                      Shot Attempts: x , Made Shots: y , FG% = z%
    public String toString() {
        String status = name + ":\n            Shots: " + shots + " , Makes: " + madeShots + " , FG%: "
                + getAverage() + "%";
        return status;
    }

    // getters
    public List<Boolean> getListOfShots() {
        return listOfShots;
    }

    // getters
    public int getShots() {
        return shots;
    }

    // getters
    public int getMadeShots() {
        return madeShots;
    }

    // getters
    public String getName() {
        return name;
    }

    // setters
    public void setShots(int shots) {
        this.shots = shots;
    }

    // setters
    public void setMadeShots(int madeShots) {
        this.madeShots = madeShots;
    }

    // setters
    public void setListOfShots(List<Boolean> listOfShots) {
        this.listOfShots = listOfShots;
    }

    // EFFECTS: Converts this to a json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("shots", shots);
        json.put("madeShots", madeShots);
        json.put("listOfShots", shotsToJson());

        return json;
    }

    // EFFECTS: converts elements in shots into individual json objects
    private JSONObject shotsToJson() {
        JSONObject shots = new JSONObject();
        int counter = 0;
        for (Boolean shot : listOfShots) {
            shots.put(Integer.toString(counter), shot);
            counter++;
        }
        return shots;
    }

}
