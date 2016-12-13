package com.example.android.popularmovies;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SortByDialogFragment extends DialogFragment {

  private DialogInterface.OnDismissListener onDismissListener;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    String sortBy = prefs.getString(getString(R.string.pref_sortby_key), getString(R.string.pref_sortby_default));
    final String[] sortByValuesArray = getResources().getStringArray(R.array.pref_sortby_values);
    int selectedItem = 0;
    if (sortBy != null && !sortBy.isEmpty()) {
      for (int i = 0; i < sortByValuesArray.length; i++) {
        if (sortBy.equals(sortByValuesArray[i])) {
          selectedItem = i;
          break;
        }
      }
    }
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(R.string.title_dialog_sortby);
    builder.setSingleChoiceItems(R.array.pref_sortby_options, selectedItem, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int newSelectedItem) {
        String newSortBy = sortByValuesArray[newSelectedItem];
        prefs.edit().putString(getString(R.string.pref_sortby_key), newSortBy).commit();
        dialog.dismiss();
      }
    });
    builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int id) {
        prefs.edit().remove(getString(R.string.pref_sortby_key));
      }
    });
    return builder.create();
  }

  public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
    this.onDismissListener = onDismissListener;
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    if (onDismissListener != null) {
      onDismissListener.onDismiss(dialog);
    }
  }

}
