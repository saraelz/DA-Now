package edu.deanza.calendar.domain;

import edu.deanza.calendar.domain.models.Subscription;

/**
 * Created by karinaantonio on 9/16/16.
 */

public interface Subscribable {

    Subscription getSubscription();

    void subscribe(Subscription subscription);

    void subscribe(Subscription subscription, SubscriptionDao dao);

    void unsubscribe();

    void unsubscribe(SubscriptionDao dao);

    boolean isSubscribed();

    String getName();

    String getKey();

}
