package edu.deanza.calendar.models;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by soso1 on 8/8/2016.
 */
public class StudentOrganization {
    protected String name;
    protected String description;

    protected String facebookURL;

    public StudentOrganization() {
    }

    public StudentOrganization(String name, String description) {
        this.name = name;
        this.description = description;
        this.facebookURL = null;
    }
    public StudentOrganization(String name, String description, String facebookURL) {
        this.name = name;
        this.description = description;
        this.facebookURL = facebookURL;
    }

    //based on Clubs.json
    public static StudentOrganization fromClubsJSON(JSONObject jsonClub) {
        StudentOrganization club = new StudentOrganization();
        try {
            club.name = jsonClub.getString("name");
            club.description = jsonClub.getString("description");
            club.facebookURL = null;
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return club;
    }

    //based on Clubs.json - can handle entire file
    public static ArrayList<StudentOrganization> fromClubsJSON(JSONArray jsonClubs) {
        ArrayList<StudentOrganization> clubs = new ArrayList<StudentOrganization>(jsonClubs.length());
        JSONObject jsonClub;
        for (int i = 0; i < jsonClubs.length(); i++) {
            jsonClub = jsonClubs.getJSONObject(i);
            StudentOrganization club = StudentOrganization.fromClubsJSON(jsonClub);

            if (club != null) {
                clubs.add(club);
            }
        }
        return clubs;
    }

    /*//incomplete method, uses Facebook API
    public static StudentOrganization fromFacebookURL(String url) {
        StudentOrganization organization = new StudentOrganization();

        //pull json from facebook

        //generate name, description from JSON

        //initialize organization.name, organization.description, organization.facebookURL

        //is there a need to treat pages different than groups?

        return organization;
    }*/


    public String getFacebookURL() {
        return facebookURL;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
