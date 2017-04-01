package edu.deanza.calendar.dal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import edu.deanza.calendar.dal.interfaces.EventRepository;
import edu.deanza.calendar.dal.interfaces.OrganizationRepository;
import edu.deanza.calendar.dal.mappers.OrganizationMapper;
import edu.deanza.calendar.util.dal.RecylcingFirebaseAccessor;
import edu.deanza.calendar.util.dal.mappers.DataMapper;
import edu.deanza.calendar.domain.Organization;
import edu.deanza.calendar.util.Callback;

/**
 * Created by karinaantonio on 8/18/16.
 */

public final class FirebaseOrganizationRepository extends RecylcingFirebaseAccessor<Organization> implements OrganizationRepository, Serializable {

    private final DataMapper<Organization> mapper;

    public FirebaseOrganizationRepository(EventRepository eventRepository) {
        mapper = new OrganizationMapper(eventRepository);
    }

    @Override
    public String getRootName() {
        return "organizations";
    }

    @Override
    public DataMapper<Organization> getMapper() {
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

    // For serialization; Java REQUIRES this method to be private, so paste it into each subclass
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        root = initializeRoot();
    }

}
