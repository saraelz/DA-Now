package edu.deanza.calendar.models;

import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sara on 5/28/2016.
 */
public class Event {

    protected String name, description, location;
    protected LocalTime startTime, endTime;

    // TODO: implement `categories` field

    public Event() {}

    public Event(String name, String description, String location, String startTime,
                 String endTime) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);

    }

    public static Event fromJson(JSONObject eventJson) {
        Event e = new Event();
        try {
            e.name = eventJson.getString("name");
            e.description = eventJson.getString("description");
            e.location = eventJson.getString("location");
            e.startTime = LocalTime.parse(eventJson.getString("start_time"));
            e.endTime = LocalTime.parse(eventJson.getString("end_time"));
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return e;
    }

}