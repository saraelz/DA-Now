package edu.deanza.calendar.util;

/**
 * Created by Karina on 10/10/2016.
 */

public class FirebaseDatabase {

    private static FirebaseDatabase ourInstance = new FirebaseDatabase();
    private static com.google.firebase.database.FirebaseDatabase firebaseDatabase;

    {
        firebaseDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
    }

    public static FirebaseDatabase getInstance() {
        return ourInstance;
    }

    public static com.google.firebase.database.FirebaseDatabase getFirebase() {
        return firebaseDatabase;
    }

}
