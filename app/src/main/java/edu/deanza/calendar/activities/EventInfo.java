package edu.deanza.calendar.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

//import org.ocpsoft.prettytime.i18n;

import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.FirebaseSubscriptionDao;
import edu.deanza.calendar.domain.interfaces.SubscriptionDao;
import edu.deanza.calendar.domain.Event;
import edu.deanza.calendar.activities.views.SubscribeButtonWrapper;

public class EventInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("edu.deanza.calendar.models.Event");
        SubscriptionDao subscriptionDao = new FirebaseSubscriptionDao(intent.getStringExtra("UID"));

        setText(event);

        SubscribeButtonWrapper subscribeButton = new SubscribeButtonWrapper(
                (ImageButton) findViewById(R.id.fab),
                this,
                event,
                subscriptionDao
        );

        /*
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>This is the text that will be shared.</p>"));
        startActivity(Intent.createChooser(sharingIntent,"Share using"));
         */
    }

    private void setText(Event event) {
        setTitle(event.getName());

        TextView time = (TextView) findViewById(R.id.event_time);
        TextView location = (TextView) findViewById(R.id.event_location);
        TextView description = (TextView) findViewById(R.id.event_description);
        time.setText(prettyPrintInterval(event.getStart(), event.getEnd()));
        location.setText(event.getLocation());
        description.setText(event.getDescription());
    }

    private String prettyPrintInterval(DateTime start, DateTime end) {
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
}
