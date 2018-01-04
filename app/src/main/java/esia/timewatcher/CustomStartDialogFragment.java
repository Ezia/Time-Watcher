package esia.timewatcher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import org.joda.time.DateTime;

import esia.timewatcher.adapters.spinner.TypeSpinnerAdapter;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.structures.Event;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.view.DateEditText;
import esia.timewatcher.view.TimeEditText;

public class CustomStartDialogFragment extends DialogFragment {

	private static int EVENT = 0;
	private static int HOBBY = 1;

	int instanceType;
	long typeId;


	public static CustomStartDialogFragment newEventInstance(long selectedTypeId) {
		CustomStartDialogFragment f = new CustomStartDialogFragment();

		Bundle args = new Bundle();
		args.putLong("type_id", selectedTypeId);
		args.putInt("instance_type", EVENT);
		f.setArguments(args);

		return f;
	}

	public static CustomStartDialogFragment newHobbyInstance(long selectedTypeId) {
		CustomStartDialogFragment f = new CustomStartDialogFragment();

		Bundle args = new Bundle();
		args.putLong("type_id", selectedTypeId);
		args.putInt("instance_type", HOBBY);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		typeId = getArguments().getLong("type_id");
		instanceType = getArguments().getInt("instance_type");
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		View view = getActivity().getLayoutInflater().inflate(R.layout.custom_start_dialog_view, null);

		TypeSpinnerAdapter typeSpinnerAdapter = new TypeSpinnerAdapter(getContext());
		Spinner typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
		typeSpinner.setAdapter(typeSpinnerAdapter);
		typeSpinner.setSelection(typeSpinnerAdapter.getItemPosition(typeId));

		AlertDialog d = new AlertDialog.Builder(getActivity())
				.setView(view)
				.setTitle((instanceType == HOBBY)?"New hobby":"New event")
				.setPositiveButton("Done",
						(dialogInterface, i1) -> onPositiveButtonClicked(dialogInterface, i1))
				.setNegativeButton("Cancel", null)
				.create();

		return d;
	}

	private void onPositiveButtonClicked(DialogInterface d, int i) {
		DateEditText dateEditText = (DateEditText) getDialog().findViewById(R.id.date_edit_text);
		TimeEditText timeEditText = (TimeEditText) getDialog().findViewById(R.id.time_edit_text);
		Spinner typeSpinner = (Spinner) getDialog().findViewById(R.id.type_spinner);

		DateTime dateTime = new DateTime(
				dateEditText.getYear(),
				dateEditText.getMonth(),
				dateEditText.getDay(),
				timeEditText.getHours(),
				timeEditText.getMinutes()
		);

		if (instanceType == HOBBY) {
			DatabaseManager.getInstance().createHobby(new Hobby(dateTime),
					typeSpinner.getSelectedItemId()
			);
			Toast.makeText(getContext(), "Hobby created", Toast.LENGTH_SHORT).show();
		} else {
			DatabaseManager.getInstance().createEvent(new Event(dateTime),
					typeSpinner.getSelectedItemId()
			);
			Toast.makeText(getContext(), "Event created", Toast.LENGTH_SHORT).show();
		}
	}
}
