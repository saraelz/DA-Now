package edu.deanza.calendar.domain.models;

import java.util.concurrent.TimeUnit;

/**
 * Created by karinaantonio on 9/11/16.
 */

public class OrganizationSubscription extends Subscription {

    private boolean notifyMeetings;

    public OrganizationSubscription(String key, long notifyBefore, TimeUnit timeUnit, boolean notifyMeetings) {
        super(key, notifyBefore, timeUnit);
        this.notifyMeetings = notifyMeetings;
    }

    public boolean isNotifyingMeetings() {
        return notifyMeetings;
    }

    public void setNotifyMeetings(boolean notifyMeetings) {
        this.notifyMeetings = notifyMeetings;
    }

}
