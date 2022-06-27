package persistence;

import model.Location;
import model.Session;
import model.Sessions;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// Citation: used my school's demo sample as template
// Represents a reader that reads sessions from JSON data stored in file
public class JsonReader {

    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads sessions from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Sessions read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSessions(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses session from JSON object and returns it
    private Sessions parseSessions(JSONObject jsonObject) {
        ArrayList<Session> listOfSession = new ArrayList<>();
        JSONArray jsonArrayOfSession = jsonObject.getJSONArray("sessions");
        for (Object json : jsonArrayOfSession) {
            JSONObject nextSession = (JSONObject) json;
            Session session = parseSession(nextSession);
            listOfSession.add(session);
        }

        return new Sessions(listOfSession);
    }

    // EFFECTS: parses session from JSON object and returns it
    private Session parseSession(JSONObject jsonObject) {
        List<Location> locations = new ArrayList<>();
        String name = jsonObject.getString("name");

        JSONArray jsonArrayOfLocations = jsonObject.getJSONArray("locations");
        for (Object json : jsonArrayOfLocations) {
            JSONObject nextLocation = (JSONObject) json;
            Location location = parseLocation(nextLocation);
            locations.add(location);
        }

        Session session = new Session(name);
        session.setLocations(locations);
        return session;
    }

    // EFFECTS: parses Location from JSON object and returns it
    private Location parseLocation(JSONObject jsonObject) {

        String name = jsonObject.getString("name");
        Location location = new Location(name);

        int shots = jsonObject.getInt("shots");
        int madeShots = jsonObject.getInt("madeShots");
        List<Boolean> listOfShots = new ArrayList<>();

        JSONObject jsonShots = jsonObject.getJSONObject("listOfShots");
        int counter = 0;
        for (int i = 0; i < jsonShots.length(); i++) {
            Boolean shot = jsonShots.optBoolean(Integer.toString(counter));
            listOfShots.add(shot);
            counter++;
        }

        location.setMadeShots(madeShots);
        location.setShots(shots);
        location.setListOfShots(listOfShots);
        return location;
    }


}
