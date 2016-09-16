package edu.deanza.calendar.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.OrganizationSubscribeOnClickListener;
import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.models.Organization;
import edu.deanza.calendar.domain.models.OrganizationSubscription;
import edu.deanza.calendar.domain.models.Subscription;

public class OrganizationsAdapter
        extends RecyclerView.Adapter<OrganizationsAdapter.OrganizationItemViewHolder> {

    private final Context context;
    private final List<Organization> organizations;
    private final List<Organization> organizationsAddedBeforeSubscriptionsReceived = new ArrayList<>();
    private Map<String, Subscription> subscriptions;
    private final SubscriptionDao subscriptionDao;
    private static ClickListener clickListener;

    private static final int NOT_SUBSCRIBED = 0;
    private static final int SUBSCRIBED = 1;
    private static final String THIS_TAG = OrganizationsAdapter.class.getName();

    public OrganizationsAdapter(Context context, List<Organization> organizations, SubscriptionDao subscriptionDao) {
        this.context = context;
        this.organizations = organizations;
        this.subscriptionDao = subscriptionDao;
    }

    public Context getContext() {
        return context;
    }

    public void add(Organization organization) {
        String name = organization.getName();
        if (subscriptions == null) {
            organizationsAddedBeforeSubscriptionsReceived.add(organization);
        }
        else if (subscriptions.containsKey(name)) {
            organization.subscribe((OrganizationSubscription) subscriptions.get(name));
        }
        organizations.add(organization);
        notifyItemInserted(organizations.size() - 1);
    }

    public void addSubscriptions(Map<String, Subscription> subscriptions) {
        this.subscriptions = subscriptions;
        for (Organization o : organizationsAddedBeforeSubscriptionsReceived) {
            String name = o.getName();
            if (subscriptions.containsKey(name)) {
                o.subscribe((OrganizationSubscription) subscriptions.get(name));
            }
        }
    }

    public interface ClickListener {
        void onItemClick(Organization clickedOrganization);
    }

    public void setOnItemClickListener(ClickListener listener) {
        clickListener = listener;
    }

    public static class OrganizationItemViewHolder extends RecyclerView.ViewHolder {

        private TextView organizationName;
        private ImageButton subscribeButton;

        private static final String THIS_TAG = OrganizationItemViewHolder.class.getName();

        public OrganizationItemViewHolder(View containingItem) {
            super(containingItem);
            organizationName = (TextView) containingItem.findViewById(R.id.item_organization_name);
            subscribeButton = (ImageButton) containingItem.findViewById(R.id.item_organization_subscribe);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return organizations.get(position).getSubscription() == null ? NOT_SUBSCRIBED : SUBSCRIBED;
    }

    @Override
    public OrganizationItemViewHolder onCreateViewHolder(ViewGroup organizationList, int viewType) {
        View organizationItem = LayoutInflater
                .from(organizationList.getContext())
                .inflate(R.layout.item_organization, organizationList, false);

        return new OrganizationItemViewHolder(organizationItem);
    }

    public void onBindViewHolder(final OrganizationItemViewHolder viewHolder, final int position) {
        final Organization organization = organizations.get(position);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(organization);
            }
        });

        viewHolder.organizationName.setText(organization.getName());

        final ImageButton subscribeButton = viewHolder.subscribeButton;
        if (getItemViewType(position) == SUBSCRIBED) {
            subscribeButton.setImageResource(R.drawable.ic_favorite);
        }
        else if (getItemViewType(position) == NOT_SUBSCRIBED) {
            subscribeButton.setImageResource(R.drawable.ic_favorite_border);
        }
        subscribeButton.setOnClickListener(new OrganizationSubscribeOnClickListener(context, organization, subscriptionDao) {
            @Override
            protected void subscribe(boolean withMeetings, View view) {
                super.subscribe(withMeetings, view);
                notifyItemChanged(viewHolder.getAdapterPosition());
            }

            @Override
            protected void unsubscribe(View view) {
                super.unsubscribe(view);
                notifyItemChanged(viewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return organizations.size();
    }

    @Override
    public long getItemId(int position) {
        return (long) organizations.get(position).hashCode();
    }
}