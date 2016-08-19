package edu.deanza.calendar.dal;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.models.Organization;
import edu.deanza.calendar.models.OrganizationEvent;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by karinaantonio on 8/18/16.
 */

public class FirebaseOrganizationRepository implements OrganizationRepository {

    private Query currentQuery;
    private List<OrganizationEvent> organizationEvents = new ArrayList<>();

    private class OrganizationChangeListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot snapshot) {

            organizationEvents.clear();
            List<Map<String, Object>> rawOrganizations = (List<Map<String, Object>>) snapshot.getValue();

            for (Map<String, Object> rawOrganization : rawOrganizations) {

                List<String> rawMeetings = (List<String>) rawOrganization.get("meetings");
                List<Interval> meetings = new ArrayList<>();
                for (String s : rawMeetings) {
                    try {
                        meetings.add(Interval.parse(s));
                    }
                    catch (IllegalArgumentException e) {
                        continue;
                    }
                }

                Organization o = new Organization(
                        (String) rawOrganization.get("name"),
                        (String) rawOrganization.get("description"),
                        (String) rawOrganization.get("location"),
                        (String) rawOrganization.get("facebookUrl"),
                        meetings
                );

                organizationEvents.add(new FirebaseOrganizationEvent(o));
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "OrganizationChangeListener:onCancelled", databaseError.toException());
        }

    }

    private List<OrganizationEvent> updateOrganizations() {
        currentQuery.addListenerForSingleValueEvent(new OrganizationChangeListener());
        return organizationEvents;
    }

    public FirebaseOrganizationRepository() {
        currentQuery = FirebaseDatabase.getInstance().getReference()
                .child("organizations")
                .orderByKey();
    }

    @Override
    public List<OrganizationEvent> all() {
        currentQuery = FirebaseDatabase.getInstance().getReference()
                .child("organizations")
                .orderByKey();
        return updateOrganizations();
    }

    @Override
    public OrganizationEvent findByName(String name) {
        currentQuery = currentQuery.equalTo(name);
        return updateOrganizations().get(0);
    }

}
