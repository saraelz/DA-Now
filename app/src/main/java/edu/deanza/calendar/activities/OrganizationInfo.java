package edu.deanza.calendar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.OnClickOrganizationSubscribeDialog;
import edu.deanza.calendar.R;
import edu.deanza.calendar.SimpleSectionedRecyclerViewAdapter;
import edu.deanza.calendar.dal.FirebaseSubscriptionDao;
import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.models.Club;
import edu.deanza.calendar.domain.models.Day;
import edu.deanza.calendar.domain.models.Event;
import edu.deanza.calendar.domain.models.Meeting;
import edu.deanza.calendar.domain.models.Organization;
import edu.deanza.calendar.domain.models.Subscription;
import edu.deanza.calendar.util.Callback;

public class OrganizationInfo extends AppCompatActivity {

    private Organization organization;
    private SubscriptionDao subscriptionDao;
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
        organization = (Organization) intent.getSerializableExtra("edu.deanza.calendar.models.Organization");
        final String UID = intent.getStringExtra("UID");
        subscriptionDao = new FirebaseSubscriptionDao(UID);

        setTitle(organization.getName());

        final FloatingActionButton subscribeButton = (FloatingActionButton) findViewById(R.id.fab);
        if (organization.isSubscribed()) {
            subscribeButton.setImageResource(R.drawable.ic_favorite_border);
        }
        else {
            subscribeButton.setImageResource(R.drawable.ic_favorite);
        }
        subscribeButton.setOnClickListener(new OnClickOrganizationSubscribeDialog(this, organization, subscriptionDao) {
            @Override
            protected void postSubscribe() {
                subscribeButton.setImageResource(R.drawable.ic_favorite);
            }

            @Override
            protected void postUnsubscribe() {
                subscribeButton.setImageResource(R.drawable.ic_favorite_border);
            }
        });

        TextView meetingLocation = (TextView) findViewById(R.id.organization_info_location);
        meetingLocation.setText(organization.getLocation());

        TextView description = (TextView) findViewById(R.id.organization_info_description);
        description.setText(organization.getDescription());

        TextView meetingDays = (TextView) findViewById(R.id.organization_info_meeting_days);
        if (organization instanceof Club) {
            for (Day day : ((Club) organization).getMeetingDays()) {
                meetingDays.setText(day.fullName() + 's');
            }
        }
        else {
            TextView meetingDaysLabel = (TextView) findViewById(R.id.organization_info_meeting_days_label);
            meetingDaysLabel.setVisibility(View.INVISIBLE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.organization_info_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        final Context context = this;

        adapter = new OrganizationInfoAdapter(this, new ArrayList<Meeting>(), subscriptionDao, organization);
        adapter.setOnEventClickListener(new EventsAdapter.ClickListener() {
            @Override
            public void onItemClick(Event clickedEvent) {
                Intent intent = new Intent(context, EventInfo.class);
                intent.putExtra("edu.deanza.calendar.models.Event", clickedEvent);
                intent.putExtra("UID", UID);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter.getSectionedAdapter());

        subscriptionDao.getUserSubscriptions(new Callback<Map<String, Subscription>>() {
            @Override
            protected void call(Map<String, Subscription> data) {
                adapter.addSubscriptions(data);
            }
        });

        organization.getEvents(new Callback<Event>() {
            @Override
            protected void call(Event data) {
                adapter.add(data);
            }
        });

    }
}

