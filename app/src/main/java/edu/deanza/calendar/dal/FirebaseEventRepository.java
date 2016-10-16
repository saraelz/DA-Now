package edu.deanza.calendar.dal;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import edu.deanza.calendar.domain.EventRepository;
import edu.deanza.calendar.domain.OrganizationRepository;
import edu.deanza.calendar.domain.models.Event;
import edu.deanza.calendar.util.Callback;

/**
 * Created by karinaantonio on 8/11/16.
 */

public final class FirebaseEventRepository extends FirebaseRepository<Event> implements EventRepository, Serializable {

    private final DataMapper<Event> mapper;

    public FirebaseEventRepository() {
        mapper = new EventMapper(new FirebaseOrganizationRepository(this));
    }

    public FirebaseEventRepository(OrganizationRepository repository) {
        mapper = new EventMapper(repository);
    }

    @Override
    String getRootName() {
        return "events";
    }

    @Override
    DataMapper<Event> getMapper() {
        return mapper;
    }

    @Override
    public void all(Callback<Event> callback) {
        currentQuery = root.orderByChild("start");
        listenToQuery(callback);
    }

    @Override
    public void findByOrganization(String organizationName, Callback<Event> callback) {
        currentQuery = root
                .orderByChild(organizationName)
                .equalTo(true);
        listenToQuery(callback);
    }

    @Override
    public void on(LocalDate date, Callback<Event> callback) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = root
                .orderByChild("start")
                .startAt(formatter.print(date))
                .endAt(formatter.print(date.plusDays(1)));
        listenToQuery(callback);
    }

    @Override
    public void before(LocalDate date, Callback<Event> callback) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = root
                .orderByChild("start")
                .endAt(formatter.print(date));
        listenToQuery(callback);
    }

    @Override
    public void after(LocalDate date, Callback<Event> callback) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = root
                .orderByChild("start")
                .startAt(formatter.print(date));
        listenToQuery(callback);
    }

    @Override
    public void between(LocalDate start, LocalDate end, Callback<Event> callback) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = root
                .orderByChild("start")
                .startAt(formatter.print(start))
                .endAt(formatter.print(end));
        listenToQuery(callback);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initialize();
    }

}
