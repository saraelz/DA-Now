package edu.deanza.calendar.dal;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.List;

import edu.deanza.calendar.models.Event;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by karinaantonio on 8/11/16.
 */

public class FirebaseEventRepository implements EventRepository {

    private Query eventNodes;
    private List<Event> events;

    private class EventChangeListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot snapshot) {
            GenericTypeIndicator<List<Event>> t = new GenericTypeIndicator<List<Event>>() {};
            events.clear();
            events.addAll(snapshot.getValue(t));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "EventChangeListener:onCancelled", databaseError.toException());
        }

    }

    private void updateEvents(Query query) {
        query.addListenerForSingleValueEvent(new EventChangeListener());
    }

    public FirebaseEventRepository() {
        eventNodes = FirebaseDatabase.getInstance().getReference()
                .child("events")
                .orderByChild("date");
    }

    public List<Event> all() {
        updateEvents(eventNodes);
        return events;
    }

    public List<Event> ofOrganization(String organization) {
        Query q = eventNodes.equalTo(organization, "organization");
        updateEvents(q);
        return events;
    }

    public List<Event> on(LocalDate date) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        Query q = eventNodes
                .startAt(formatter.print(date), "date")
                .endAt(formatter.print(date.plusDays(1)), "date");
        updateEvents(q);
        return events;
    }

    public List<Event> before(LocalDate date) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        Query q = eventNodes.endAt(formatter.print(date), "date");
        updateEvents(q);
        return events;
    }

    public List<Event> after(LocalDate date) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        Query q = eventNodes.startAt(formatter.print(date), "date");
        updateEvents(q);
        return events;
    }

    public List<Event> between(LocalDate dateA, LocalDate dateB) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        Query q = eventNodes
                .startAt(formatter.print(dateA), "date")
                .endAt(formatter.print(dateB), "date");
        updateEvents(q);
        return events;
    }

}
