package edu.deanza.calendar.domain.interfaces;

import edu.deanza.calendar.domain.Subscription;

/**
 * Created by karinaantonio on 9/16/16.
 */

public interface Subscribable {

    Subscription getSubscription();

    void subscribe(Subscription subscription, SubscriptionDao dao);

    void unsubscribe(SubscriptionDao dao);

    boolean isSubscribed();

    String getName();

    String getKey();

}
