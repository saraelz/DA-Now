package edu.deanza.calendar.activities.listeners;

import android.content.Context;
import android.view.View;

import edu.deanza.calendar.domain.interfaces.SubscriptionDao;
import edu.deanza.calendar.domain.Organization;
import edu.deanza.calendar.domain.OrganizationSubscription;
import edu.deanza.calendar.util.activities.views.OnClickMultiChoiceDialog;

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
            unsubscribe();
        }
        else {
            super.onClick(subscribeButton);
        }
    }

    @Override
    public void onCancel() {}

    @Override
    public void postSubscriptionChange() {}

    @Override
    public void postSubscribe() {}

    @Override
    public void postUnsubscribe() {}

    void subscribe(boolean withEvents, boolean withMeetings) {
        OrganizationSubscription.Builder builder = new OrganizationSubscription.Builder();
        builder.notifyEvents(withEvents)
                .notifyMeetings(withMeetings);
        final OnClickOrganizationSubscribeDialog us = this;
        new OnClickSubscribeTimeDialog(context, organization, subscriptionDao, builder) {
            @Override
            public void postSubscriptionChange() {
                super.postSubscriptionChange();
                us.postSubscriptionChange();
            }
            @Override
            public void postSubscribe() {
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

    void unsubscribe() {
        final OnClickOrganizationSubscribeDialog us = this;
        new OnClickSubscribeTimeDialog(context, organization, subscriptionDao) {
            @Override
            public void postSubscriptionChange() {
                super.postSubscriptionChange();
                us.postSubscriptionChange();
            }
            @Override
            public void postUnsubscribe() {
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

}
