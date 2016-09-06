package edu.deanza.calendar.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

import edu.deanza.calendar.R;
import edu.deanza.calendar.domain.models.Organization;

public class OrganizationsList extends ListActivity {

    //public ClubRespository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizations_list);


        //repository = new FirebaseClubRepository();
        //populateListView(repository.all());
    }

    // pre: n/a
    // post: n/a
    // purpose: (1) takes ArrayList of StudentOrganization items, (2) grabs title of each organization
    // (3) populates ListView with clickable StudentOrganization items
    protected void populateListView(ArrayList<Organization> organizations) {
        String organizationNames[] = new String[organizations.size()];
        for (int i = 0; i < organizations.size(); i++) {
            organizationNames[i] = organizations.get(i).getName();
        }

        //Build Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,   //context for the activity
                R.layout.item_organization, //layout to use (create)
                organizationNames //items to display
        );
        //Configure listView
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        /*Intent i = new Intent(this, OrganizationInfo.class);

        Organization item = (Organization) adapter.getItem(position);

        //i.putExtra("organization", name);
        //i.putExtra("events", date);

        startActivityForResult(i, 1);*/
    }
}