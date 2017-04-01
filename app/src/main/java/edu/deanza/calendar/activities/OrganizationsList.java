package edu.deanza.calendar.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Map;

import edu.deanza.calendar.activities.adapters.OrganizationsAdapter;
import edu.deanza.calendar.util.DividerItemDecoration;
import edu.deanza.calendar.R;
import edu.deanza.calendar.dal.FirebaseOrganizationRepository;
import edu.deanza.calendar.dal.FirebaseSubscriptionDao;
import edu.deanza.calendar.domain.interfaces.SubscriptionDao;
import edu.deanza.calendar.dal.interfaces.OrganizationRepository;
import edu.deanza.calendar.domain.Organization;
import edu.deanza.calendar.domain.Subscription;
import edu.deanza.calendar.util.Callback;
import edu.deanza.calendar.util.UidGetter;

public class OrganizationsList extends Fragment {

    private RecyclerView recyclerView;
    private OrganizationsAdapter adapter;
    private LinearLayoutManager layoutManager;

    private static final String THIS_TAG = OrganizationsList.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_organizations_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.organization_recycler_view);
        initializeRecyclerView(recyclerView);

        return view;
    }

    private void initializeRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }
    
    private void initializeAdapter(final Context context) {
        final String UID = UidGetter.get(context);
        SubscriptionDao subscriptionDao = new FirebaseSubscriptionDao(UID);

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
        recyclerView.addItemDecoration(new DividerItemDecoration(context));

        FirebaseOrganizationRepository organizationRepository = new FirebaseOrganizationRepository();
        organizationRepository.enableLoadingDialog(context);

        fetchData(organizationRepository, subscriptionDao);
    }
    
    private void fetchData(OrganizationRepository organizationRepository,
                           SubscriptionDao subscriptionDao) {
        organizationRepository.all(new Callback<Organization>() {
            @Override
            protected void call(Organization data) {
                adapter.add(data);
            }
        });

        subscriptionDao.getUserSubscriptions(new Callback<Map<String, Subscription>>() {
            @Override
            protected void call(Map<String, Subscription> data) {
                adapter.addSubscriptions(data);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Organizations");
        initializeAdapter(getContext());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.organizations_toolbar_menu, menu);
    }

}
