package edu.deanza.calendar.dal;

import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.domain.EventRepository;
import edu.deanza.calendar.domain.models.Day;
import edu.deanza.calendar.domain.models.Club;
import edu.deanza.calendar.domain.models.Organization;

/**
 * Created by karinaantonio on 8/19/16.
 */

class OrganizationMapper implements DataMapper<Organization> {

    private final EventRepository repository;

    public OrganizationMapper(EventRepository repository) {
        this.repository = repository;
    }

    public Organization map(String name, Map<Object, Object> rawOrganization) {

        String description = (String) rawOrganization.get("description");
        String location = (String) rawOrganization.get("location");
        String facebookUrl = (String) rawOrganization.get("facebookUrl");

        List<String> rawMeetings = (List<String>) rawOrganization.get("meetings");
        List<Interval> meetings = new ArrayList<>();
        if (rawMeetings != null) {
            for (String s : rawMeetings) {
                try {
                    meetings.add(Interval.parse(s));
                } catch (IllegalArgumentException e) {
                    continue;
                }
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
