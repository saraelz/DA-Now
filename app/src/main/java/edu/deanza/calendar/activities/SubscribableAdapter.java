package edu.deanza.calendar.activities;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.R;
import edu.deanza.calendar.SubscribeOnClickListener;
import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.Subscribable;
import edu.deanza.calendar.domain.models.Organization;
import edu.deanza.calendar.domain.models.Subscription;

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
            subscribable.subscribe(subscriptions.get(key));
        }
        subscribables.add(subscribable);
        notifyItemInserted(subscribables.size() - 1);
    }

    public void addSubscriptions(Map<String, Subscription> subscriptions) {
        this.subscriptions = subscriptions;
        for (T o : subscribablesAddedBeforeSubscriptionsReceived) {
            String key = o.getKey();
            if (subscriptions.containsKey(key)) {
                o.subscribe(subscriptions.get(key));
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
    public void onBindViewHolder(VH viewHolder, int position) {
        T subscribable = subscribables.get(position);
        ImageButton subscribeButton = viewHolder.subscribeButton;
        if (subscribable.isSubscribed()) {
            subscribeButton.setImageResource(R.drawable.ic_favorite_border);
        }
        else {
            subscribeButton.setImageResource(R.drawable.ic_favorite);
        }
        subscribeButton.setOnClickListener(getSubscribeOnClickListener(viewHolder, subscribable));
    }

    abstract SubscribeOnClickListener getSubscribeOnClickListener(VH viewHolder, T subscribable);

    void postSubscribe(VH viewHolder, String name) {
        notifyItemChanged(viewHolder.getAdapterPosition());
        Snackbar.make(viewHolder.itemView,
                "Subscribed to " + name,
                Snackbar.LENGTH_LONG)
                .show();
    }

    void postUnsubscribe(VH viewHolder, String name) {
        notifyItemChanged(viewHolder.getAdapterPosition());
        Snackbar.make(
                viewHolder.itemView,
                "Unsubscribed from " + name,
                Snackbar.LENGTH_LONG)
                .show();
    }

    void onCancel(VH viewHolder) {
        Snackbar.make(
                viewHolder.itemView,
                "Action cancelled.",
                Snackbar.LENGTH_LONG)
                .show();
    }

}