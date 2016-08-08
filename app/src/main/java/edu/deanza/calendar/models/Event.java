package edu.deanza.calendar.models;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public static Event fromJson(JSONObject jsonEvent) {
        Event e = new Event();
        try {
            e.name = jsonEvent.getString("name");
            e.description = jsonEvent.getString("description");
            e.location = jsonEvent.getString("location");
            e.startTime = DateTime.parse(jsonEvent.getString("start_time"));
            e.endTime = DateTime.parse(jsonEvent.getString("end_time"));
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return e;
    }

    public static ArrayList<Event> fromJson(JSONArray jsonEvents) {
        ArrayList<Event> events = new ArrayList<Event>(jsonEvents.length());
        JSONObject jsonEvent;
        for (int i = 0; i < jsonEvents.length(); i++) {
            JSONObject jsonEvent = jsonEvents.getJSONObject(i);
            Event event = Event.fromJson(jsonEvent);

            if (event != null) {
                events.add(event)
            }
        }
        return events
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