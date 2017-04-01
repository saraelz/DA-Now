package edu.deanza.calendar.domain;

import java.io.Serializable;
import java.util.List;

import edu.deanza.calendar.domain.interfaces.Subscribable;

public class Club extends Organization implements Subscribable, Serializable {

    private final List<Day> meetingDays;

    public Club(String name, String description, String location, String facebookUrl,
                List<RegularMeeting> meetings, List<Day> meetingDays) {
        super(name, description, location, facebookUrl, meetings);
        this.meetingDays = meetingDays;
    }

    public List<Day> getMeetingDays() {
        return meetingDays;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Club{");
        sb.append(super.toString());
        sb.append("meetingDays=").append(meetingDays);
        sb.append('}');
        return sb.toString();
    }
}
