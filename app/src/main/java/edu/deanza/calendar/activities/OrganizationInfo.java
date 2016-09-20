package edu.deanza.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Map;

import edu.deanza.calendar.OnClickOrganizationSubscribeDialog;
import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.FirebaseSubscriptionDao;
import edu.deanza.calendar.dal.SubscriptionDao;
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
        subscriptionDao = new FirebaseSubscriptionDao(intent.getStringExtra("UID"));

        setTitle(organization.getName());

        final FloatingActionButton subscribeButton = (FloatingActionButton) findViewById(R.id.fab);
        if (organization.getSubscription() == null) {
            subscribeButton.setImageResource(R.drawable.ic_favorite_border);
        }
        else {
            subscribeButton.setImageResource(R.drawable.ic_favorite);
        }
        subscribeButton.setOnClickListener(new OnClickOrganizationSubscribeDialog(this, organization, subscriptionDao) {
            @Override
            protected void postSubscribe() {
                super.postSubscribe();
                subscribeButton.setImageResource(R.drawable.ic_favorite);
            }

            @Override
            protected void postUnsubscribe() {
                super.postUnsubscribe();
                subscribeButton.setImageResource(R.drawable.ic_favorite_border);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.organization_info_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new OrganizationInfoAdapter(this, new ArrayList<Meeting>(), subscriptionDao, organization);
        adapter.setHasStableIds(true);
        adapter.setOnEventClickListener(new EventsAdapter.ClickListener() {
            @Override
            public void onItemClick(Event clickedEvent) {
                // TODO: Intent to EventInfo
            }
        });
        recyclerView.setAdapter(adapter);

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

