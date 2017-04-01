package edu.deanza.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.R;
import edu.deanza.calendar.activities.adapters.EventsAdapter;
import edu.deanza.calendar.activities.adapters.OrganizationInfoAdapter;
import edu.deanza.calendar.activities.views.SubscribeButtonWrapper;
import edu.deanza.calendar.dal.FirebaseEventRepository;
import edu.deanza.calendar.dal.FirebaseSubscriptionDao;
import edu.deanza.calendar.dal.interfaces.EventRepository;
import edu.deanza.calendar.domain.interfaces.SubscriptionDao;
import edu.deanza.calendar.domain.Club;
import edu.deanza.calendar.domain.Day;
import edu.deanza.calendar.domain.Event;
import edu.deanza.calendar.domain.Meeting;
import edu.deanza.calendar.domain.Organization;
import edu.deanza.calendar.domain.Subscription;
import edu.deanza.calendar.util.Callback;

public class OrganizationInfo extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrganizationInfoAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Organization organization = (Organization) intent.getSerializableExtra("edu.deanza.calendar.models.Organization");
        String UID = intent.getStringExtra("UID");
        SubscriptionDao subscriptionDao = new FirebaseSubscriptionDao(UID);

        setText(organization);

        SubscribeButtonWrapper subscribeButton = new SubscribeButtonWrapper(
                (ImageButton) findViewById(R.id.fab),
                this,
                organization,
                subscriptionDao
        ) {
            @Override
            protected void postSubscriptionChange() {
                super.postSubscriptionChange();
                adapter.notifyDataSetChanged();
            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.organization_info_recycler_view);
        initializeRecyclerView(recyclerView);
        initializeAdapter(organization, subscriptionDao, UID);
    }
    
    private void setText(Organization organization) {
        setTitle(organization.getName());

        TextView meetingLocation = (TextView) findViewById(R.id.organization_info_location);
        TextView description = (TextView) findViewById(R.id.organization_info_description);
        TextView meetingDays = (TextView) findViewById(R.id.organization_info_meeting_days);
        meetingLocation.setText(organization.getLocation());
        description.setText(organization.getDescription());
        if (organization instanceof Club) {
            for (Day day : ((Club) organization).getMeetingDays()) {
                meetingDays.setText(day.fullName() + 's');
            }
        }
        else {
            TextView meetingDaysLabel = (TextView) findViewById(R.id.organization_info_meeting_days_label);
            meetingDaysLabel.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initializeAdapter(Organization organization, final SubscriptionDao subscriptionDao,
                                   final String UID) {
        adapter = new OrganizationInfoAdapter(
                this, new ArrayList<Meeting>(), subscriptionDao, organization);
        adapter.setOnEventClickListener(new EventsAdapter.ClickListener() {
            @Override
            public void onItemClick(Event clickedEvent) {
                Intent intent = new Intent(getBaseContext(), EventInfo.class);
                intent.putExtra("edu.deanza.calendar.models.Event", clickedEvent);
                intent.putExtra("UID", UID);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter.getSectionedAdapter());

        fetchData(organization, subscriptionDao);
    }

    private void fetchData(final Organization organization, SubscriptionDao subscriptionDao) {
        organization.getEvents(new Callback<Event>() {
            @Override
            protected void call(Event data) {
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

}

