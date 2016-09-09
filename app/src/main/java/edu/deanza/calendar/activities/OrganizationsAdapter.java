package edu.deanza.calendar.activities;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import edu.deanza.calendar.R;
import edu.deanza.calendar.domain.models.Organization;

public class OrganizationsAdapter
        extends RecyclerView.Adapter<OrganizationsAdapter.OrganizationItemViewHolder> {

    public List<Organization> organizations;

    public static class OrganizationItemViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView organizationName;
        private ImageButton subscribeButton;

        public OrganizationItemViewHolder(View containingItem) {
            super(containingItem);
            organizationName = (TextView) containingItem.findViewById(R.id.item_organization_name);
            subscribeButton = (ImageButton) containingItem.findViewById(R.id.item_organization_subscribe);
            containingItem.setOnClickListener(this);
            subscribeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: subscribe, and delegate that handler to this Adapter's containing Activity/Fragment
                    Log.i("OrgItem/subscribeButton", "subscribed to some Organization");
                }
            });
        }

        @Override
        public void onClick(View view) {
            // TODO: switch activities/fragments to the OrganizationInfoPage, and delegate that handler
            Log.i("OrgItem", "*switch to some OrganizationInfoPage now*");
        }

    }

    public OrganizationsAdapter(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public void repopulate(List<Organization> organizations) {
        this.organizations.clear();
        this.organizations.addAll(organizations);
        notifyDataSetChanged();
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
        final Organization organization = organizations.get(position);

        viewHolder.organizationName.setText(organization.getName());

        // TODO: set icon according to whether or not org is already subscribed to
//        Drawable image =
//        viewHolder.subscribeButton.setImageDrawable();
//        viewHolder.subscribeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO: toggle image and the org's subscription state (which updates the entry in the Repo)
//            }
//        });


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