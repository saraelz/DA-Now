package edu.deanza.calendar.activities;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import edu.deanza.calendar.R;
import edu.deanza.calendar.OnClickSubscribeTimeDialog;
import edu.deanza.calendar.SubscribeOnClickListener;
import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.models.Event;

public class EventsAdapter extends SubscribableAdapter<Event, EventsAdapter.EventItemViewHolder> {

    private int todayPosition;

    public EventsAdapter(Context context, List<Event> subscribables, SubscriptionDao subscriptionDao) {
        super(context, subscribables, subscriptionDao);
        todayPosition = -1;
    }

    public int getTodayPosition() {
        return  todayPosition;
    }

    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(Event clickedEvent);
    }

    public void setOnItemClickListener(ClickListener listener) {
        clickListener = listener;
    }

    public static class EventItemViewHolder extends SubscribableAdapter.SubscribableItemViewHolder {

        private TextView eventName;
        private TextView eventTime;
        private TextView eventDayOfMonth;
        private TextView eventWeekday;
        private TextView eventOrganizations;

        public EventItemViewHolder(View containingItem) {
            super(containingItem);
            eventName = (TextView) containingItem.findViewById(R.id.item_event_name);
            eventTime = (TextView) containingItem.findViewById(R.id.item_event_time);
            eventDayOfMonth = (TextView) containingItem.findViewById(R.id.item_event_dom);
            eventWeekday = (TextView) containingItem.findViewById(R.id.item_event_dow);
            eventOrganizations = (TextView) containingItem.findViewById(R.id.item_event_organizations);
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

        viewHolder.eventName.setText(event.getName());

        Iterator<String> i = event.getOrganizationNames().iterator();
        if (i.hasNext()) {
            StringBuilder organizationNamesList = new StringBuilder();
            organizationNamesList.append(i.next());
            while (i.hasNext()) {
                organizationNamesList.append(", ");
                organizationNamesList.append(i.next());
            }
            viewHolder.eventOrganizations.setText(organizationNamesList.toString());
        }
        else {
            viewHolder.itemView.findViewById(R.id.item_event_organizations_label)
                    .setVisibility(View.INVISIBLE);
        }

        DateTime startDate = event.getStart();
        DateTime endDate = event.getEnd();
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("h:mm aa");
        viewHolder.eventTime.setText(timeFormatter.print(startDate) + " - " + timeFormatter.print(endDate));

        Date today = new Date();
        if (startDate.toDate().equals(today)){
            todayPosition = position;
        }
        else if (startDate.toDate().after(today) && todayPosition == -1) {
            todayPosition = position;
        }

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