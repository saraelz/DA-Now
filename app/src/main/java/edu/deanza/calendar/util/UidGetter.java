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

public class UidGetter {

    private static final String UID_FILENAME = "UID";
    private static String uid;

    private UidGetter() {
    }

    public static String get(Context context) {
        if (uid != null) {
            return uid;
        }

        boolean uidExists = context
                .getFileStreamPath(UID_FILENAME)
                .exists();
        if (!uidExists) {
            uid = saveUidToFile(context);
        }
        else {
            uid = readUidFromFile(context);
        }
        return uid;
    }

    private static String saveUidToFile(Context context) {
        uid = UUID.randomUUID().toString();
        try (FileOutputStream fos = context.openFileOutput(UID_FILENAME, Context.MODE_PRIVATE)) {
            fos.write(uid.getBytes());
        }
        catch (IOException ex) {
            // TODO: Show dialog box?
            Log.wtf(UID_FILENAME, "Writing the UID to file failed, skipping! This session's" +
                    "subscriptions will be lost on app exit", ex);
            return uid;
        }
        return uid;
    }

    private static String readUidFromFile(Context context) {
        try (FileInputStream fis = context.openFileInput(UID_FILENAME)) {
            StringBuilder builder = new StringBuilder();
            int charCode;
            while ((charCode = fis.read()) != -1) {
                builder.append((char) charCode);
            }
            uid = builder.toString();
        }
        catch (IOException ex) {
            Log.wtf(UID_FILENAME, "Reading the UID file failed, creating a new one", ex);
            return saveUidToFile(context);
        }
        return uid;
    }
}


