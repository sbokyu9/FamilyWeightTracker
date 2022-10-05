package persistence;


//modeled from  JsonSerializationDemo Project
// Represents a reader that reads workroom from JSON data stored in file

import model.Log;
import model.Member;
import model.exceptions.NegativeValueException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Stream;

// Modeled from JsonSerializationDemo
// Represents a reader that reads from JSON representation of family (list of members)
public class JsonReader {

    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }



    // EFFECTS: reads member from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ArrayList<Member> read() throws IOException {
        String jsonData = readFile(source);
        JSONArray jsonArray = new JSONArray(jsonData);
        return parseFamily(jsonArray);
    }

    // EFFECTS: Parses family from JSON object
    private ArrayList<Member> parseFamily(JSONArray jsonArray) {
        ArrayList<Member> family = new ArrayList<>();
        for (Object j: jsonArray) {
            JSONObject nextMember  = (JSONObject) j;
            family.add(parseMember(nextMember));
        }
        return family;
    }


    // EFFECTS: Parses member from JSON object and returns it
    private Member parseMember(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        int height = jsonObject.getInt("height");
        Member m = new Member(name, height);
        addWeightLog(m, jsonObject);
        return m;
    }


    // EFFECTS: parses weight log from JSON object
    private void addWeightLog(Member m, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("weightLog");
        for (Object json : jsonArray) {
            JSONObject nextLog = (JSONObject) json;
            addLog(m, nextLog);
        }
    }


    // EFFECTS: parses Log from JSON object and adds it to Member
    private void addLog(Member m, JSONObject jsonObject) {
        Double weight = jsonObject.getDouble("weight");
        String date = jsonObject.getString("date");
        Log log = new Log(weight);
        log.updateDate(date);
        m.addLogToWeightLog(log);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }
        return contentBuilder.toString();
    }




}
