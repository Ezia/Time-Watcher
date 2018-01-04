package esia.timewatcher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.joda.time.DateTime;

import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.view.DateEditText;
import esia.timewatcher.view.TimeEditText;

public class CustomStopDialogFragment extends DialogFragment {

	long hobbyId;

	public static CustomStopDialogFragment newInstance(long hobbyId) {
		CustomStopDialogFragment f = new CustomStopDialogFragment();

		Bundle args = new Bundle();
		args.putLong("hobby_id", hobbyId);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hobbyId = getArguments().getLong("hobby_id");
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		View view = getActivity().getLayoutInflater().inflate(R.layout.custom_stop_dialog_view, null);

		AlertDialog d = new AlertDialog.Builder(getActivity())
				.setView(view)
				.setPositiveButton("Done",
						(dialogInterface, i1) -> onPositiveButtonClicked(dialogInterface, i1))
				.setNegativeButton("Cancel", null)
				.create();

		return d;
	}

	private void onPositiveButtonClicked(DialogInterface d, int i) {
		HobbyData hobbyData = DatabaseManager.getInstance().requestHobby(hobbyId);

		DateEditText dateEditText = (DateEditText) getDialog().findViewById(R.id.date_edit_text);
		TimeEditText timeEditText = (TimeEditText) getDialog().findViewById(R.id.time_edit_text);

		DatabaseManager.getInstance().updateHobby(hobbyId,
				new Hobby(hobbyData.getHobby().getStartDate(),
						new DateTime(
								dateEditText.getYear(),
								dateEditText.getMonth(),
								dateEditText.getDay(),
								timeEditText.getHours(),
								timeEditText.getMinutes()
						)
				),
				hobbyData.getTypeData().getId());

		Toast.makeText(getContext(), "Stopped hobby", Toast.LENGTH_SHORT).show();
	}
}
