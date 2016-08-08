package edu.deanza.calendar.models;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sara on 5/28/2016.
 */
public class Event {

    protected String name, description, location;
    protected DateTime startTime, endTime;

    // TODO: implement `categories` field

    public Event() {}

    public Event(String name, String description, String location, String startTime,
                 String endTime) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.startTime = DateTime.parse(startTime);
        this.endTime = DateTime.parse(endTime);

    }

    public static Event fromJson(JSONObject eventJson) {
        Event e = new Event();
        try {
            e.name = eventJson.getString("name");
            e.description = eventJson.getString("description");
            e.location = eventJson.getString("location");
            e.startTime = DateTime.parse(eventJson.getString("start_time"));
            e.endTime = DateTime.parse(eventJson.getString("end_time"));
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return e;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

}