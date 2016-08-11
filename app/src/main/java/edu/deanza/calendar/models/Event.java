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

    protected String name;
    protected String description;
    protected String location;
    protected String organization;
    protected DateTime startTime;
    protected DateTime endTime;

    // TODO: implement `categories` field

    public Event() {}

    public Event(String name, String description, String location, String organization,
                 String startTime, String endTime) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.organization = organization;
        this.startTime = DateTime.parse(startTime);
        this.endTime = DateTime.parse(endTime);
    }

    //based on CalendarEvents.json - can handle only one object
    public static Event fromCalendarEventsJSON(JSONObject jsonEvent) {
        Event e = new Event();
        try {
            e.name = jsonEvent.getString("name");
            e.description = jsonEvent.getString("description");
            e.location = jsonEvent.getString("location");
            e.startTime = DateTime.parse(jsonEvent.getString("start_time"));
            e.endTime = DateTime.parse(jsonEvent.getString("end_time"));
            e.organization = jsonEvent.getString("organization");
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return e;
    }

    //based on CalendarEvents.json - can handle entire file
    public static ArrayList<Event> fromCalendarEventsJSON(JSONArray jsonEvents) {
        ArrayList<Event> events = new ArrayList<Event>(jsonEvents.length());
        JSONObject jsonEvent;
        for (int i = 0; i < jsonEvents.length(); i++) {
            jsonEvent = jsonEvents.getJSONObject(i);
            Event event = Event.fromCalendarEventsJSON(jsonEvent);

            if (event != null) {
                events.add(event);
            }
        }
        return events;
    }

    //based on Clubs.json - can handle only one object
    //pre: club information from a single JSONObject
    //post: return all meeting times for a club
    public static ArrayList<Event> fromClubsJSON(JSONObject jsonClub)
    {
        ArrayList<Event> clubMeetings = new ArrayList<Event>();
        try {
            String sponsor = jsonClub.getString("name");
            String name = sponsor + " Club Meeting";
            String description = jsonClub.getString("description");
            String location = jsonClub.getString("location");
            String start_time = jsonClub.getString("start_time");
            String end_time = jsonClub.getString("end_time");
            JSONArray clubDates = jsonClub.getJSONArray("dates");
            for (int i = 0; i < clubDates.length(); i++)
            {
                String date = clubDates.getString(i);

                start_time = date + "T" + start_time;
                end_time = date + "T" + end_time;
                Event e = new Event(name, sponsor, description, location, start_time, end_time);
                clubMeetings.add(e);
            }
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return clubMeetings;
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

    public String getOrganization() {
        return organization;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

}