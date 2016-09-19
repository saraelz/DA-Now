package edu.deanza.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import java.util.concurrent.TimeUnit;

import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.Subscribable;
import edu.deanza.calendar.domain.models.Subscription;

/**
 * Created by karinaantonio on 9/16/16.
 */

public class OnClickSubscribeTimeDialog implements SubscribeOnClickListener {

    protected final Context context;
    protected final Subscribable subscribable;
    protected final SubscriptionDao subscriptionDao;
    protected final Subscription.Builder subscriptionBuilder;

    public OnClickSubscribeTimeDialog(Context context, Subscribable subscribable,
                                      SubscriptionDao subscriptionDao,
                                      Subscription.Builder subscriptionBuilder) {
        this.context = context;
        this.subscribable = subscribable;
        this.subscriptionDao = subscriptionDao;
        this.subscriptionBuilder = subscriptionBuilder;
    }

    public OnClickSubscribeTimeDialog(Context context, Subscribable subscribable,
                                      SubscriptionDao subscriptionDao) {
        this.context = context;
        this.subscribable = subscribable;
        this.subscriptionDao = subscriptionDao;
        this.subscriptionBuilder = new Subscription.Builder();
    }

    @Override
    public void onClick(View view) {
        if (subscribable.getSubscription() == null) {
            createSubscribeDialog();
        }
        else {
            unsubscribe();
        }
    }

    protected void onCancel() {}

    public void createSubscribeDialog() {
        final int minuteOptions[] = {
                15,
                30,
                60,
                0
        };
        int numberOfMinuteOptions = minuteOptions.length;
        String optionText[] = new String[numberOfMinuteOptions + 1];
        for (int i = 0; i < numberOfMinuteOptions - 1; ++i) {
            optionText[i] = minuteOptions[i] + " minutes before";
        }
        final int CANCEL_INDEX = optionText.length - 1;
        optionText[CANCEL_INDEX] = "Cancel";
        final int NONE_INDEX = CANCEL_INDEX - 1;
        optionText[NONE_INDEX] = "None";

        final OnClickSubscribeTimeDialog us = this;
        AlertDialog.Builder dialogAlert  = new AlertDialog.Builder(context);
        dialogAlert
                .setTitle("When would you like a reminder?")
                .setCancelable(true)
                .setItems(optionText, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int clickedItemIndex) {
                        if (clickedItemIndex != CANCEL_INDEX) {
                            Subscription subscription =
                                    subscriptionBuilder
                                            .key(subscribable.getKey())
                                            .notifyBefore(minuteOptions[clickedItemIndex])
                                            .timeUnit(TimeUnit.MINUTES).build();
                            subscribe(subscription);
                        }
                        else {
                            onCancel();
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        us.onCancel();
                    }
                })
                .create()
                .show();
    }

    public void subscribe(Subscription subscription) {
        subscriptionDao.add(subscription);
        subscribable.subscribe(subscription);
        postSubscribe();
    }

    protected void postSubscribe() {}

    public void unsubscribe() {
        subscriptionDao.remove(subscribable.getSubscription());
        subscribable.unsubscribe();
        postUnsubscribe();
    }

    protected void postUnsubscribe() {}
}
