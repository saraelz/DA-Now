package edu.deanza.calendar.activities.listeners;

import android.view.View;

/**
 * Created by karinaantonio on 9/17/16.
 */

public interface SubscribeOnClickListener extends View.OnClickListener {

    void postSubscriptionChange();

    void postSubscribe();

    void postUnsubscribe();

}
