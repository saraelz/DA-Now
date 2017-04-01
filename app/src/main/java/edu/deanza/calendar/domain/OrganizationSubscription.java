package edu.deanza.calendar.domain;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Created by karinaantonio on 9/11/16.
 */

public class OrganizationSubscription extends Subscription implements Serializable {

    private final boolean notifyMeetings;
    private final boolean notifyEvents;

    public OrganizationSubscription(String key, long notifyBefore, TimeUnit timeUnit,
                                    boolean notifyMeetings, boolean notifyEvents) {
        super(key, notifyBefore, timeUnit);
        this.notifyMeetings = notifyMeetings;
        this.notifyEvents = notifyEvents;
    }

    public boolean isNotifyingMeetings() {
        return notifyMeetings;
    }

    public boolean isNotifyingEvents() {
        return notifyEvents;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrganizationSubscription{");
        sb.append(super.toString());
        sb.append("notifyMeetings=").append(notifyMeetings);
        sb.append(", notifyEvents=").append(notifyEvents);
        sb.append('}');
        return sb.toString();
    }

    public static class Builder extends Subscription.Builder {

        private boolean notifyMeetingsPiece;
        private boolean notifyEventsPiece;

        public Builder notifyMeetings(boolean notifyMeetings) {
            this.notifyMeetingsPiece = notifyMeetings;
            return this;
        }

        public Builder notifyEvents(boolean notifyEvents) {
            this.notifyEventsPiece = notifyEvents;
            return this;
        }

        @Override
        public OrganizationSubscription build() {
            return new OrganizationSubscription(
                    keyPiece,
                    notifyBeforePiece,
                    timeUnitPiece,
                    notifyMeetingsPiece,
                    notifyEventsPiece
            );
        }
    }

}
