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
import java.util.List;
import java.util.Locale;

import edu.deanza.calendar.R;
import edu.deanza.calendar.OnClickSubscribeTimeDialog;
import edu.deanza.calendar.SubscribeOnClickListener;
import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.models.Meeting;

public class MeetingsAdapter extends SubscribableAdapter<Meeting, MeetingsAdapter.MeetingItemViewHolder> {

    private int todayPosition;

    public MeetingsAdapter(Context context, List<Meeting> subscribables, SubscriptionDao subscriptionDao) {
        super(context, subscribables, subscriptionDao);
        todayPosition = -1;
    }

    public int getTodayPosition() {
        return  todayPosition;
    }

    public static class MeetingItemViewHolder extends SubscribableAdapter.SubscribableItemViewHolder {

        protected TextView meetingTime;
        protected TextView meetingDayOfMonth;
        protected TextView meetingWeekday;

        public MeetingItemViewHolder(View containingItem) {
            super(containingItem);
            meetingTime = (TextView) containingItem.findViewById(R.id.item_regular_meeting_time);
            meetingDayOfMonth = (TextView) containingItem.findViewById(R.id.item_regular_meeting_dom);
            meetingWeekday = (TextView) containingItem.findViewById(R.id.item_regular_meeting_dow);
        }

        @Override
        int subscribeButtonId() {
            return R.id.item_regular_meeting_subscribe;
        }
    }

    @Override
    public MeetingItemViewHolder onCreateViewHolder(ViewGroup meetingList, int viewType) {
        View meetingItem = LayoutInflater
                .from(meetingList.getContext())
                .inflate(R.layout.item_card_meeting, meetingList, false);

        return new MeetingItemViewHolder(meetingItem);
    }

    @Override
    public void onBindViewHolder(MeetingItemViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);

        final Meeting meeting = subscribables.get(position);

        DateTime startDate = meeting.getStart();
        DateTime endDate = meeting.getEnd();
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("h:mm aa");
        viewHolder.meetingTime.setText(timeFormatter.print(startDate) + " - " + timeFormatter.print(endDate));

        viewHolder.meetingDayOfMonth.setText(String.valueOf(startDate.getDayOfMonth()));

        DateTimeFormatter weekdayFormatter = DateTimeFormat.forPattern("EEE");
        viewHolder.meetingWeekday.setText(weekdayFormatter.print(startDate));

        Date today = new Date();
        if (startDate.toDate().equals(today)){
            todayPosition = position;
        }
        else if (startDate.toDate().after(today) && todayPosition == -1) {
            todayPosition = position;
        }

    }

    @Override
    SubscribeOnClickListener getSubscribeOnClickListener(final MeetingItemViewHolder viewHolder,
                                                         final Meeting meeting) {
        // DayOfWeek, Month day
        String datePattern = "E, M d";
        String date = meeting.getStart().toString(datePattern, Locale.US);
        final String name = "meeting on " + date;

        return new OnClickSubscribeTimeDialog(context, meeting, subscriptionDao) {
            @Override
            protected void postSubscribe() {
                super.postSubscribe();
                notifyItemChanged(viewHolder.getAdapterPosition());
                Snackbar.make(viewHolder.itemView,
                        "Subscribed to " + name,
                        Snackbar.LENGTH_LONG)
                        .show();
            }

            @Override
            protected void postUnsubscribe() {
                super.postUnsubscribe();
                notifyItemChanged(viewHolder.getAdapterPosition());
                Snackbar.make(
                        viewHolder.itemView,
                        "Unsubscribed from " + name,
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

