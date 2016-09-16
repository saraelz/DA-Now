package edu.deanza.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
        // TODO: set image button icon depending on org.getsub
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new OrganizationSubscribeOnClickListener(this, organization, subscriptionDao));

    }
}
