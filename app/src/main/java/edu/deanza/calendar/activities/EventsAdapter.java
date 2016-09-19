package edu.deanza.calendar.activities;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.List;

import edu.deanza.calendar.R;
import edu.deanza.calendar.OnClickSubscribeTimeDialog;
import edu.deanza.calendar.SubscribeOnClickListener;
import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.models.Event;

public class EventsAdapter extends SubscribableAdapter<Event, EventsAdapter.EventItemViewHolder> {

    public EventsAdapter(Context context, List<Event> subscribables, SubscriptionDao subscriptionDao) {
        super(context, subscribables, subscriptionDao);
    }

    //which event is closest to today's date?
    public int getTodayIndex() {

        Date today = new Date();

        for (int i = 0; i < subscribables.size(); i++) {
            Date compare = subscribables.get(i).getStart().toDate();
            if (compare.equals(today) || compare.after(today)){
                return i + 1;
            }
        }

        return subscribables.size(); //try also subscribables.size{}-1;
    }

    // pre: newEvent - the event that we want to add to the adapter
    // post: returns true if parameter's month does not match  month of previous event in adapter
    // purpose: indicates whether we need to create a new "month" divider
    public boolean needsNewDivider(Event newEvent) {
        if (newEvent == null)
            return false;

        if (subscribables.size() <= 0)
            return true;

        Event lastEvent = (Event) subscribables.get(subscribables.size()-1);
        if (lastEvent.getEnd().getMonthOfYear() != newEvent.getStart().getMonthOfYear())
            return true;
        else
            return false;
    }

    // manage ClickListener data
    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(Event clickedEvent);
    }

    public void setOnItemClickListener(ClickListener listener) {
        clickListener = listener;
    }

    // based on item_card_event
    public static class EventItemViewHolder extends SubscribableAdapter.SubscribableItemViewHolder {

        private TextView eventName;
        private TextView eventTime;
        private TextView eventDayOfMonth;
        private TextView eventWeekday;

        public EventItemViewHolder(View containingItem) {
            super(containingItem);
            eventName = (TextView) containingItem.findViewById(R.id.event_name);
            eventTime = (TextView) containingItem.findViewById(R.id.event_time);
            eventDayOfMonth = (TextView) containingItem.findViewById(R.id.event_dom);
            eventWeekday = (TextView) containingItem.findViewById(R.id.event_weekday);
        }

        @Override
        int subscribeButtonId() {
            return R.id.item_event_subscribe;
        }
    }

    @Override
    public EventItemViewHolder onCreateViewHolder(ViewGroup eventList, int viewType) {
        View eventItem = LayoutInflater
                .from(eventList.getContext())
                .inflate(R.layout.item_card_event, eventList, false);

        return new EventItemViewHolder(eventItem);
    }


    @Override
    public void onBindViewHolder(EventItemViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        final Event event = subscribables.get(position);

        //set onClickListener for item
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(event);
            }
        });


        viewHolder.eventName.setText(event.getName());

        DateTime startDate = event.getStart();
        DateTime endDate = event.getEnd();
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("h:mm aa");
        viewHolder.eventTime.setText(timeFormatter.print(startDate) + " - " + timeFormatter.print(endDate));

        viewHolder.eventDayOfMonth.setText(String.valueOf(startDate.getDayOfMonth()));

        DateTimeFormatter weekdayFormatter = DateTimeFormat.forPattern("EEE");
        viewHolder.eventWeekday.setText(weekdayFormatter.print(startDate));

        /*if(position > 0 && position <= events.size()){
            Event previousEvent = events.get(position-1); // gives error
            //if previous event has same date, initialize string to empty
            //if previous event has different month, start a new section
            if (previousEvent.getStart().toDate() == event.getStart().toDate()){
                viewHolder.eventDayOfMonth.setText("");
                viewHolder.eventWeekday.setText("");
            }
        }*/

    }

    //subscribe to an event
    @Override
    SubscribeOnClickListener getSubscribeOnClickListener(final EventItemViewHolder viewHolder,
                                                         final Event event) {
        return new OnClickSubscribeTimeDialog(context, event, subscriptionDao) {
            @Override
            protected void postSubscribe() {
                super.postSubscribe();
                notifyItemChanged(viewHolder.getAdapterPosition());
                Snackbar.make(viewHolder.itemView,
                        "Subscribed to " + event.getName(),
                        Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            protected void postUnsubscribe() {
                super.postUnsubscribe();
                notifyItemChanged(viewHolder.getAdapterPosition());
                Snackbar.make(
                        viewHolder.itemView,
                        "Unsubscribed from " + event.getName(),
                        Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            protected void onCancel() {
                super.onCancel();
                Snackbar.make(
                        viewHolder.itemView,
                        "Action cancelled.",
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        };
    }
}