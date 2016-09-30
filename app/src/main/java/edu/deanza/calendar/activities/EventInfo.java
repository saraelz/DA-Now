package edu.deanza.calendar.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

//package org.ocpsoft.prettytime.i18n;

import edu.deanza.calendar.activities.listeners.OnClickSubscribeTimeDialog;
import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.FirebaseSubscriptionDao;
import edu.deanza.calendar.domain.SubscriptionDao;
import edu.deanza.calendar.domain.models.Event;

public class EventInfo extends AppCompatActivity {

    Event event;
    SubscriptionDao subscriptionDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        event = (Event) intent.getSerializableExtra("edu.deanza.calendar.models.Event");
        subscriptionDao = new FirebaseSubscriptionDao(intent.getStringExtra("UID"));

        final String name = event.getName();
        setTitle(name);

        TextView location = (TextView) findViewById(R.id.event_location);
        location.setText(event.getLocation());

        TextView time = (TextView) findViewById(R.id.event_time);
        time.setText(print_date());

        TextView description = (TextView) findViewById(R.id.event_description);
        description.setText(event.getDescription());

        //new PrettyTime().format(event.getStart());

        final FloatingActionButton subscribeButton = (FloatingActionButton) findViewById(R.id.fab);
        if (event.isSubscribed()) {
            subscribeButton.setImageResource(R.drawable.ic_favorite_border);
        }
        else {
            subscribeButton.setImageResource(R.drawable.ic_favorite);
        }
        subscribeButton.setOnClickListener(new OnClickSubscribeTimeDialog(this, event, subscriptionDao) {
            @Override
            public void postSubscribe() {
                subscribeButton.setImageResource(R.drawable.ic_favorite);
                Snackbar.make(subscribeButton,
                        "Subscribed to " + name,
                        Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            public void postUnsubscribe() {
                subscribeButton.setImageResource(R.drawable.ic_favorite_border);
                Snackbar.make(subscribeButton,
                        "Unsubscribed from " + name,
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        });

        /*
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>This is the text that will be shared.</p>"));
        startActivity(Intent.createChooser(sharingIntent,"Share using"));
         */

    }

    private String print_date() {
        DateTime start = event.getStart();
        DateTime end = event.getEnd();

        DateTimeFormatter date_fmt = DateTimeFormat.forPattern("EEEE, MMM d");
        DateTimeFormatter time_fmt = DateTimeFormat.forPattern("h:ss aaaa");

        if (start.withTimeAtStartOfDay().isBefore(end.withTimeAtStartOfDay())) {
            return date_fmt.print(start) + " at " + time_fmt.print(start) + " to " + date_fmt.print(end) + " at " + time_fmt.print(end);
        }
        else if(start.withTimeAtStartOfDay().equals(DateTime.now().withTimeAtStartOfDay())) {
            return "Today at " + time_fmt.print(start) + " - " + time_fmt.print(end);
        }
        else {
            return date_fmt.print(start) + " at " + time_fmt.print(start) + " - " + time_fmt.print(end);
        }
    }

    private String print_event() {
        return event.getName() + "\n" + print_date() + "\n" + event.getLocation() + "\n\n" + event.getDescription();
    }
}
