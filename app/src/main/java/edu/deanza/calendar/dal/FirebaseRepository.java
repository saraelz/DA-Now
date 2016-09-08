package edu.deanza.calendar.dal;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.deanza.calendar.util.Callback;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by karinaantonio on 8/29/16.
 */

abstract class FirebaseRepository<T> {

    DatabaseReference root;
    Query currentQuery;
    private final ListOrderedMap<String, T> currentData = new ListOrderedMap<>();
    private AsyncTask runningTask;

    class RecyclingEventListener implements ValueEventListener {

        private final Callback<T> continuation;

        public RecyclingEventListener(Callback<T> continuation) {
            this.continuation = continuation;
        }

        @Override
        public void onDataChange(final DataSnapshot nodes) {
            if (nodes.getValue() == null) {
                Log.i("FBRepo.onDataChange", "on an empty node");
                return;
            }

            runningTask = new AsyncTask<Void, Void, ListOrderedMap<String, T>>() {

                @Override
                protected ListOrderedMap<String, T> doInBackground(Void... voids) {
                    ListOrderedMap<String, T> newData = new ListOrderedMap<>();
                    for (DataSnapshot node : nodes.getChildren()) {
                        String key = node.getKey();
                        int existingDataIndex = currentData.indexOf(key);
                        if (existingDataIndex == -1) {
                            append(key, node, newData);
                        }
                        else {
                            recycle(key, existingDataIndex, newData);
                        }
                    }
                    return newData;
                }

                @Override
                protected void onPostExecute(ListOrderedMap<String, T> newData) {
                    currentData.clear();
                    currentData.putAll(newData);
                    for (T data : newData.valueList()) {
                        continuation.setArgument(data);
                        continuation.run();
                    }
                }

                private void recycle(String key, int existingDataIndex, ListOrderedMap<String, T> newData) {
                    newData.put(key, currentData.getValue(existingDataIndex));
                }

                private void append(String key, DataSnapshot node, ListOrderedMap<String, T> newData) {
                    Map<Object, Object> rawData = (Map<Object, Object>) node.getValue();
                    newData.put(key, getMapper().map(key, rawData));
                }

            }.execute();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "RecyclingEventListener:onCancelled", databaseError.toException());
        }

    }

    final void listenToQuery(final Callback<T> callback) {
        if (runningTask != null) {
            runningTask.cancel(true);
        }
        currentQuery.addListenerForSingleValueEvent(new RecyclingEventListener(callback));
    }

    abstract DataMapper<T> getMapper();

}
