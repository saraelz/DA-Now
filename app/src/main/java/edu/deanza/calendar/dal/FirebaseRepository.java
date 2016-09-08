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

    class RecyclingEventQueryListener implements ValueEventListener {

        private final Callback<T> continuation;

        public RecyclingEventQueryListener(Callback<T> continuation) {
            this.continuation = continuation;
        }

        @Override
        public void onDataChange(final DataSnapshot nodes) {
            if (nodes.getValue() == null) {
                Log.i("QueryListener", "on an empty node underneath: " + nodes.getKey());
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
            Log.w(TAG, "RecyclingEventQueryListener:onCancelled", databaseError.toException());
        }

    }

    class RecyclingEventLocationListener implements ValueEventListener {

        private final Callback<T> continuation;

        public RecyclingEventLocationListener(Callback<T> continuation) {
            this.continuation = continuation;
        }

        @Override
        public void onDataChange(final DataSnapshot node) {
            if (node.getValue() == null) {
                Log.i("LocationListener", "on an empty node: " + node.getKey());
                continuation.setArgument(null);
                continuation.run();
                return;
            }

            T newData;
            String key = node.getKey();
            int existingDataIndex = currentData.indexOf(key);
            if (existingDataIndex == -1) {
                newData = construct(key, node);
            }
            else {
                newData = recycle(existingDataIndex);
            }
            continuation.setArgument(newData);
            continuation.run();
        }

        private T recycle(int existingDataIndex) {
            return currentData.getValue(existingDataIndex);
        }

        private T construct(String key, DataSnapshot node) {
            Map<Object, Object> rawData = (Map<Object, Object>) node.getValue();
            return getMapper().map(key, rawData);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w(TAG, "RecyclingEventLocationListener:onCancelled", databaseError.toException());
        }

    }

    final void listenToQuery(Callback<T> callback) {
        if (runningTask != null) {
            runningTask.cancel(true);
        }
        currentQuery.addListenerForSingleValueEvent(new RecyclingEventQueryListener(callback));
    }

    final void listenToLocation(Callback<T> callback) {
        currentQuery.addListenerForSingleValueEvent(new RecyclingEventLocationListener(callback));
    }

    abstract DataMapper<T> getMapper();

}
