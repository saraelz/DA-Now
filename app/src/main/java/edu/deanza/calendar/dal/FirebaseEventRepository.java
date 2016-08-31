package edu.deanza.calendar.dal;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.models.Event;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by karinaantonio on 8/11/16.
 */

public class FirebaseEventRepository implements EventRepository {

    private Query currentQuery;
    private List<Event> events = new ArrayList<>();

    private class EventChangeListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot eventNodes) {
            events.clear();
            for (DataSnapshot eventNode : eventNodes.getChildren()) {
                String eventKey = eventNode.getKey();
                int nameStartDelimiter = eventKey.lastIndexOf('|');
                String name = eventKey.substring(nameStartDelimiter + 1, eventKey.length());

                Map<String, String> rawEvent = (Map<String, String>) eventNode.getValue();
                Event e = new Event(
                        eventKey.substring(nameStartDelimiter + 1, eventKey.length()),
                        rawEvent.get("description"),
                        rawEvent.get("location"),
                        rawEvent.get("organizationName"),
                        DateTime.parse(rawEvent.get("start")),
                        DateTime.parse(rawEvent.get("end"))
                );
                events.add(e);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "EventChangeListener:onCancelled", databaseError.toException());
        }

    }

    private List<Event> updateEvents() {
        currentQuery.addListenerForSingleValueEvent(new EventChangeListener());
        return events;
    }

    public FirebaseEventRepository() {
        currentQuery = FirebaseDatabase.getInstance().getReference()
                .child("events")
                .orderByKey();
    }

    @Override
    public List<Event> all() {
        currentQuery = FirebaseDatabase.getInstance().getReference()
                .child("events")
                .orderByKey();
        return updateEvents();
    }

    @Override
    public List<Event> findByOrganization(String organizationName) {
        currentQuery = currentQuery.equalTo(organizationName, "organizationName");
        return updateEvents();
    }

    @Override
    public List<Event> on(LocalDate date) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = currentQuery
                .startAt(formatter.print(date))
                .endAt(formatter.print(date.plusDays(1)));
        return updateEvents();
    }

    @Override
    public List<Event> before(LocalDate date) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = currentQuery.endAt(formatter.print(date));
        return updateEvents();
    }

    @Override
    public List<Event> after(LocalDate date) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = currentQuery.startAt(formatter.print(date));
        return updateEvents();
    }

    @Override
    public List<Event> between(LocalDate start, LocalDate end) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = currentQuery
                .startAt(formatter.print(start))
                .endAt(formatter.print(end));
        return updateEvents();
    }

}
