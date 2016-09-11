package edu.deanza.calendar.domain.models;

import java.util.concurrent.TimeUnit;

/**
 * Created by karinaantonio on 9/11/16.
 */

public class Subscription {

    private String key;
    private long notifyBefore;
    private TimeUnit timeUnit;

    public Subscription(String key, long notifyBefore, TimeUnit timeUnit) {
        this.key = key;
        this.notifyBefore = notifyBefore;
        this.timeUnit = timeUnit;
    }

    public String getKey() {
        return key;
    }

    public long getNotifyBefore() {
        return notifyBefore;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setNotifyBefore(long notifyBefore) {
        this.notifyBefore = notifyBefore;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    // TODO: Implement notifications
    // public abstract void setNotification();

}
