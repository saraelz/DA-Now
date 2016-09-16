package edu.deanza.calendar.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by karinaantonio on 9/15/16.
 */

public abstract class OnClickMultiChoiceDialog implements View.OnClickListener {

    protected final Context context;

    protected abstract String[] options();
    protected abstract boolean[] defaultOptionStates();
    private boolean optionStates[];

    protected abstract String dialogTitle();
    protected abstract void onClickedOkWithSelection(View view);
    protected abstract void onClickedOkWithoutSelection(View view);

    public OnClickMultiChoiceDialog(Context context) {
        this.context = context;
        optionStates = defaultOptionStates().clone();
    }

    @Override
    public void onClick(final View view) {
        boolean defaultOptionStates[] = defaultOptionStates();

        AlertDialog.Builder dialogAlert  = new AlertDialog.Builder(context);
        dialogAlert
                .setTitle(dialogTitle())
                .setCancelable(true)
                .setMultiChoiceItems(options(), defaultOptionStates,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int clickedOption,
                                                boolean becameChecked) {
                                if (becameChecked) {
                                    optionStates[clickedOption] = true;
                                }
                                else {
                                    // clickedOption became unchecked
                                    optionStates[clickedOption] = false;
                                }
                            }
                        })
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                boolean nothingSelected = true;
                                for (boolean isSelected : optionStates) {
                                    if (isSelected) {
                                        nothingSelected = false;
                                        break;
                                    }
                                }
                                if (nothingSelected) {
                                    onClickedOkWithoutSelection(view);
                                }
                                else {
                                    onClickedOkWithSelection(view);
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                // Dismiss the dialog
                            }
                        })
                .create()
                .show();
    }

    protected boolean[] getOptionStates() {
        return optionStates;
    }

}
