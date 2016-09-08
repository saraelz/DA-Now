package edu.deanza.calendar.domain;

import org.joda.time.LocalDate;

import java.util.List;

import edu.deanza.calendar.domain.models.Event;
import edu.deanza.calendar.util.Callback;

/**
 * Created by karinaantonio on 8/11/16.
 */

public interface EventRepository {

    void all(Callback<List<Event>> callback);

    void findByOrganization(String organizationName, Callback<List<Event>> callback);

    void on(LocalDate date, Callback<List<Event>> callback);

    void before(LocalDate date, Callback<List<Event>> callback);

    void after(LocalDate date, Callback<List<Event>> callback);

    void between(LocalDate start, LocalDate end, Callback<List<Event>> callback);

}
