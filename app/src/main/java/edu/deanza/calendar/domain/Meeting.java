package edu.deanza.calendar.domain;

import org.joda.time.DateTime;

import java.io.Serializable;

import edu.deanza.calendar.dal.interfaces.SubscriptionDao;
import edu.deanza.calendar.dal.interfaces.Subscribable;

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
    public void subscribe(Subscription subscription) {
        this.subscription = subscription;
    }

    public void organizationSubscribe(OrganizationSubscription subscription) {
        this.subscription = new Subscription(
                getKey(),
                subscription.getNotifyBefore(),
                subscription.getTimeUnit()
        );
    }

    @Override
    public void subscribe(Subscription subscription, SubscriptionDao dao) {
        subscribe(subscription);
        dao.add(subscription);
    }

    public void organizationSubscribe(OrganizationSubscription subscription, SubscriptionDao dao) {
        organizationSubscribe(subscription);
        dao.add(this.subscription);
    }

    @Override
    public void unsubscribe() {
        this.subscription = null;
    }

    @Override
    public void unsubscribe(SubscriptionDao dao) {
        dao.remove(subscription);
        unsubscribe();
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


