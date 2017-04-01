package edu.deanza.calendar.domain;

import org.joda.time.DateTime;

import java.io.Serializable;

import edu.deanza.calendar.domain.interfaces.Subscribable;
import edu.deanza.calendar.domain.interfaces.SubscriptionDao;

/**
 * All meetings belong to an Organization.
 */

public abstract class Meeting implements Subscribable, Serializable {

    final DateTime start;
    final DateTime end;
    Subscription subscription;

    public Meeting(DateTime start, DateTime end) {
        this.start = start;
        this.end = end;
    }

    public DateTime getStart() {
        return start;
    }

    public DateTime getEnd() {
        return end;
    }

    @Override
    public Subscription getSubscription() {
        return subscription;
    }

    @Override
    public void subscribe(Subscription subscription, SubscriptionDao dao) {
        this.subscription = subscription;
        dao.add(subscription);
    }

    @Override
    public void unsubscribe(SubscriptionDao dao) {
        dao.remove(subscription);
        this.subscription = null;
    }

    @Override
    public boolean isSubscribed() {
        return subscription != null;
    }

    @Override
    public abstract String getKey();

    @Override
    public abstract String getName();

}


