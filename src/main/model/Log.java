package model;


import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDate;

// A log represents a single entry, tracking their weight and the date as type strings
public class Log implements Writable {

    private Double weight;
    private String date;

    // Log constructor, creates a log with today's date
    public Log(Double weight) {
        this.weight = weight;
        LocalDate dateToday = LocalDate.now();
        this.date = dateToday.toString();
    }

    public Double getWeight() {
        return weight;
    }

    public String getDate() {
        return date;
    }

    // MODIFIES: this
    // EFFECTS: changes the date of the log to given date
    public void updateDate(String givenDate) {
        this.date = givenDate;
    }


    // EFFECTS: returns Log as JSON object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("weight", weight);
        json.put("date", date);
        return json;
    }
}
