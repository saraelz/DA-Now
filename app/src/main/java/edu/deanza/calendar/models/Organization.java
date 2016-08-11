package edu.deanza.calendar.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by soso1 on 8/8/2016.
 */

public class Organization {

    protected String name;
    protected String description;
    protected String facebookUrl;

    public Organization() {}

    public Organization(String name, String description) {
        this.name = name;
        this.description = description;
        this.facebookUrl = null;
    }
    public Organization(String name, String description, String facebookUrl) {
        this.name = name;
        this.description = description;
        this.facebookUrl = facebookUrl;
    }

    //based on Clubs.json
    public static Organization fromClubsJSON(JSONObject jsonClub) {
        Organization club = new Organization();
        try {
            club.name = jsonClub.getString("name");
            club.description = jsonClub.getString("description");
            club.facebookUrl = null;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return club;
    }

    //based on Clubs.json - can handle entire file
    public static ArrayList<Organization> fromClubsJSON(JSONArray jsonClubs) {
        ArrayList<Organization> clubs = new ArrayList<Organization>(jsonClubs.length());
        JSONObject jsonClub;
        for (int i = 0; i < jsonClubs.length(); i++) {
            jsonClub = jsonClubs.getJSONObject(i);
            Organization club = Organization.fromClubsJSON(jsonClub);

            if (club != null) {
                clubs.add(club);
            }
        }
        return clubs;
    }

    /*//incomplete method, uses Facebook API
    public static Organization fromFacebookURL(String url) {
        Organization organization = new Organization();

        //pull json from facebook

        //generate name, description from JSON

        //initialize organization.name, organization.description, organization.facebookUrl

        //is there a need to treat pages different than groups?

        return organization;
    }*/

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

}
