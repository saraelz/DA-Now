package edu.deanza.calendar.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import edu.deanza.calendar.OnClickSubscribeTimeDialog;
import edu.deanza.calendar.R;
import edu.deanza.calendar.SubscribeOnClickListener;
import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.models.Event;
import edu.deanza.calendar.domain.models.Meeting;

public class EventsAdapter extends MeetingsAdapter {

    public EventsAdapter(Context context, List<Meeting> subscribables, SubscriptionDao subscriptionDao) {
        super(context, subscribables, subscriptionDao);
    }

    private static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(Event clickedEvent);
    }

    public void setOnItemClickListener(ClickListener listener) {
        clickListener = listener;
    }

    public static class EventItemViewHolder extends MeetingsAdapter.MeetingItemViewHolder {

        private TextView eventName;
        private TextView eventOrganizations;

        public EventItemViewHolder(View containingItem) {
            super(containingItem);
            eventName = (TextView) containingItem.findViewById(R.id.item_event_name);
            eventOrganizations = (TextView) containingItem.findViewById(R.id.item_event_organizations);

            meetingTime = (TextView) containingItem.findViewById(R.id.item_event_time);
            meetingDayOfMonth = (TextView) containingItem.findViewById(R.id.item_event_dom);
            meetingWeekday = (TextView) containingItem.findViewById(R.id.item_event_dow);
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
    public void onBindViewHolder(MeetingItemViewHolder meetingViewHolder, int position) {
        super.onBindViewHolder(meetingViewHolder, position);

        Event event = (Event) subscribables.get(position);
        EventItemViewHolder viewHolder = (EventItemViewHolder) meetingViewHolder;

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
    SubscribeOnClickListener getSubscribeOnClickListener(final MeetingItemViewHolder viewHolder,
                                                         Meeting meeting) {
        Event event = (Event) meeting;
        final String name = event.getName();
        final EventsAdapter us = this;

        return new OnClickSubscribeTimeDialog(context, event, subscriptionDao) {
            @Override
            protected void postSubscribe() {
                us.postSubscribe(viewHolder, name);
            }

            @Override
            protected void postUnsubscribe() {
                us.postUnsubscribe(viewHolder, name);
            }

            @Override
            protected void onCancel() {
                us.onCancel(viewHolder);
            }
        };
    }

}