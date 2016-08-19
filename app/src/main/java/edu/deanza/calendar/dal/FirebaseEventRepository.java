package edu.deanza.calendar.dal;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.models.OrganizationEvent;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by karinaantonio on 8/11/16.
 */

public class FirebaseEventRepository implements EventRepository {

    private Query currentQuery;
    private List<OrganizationEvent> organizationEvents = new ArrayList<>();

    private class EventChangeListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot eventNodes) {
            organizationEvents.clear();
            Map<String, Map<Object, Object>> rawEvents = (Map<String, Map<Object, Object>>) eventNodes.getValue();
            EventMapper mapper = new EventMapper();
            for (Map.Entry<String, Map<Object, Object>> rawEvent : rawEvents.entrySet()) {
                organizationEvents.add(
                        new FirebaseOrganizationEvent(
                                mapper.map(rawEvent.getKey(), rawEvent.getValue())
                        )
                );
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "EventChangeListener:onCancelled", databaseError.toException());
        }

    }

    private List<OrganizationEvent> updateEvents() {
        currentQuery.addListenerForSingleValueEvent(new EventChangeListener());
        return organizationEvents;
    }

    public FirebaseEventRepository() {
        currentQuery = FirebaseDatabase.getInstance().getReference()
                .child("events")
                .orderByKey();
    }

    @Override
    public List<OrganizationEvent> all() {
        currentQuery = FirebaseDatabase.getInstance().getReference()
                .child("events")
                .orderByKey();
        return updateEvents();
    }

    @Override
    public List<OrganizationEvent> findByOrganization(String organizationName) {
        currentQuery = currentQuery.equalTo(organizationName, "organizationName");
        return updateEvents();
    }

    @Override
    public List<OrganizationEvent> on(LocalDate date) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = currentQuery
                .startAt(formatter.print(date))
                .endAt(formatter.print(date.plusDays(1)));
        return updateEvents();
    }

    @Override
    public List<OrganizationEvent> before(LocalDate date) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = currentQuery.endAt(formatter.print(date));
        return updateEvents();
    }

    @Override
    public List<OrganizationEvent> after(LocalDate date) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = currentQuery.startAt(formatter.print(date));
        return updateEvents();
    }

    @Override
    public List<OrganizationEvent> between(LocalDate start, LocalDate end) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        currentQuery = currentQuery
                .startAt(formatter.print(start))
                .endAt(formatter.print(end));
        return updateEvents();
    }

}
