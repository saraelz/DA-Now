package edu.deanza.calendar.util;

/**
 * Created by karinaantonio on 9/1/16.
 */

public abstract class Callback<ArgumentT> implements Runnable {

    private ArgumentT data;
    private boolean nullableArgument;

    public Callback() {}

    public final void setArgument(ArgumentT data) {
        this.data = data;
        if (data == null) {
            nullableArgument = true;
        }
    }

    @Override
    public final void run() {
        if (data != null || nullableArgument) {
            call(data);
        }
    }

    public final void run(ArgumentT data) {
        setArgument(data);
        run();
    }

    protected abstract void call(ArgumentT data);

}
