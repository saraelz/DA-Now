package edu.deanza.calendar.dal.mappers;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.dal.interfaces.EventRepository;
import edu.deanza.calendar.util.dal.mappers.DataMapper;
import edu.deanza.calendar.domain.Club;
import edu.deanza.calendar.domain.Day;
import edu.deanza.calendar.domain.Organization;

/**
 * Created by karinaantonio on 8/19/16.
 */

public class OrganizationMapper implements DataMapper<Organization>, Serializable {

    private final EventRepository repository;

    public OrganizationMapper(EventRepository repository) {
        this.repository = repository;
    }

    public Organization map(String name, Map<Object, Object> rawOrganization) {

        String description = (String) rawOrganization.get("description");
        String location = (String) rawOrganization.get("location");
        String facebookUrl = (String) rawOrganization.get("facebookUrl");

        List<String> rawMeetings = (List<String>) rawOrganization.get("meetings");
        List<Organization.RegularMeeting> meetings = new ArrayList<>();
        if (rawMeetings != null) {
            for (String s : rawMeetings) {
                Interval interval;
                try {
                    interval = Interval.parse(s);
                } catch (IllegalArgumentException e) {
                    continue;
                }

                DateTime start = interval.getStart();
                DateTime end = interval.getEnd();


                Organization.RegularMeeting meeting = new Organization.RegularMeeting(start, end, name);
                meetings.add(meeting);
            }
        }

        Organization o;

        if (rawOrganization.get("fromSpreadsheet") != null) {
            List<String> rawDays = (List<String>) rawOrganization.get("meetingDays");
            List<Day> days = new ArrayList<>();
            if (rawDays != null) {
                for (String s : rawDays) {
                    days.add(Day.valueOf(s));
                }
            }

            o = new Club(name, description, location, facebookUrl, meetings, repository, days);
        }
        else {
            o = new Organization(name, description, location, facebookUrl, meetings, repository);
        }

        return o;

    }

}
