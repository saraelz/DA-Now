package edu.deanza.calendar.util;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by karinaantonio on 9/16/16.
 */

public class UidGenerator {

    private static final String UID_FILENAME = "UID";
    private String uid;
    private final Context context;
    private final String CONTEXT_TAG;

    public UidGenerator(Context context, String CONTEXT_TAG) {
        this.context = context;
        this.CONTEXT_TAG = CONTEXT_TAG;
    }

    public String generate() {
        if (uid != null) {
            return uid;
        }

        boolean uidExists = context
                .getFileStreamPath(UID_FILENAME)
                .exists();
        if (!uidExists) {
            uid = saveUidToFile();
        }
        else {
            uid = readUidFromFile();
        }
        return uid;
    }

    private String saveUidToFile() {
        try (FileOutputStream fos = context.openFileOutput(UID_FILENAME, Context.MODE_PRIVATE)) {
            fos.write(uid.getBytes());
        }
        catch (IOException ex) {
            // TODO: Show dialog box?
            Log.wtf(CONTEXT_TAG, "Writing the UID to file failed, skipping! This session's" +
                    "subscriptions will be lost on app exit", ex);
            return uid;
        }
        return uid;
    }

    private String readUidFromFile() {
        try (FileInputStream fis = context.openFileInput(UID_FILENAME)) {
            StringBuilder builder = new StringBuilder();
            int charCode;
            while ((charCode = fis.read()) != -1) {
                builder.append((char) charCode);
            }
            uid = builder.toString();
        }
        catch (IOException ex) {
            Log.wtf(CONTEXT_TAG, "Reading the UID file failed, creating a new one", ex);
            return saveUidToFile();
        }
        return uid;
    }
}


