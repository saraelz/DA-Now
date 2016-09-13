package edu.deanza.calendar.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.deanza.calendar.R;
import edu.deanza.calendar.domain.models.Organization;

public class OrganizationsAdapter
        extends RecyclerView.Adapter<OrganizationsAdapter.OrganizationItemViewHolder> {

    private Context context;
    private static ClickListener clickListener;
    public List<Organization> organizations;

    public Context getContext() {
        return context;
    }


    public static class OrganizationItemViewHolder
            extends RecyclerView.ViewHolder
            implements View.OnClickListener{

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
                    clickListener.onItemClick(getAdapterPosition(), view);
                }
            });
        }

        @Override
        public void onClick(View v) {
            // TODO: switch activities/fragments to the OrganizationInfoPage, and delegate that handler
            Log.i("OrgItem", "*switch to some OrganizationInfoPage now*");
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        //void onItemLongClick(int position, View v);
    }

    public OrganizationsAdapter(Context context, List<Organization> organizations) {
        this.context = context;
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

        //Initialize button
        final ImageButton button = viewHolder.subscribeButton;
        Boolean clicked; // keeps track of state of button
        clicked = new Boolean(false); //change later to pull this value from personal user data in Firebase
        button.setTag(clicked); // setting to false - wasn't clicked

        // TODO: set icon according to whether or not org is already subscribed to
        // button.setImageDrawable();

        button.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(final View view) {
                  // toggle image and the org's subscription state (which updates the entry in the Repo)

                  //set button as "clicked"
                  if( ((Boolean) button.getTag())==false ){
                      // TODO: add dialog box with option of subscribing to meetings
                      // https://developer.android.com/guide/topics/ui/dialogs.html
                      String options[] = {"Main Events", "Meetings"};
                      boolean checkedValues[] = {true, true};

                      final ArrayList mSelectedItems = new ArrayList();
                      mSelectedItems.add(0);
                      mSelectedItems.add(1);

                      //What OrgName events would you like to follow?
                      //Follow OrgName events
                      AlertDialog.Builder dialogAlert  = new AlertDialog.Builder(getContext());
                      dialogAlert.setTitle("Receive notifications for " + organization.getName())
                              .setCancelable(true)
                              .setMultiChoiceItems(options, checkedValues,
                                      new DialogInterface.OnMultiChoiceClickListener() {
                                          @Override
                                          public void onClick(DialogInterface dialog, int which,
                                                              boolean isChecked) {
                                              if (isChecked) {
                                                  // If the user checked the item, add it to the selected items
                                                  mSelectedItems.add(which);
                                              } else if (mSelectedItems.contains(which)) {
                                                  // Else, if the item is already in the array, remove it
                                                  mSelectedItems.remove(Integer.valueOf(which));
                                              }
                                          }
                                      })
                              .setPositiveButton("Ok",
                                  new DialogInterface.OnClickListener(){
                                      public void onClick(DialogInterface dialog, int which) {
                                          //dismiss the dialog
                                          if (!mSelectedItems.isEmpty())
                                          {
                                              button.setImageResource(R.drawable.ic_favorite);
                                              button.setTag(new Boolean(true));
                                              Snackbar.make(view, "Subscribed to " + organization.getName(), Snackbar.LENGTH_LONG)
                                                      .setAction("Action", null).show();
                                          }
                                          else {
                                              Snackbar.make(view, "Action cancelled.", Snackbar.LENGTH_LONG)
                                                      .setAction("Action", null).show();
                                          }
                                      }
                                  })
                              .setNegativeButton("Cancel",
                                      new DialogInterface.OnClickListener(){
                                          public void onClick(DialogInterface dialog, int which) {
                                              //dismiss the dialog
                                          }
                                      })
                              .create()
                              .show();
                  }

                  //"unclick" button, undo changes
                  else {
                      Snackbar.make(view, "Unsubscribed from " + organization.getName(), Snackbar.LENGTH_LONG)
                              .setAction("Action", null).show();
                      button.setImageResource(R.drawable.ic_favorite_border);
                      button.setTag(new Boolean(false));
                  }
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