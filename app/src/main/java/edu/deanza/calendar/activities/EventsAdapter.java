package edu.deanza.calendar.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.deanza.calendar.R;
import edu.deanza.calendar.domain.models.Event;

public class EventsAdapter
        extends RecyclerView.Adapter<EventsAdapter.EventItemViewHolder> {

    private Context context;
    private static ClickListener clickListener;
    public List<Event> events;

    public Context getContext() {
        return context;
    }


    public static class EventItemViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView eventName;
        private TextView eventDate;
        private ImageButton subscribeButton;

        public EventItemViewHolder(View containingItem) {
            super(containingItem);
            eventName = (TextView) containingItem.findViewById(R.id.item_event_name);
            eventDate = (TextView) containingItem.findViewById(R.id.item_event_date);
            subscribeButton = (ImageButton) containingItem.findViewById(R.id.item_event_subscribe);
            containingItem.setOnClickListener(this);
            subscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: subscribe, and delegate that handler to this Adapter's containing Activity/Fragment
                    Log.i("OrgItem/subscribeButton", "subscribed to some Event");
                    clickListener.onItemClick(getAdapterPosition(), view);
                }
            });
        }

        @Override
        public void onClick(View v) {
            // TODO: switch activities/fragments to the EventInfoPage, and delegate that handler
            Log.i("OrgItem", "*switch to some EventInfoPage now*");
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        //void onItemLongClick(int position, View v);
    }

    public EventsAdapter(Context context, List<Event> events) {
        this.context = context;
        this.events = events;
    }

    public void repopulate(List<Event> events) {
        this.events.clear();
        this.events.addAll(events);
        notifyDataSetChanged();
    }

    @Override
    public EventItemViewHolder onCreateViewHolder(ViewGroup eventList, int viewType) {
        View eventItem = LayoutInflater
                .from(eventList.getContext())
                .inflate(R.layout.item_event, eventList, false);

        return new EventItemViewHolder(eventItem);
    }


    @Override
    public void onBindViewHolder(EventItemViewHolder viewHolder, int position) {
        //grab this event and most recent event
        final Event event = events.get(position);

        //set name viewholder
        viewHolder.eventName.setText(event.getName());

        //set date viewholder
        viewHolder.eventDate.setText(String.valueOf(event.getStart().getDayOfMonth()));
        /*
        //Event previousEvent = events.get(position-1); --> gives error
        if (previousEvent.getStart().toDate() == event.getStart().toDate()){
            viewHolder.eventDate.setText("");
        }
        else{
            viewHolder.eventDate.setText(event.getStart().getDayOfMonth());
        }*/

        //Initialize button
        final ImageButton button = viewHolder.subscribeButton;
        Boolean clicked; // keeps track of state of button
        clicked = new Boolean(false); //change later to pull this value from personal user data in Firebase
        button.setTag(clicked); // setting to false - wasn't clicked

        // TODO: set icon according to whether or not org is already subscribed to
        // button.setImageDrawable();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                // toggle image and the org's subscription state (which updates the entry in the Repo)
                //set button as "clicked"
                if (((Boolean) button.getTag()) == false) {
                    final String options[] = {"10 minutes before", "15 minutes before", "30 minutes before", "60 minutes before", "Cancel"};

                    AlertDialog.Builder dialogAlert  = new AlertDialog.Builder(getContext());
                    dialogAlert.setTitle("Would you like a reminder?")
                            .setCancelable(true)
                            .setItems(options, new DialogInterface.OnClickListener(){
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //TODO: save preferences in Firebase
                                            button.setImageResource(R.drawable.ic_favorite);
                                            button.setTag(new Boolean(true));
                                            if (i != 4)//options.length()-1)
                                            {
                                                Snackbar.make(view, "Added " + event.getName() + " to calendar", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                            }
                                            else{
                                                Snackbar.make(view, "Action cancelled.", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                            }
                                        }
                                    })
                            .create()
                            .show();
                }

                //"unclick" button, undo changes
                else {
                    button.setImageResource(R.drawable.ic_favorite_border);
                    button.setTag(new Boolean(false));
                    Snackbar.make(view, "Removed " + event.getName() + " from calendar", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    @Override
    public long getItemId(int position) {
        return (long) events.get(position).hashCode();
    }
}