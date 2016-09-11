package edu.deanza.calendar.domain;

import org.joda.time.LocalDate;

import edu.deanza.calendar.domain.models.Event;
import edu.deanza.calendar.util.Callback;

/**
 * Created by karinaantonio on 8/11/16.
 */

public interface EventRepository {

    void all(Callback<Event> callback);

    void findByOrganization(String organizationName, Callback<Event> callback);

    void on(LocalDate date, Callback<Event> callback);

    void before(LocalDate date, Callback<Event> callback);

    void after(LocalDate date, Callback<Event> callback);

    void between(LocalDate start, LocalDate end, Callback<Event> callback);

}
