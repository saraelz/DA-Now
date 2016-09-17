package edu.deanza.calendar.domain.models;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by karinaantonio on 9/11/16.
 */

public class Subscription implements Serializable {

    private final String key;
    private final long notifyBefore;
    private final TimeUnit timeUnit;

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

    // TODO: Implement notifications
    // public abstract void setNotification();

    public static class Builder {

        String keyPiece;
        long notifyBeforePiece;
        TimeUnit timeUnitPiece;

        public Builder key(String key) {
            keyPiece = key;
            return this;
        }

        public Builder notifyBefore(long notifyBefore) {
            notifyBeforePiece = notifyBefore;
            return this;
        }

        public Builder timeUnit(TimeUnit timeUnit) {
            timeUnitPiece = timeUnit;
            return this;
        }

        public Subscription build() {
            return new Subscription(keyPiece, notifyBeforePiece, timeUnitPiece);
        }

    }

}
