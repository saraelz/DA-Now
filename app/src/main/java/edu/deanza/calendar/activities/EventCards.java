package edu.deanza.calendar.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.joda.time.DateTime;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.FirebaseEventRepository;
import edu.deanza.calendar.dal.FirebaseOrganizationRepository;
import edu.deanza.calendar.domain.EventRepository;
import edu.deanza.calendar.domain.OrganizationRepository;
import edu.deanza.calendar.domain.models.Event;
import edu.deanza.calendar.util.Callback;

public class EventCards extends Fragment{

    private EventRepository repository = new FirebaseEventRepository();
    private RecyclerView cardView;
    private EventsAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //initializeList();
        getActivity().setTitle("Events Calendar");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_card, container, false);

        cardView = (RecyclerView) view.findViewById(R.id.cardView);
        cardView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cardView.setLayoutManager(layoutManager);


        adapter = new EventsAdapter(getContext(), new ArrayList<Event>());

        adapter.setOnItemClickListener(new EventsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
            }
        });

        adapter.setHasStableIds(true);
        cardView.setAdapter(adapter);

        repository.all(new Callback<List<Event>>() {
            @Override
            protected void call(List<Event> data) {
                adapter.repopulate(data);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /*public void initializeList() {


        List<String> sponsors = new ArrayList<String>();
        sponsors.add("DASB");

        events.add(new Event("Tent City", "Radical sleepover!", "Quad", sponsors,
                DateTime.now(), DateTime.now().plusHours(2), new FirebaseOrganizationRepository()));
    }*/
}
