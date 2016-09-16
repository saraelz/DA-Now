package edu.deanza.calendar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.FirebaseOrganizationRepository;
import edu.deanza.calendar.dal.FirebaseSubscriptionDao;
import edu.deanza.calendar.dal.SubscriptionDao;
import edu.deanza.calendar.domain.OrganizationRepository;
import edu.deanza.calendar.domain.models.Organization;
import edu.deanza.calendar.util.Callback;

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

        final String UID = new UidGenerator().generate();
        subscriptionDao = new FirebaseSubscriptionDao(UID);

        recyclerView = (RecyclerView) findViewById(R.id.organization_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new OrganizationsAdapter(this, new ArrayList<Organization>());
        final Context context = this;
        adapter.setOnItemClickListener(new OrganizationsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Organization organization = adapter.organizations.get(position);
                Intent intent = new Intent(context, OrganizationInfo.class);
                //EventBus.getDefault().postSticky(organization);
                //intent.putExtra("organization", organization);
                intent.putExtra("OrgName", organization.getName());
                intent.putExtra("OrgDescription", organization.getDescription());
                startActivityForResult(intent,0);
            }
        });
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        repository.all(new Callback<Organization>() {
            @Override
            protected void call(Organization data) {
                adapter.add(data);
            }
        });

    }

    private class UidGenerator {

        String UID_FILENAME = "UID";
        String uid = UUID.randomUUID().toString();

        public String generate() {
            String uid;
            boolean uidExists = getBaseContext()
                    .getFileStreamPath(UID_FILENAME)
                    .exists();
            if (!uidExists) {
                uid = saveUidToFile();
            }
            else {
                uid = readUidFromFile();
            }
            return uid;
        }

        private String saveUidToFile() {
            try (FileOutputStream fos = openFileOutput(UID_FILENAME, Context.MODE_PRIVATE)) {
                fos.write(uid.getBytes());
            }
            catch (IOException ex) {
                // TODO: Show dialog box?
                Log.wtf(THIS_TAG, "Writing the UID to file failed, skipping! This session's" +
                        "subscriptions will be lost on app exit", ex);
                return uid;
            }
            return uid;
        }

        private String readUidFromFile() {
            try (FileInputStream fis = openFileInput(UID_FILENAME)) {
                StringBuilder builder = new StringBuilder();
                int charCode;
                while ((charCode = fis.read()) != -1) {
                    builder.append((char) charCode);
                }
                uid = builder.toString();
            }
            catch (IOException ex) {
                Log.wtf(THIS_TAG, "Reading the UID file failed, creating a new one", ex);
                return saveUidToFile();
            }
            return uid;
        }
    }

}
