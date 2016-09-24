package edu.deanza.calendar;

import android.content.Context;
import android.view.View;

import edu.deanza.calendar.domain.SubscriptionDao;
import edu.deanza.calendar.domain.models.Organization;
import edu.deanza.calendar.domain.models.OrganizationSubscription;
import edu.deanza.calendar.util.OnClickMultiChoiceDialog;

/**
 * Created by karinaantonio on 9/15/16.
 */

public class OnClickOrganizationSubscribeDialog extends OnClickMultiChoiceDialog
        implements SubscribeOnClickListener {

    private final Organization organization;
    private final SubscriptionDao subscriptionDao;

    public OnClickOrganizationSubscribeDialog(Context context, Organization organization,
                                              SubscriptionDao subscriptionDao) {
        super(context);
        this.organization = organization;
        this.subscriptionDao = subscriptionDao;
    }

    @Override
    protected String[] getOptions() {
        return new String[] {"Events", "Meetings"};
    }

    @Override
    protected boolean[] getDefaultOptionStates() {
        return new boolean[] {true, true};
    }

    @Override
    protected String getDialogTitle() {
        return "Recieve notifications for " + organization.getName();
    }

    @Override
    protected void onClickedOkWithSelection() {
        // TODO: Discuss: should subscribing to meetings only be an option?
        final int SUBSCRIBE_TO_EVENTS_OPTION_INDEX = 0;
        final int SUBSCRIBE_TO_MEETINGS_OPTION_INDEX = 1;
        boolean optionStates[] = getOptionStates();
        subscribe(optionStates[SUBSCRIBE_TO_EVENTS_OPTION_INDEX],
                optionStates[SUBSCRIBE_TO_MEETINGS_OPTION_INDEX]);
    }

    @Override
    protected void onClickedOkWithoutSelection() {
        // TODO: Discuss: remove subscription instead?
        onCancel();
    }

    @Override
    public void onClick(final View subscribeButton) {
        if (organization.isSubscribed()) {
            super.onClick(subscribeButton);
        }
        else {
            unsubscribe();
        }
    }

    @Override
    protected void onCancel() {}


    private void subscribe(boolean withEvents, boolean withMeetings) {
        OrganizationSubscription.Builder builder = new OrganizationSubscription.Builder();
        builder.notifyEvents(withEvents)
                .notifyMeetings(withMeetings);
        final OnClickOrganizationSubscribeDialog us = this;
        new OnClickSubscribeTimeDialog(context, organization, subscriptionDao, builder) {
            @Override
            protected void postSubscribe() {
                super.postSubscribe();
                us.postSubscribe();
            }
            @Override
            public void onCancel() {
                super.onCancel();
                us.onCancel();
            }
        }.createSubscribeDialog();
    }

    protected void postSubscribe() {}

    private void unsubscribe() {
        final OnClickOrganizationSubscribeDialog us = this;
        new OnClickSubscribeTimeDialog(context, organization, subscriptionDao) {
            @Override
            protected void postUnsubscribe() {
                super.postUnsubscribe();
                us.postUnsubscribe();
            }
            @Override
            public void onCancel() {
                super.onCancel();
                us.onCancel();
            }
        }.unsubscribe();
    }

    protected void postUnsubscribe() {}
}
