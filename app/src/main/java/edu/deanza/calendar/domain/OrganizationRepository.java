package edu.deanza.calendar.domain;

import java.util.List;

import edu.deanza.calendar.domain.models.Organization;
import edu.deanza.calendar.util.Callback;

/**
 * Created by karinaantonio on 8/18/16.
 */

public interface OrganizationRepository {

    void all(Callback<List<Organization>> callback);

    void findByName(String name, Callback<List<Organization>> callback);

}
