package edu.deanza.calendar.activities;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import edu.deanza.calendar.activities.listeners.OnClickSubscribeTimeDialog;
import edu.deanza.calendar.R;
import edu.deanza.calendar.activities.listeners.SubscribeOnClickListener;
import edu.deanza.calendar.domain.SubscriptionDao;
import edu.deanza.calendar.domain.models.Event;
import edu.deanza.calendar.domain.models.Meeting;
import edu.deanza.calendar.domain.models.Organization;
import edu.deanza.calendar.util.Callback;
import edu.deanza.calendar.util.UidGenerator;

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

        final Event event = (Event) subscribables.get(position);
        final EventItemViewHolder viewHolder = (EventItemViewHolder) meetingViewHolder;

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(event);
            }
        });

        viewHolder.eventName.setText(event.getName());

        if (event.isAllDay()) {
            viewHolder.meetingTime.setText("All day");
        }

        if (!event.getOrganizationNames().isEmpty()) {
            final SpannableStringBuilder organizationNamesList = new SpannableStringBuilder();
            event.getOrganizations (new Callback<Organization>() {
                @Override
                protected void call(final Organization data) {
                    if (organizationNamesList.length() != 0) // not the first item in the list
                        organizationNamesList.append(", ");

                    String organizationName = data.getName(); //
                    organizationNamesList.append(organizationName);
                    organizationNamesList.setSpan(new ClickableSpan() {
                        @Override
                        public void onClick(View widget) {
                            Intent intent = new Intent(context, OrganizationInfo.class);
                            intent.putExtra("edu.deanza.calendar.models.Organization", data);
                            intent.putExtra("UID", "");//UidGenerator.UID); //TODO: PASS UID
                            context.startActivity(intent);

                        }
                    }, organizationNamesList.length() - organizationName.length(), organizationNamesList.length(), 0);
                    }
                }
            );

            viewHolder.eventOrganizations.setMovementMethod(LinkMovementMethod.getInstance());
            viewHolder.eventOrganizations.setText(organizationNamesList, TextView.BufferType.SPANNABLE);
        }
        else {
            viewHolder.itemView.findViewById(R.id.item_event_organizations)
                    .setVisibility(View.GONE);
        }

        /*Iterator<String> i = event.getOrganizationNames().iterator();
        if (i.hasNext()) {
            SpannableStringBuilder organizationNamesList = new SpannableStringBuilder();
            do {
                final String organizationName = i.next();
                organizationNamesList.append(organizationName);
                organizationNamesList.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        event.getOrganizationRepository().findByName(organizationName, new Callback<Organization>() {
                            @Override
                            protected void call(Organization clickedOrganization) {
                                Intent intent = new Intent(context, OrganizationInfo.class);
                                intent.putExtra("edu.deanza.calendar.models.Organization", clickedOrganization);
                                intent.putExtra("UID", "");//UidGenerator.UID); //TODO: PASS UID
                                context.startActivity(intent);
                            }
                        });
                    }
                }, organizationNamesList.length() - organizationName.length(), organizationNamesList.length(), 0);
            } while (i.hasNext());

            viewHolder.eventOrganizations.setMovementMethod(LinkMovementMethod.getInstance());
            viewHolder.eventOrganizations.setText(organizationNamesList, TextView.BufferType.SPANNABLE);
        }
        else {
            viewHolder.itemView.findViewById(R.id.item_event_organizations)
                    .setVisibility(View.GONE);
        }*/
    }

}