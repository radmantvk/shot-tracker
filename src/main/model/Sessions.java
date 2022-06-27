package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.List;

/**
 * represents a list of shooting sessions
 */
public class Sessions implements Writable {
    private final List<Session> sessions;

    // instantiates this as empty
    public Sessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    // getters
    public List<Session> getSessions() {
        return sessions;
    }

    // EFFECTS: converts this to a Json object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("sessions", sessionsToJson());
        return json;
    }

    // EFFECTS: converts the arraylist of sessions to a json array
    private JSONArray sessionsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Session session : sessions) {
            jsonArray.put(session.toJson());
        }

        return jsonArray;
    }
}
