package edu.deanza.calendar.activities;

import android.content.Context;
import android.content.Intent;
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

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.R;
import edu.deanza.calendar.SimpleSectionedRecyclerViewAdapter;
import edu.deanza.calendar.dal.FirebaseEventRepository;
import edu.deanza.calendar.dal.FirebaseSubscriptionDao;
import edu.deanza.calendar.domain.SubscriptionDao;
import edu.deanza.calendar.domain.EventRepository;
import edu.deanza.calendar.domain.models.Event;
import edu.deanza.calendar.domain.models.Meeting;
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
        setHasOptionsMenu(true);

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

        adapter = new EventsAdapter(context, new ArrayList<Meeting>(), subscriptionDao);
        adapter.setHasStableIds(true);
        adapter.setOnItemClickListener(new EventsAdapter.ClickListener() {
            @Override
            public void onItemClick(Event clickedEvent) {
                Intent intent = new Intent(context, EventInfo.class);
                intent.putExtra("edu.deanza.calendar.models.Event", clickedEvent);
                intent.putExtra("UID", UID);
                startActivity(intent);

            }
        });

        final SimpleSectionedRecyclerViewAdapter sectionedAdapter = new SimpleSectionedRecyclerViewAdapter(
                context,
                R.layout.section,
                R.id.section_text,
                adapter
        );
        cardView.setAdapter(sectionedAdapter);

        final List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
        repository.all(new Callback<Event>() {
            @Override
            protected void call(Event data) {
                if (adapter.willAddNewMonth(data)) {
                    int newIndex = adapter.getItemCount();
                    int newMonth = data.getStart().getMonthOfYear();
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(
                            newIndex,
                            new DateFormatSymbols()
                                    .getMonths()
                                    [newMonth-1])
                    );
                    sectionedAdapter.setSections(sections);
                }
                adapter.add(data);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.events_toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_today:
                return scrollToSoonestEvent();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean scrollToSoonestEvent() {
        int soonestIndex = adapter.getSoonestIndex();
        if (soonestIndex != -1)
            layoutManager.scrollToPositionWithOffset(soonestIndex, 0);
        else
            Toast.makeText(getContext(), "No upcoming events.", Toast.LENGTH_LONG).show();
        return true;
    }
}
