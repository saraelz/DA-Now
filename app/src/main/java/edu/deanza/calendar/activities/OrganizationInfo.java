package edu.deanza.calendar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import edu.deanza.calendar.R;
import edu.deanza.calendar.domain.models.Organization;

public class OrganizationInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "My message", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //Organization organization = EventBus.getDefault().removeStickyEvent(Organization.class);
        Organization organization = (Organization) getIntent().getSerializableExtra("org");
        setTitle(organization.getName());

    }
}

