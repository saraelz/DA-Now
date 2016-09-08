package edu.deanza.calendar.dal;

import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import edu.deanza.calendar.domain.EventRepository;
import edu.deanza.calendar.domain.OrganizationRepository;
import edu.deanza.calendar.domain.models.Organization;
import edu.deanza.calendar.util.Callback;

/**
 * Created by karinaantonio on 8/18/16.
 */

public final class FirebaseOrganizationRepository extends FirebaseRepository<Organization> implements OrganizationRepository {

    {
        root = FirebaseDatabase.getInstance().getReference().child("organizations");
        currentQuery = root.orderByKey();
    }

    private final DataMapper<Organization> mapper;

    public FirebaseOrganizationRepository() {
        mapper = new OrganizationMapper(new FirebaseEventRepository(this));
    }

    public FirebaseOrganizationRepository(EventRepository repository) {
        mapper = new OrganizationMapper(repository);
    }

    @Override
    DataMapper<Organization> getMapper() {
        return mapper;
    }

    @Override
    public void all(Callback<List<Organization>> callback) {
        currentQuery = root.orderByKey();
        listenToQuery(callback);

    }

    @Override
    public void findByName(String name, Callback<List<Organization>> callback) {
        currentQuery = currentQuery.equalTo(name);
        listenToQuery(callback);

    }

}
