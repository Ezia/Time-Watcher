package esia.timewatcher.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogHelper {
	public static AlertDialog newYesNoDialog(Context context,
											 String message,
											 DialogInterface.OnClickListener yesListener,
											 DialogInterface.OnClickListener noListener) {
		return new AlertDialog.Builder(context)
				.setPositiveButton("Yes", yesListener)
				.setNegativeButton("No", noListener)
				.setMessage(message)
				.create();
	}
}
