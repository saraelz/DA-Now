package edu.deanza.calendar.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.deanza.calendar.R;
import edu.deanza.calendar.domain.models.Event;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class EventsAdapter extends
        RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public ImageButton favoriteButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.event_name);
            favoriteButton = (ImageButton) itemView.findViewById(R.id.favorite_button);
        }

    }

    //constructor & member variables

    // Store a member variable for the Events
    private List<Event> mEvents;
    // Store the context for easy access
    private Context mContext;

    // Pass in the Event array into the constructor
    public EventsAdapter(Context context, List<Event> events) {
        mEvents = events;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }
    
    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View eventView = inflater.inflate(R.layout.item_event, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(eventView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(EventsAdapter.ViewHolder viewHolder, int position) {
         // Get the data model based on position
        final Event event = mEvents.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.nameTextView;
        textView.setText(event.getName());

        /*//Initialize button
        final ImageButton button = viewHolder.favoriteButton;
        Boolean clicked; // keeps track of state of button
        clicked = new Boolean(false); //change later to pull this value from personal user data in Firebase
        button.setTag(clicked); // setting to false - wasn't clicked

        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(getContext(), //try getApplicationContext()
                                event.getName() + " has been clicked!", Toast.LENGTH_SHORT).show();

                        //set button as "clicked"
                        if( ((Boolean) button.getTag())==false ){
                            button.setImageResource(R.drawable.ic_favorite);
                            button.setTag(new Boolean(true));

                            if(event.getEnd().isBeforeNow())
                            {
                                Toast.makeText(getContext(), //try getApplicationContext()
                                        "This event has passed.", Toast.LENGTH_SHORT).show();
                            }
                            // 2do: add dialog boxes
                            // https://developer.android.com/guide/topics/ui/dialogs.html
                        }

                        //"unclick" button, undo changes
                        else {
                            button.setImageResource(R.drawable.ic_favorite_border);
                            button.setTag(new Boolean(false));
                        }
                    }});*/


    }



    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}