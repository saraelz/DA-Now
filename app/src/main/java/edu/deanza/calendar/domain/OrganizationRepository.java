package edu.deanza.calendar.domain;

import java.util.List;

import edu.deanza.calendar.domain.models.Organization;

/**
 * Created by karinaantonio on 8/18/16.
 */

public interface OrganizationRepository {

    public List<Organization> all();

    public Organization findByName(String name);

}
