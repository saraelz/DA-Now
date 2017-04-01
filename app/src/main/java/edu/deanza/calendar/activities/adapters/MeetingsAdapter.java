package edu.deanza.calendar.activities.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.List;

import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.interfaces.SubscriptionDao;
import edu.deanza.calendar.domain.Meeting;

public class MeetingsAdapter extends SubscribableAdapter<Meeting, MeetingsAdapter.MeetingItemViewHolder> {

    public MeetingsAdapter(Context context, List<Meeting> subscribables, SubscriptionDao subscriptionDao) {
        super(context, subscribables, subscriptionDao);
    }

    static class MeetingItemViewHolder extends SubscribableAdapter.SubscribableItemViewHolder {

        TextView meetingTime;
        TextView meetingDayOfMonth;
        TextView meetingWeekday;

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
    }

    public boolean willAddNewMonth(Meeting newMeeting) {
        int numberOfEvents = subscribables.size();

        if (numberOfEvents == 0) {
            return true;
        }

        Meeting lastEvent = subscribables.get(numberOfEvents - 1);
        return lastEvent.getEnd().getMonthOfYear() != newMeeting.getStart().getMonthOfYear();
    }

    public int getSoonestIndex() {
        Date today = new Date();

        for (int i = 0; i < subscribables.size(); ++i) {
            Date date = subscribables.get(i).getStart().toDate();
            if (date.equals(today) || date.after(today)) {
                return i + 1;
            }
        }

        return subscribables.size() - 1;
    }

}

