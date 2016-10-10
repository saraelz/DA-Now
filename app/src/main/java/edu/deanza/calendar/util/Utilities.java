package edu.deanza.calendar.util;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Karina on 10/10/2016.
 */

public class Utilities {

    private static Utilities ourInstance = new Utilities();
    private static FirebaseDatabase firebase;

    {
        firebase = FirebaseDatabase.getInstance();
        firebase.setPersistenceEnabled(true);
    }

    public static Utilities getInstance() {
        return ourInstance;
    }

    public static FirebaseDatabase getFirebase() {
        return firebase;
    }

}
