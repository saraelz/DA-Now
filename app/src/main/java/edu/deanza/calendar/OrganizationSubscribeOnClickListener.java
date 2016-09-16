package edu.deanza.calendar;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageButton;

import java.util.concurrent.TimeUnit;

import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.models.Organization;
import edu.deanza.calendar.domain.models.OrganizationSubscription;
import edu.deanza.calendar.util.OnClickMultiChoiceDialog;

/**
 * Created by karinaantonio on 9/15/16.
 */

public class OrganizationSubscribeOnClickListener extends OnClickMultiChoiceDialog {

    private final Organization organization;
    private final SubscriptionDao subscriptionDao;

    public OrganizationSubscribeOnClickListener(Context context, Organization organization, SubscriptionDao subscriptionDao) {
        super(context);
        this.organization = organization;
        this.subscriptionDao = subscriptionDao;
    }

    @Override
    protected String[] options() {
        return new String[] {"Main Events", "Meetings"};
    }

    @Override
    protected boolean[] defaultOptionStates() {
        return new boolean[] {true, true};
    }

    @Override
    protected String dialogTitle() {
        return "Recieve notifications for " + organization.getName();
    }

    @Override
    protected void onClickedOkWithSelection(View view) {
        // TODO: Discuss: should subscribing to meetings only be an option?
        final int SUBSCRIBE_TO_MEETINGS_OPTION_INDEX = 1;
        if (getOptionStates()[SUBSCRIBE_TO_MEETINGS_OPTION_INDEX]) {
            subscribe(true, view);
        }
        else {
            subscribe(false, view);
        }
        Snackbar.make(view, "Subscribed to " + organization.getName(), Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
    }

    @Override
    protected void onClickedOkWithoutSelection(View view) {
        // TODO: Discuss: remove subscription instead?
        Snackbar.make(view, "Action cancelled.", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show();
    }

    @Override
    public void onClick(final View view) {
        if (organization.getSubscription() == null) {
            super.onClick(view);
        }
        else {
            unsubscribe(view);
            Snackbar.make(view, "Unsubscribed from " + organization.getName(), Snackbar.LENGTH_LONG)
                    .setAction("action", null)
                    .show();
        }
    }

    protected void subscribe(boolean withMeetings, View view) {
        ImageButton subscribeButton = (ImageButton) view;
        subscribeButton.setImageResource(R.drawable.ic_favorite);

        OrganizationSubscription defaultSubscription = new OrganizationSubscription(
                organization.getName(),
                15,
                TimeUnit.MINUTES,
                withMeetings
        );
        subscriptionDao.add(defaultSubscription);
        organization.subscribe(defaultSubscription);
    }

    protected void unsubscribe(View view) {
        ImageButton subscribeButton = (ImageButton) view;
        subscribeButton.setImageResource(R.drawable.ic_favorite_border);

        subscriptionDao.remove(organization.getSubscription());
        organization.unsubscribe();
    }

}
