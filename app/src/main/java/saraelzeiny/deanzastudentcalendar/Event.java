package saraelzeiny.deanzastudentcalendar;

//constructor for localDateTime: LocalDateTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour)

import android.widget.ArrayAdapter;

import org.joda.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Created by Sara on 5/28/2016.
 */
public class Event {
    protected String name, description, place;
    protected LocalDateTime start, finish;
    protected enum source {
        Facebook, ClubMeeting, TransferAppointment, Website
    }
    protected ArrayList<String> tags;

    Event()
    {

    }

    Event(String eventName, String details, String location, LocalDateTime startTime,
          LocalDateTime finishTime)
    {
        name = eventName;
        description = details;
        place = location;
        start = startTime;
        finish = finishTime;

        tags = new ArrayList<String>();
        //generate_tags();
    }

    //NEED TO ALPHABETIZE ALL TAGS
    protected void generate_tags()
    {
        int tempDay = start.getDayOfWeek();
        switch (tempDay)
        {
            case 1: tags.add("Mo");
                break;
            case 2: tags.add("Tu");
                break;
            case 3: tags.add("We");
                break;
            case 4: tags.add("Th");
                break;
            case 5: tags.add("Fr");
                break;
            case 6: tags.add("Sa");
                break;
            case 7: tags.add("Su");
                break;
            //no default case
        }
        //does this make sense to continue with month and month/day?
        //cases from LocalDateTime API http://joda-time.sourceforge.net/apidocs/org/joda/time/DateTimeConstants.html

        //This will split the string around the spaces and stores them into the given string array
        String tempNameArr[] = name.split(" ");
        for (String tempName : tempNameArr)
        {
            tags.add(tempName);
        }

        //excessive?
        String tempDescriptionArr[] = description.split(" ");
        for (String tempDescription : tempDescriptionArr)
        {
            tags.add(tempDescription);
        }
    }

    //allows us to add any tag to list
    public void tag(String t)
    {
        tags.add(t);
        //sort/alphabetize new tag
    }

    /*public boolean search_tags(String input)
    {
        //return ArrayList<String> instead?
        String keys[] = input.split(" ");
        for (String key: keys)
            if (tags.contains(key)) //or do another for loop, check beginning of input to match tags
                return true;
        //display in ListView
    }*/
}
