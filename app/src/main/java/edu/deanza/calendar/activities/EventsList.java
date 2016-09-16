package edu.deanza.calendar.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.deanza.calendar.R;
import edu.deanza.calendar.domain.models.Event;

public class EventsList extends AppCompatActivity {

    //public FirebaseEventRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);
        /*repository = new FirebaseEventRepository();
        LocalDate today = new LocalDate();
        List<Event> e = new ArrayList<>();
        repository.after(today, e);*/

        List<Event> e = new ArrayList<Event>();
        //e.add(new Event("Self care week", "We'll have dogs in the quad!", "Main quad", "DASB", DateTime.parse("2012-01-10T23:13:26"), DateTime.parse("2012-01-10T23:13:26")));
        //e.add(new Event("Tent City", "Totally radical sleepover", "Main quad", "SFJ", DateTime.parse("2016-01-10T23:13:26"), DateTime.parse("2016-01-10T23:13:26")));
        populateListView(e);
    }

    protected void populateListView(List<Event> events)
    {
        // Grab RecyclerView
        RecyclerView rvEvents = (RecyclerView) findViewById(R.id.rvEvents);

        // Attach the adapter to the RecyclerView to populate items
        EventsAdapter adapter = new EventsAdapter(this, events);
        rvEvents.setAdapter(adapter);

        // Set layout manager to position the items
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
    }
}
