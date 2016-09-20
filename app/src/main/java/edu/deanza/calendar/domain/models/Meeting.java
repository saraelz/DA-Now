package edu.deanza.calendar.domain.models;

import org.joda.time.DateTime;

import java.io.Serializable;

import edu.deanza.calendar.domain.Subscribable;

/**
 * Created by karinaantonio on 9/20/16.
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

    @Override
    public void unsubscribe() {
        this.subscription = null;
    }

    @Override
    public abstract String getKey();

}


