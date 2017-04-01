package edu.deanza.calendar.dal.interfaces;

import edu.deanza.calendar.domain.Organization;
import edu.deanza.calendar.util.Callback;

/**
 * Created by karinaantonio on 8/18/16.
 */

public interface OrganizationRepository {

    void all(Callback<Organization> callback);

    void findByName(String name, Callback<Organization> callback);

}
