package saraelzeiny.deanzastudentcalendar;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

/**
 * Created by Sara on 5/28/2016.
 */
public class FacebookEvent extends Event {
    private String id;
    //private name, description, start_time, end_time;
    //private String place, category; //will require some translation from original form

    //unique
    private int attending_count, interested_count;

    FacebookEvent(String id)
    /* String na, String time1, String time2=null,
                  String description=null, String place=null, String category=null, int attending=0,
                  int interested=0)*/
    {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/" + id + "/events",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                    }
                }
        ).executeAsync();
    }
}
