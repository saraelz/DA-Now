package edu.deanza.calendar.util.activities.views;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Created by karinaantonio on 9/15/16.
 */

public abstract class OnClickMultiChoiceDialog implements View.OnClickListener {

    protected final Context context;

    protected abstract String[] getOptions();
    protected abstract boolean[] getDefaultOptionStates();
    private boolean optionStates[];

    protected abstract String getDialogTitle();
    protected abstract void onClickedOkWithSelection();
    protected abstract void onClickedOkWithoutSelection();
    protected abstract void onCancel();

    public OnClickMultiChoiceDialog(Context context) {
        this.context = context;
        optionStates = getDefaultOptionStates().clone();
    }

    protected boolean[] getOptionStates() {
        return optionStates;
    }

    @Override
    public void onClick(final View view) {
        boolean defaultOptionStates[] = getDefaultOptionStates();

        final OnClickMultiChoiceDialog us = this;
        AlertDialog.Builder dialogAlert  = new AlertDialog.Builder(context);
        dialogAlert
                .setTitle(getDialogTitle())
                .setCancelable(true)
                .setMultiChoiceItems(getOptions(), defaultOptionStates,
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
                                    onClickedOkWithoutSelection();
                                }
                                else {
                                    onClickedOkWithSelection();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which) {
                                onCancel();
                            }
                        })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        us.onCancel();
                    }
                })
                .create()
                .show();
    }
}
