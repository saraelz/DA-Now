package saraelzeiny.deanzastudentcalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.appevents.AppEventsLogger;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //track analytics using FB--can remove feature later
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }

    private void FacebookStalking()
    {
        long [] facebookGroups = {885940821502890L, //De Anza DECA Club
                219645201389350L, //VIDA: Vasconcellos Institute for Democracy In Action
                2236277819L, //De Anza College
        };
        long [] facebookPages = {265047173570399L, //ICC
                192472534143760L //DASB
        };
    }

    private void grabFacebookPage(int id)
    {
        /* make the API call */
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