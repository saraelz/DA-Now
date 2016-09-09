package edu.deanza.calendar.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.FirebaseOrganizationRepository;
import edu.deanza.calendar.domain.OrganizationRepository;
import edu.deanza.calendar.domain.models.Organization;
import edu.deanza.calendar.util.Callback;

public class OrganizationsList extends AppCompatActivity {

    private OrganizationRepository repository = new FirebaseOrganizationRepository();
    private RecyclerView recyclerView;
    private OrganizationsAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizations_list);

        recyclerView = (RecyclerView) findViewById(R.id.organization_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new OrganizationsAdapter(new ArrayList<Organization>());
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        repository.all(new Callback<List<Organization>>() {
            @Override
            protected void call(List<Organization> data) {
                adapter.repopulate(data);
            }
        });
    }

}
