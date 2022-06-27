package persistence;

import org.json.JSONObject;

// Citation: used my school's demo sample as template
public interface Writable {
    //EFFECTS: returns this as JSON object
    JSONObject toJson();
}
