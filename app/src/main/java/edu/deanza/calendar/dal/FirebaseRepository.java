package edu.deanza.calendar.dal;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.collections4.map.ListOrderedMap;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Map;

import edu.deanza.calendar.util.Callback;
import edu.deanza.calendar.util.Utilities;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by karinaantonio on 8/29/16.
 */

abstract class FirebaseRepository<T> implements Serializable {

    transient DatabaseReference root;
    transient Query currentQuery;
    private final ListOrderedMap<String, T> currentData = new ListOrderedMap<>();
    private transient AsyncTask runningTask;
    // Note: context becomes and remains null after serialization
    private transient Context context;

    private static final String THIS_CLASS_TAG = FirebaseRepository.class.getName();

    {
        initialize();
    }

    void initialize() {
        initializeRoot();
    }

    private void initializeRoot() {
        this.root = Utilities.getFirebase().getReference().child(getRootName());
    }

    abstract String getRootName();

    abstract DataMapper<T> getMapper();

    class RecyclingEventQueryListener implements ValueEventListener {

        private final Callback<T> continuation;

        public RecyclingEventQueryListener(Callback<T> continuation) {
            this.continuation = continuation;
        }

        @Override
        public void onDataChange(final DataSnapshot nodes) {
            if (nodes.getValue() == null) {
                Log.i(THIS_CLASS_TAG, "RecyclingEventQueryListener:onDataChange: on an empty node underneath: " + nodes.getKey());
                return;
            }

            runningTask = new AsyncTask<Void, T, ListOrderedMap<String, T>>() {

                ProgressDialog dialog;
                {
                    if (context != null) {
                        dialog= new ProgressDialog(context);
                    }
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();

                    if (dialog != null) {
                        dialog.setMessage("Getting Data ...");
                        dialog.setIndeterminate(false);
                        dialog.setCancelable(true);
                        dialog.show();
                    }
                }

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
                protected void onProgressUpdate(T... newElement) {
                    continuation.run(newElement[0]);
                }

                @Override
                protected void onPostExecute(ListOrderedMap<String, T> newData) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    currentData.clear();
                    currentData.putAll(newData);
                }

                private void recycle(String key, int existingDataIndex, ListOrderedMap<String, T> newData) {
                    T recycledData = currentData.getValue(existingDataIndex);
                    newData.put(key, recycledData);
                    publishProgress(recycledData);
                }

                private void append(String key, DataSnapshot node, ListOrderedMap<String, T> newData) {
                    Map<Object, Object> rawData = (Map<Object, Object>) node.getValue();
                    T newElement = getMapper().map(key, rawData);
                    newData.put(key, newElement);
                    publishProgress(newElement);
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
                Log.i(THIS_CLASS_TAG, "RecyclingEventLocationListener:onDataChange: on an empty node: " + node.getKey());
                continuation.run(null);
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
            continuation.run(newData);
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

    // For serialization; copy and paste in each subclass
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        initialize();
    }

    public void enableLoadingDialog(Context context) {
        this.context = context;
    }

}
