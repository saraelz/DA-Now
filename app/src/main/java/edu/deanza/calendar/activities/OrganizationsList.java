package edu.deanza.calendar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import edu.deanza.calendar.DividerItemDecoration;
import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.FirebaseOrganizationRepository;
import edu.deanza.calendar.dal.FirebaseSubscriptionDao;
import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.OrganizationRepository;
import edu.deanza.calendar.domain.models.Organization;
import edu.deanza.calendar.domain.models.Subscription;
import edu.deanza.calendar.util.Callback;
import edu.deanza.calendar.util.UidGenerator;

public class OrganizationsList extends AppCompatActivity {

    private OrganizationRepository repository = new FirebaseOrganizationRepository();
    private SubscriptionDao subscriptionDao;
    private RecyclerView recyclerView;
    private OrganizationsAdapter adapter;
    private LinearLayoutManager layoutManager;

    private static final String THIS_TAG = OrganizationsList.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizations_list);

        final Context context = this;
        final String UID = new UidGenerator(context, THIS_TAG).generate();
        subscriptionDao = new FirebaseSubscriptionDao(UID);
        subscriptionDao.getUserSubscriptions(new Callback<Map<String, Subscription>>() {
            @Override
            protected void call(Map<String, Subscription> data) {
                adapter.addSubscriptions(data);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.organization_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        adapter = new OrganizationsAdapter(context, new ArrayList<Organization>(), subscriptionDao);
        adapter.setHasStableIds(true);
        adapter.setOnItemClickListener(new OrganizationsAdapter.ClickListener() {
            @Override
            public void onItemClick(Organization clickedOrganization) {
                Intent intent = new Intent(context, OrganizationInfo.class);
                intent.putExtra("edu.deanza.calendar.models.Organization", clickedOrganization);
                intent.putExtra("UID", UID);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        repository.all(new Callback<Organization>() {
            @Override
            protected void call(Organization data) {
                adapter.add(data);
            }
        });

    }

}
