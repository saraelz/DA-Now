package edu.deanza.calendar.domain.models;

import java.io.Serializable;

/**
 * Created by karinaantonio on 8/10/16.
 */

public enum Day implements Serializable {

    SUNDAY("Sunday", "Sun"),
    MONDAY("Monday", "Mon"),
    TUESDAY("Tuesday", "Tue"),
    WEDNESDAY("Wednesday", "Wed"),
    THURSDAY("Thursday", "Thu"),
    FRIDAY("Friday", "Fri"),
    SATURDAY("Saturday", "Sat");

    private final String full;
    private final String abbreviated;

    private Day(String full, String abbreviated) {
        this.full = full;
        this.abbreviated = abbreviated;
    }

    public String fullName() {
        return full;
    }

    public String abbreviatedName() {
        return abbreviated;
    }

}
