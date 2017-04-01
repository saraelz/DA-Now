package edu.deanza.calendar.activities.views;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.widget.ImageButton;

import edu.deanza.calendar.R;
import edu.deanza.calendar.activities.listeners.OnClickOrganizationSubscribeDialog;
import edu.deanza.calendar.activities.listeners.OnClickSubscribeTimeDialog;
import edu.deanza.calendar.activities.listeners.SubscribeOnClickListener;
import edu.deanza.calendar.dal.interfaces.Subscribable;
import edu.deanza.calendar.dal.interfaces.SubscriptionDao;
import edu.deanza.calendar.domain.Meeting;
import edu.deanza.calendar.domain.Organization;

/**
 * Created by Karina on 10/1/2016.
 */

public class SubscribeButtonWrapper {

    private final ImageButton button;
    private final Subscribable subscribable;

    public SubscribeButtonWrapper(ImageButton button, Context context, Subscribable subscribable, SubscriptionDao dao) {
        this.button = button;
        this.subscribable = subscribable;

        setSubscribedImage(subscribable.isSubscribed());

        SubscribeOnClickListener listener;
        final SubscribeButtonWrapper us = this;

        if (isMeeting(subscribable)) {
            Meeting meeting = (Meeting) subscribable;
            listener = new OnClickSubscribeTimeDialog(context, meeting, dao) {
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
                public void postUnsubscribe() {
                    super.postUnsubscribe();
                    us.postUnsubscribe();
                }
            };
        }
        else {
            Organization organization = (Organization) subscribable;
            listener = new OnClickOrganizationSubscribeDialog(context, organization, dao) {
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
                public void postUnsubscribe() {
                    super.postUnsubscribe();
                    us.postUnsubscribe();
                }
            };
        }
        button.setOnClickListener(listener);
    }

    private boolean isMeeting(Subscribable subscribable) {
        return subscribable instanceof Meeting;
    }

    private void setSubscribedImage(boolean isSubscribed) {
        int imageResource = isSubscribed ? R.drawable.ic_favorite : R.drawable.ic_favorite_border;
        button.setImageResource(imageResource);
    }

    protected void postSubscriptionChange() {}

    protected void postSubscribe() {
        setSubscribedImage(true);
        Snackbar.make(button,
                "Subscribed to " + subscribable.getName(),
                Snackbar.LENGTH_LONG)
                .show();
    }

    protected void postUnsubscribe() {
        setSubscribedImage(false);
        Snackbar.make(button,
                "Unsubscribed from " + subscribable.getName(),
                Snackbar.LENGTH_LONG)
                .show();
    }

}
