package edu.deanza.calendar.domain.models;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by karinaantonio on 9/11/16.
 */

public class OrganizationSubscription extends Subscription implements Serializable {

    private final boolean notifyMeetings;

    public OrganizationSubscription(String key, long notifyBefore, TimeUnit timeUnit, boolean notifyMeetings) {
        super(key, notifyBefore, timeUnit);
        this.notifyMeetings = notifyMeetings;
    }

    public boolean isNotifyingMeetings() {
        return notifyMeetings;
    }

    public static class Builder extends Subscription.Builder {

        private boolean notifyMeetingsPiece;

        public Subscription.Builder notifyMeetings(boolean notifyMeetings) {
            this.notifyMeetingsPiece = notifyMeetings;
            return this;
        }

        @Override
        public OrganizationSubscription build() {
            return new OrganizationSubscription(
                    keyPiece,
                    notifyBeforePiece,
                    timeUnitPiece,
                    notifyMeetingsPiece
            );
        }
    }

}
