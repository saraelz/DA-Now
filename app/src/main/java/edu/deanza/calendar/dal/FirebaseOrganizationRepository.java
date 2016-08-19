package edu.deanza.calendar.dal;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        public void onDataChange(DataSnapshot organizationNodes) {
            organizationEvents.clear();
            Map<String, Map<Object, Object>> rawOrganizations = (Map<String, Map<Object, Object>>) organizationNodes.getValue();
            OrganizationMapper mapper = new OrganizationMapper();
            for (Map.Entry<String, Map<Object, Object>> rawOrganization : rawOrganizations.entrySet()) {
                organizationEvents.add(
                        new FirebaseOrganizationEvent(
                                mapper.map(rawOrganization.getKey(), rawOrganization.getValue())
                        )
                );
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
