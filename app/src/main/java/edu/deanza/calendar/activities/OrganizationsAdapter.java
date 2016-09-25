package edu.deanza.calendar.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.deanza.calendar.activities.listeners.OnClickOrganizationSubscribeDialog;
import edu.deanza.calendar.R;
import edu.deanza.calendar.activities.listeners.SubscribeOnClickListener;
import edu.deanza.calendar.domain.SubscriptionDao;
import edu.deanza.calendar.domain.models.Organization;

public class OrganizationsAdapter
        extends SubscribableAdapter<Organization, OrganizationsAdapter.OrganizationItemViewHolder> {

    public OrganizationsAdapter(Context context, List<Organization> subscribables,
                                SubscriptionDao subscriptionDao) {
        super(context, subscribables, subscriptionDao);
    }

    // manage ClickListener data
    protected static ClickListener clickListener;

    public interface ClickListener {
        void onItemClick(Organization clickedOrganization);
    }

    public void setOnItemClickListener(ClickListener listener) {
        clickListener = listener;
    }

    // based on item_organization
    public static class OrganizationItemViewHolder
            extends SubscribableAdapter.SubscribableItemViewHolder {

        private TextView organizationName;

        private static final String THIS_TAG = OrganizationItemViewHolder.class.getName();

        public OrganizationItemViewHolder(View containingItem) {
            super(containingItem);
            organizationName = (TextView) containingItem.findViewById(R.id.item_organization_name);
        }

        @Override
        int subscribeButtonId() {
            return R.id.item_organization_subscribe;
        }
    }

    @Override
    public OrganizationItemViewHolder onCreateViewHolder(ViewGroup organizationList, int viewType) {
        View organizationItem = LayoutInflater
                .from(organizationList.getContext())
                .inflate(R.layout.item_organization, organizationList, false);

        return new OrganizationItemViewHolder(organizationItem);
    }

    @Override
    public void onBindViewHolder(OrganizationItemViewHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);
        final Organization organization = subscribables.get(position);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onItemClick(organization);
            }
        });

        viewHolder.organizationName.setText(organization.getName());
    }

    @Override
    SubscribeOnClickListener getSubscribeOnClickListener(final OrganizationItemViewHolder viewHolder,
                                                         Organization organization) {
        final String name = organization.getName();
        final OrganizationsAdapter us = this;

        return new OnClickOrganizationSubscribeDialog(context, organization, subscriptionDao) {
            @Override
            public void postSubscribe() {
                notifyItemChanged(viewHolder.getAdapterPosition());
                us.postSubscribe(viewHolder, name);
            }

            @Override
            public void postUnsubscribe() {
                notifyItemChanged(viewHolder.getAdapterPosition());
                us.postUnsubscribe(viewHolder, name);
            }

            @Override
            public void onCancel() {
                us.onCancel(viewHolder);
            }
        };
    }

}