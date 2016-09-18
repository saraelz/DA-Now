package edu.deanza.calendar.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.FirebaseEventRepository;
import edu.deanza.calendar.dal.FirebaseSubscriptionDao;
import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.EventRepository;
import edu.deanza.calendar.domain.models.Event;
import edu.deanza.calendar.domain.models.Subscription;
import edu.deanza.calendar.util.Callback;
import edu.deanza.calendar.util.UidGenerator;

public class EventCards extends Fragment {

    private EventRepository repository = new FirebaseEventRepository();
    private SubscriptionDao subscriptionDao;
    private RecyclerView cardView;
    private EventsAdapter adapter;
    private LinearLayoutManager layoutManager;

    private static final String THIS_TAG = EventCards.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Events Calendar");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //set toolbar icons
        setHasOptionsMenu(true);

        // TODO: Give UID to cards fragment
        final Context context = getContext();
        final String UID = new UidGenerator(context, THIS_TAG).generate();
        subscriptionDao = new FirebaseSubscriptionDao(UID);
        subscriptionDao.getUserSubscriptions(new Callback<Map<String, Subscription>>() {
            @Override
            protected void call(Map<String, Subscription> data) {
                adapter.addSubscriptions(data);
            }
        });

        View view = inflater.inflate(R.layout.fragment_event_cards, container, false);
        cardView = (RecyclerView) view.findViewById(R.id.cardView);
        cardView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cardView.setLayoutManager(layoutManager);

        adapter = new EventsAdapter(getContext(), new ArrayList<Event>(), subscriptionDao);
        adapter.setHasStableIds(true);
        // TODO: Intent to EventInfo
//        adapter.setOnItemClickListener(new EventsAdapter.ClickListener() {
//            @Override
//            public void onItemClick(int position, View v) {
//            }
//        });
        cardView.setAdapter(adapter);

        repository.all(new Callback<Event>() {
            @Override
            protected void call(Event data) {
                adapter.add(data);
            }
        });        repository.all(new Callback<Event>() {
            @Override
            protected void call(Event data) {
                adapter.add(data);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //set toolbar icons
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.events_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //Toast.makeText(getContext(), "Home button pressed", Toast.LENGTH_LONG).show();
                return true;
            case R.id.menu_today:
                //scroll to today's event or closest upcoming event
                int today = adapter.getTodayPosition();
                if (today != -1)
                    layoutManager.scrollToPositionWithOffset(today, 20);
                else
                    Toast.makeText(getContext(), "No upcoming events.", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
