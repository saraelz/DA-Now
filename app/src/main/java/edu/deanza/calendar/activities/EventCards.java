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
import edu.deanza.calendar.util.SectionedRecyclerViewAdapter;
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

    private RecyclerView cardView;
    private EventsAdapter adapter;
    private LinearLayoutManager layoutManager;

    private static final String THIS_TAG = EventCards.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_event_cards, container, false);
        cardView = (RecyclerView) view.findViewById(R.id.cardView);
        initializeRecyclerView(cardView);

        return view;
    }

    private void initializeRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initializeAdapter(final Context context) {
        final String UID = new UidGenerator(context, THIS_TAG).generate();
        SubscriptionDao subscriptionDao = new FirebaseSubscriptionDao(UID);

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

        final SectionedRecyclerViewAdapter sectionedAdapter = new SectionedRecyclerViewAdapter<>(
                context,
                R.layout.section,
                R.id.section_text,
                adapter
        );
        cardView.setAdapter(sectionedAdapter);

        fetchData(new FirebaseEventRepository(context), subscriptionDao, sectionedAdapter);
    }

    private void fetchData(EventRepository eventRepository, SubscriptionDao subscriptionDao,
                           final SectionedRecyclerViewAdapter sectionedAdapter) {
        final List<SectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();
        eventRepository.all(new Callback<Event>() {
            @Override
            protected void call(Event data) {
                if (adapter.willAddNewMonth(data)) {
                    int newIndex = adapter.getItemCount();
                    int newMonth = data.getStart().getMonthOfYear();
                    sections.add(new SectionedRecyclerViewAdapter.Section(
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

        subscriptionDao.getUserSubscriptions(new Callback<Map<String, Subscription>>() {
            @Override
            protected void call(Map<String, Subscription> data) {
                adapter.addSubscriptions(data);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Events Calendar");
        initializeAdapter(getContext());
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
