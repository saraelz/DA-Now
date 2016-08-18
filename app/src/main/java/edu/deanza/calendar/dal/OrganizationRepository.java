package edu.deanza.calendar.dal;

import java.util.List;

import edu.deanza.calendar.models.Event;
import edu.deanza.calendar.models.Organization;

/**
 * Created by karinaantonio on 8/18/16.
 */

public interface OrganizationRepository {

    public List<Organization> all();

    public Organization findByName(String name);

}
