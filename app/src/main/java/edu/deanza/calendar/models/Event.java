package edu.deanza.calendar.models;

//constructor for localDateTime: LocalDateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour)

import org.joda.time.LocalTime;

/**
 * Created by Sara on 5/28/2016.
 */
public class Event {
    protected String name, description, location;
    protected LocalTime startTime, endTime;
    protected enum source {
        Facebook, ClubMeeting, TransferAppointment, Website
    }
    // TODO: add `categories` field

    Event(String name, String description, String location, String startTime,
          String endTime)
    {
        this.name = name;
        this.description = description;
        this.location = location;
        this.startTime = LocalTime.parse(startTime);
        this.endTime = LocalTime.parse(endTime);

    }

}
