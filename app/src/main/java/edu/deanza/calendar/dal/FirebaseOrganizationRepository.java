package edu.deanza.calendar.dal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import edu.deanza.calendar.domain.EventRepository;
import edu.deanza.calendar.domain.OrganizationRepository;
import edu.deanza.calendar.domain.models.Organization;
import edu.deanza.calendar.util.Callback;

/**
 * Created by karinaantonio on 8/18/16.
 */

public final class FirebaseOrganizationRepository extends FirebaseRepository<Organization> implements OrganizationRepository, Serializable {

    private final DataMapper<Organization> mapper;

    public FirebaseOrganizationRepository() {
        mapper = new OrganizationMapper(new FirebaseEventRepository(this));
    }

    public FirebaseOrganizationRepository(EventRepository repository) {
        mapper = new OrganizationMapper(repository);
    }

    @Override
    String getRootName() {
        return "organizations";
    }

    @Override
    DataMapper<Organization> getMapper() {
        return mapper;
    }

    @Override
    public void all(Callback<Organization> callback) {
        currentQuery = root.orderByKey();
        listenToQuery(callback);

    }

    @Override
    public void findByName(String name, Callback<Organization> callback) {
        currentQuery = root.child(name);
        listenToLocation(callback);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initializeRoot();
    }

}
