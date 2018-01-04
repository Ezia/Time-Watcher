package esia.timewatcher;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.LinkedList;

import esia.timewatcher.adapters.spinner.TypeIconSpinnerAdapter;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.TypeData;
import esia.timewatcher.structures.Type;
import esia.timewatcher.utils.BitmapUtils;

/**
 * Created by esia on 01/01/18.
 */

public class ModifyTypeDialogFragment extends DialogFragment {

	long typeId = 0;


	public static ModifyTypeDialogFragment newInstance(long typeid) {
		ModifyTypeDialogFragment f = new ModifyTypeDialogFragment();

		Bundle args = new Bundle();
		args.putLong("type_id", typeid);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		typeId = getArguments().getLong("type_id");
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		TypeData data = DatabaseManager.getInstance().requestType(typeId);

		View view = getActivity().getLayoutInflater().inflate(R.layout.type_modification_dialog_view, null);

		EditText nameEditText = (EditText) view.findViewById(R.id.type_name_edit_text);
		nameEditText.setText(data.getType().getName());

		Spinner spinner = (Spinner) view.findViewById(R.id.type_icon_spinner);
		LinkedList<Bitmap> icons =  BitmapUtils.loadTypeIcons(getContext());
		int i;
		for (i = 0; i < icons.size(); ++i) {
			if (icons.get(i).sameAs(data.getType().getIcon())) {
				break;
			}
		}
		if (i == icons.size()) {
			icons.add(data.getType().getIcon());
		}
		TypeIconSpinnerAdapter spinnerAdapter = new TypeIconSpinnerAdapter(getContext(), icons);
		spinner.setAdapter(spinnerAdapter);
		spinner.setSelection(i);

		AlertDialog d = new AlertDialog.Builder(getActivity())
				.setView(view)
				.setPositiveButton("Done",
						(dialogInterface, i1) -> onPositiveButtonClicked(dialogInterface, i1))
				.setNegativeButton("Cancel", null)
				.create();

		return d;
	}

	private void onPositiveButtonClicked(DialogInterface d, int i) {
		Spinner spinner = (Spinner) getDialog().findViewById(R.id.type_icon_spinner);
		EditText nameEditText = (EditText) getDialog().findViewById(R.id.type_name_edit_text);

		Bitmap icon = (Bitmap)spinner.getSelectedItem();
		String name = nameEditText.getText().toString();

		DatabaseManager.getInstance().updateType(typeId, new Type(name, icon));

		Toast.makeText(getContext(), "Type updated", Toast.LENGTH_SHORT).show();
	}
}
