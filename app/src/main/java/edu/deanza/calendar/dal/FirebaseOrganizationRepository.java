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

import edu.deanza.calendar.models.Event;
import edu.deanza.calendar.models.Organization;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by karinaantonio on 8/18/16.
 */

public class FirebaseOrganizationRepository implements OrganizationRepository {

    private Query currentQuery;
    private List<Organization> organizations = new ArrayList<>();

    private class OrganizationChangeListener implements ValueEventListener {

        @Override
        public void onDataChange(DataSnapshot snapshot) {
            organizations.clear();
            List<Map<String, String>> rawOrganizations = (List<Map<String, String>>) snapshot.getValue();
            for (Map<String, String> rawOrganization : rawOrganizations) {
                Organization o = new Organization(
                        rawOrganization.get("name"),
                        rawOrganization.get("description"),
                        rawOrganization.get("location"),
                        rawOrganization.get("facebookUrl")
                );
                organizations.add(o);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "OrganizationChangeListener:onCancelled", databaseError.toException());
        }

    }

    private List<Organization> updateOrganizations() {
        currentQuery.addListenerForSingleValueEvent(new OrganizationChangeListener());
        return organizations;
    }

    public FirebaseOrganizationRepository() {
        currentQuery = FirebaseDatabase.getInstance().getReference()
                .child("organizations")
                .orderByKey();
    }

    @Override
    public List<Organization> all() {
        currentQuery = FirebaseDatabase.getInstance().getReference()
                .child("organizations")
                .orderByKey();
        return updateOrganizations();
    }

    @Override
    public Organization findByName(String name) {
        currentQuery = currentQuery.equalTo(name);
        return updateOrganizations().get(0);
    }

}
