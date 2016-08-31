package edu.deanza.calendar.dal;

import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;

import edu.deanza.calendar.domain.EventRepository;
import edu.deanza.calendar.domain.OrganizationRepository;
import edu.deanza.calendar.domain.models.Event;
import edu.deanza.calendar.util.Callback;

/**
 * Created by karinaantonio on 8/11/16.
 */

public final class FirebaseEventRepository extends FirebaseRepository<Event> implements EventRepository {

    {
        root = FirebaseDatabase.getInstance().getReference().child("events");
        currentQuery = root.orderByKey();
    }

    private final DataMapper<Event> mapper;

    public FirebaseEventRepository() {
        mapper = new EventMapper(new FirebaseOrganizationRepository(this));
    }

    public FirebaseEventRepository(OrganizationRepository repository) {
        mapper = new EventMapper(repository);
    }


    @Override
    DataMapper<Event> getMapper() {
        return mapper;
    }

    @Override
    public void all(Callback<List<Event>> callback) {
        currentQuery = root.orderByKey();
        listenToQuery(callback);
    }

    @Override
    public void findByOrganization(String organizationName, Callback<List<Event>> callback) {
        currentQuery = root.orderByChild("start").equalTo(organizationName, "organizationName");
        listenToQuery(callback);
    }

    @Override
    public void on(LocalDate date, Callback<List<Event>> callback) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = currentQuery
                .startAt(formatter.print(date))
                .endAt(formatter.print(date.plusDays(1)));
        listenToQuery(callback);
    }

    @Override
    public void before(LocalDate date, Callback<List<Event>> callback) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = currentQuery.endAt(formatter.print(date));
        listenToQuery(callback);
    }

    @Override
    public void after(LocalDate date, Callback<List<Event>> callback) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = currentQuery.startAt(formatter.print(date));
        listenToQuery(callback);
    }

    @Override
    public void between(LocalDate start, LocalDate end, Callback<List<Event>> callback) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = currentQuery
                .startAt(formatter.print(start))
                .endAt(formatter.print(end));
        listenToQuery(callback);
    }

}
