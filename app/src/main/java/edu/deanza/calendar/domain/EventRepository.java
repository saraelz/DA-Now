package edu.deanza.calendar.domain;

import org.joda.time.LocalDate;

import java.util.List;

import edu.deanza.calendar.domain.models.OrganizationEvent;

/**
 * Created by karinaantonio on 8/11/16.
 */

public interface EventRepository {

    public List<OrganizationEvent> all();

    public List<OrganizationEvent> findByOrganization(String organizationName);

    public List<OrganizationEvent> on(LocalDate date);

    public List<OrganizationEvent> before(LocalDate date);

    public List<OrganizationEvent> after(LocalDate date);

    public List<OrganizationEvent> between(LocalDate start, LocalDate end);

}
