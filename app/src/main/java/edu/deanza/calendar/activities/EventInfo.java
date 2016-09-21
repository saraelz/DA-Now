package edu.deanza.calendar.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import edu.deanza.calendar.OnClickOrganizationSubscribeDialog;
import edu.deanza.calendar.OnClickSubscribeTimeDialog;
import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.FirebaseSubscriptionDao;
import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.models.Event;

public class EventInfo extends AppCompatActivity {

    Event event;
    SubscriptionDao subscriptionDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("edu.deanza.calendar.models.Event");
        subscriptionDao = new FirebaseSubscriptionDao(intent.getStringExtra("UID"));

        setTitle(event.getName());

        /*FloatingActionButton subscribeButton = (FloatingActionButton) findViewById(R.id.fab);
        if (event.getSubscription() == null) {
            subscribeButton.setImageResource(R.drawable.ic_favorite_border);
        }
        else {
            subscribeButton.setImageResource(R.drawable.ic_favorite);
        }
        subscribeButton.setOnClickListener(new OnClickSubscribeTimeDialog(this, event, subscriptionDao));*/

    }
}
