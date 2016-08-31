package edu.deanza.calendar.util;

/**
 * Created by karinaantonio on 9/1/16.
 */

public abstract class Callback<ArgumentT> implements Runnable {

    private ArgumentT data;
    private Callback<?> callback;

    public Callback() {}

    public Callback(ArgumentT data) {
        setArgument(data);
    }

    public Callback(Callback<?> callback) {
        this.callback = callback;
    }

    public Callback(ArgumentT data, Callback<?> callback) {
        this.data = data;
        this.callback = callback;
    }

    public final void setArgument(ArgumentT data) {
        this.data = data;
    }

    public final Callback<?> setCallback(Callback<?> callback) {
        this.callback = callback;
        return callback;
    }

    @Override
    public final void run() {
        if (data != null) {
            call(data);
            if (callback != null) {
                callback.run();
            }
        }
        else if (callback != null) {
            callback.run();
        }
    }

    protected abstract void call(ArgumentT data);

}
