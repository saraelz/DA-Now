package edu.deanza.calendar.dal;

import java.util.List;

import edu.deanza.calendar.models.OrganizationEvent;

/**
 * Created by karinaantonio on 8/18/16.
 */

public interface OrganizationRepository {

    public List<OrganizationEvent> all();

    public OrganizationEvent findByName(String name);

}
