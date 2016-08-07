package edu.deanza.calendar.models;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

/**
 * Created by Sara on 5/28/2016.
 */
public class FacebookEvent extends Event {

    //unique characteristics
    private String id, link; //unique facebook ID
    private int attending_count, interested_count;

    FacebookEvent(String id)
    {
        //pull name, description, category, start_time, end_time, place, category
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + id + "/events",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse event)
                    {
                        /* handle the result */
                    }
                }
        ).executeAsync();
    }

    public String getLink()
    {
        return "https://www.facebook.com/events/" + id;
    }
}
