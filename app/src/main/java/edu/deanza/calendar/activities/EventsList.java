package edu.deanza.calendar.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.FirebaseEventRepository;
import edu.deanza.calendar.domain.EventRepository;
import edu.deanza.calendar.domain.models.Event;
import edu.deanza.calendar.util.Callback;

public class EventsList extends AppCompatActivity {

    private EventRepository repository = new FirebaseEventRepository();
    private RecyclerView recyclerView;
    private EventsAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        recyclerView = (RecyclerView) findViewById(R.id.rvEvents);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new EventsAdapter(this, new ArrayList<Event>());
        adapter.setOnItemClickListener(new EventsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }
        });
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        LocalDate today = new LocalDate();
        /*repository.between(today, today.plusDays(7), new Callback<List<Event>>() {
            @Override
            protected void call(List<Event> data) {
                adapter.repopulate(data);
            }
        });*/
        repository.all(new Callback<List<Event>>() {
            @Override
            protected void call(List<Event> data) {
                adapter.repopulate(data);
            }
        });
    }
}