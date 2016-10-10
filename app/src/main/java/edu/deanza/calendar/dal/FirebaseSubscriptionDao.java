package edu.deanza.calendar.dal;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.deanza.calendar.domain.SubscriptionDao;
import edu.deanza.calendar.domain.models.Subscription;
import edu.deanza.calendar.util.Callback;
import edu.deanza.calendar.util.Utilities;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by karinaantonio on 9/11/16.
 */

public class FirebaseSubscriptionDao implements SubscriptionDao {

    private DatabaseReference root;
    private SubscriptionMapper mapper = new SubscriptionMapper();

    public FirebaseSubscriptionDao(String uid) {
        root = Utilities.getFirebase().getReference().child("users").child(uid);
    }

    @Override
    public void getUserSubscriptions(final Callback<Map<String, Subscription>> callback) {
        root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot subscriptionNodes) {
                Map<String, Subscription> subscriptions = new HashMap<>();
                for (DataSnapshot node : subscriptionNodes.getChildren()) {
                    Map<Object, Object> rawSubscription = (Map<Object, Object>) node.getValue();
                    String key = node.getKey();
                    subscriptions.put(key, mapper.map(key, rawSubscription));
                }
                callback.setArgument(subscriptions);
                callback.run();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "SubscriptionListener:onCancelled", databaseError.toException());
            }
        });
    }

    @Override
    public void add(Subscription subscription) {
        root.child(subscription.getKey()).setValue(mapper.reverseMap(subscription));
    }

    @Override
    public void update(Subscription subscription) {
        // DatabaseReference#setValue clobbers
        add(subscription);
    }

    @Override
    public void remove(Subscription subscription) {
        root.child(subscription.getKey()).setValue(null);
    }

}
