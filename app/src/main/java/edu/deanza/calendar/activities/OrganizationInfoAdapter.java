package edu.deanza.calendar.activities;

import android.content.Context;
import android.view.ViewGroup;

import org.joda.time.DateTimeComparator;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import edu.deanza.calendar.OnClickSubscribeTimeDialog;
import edu.deanza.calendar.R;
import edu.deanza.calendar.SimpleSectionedRecyclerViewAdapter;
import edu.deanza.calendar.SubscribeOnClickListener;
import edu.deanza.calendar.domain.SubscriptionDao;
import edu.deanza.calendar.domain.models.Event;
import edu.deanza.calendar.domain.models.Meeting;
import edu.deanza.calendar.domain.models.Organization;

/**
 * Created by karinaantonio on 9/20/16.
 */

public class OrganizationInfoAdapter extends SubscribableAdapter<Meeting, MeetingsAdapter.MeetingItemViewHolder> {

    private final Organization organization;
    private final MeetingsAdapter meetingsAdapter;
    private final EventsAdapter eventsAdapter;
    private final SimpleSectionedRecyclerViewAdapter sectionedAdapter;
    List<SimpleSectionedRecyclerViewAdapter.Section> sections = new ArrayList<>();

    private static final class AggregateViewTypes {
        private static final int RAW_REGULAR_MEETING = 1;
        private static final int RAW_EVENT = 2;

        public static int convertToRegularMeeting(int subscriptionViewType) {
            return subscriptionViewType * RAW_REGULAR_MEETING;
        }
        public static int convertToEvent(int subscriptionViewType) {
            return subscriptionViewType * RAW_EVENT;
        }
    }

    public OrganizationInfoAdapter(Context context, List<Meeting> subscribables,
                                   SubscriptionDao subscriptionDao, Organization organization) {
        super(context, subscribables, subscriptionDao);
        this.organization = organization;

        final OrganizationInfoAdapter us = this;

        this.meetingsAdapter = new MeetingsAdapter(context, subscribables, subscriptionDao) {
            @Override
            SubscribeOnClickListener getSubscribeOnClickListener(final MeetingItemViewHolder viewHolder, final Meeting meeting) {
                DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendLiteral(us.organization.getName())
                        .appendLiteral(" meeting on ")
                        // DayOfWeek, Month day
                        .appendPattern("EEEE, MMMM d")
                        .toFormatter();
                final String name = meeting.getStart()
                                .toString(formatter);

                return new OnClickSubscribeTimeDialog(context, meeting, subscriptionDao) {
                    @Override
                    protected void postSubscribe() {
                        // Redirect default behavior to this aggregate adapter, which is directly
                        // attached to the RecyclerView thus being the exclusive entry point to
                        // displaying view changes
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
        };

        this.eventsAdapter = new EventsAdapter(context, subscribables, subscriptionDao) {
            @Override
            SubscribeOnClickListener getSubscribeOnClickListener(final MeetingItemViewHolder viewHolder, Meeting meeting) {
                Event event = (Event) meeting;
                final String name = event.getName();

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
        };

        sectionedAdapter = new SimpleSectionedRecyclerViewAdapter(
                context,
                R.layout.section,
                R.id.section_text,
                this
        );

        for (Meeting meeting : organization.getMeetings()) {
            tryToAddSection(meeting);
            super.add(meeting);
        }
    }

    public void setOnEventClickListener(EventsAdapter.ClickListener listener) {
        eventsAdapter.setOnItemClickListener(listener);
    }

    public int getTodayPosition() {
        return  eventsAdapter.getTodayPosition();
    }

    public boolean willAddNewMonth(Meeting newMeeting) {
        return meetingsAdapter.willAddNewMonth(newMeeting) || eventsAdapter.willAddNewMonth(newMeeting);
    }

    @Override
    public void add(Meeting meeting) {
        tryToAddSection(meeting);
        super.add(meeting);
        Collections.sort(subscribables, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting meetingA, Meeting meetingB) {
                return DateTimeComparator
                        .getInstance()
                        .compare(meetingA.getStart(), meetingB.getStart());
            }
        });
    }

    public SimpleSectionedRecyclerViewAdapter getSectionedAdapter() {
        return sectionedAdapter;
    }

    private void tryToAddSection(Meeting meeting) {
        if (willAddNewMonth(meeting)) {
            int newIndex = getItemCount();
            int newMonth = meeting.getStart().getMonthOfYear();
            sections.add(new SimpleSectionedRecyclerViewAdapter.Section(
                    newIndex,
                    new DateFormatSymbols()
                            .getMonths()
                            [newMonth-1])
            );
            sectionedAdapter.setSections(sections);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int viewType;
        if (isRegularMeeting(position)) {
            viewType = AggregateViewTypes.convertToRegularMeeting(super.getItemViewType(position));
        }
        else {
            viewType = AggregateViewTypes.convertToEvent(super.getItemViewType(position));
        }
        return viewType;
    }

    @Override
    public MeetingsAdapter.MeetingItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MeetingsAdapter.MeetingItemViewHolder viewHolder;
        if (isRegularMeetingViewType(viewType)) {
            viewHolder = meetingsAdapter.onCreateViewHolder(parent, viewType);
        }
        else {
            viewHolder = eventsAdapter.onCreateViewHolder(parent, viewType);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MeetingsAdapter.MeetingItemViewHolder viewHolder, int position) {
        if (isRegularMeeting(position)) {
            meetingsAdapter.onBindViewHolder(viewHolder, position);
        }
        else {
            eventsAdapter.onBindViewHolder(viewHolder, position);
        }
    }

    // Never used; subscription behavior taken care of by the component adapters
    @Override
    SubscribeOnClickListener getSubscribeOnClickListener(
            MeetingsAdapter.MeetingItemViewHolder viewHolder, Meeting meeting) {
        return null;
    }

    private boolean isRegularMeeting(Meeting meeting) {
        return meeting instanceof Organization.RegularMeeting;
    }

    private boolean isRegularMeeting(int position) {
        return isRegularMeeting(subscribables.get(position));
    }

    private boolean isRegularMeetingViewType(int viewType) {
        return Math.abs(viewType) == AggregateViewTypes.RAW_REGULAR_MEETING;
    }

}
