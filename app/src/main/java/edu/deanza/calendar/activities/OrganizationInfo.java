package edu.deanza.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.deanza.calendar.OrganizationSubscribeOnClickListener;
import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.FirebaseSubscriptionDao;
import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.models.Organization;

public class OrganizationInfo extends AppCompatActivity {

    Organization organization;
    SubscriptionDao subscriptionDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        organization = (Organization) intent.getSerializableExtra("edu.deanza.calendar.models.Organization");
        subscriptionDao = new FirebaseSubscriptionDao(intent.getStringExtra("UID"));

        setTitle(organization.getName());
        TextView descriptionView = (TextView) findViewById(R.id.organization_description);
        descriptionView.setText(organization.getDescription());

        FloatingActionButton subscribeButton = (FloatingActionButton) findViewById(R.id.fab);
        if (organization.getSubscription() == null) {
            subscribeButton.setImageResource(R.drawable.ic_favorite_border);
        }
        else {
            subscribeButton.setImageResource(R.drawable.ic_favorite);
        }
        subscribeButton.setOnClickListener(new OrganizationSubscribeOnClickListener(this, organization, subscriptionDao));

    }
}
