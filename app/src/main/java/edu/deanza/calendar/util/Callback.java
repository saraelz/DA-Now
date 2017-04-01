package edu.deanza.calendar.util;

/**
 * Created by karinaantonio on 9/1/16.
 */

public abstract class Callback<ArgumentT> implements Runnable {

    private ArgumentT data;

    public Callback() {}

    public final void setArgument(ArgumentT data) {
        this.data = data;
    }

    @Override
    public final void run() {
        call(data);
    }

    protected abstract void call(ArgumentT data);

}
