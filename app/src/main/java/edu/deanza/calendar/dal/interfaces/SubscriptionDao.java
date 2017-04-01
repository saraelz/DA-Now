package edu.deanza.calendar.dal.interfaces;

import java.util.Map;

import edu.deanza.calendar.domain.Subscription;
import edu.deanza.calendar.util.Callback;

/**
 * Created by karinaantonio on 9/11/16.
 */

public interface SubscriptionDao {

    void getUserSubscriptions(Callback<Map<String, Subscription>> callback);

    void add(Subscription subscription);

    void update(Subscription subscription);

    void remove(Subscription subscription);

}
