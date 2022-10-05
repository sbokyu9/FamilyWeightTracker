package persistence;


import org.json.JSONObject;

//modeled from  JsonSerializationDemo Project

public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
