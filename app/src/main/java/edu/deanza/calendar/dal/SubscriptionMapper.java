package edu.deanza.calendar.dal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import edu.deanza.calendar.domain.models.OrganizationSubscription;
import edu.deanza.calendar.domain.models.Subscription;

/**
 * Created by karinaantonio on 9/11/16.
 */

public class SubscriptionMapper implements DataMapper<Subscription>, Serializable {

    @Override
    public Subscription map(String key, Map<Object, Object> rawData) {
        long notifyBefore = (long) rawData.get("notification");
        TimeUnit timeUnit;
        switch ((String) rawData.get("timeUnit")) {
            case "d":
                timeUnit = TimeUnit.DAYS;
                break;
            case "h":
                timeUnit = TimeUnit.HOURS;
                break;
            case "m":
            default:
                timeUnit = TimeUnit.MINUTES;
                break;
        }

        Subscription s;
        if (key.contains("|")) {
            // s is an Meeting subscription
            s = new Subscription(key, notifyBefore, timeUnit);
        }
        else {
            boolean notifyMeetings = (boolean) rawData.get("notifyMeetings");
            boolean notifyEvents = (boolean) rawData.get("notifyEvents");
            s = new OrganizationSubscription(key, notifyBefore, timeUnit, notifyMeetings, notifyEvents);
        }

        return s;
    }

    public Map<String, Object> reverseMap(Subscription subscription) {
        Map<String, Object> rawMap = new HashMap<>();

        rawMap.put("notification", subscription.getNotifyBefore());

        String timeUnit;
        switch (subscription.getTimeUnit()) {
            case DAYS:
                timeUnit = "d";
                break;
            case HOURS:
                timeUnit = "h";
                break;
            case MINUTES:
            default:
                timeUnit = "m";
                break;
        }
        rawMap.put("timeUnit", timeUnit);

        if (subscription instanceof OrganizationSubscription) {
            rawMap.put("notifyMeetings", ((OrganizationSubscription) subscription).isNotifyingMeetings());
            rawMap.put("notifyEvents", ((OrganizationSubscription) subscription).isNotifyingEvents());
        }

        return rawMap;
    }

}
