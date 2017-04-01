package edu.deanza.calendar.activities.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.domain.interfaces.SubscriptionDao;
import edu.deanza.calendar.domain.interfaces.Subscribable;
import edu.deanza.calendar.domain.Subscription;
import edu.deanza.calendar.activities.views.SubscribeButtonWrapper;

/**
 * Created by karinaantonio on 9/16/16.
 */

public abstract class SubscribableAdapter
        <T extends Subscribable, VH extends SubscribableAdapter.SubscribableItemViewHolder>
        extends RecyclerView.Adapter<VH> {

    protected final Context context;
    protected final List<T> subscribables;
    private final List<T> subscribablesAddedBeforeSubscriptionsReceived = new ArrayList<>();
    private Map<String, Subscription> subscriptions;
    protected final SubscriptionDao subscriptionDao;

    private static final int NOT_SUBSCRIBED = -1;
    private static final int SUBSCRIBED = 1;
    private static final String THIS_TAG = SubscribableAdapter.class.getName();

    public SubscribableAdapter(Context context, List<T> subscribables, SubscriptionDao subscriptionDao) {
        this.context = context;
        this.subscribables = subscribables;
        this.subscriptionDao = subscriptionDao;
    }

    public void add(T subscribable) {
        String key = subscribable.getKey();
        if (subscriptions == null) {
            subscribablesAddedBeforeSubscriptionsReceived.add(subscribable);
        }
        else if (subscriptions.containsKey(key)) {
            // Because we're passing in the DAO, new Events/RegularMeetings retrieved will
            // automatically become subscribed to in the database, if their Organization is
            // subscribed
            subscribable.subscribe(subscriptions.get(key), subscriptionDao);
        }
        subscribables.add(subscribable);
        notifyItemInserted(subscribables.size() - 1);
    }

    public void addSubscriptions(Map<String, Subscription> subscriptions) {
        this.subscriptions = subscriptions;
        for (T o : subscribablesAddedBeforeSubscriptionsReceived) {
            String key = o.getKey();
            if (subscriptions.containsKey(key)) {
                o.subscribe(subscriptions.get(key), subscriptionDao);
            }
        }
    }

    public static abstract class SubscribableItemViewHolder extends RecyclerView.ViewHolder {

        public ImageButton subscribeButton;

        public SubscribableItemViewHolder(View containingItem) {
            super(containingItem);
            subscribeButton = (ImageButton) containingItem.findViewById(subscribeButtonId());
        }

        abstract int subscribeButtonId();

    }

    @Override
    public int getItemViewType(int position) {
        return subscribables.get(position).isSubscribed() ? SUBSCRIBED : NOT_SUBSCRIBED;
    }

    @Override
    public int getItemCount() {
        return subscribables.size();
    }

    @Override
    public long getItemId(int position) {
        return (long) subscribables.get(position).hashCode();
    }

    @Override
    public void onBindViewHolder(final VH viewHolder, int position) {
        T subscribable = subscribables.get(position);
        final SubscribableAdapter us = this;
        SubscribeButtonWrapper wrapper = new SubscribeButtonWrapper(viewHolder.subscribeButton, context, subscribable, subscriptionDao) {
            @Override
            protected void postSubscriptionChange() {
                super.postSubscriptionChange();
                us.postSubscriptionChange(viewHolder);
            }
            @Override
            protected void postSubscribe() {
                super.postSubscribe();
                us.postSubscribe(viewHolder);
            }
            @Override
            protected void postUnsubscribe() {
                super.postUnsubscribe();
                us.postUnsubscribe(viewHolder);
            }
        };
    }

    protected void postSubscriptionChange(VH viewHolder) {
        notifyItemChanged(viewHolder.getAdapterPosition());
    }
    protected void postSubscribe(VH viewHolder) {
        notifyItemChanged(viewHolder.getAdapterPosition());
    }
    protected void postUnsubscribe(VH viewHolder) {
        notifyItemChanged(viewHolder.getAdapterPosition());
    }

}