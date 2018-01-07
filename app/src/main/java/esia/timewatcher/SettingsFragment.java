package esia.timewatcher;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.v4.util.LogWriter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.joda.time.DateTime;

import esia.timewatcher.adapters.recycler.SimpleRecyclerViewAdapter;
import esia.timewatcher.adapters.spinner.TypeIconSpinnerAdapter;
import esia.timewatcher.adapters.recycler.TypeRecyclerViewAdapter;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.structures.Type;
import esia.timewatcher.utils.BitmapUtils;
import esia.timewatcher.utils.TimeUtils;
import esia.timewatcher.view.DateEditText;


public class SettingsFragment extends Fragment implements SimpleRecyclerViewAdapter.DialogListener {

	@Override
	public void onDialogRequest(DialogFragment dialog) {
		dialog.show(getFragmentManager(), "dialog");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.settings_fragment, container, false);

		RecyclerView typeRecyclerView = (RecyclerView) view.findViewById(R.id.type_recycler_view);
		typeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		TypeRecyclerViewAdapter typeAdapter = new TypeRecyclerViewAdapter(getContext(), this);
		typeRecyclerView.setAdapter(typeAdapter);

		Spinner typeIconSpinner = (Spinner) view.findViewById(R.id.type_icon_spinner);
		typeIconSpinner.setAdapter(new TypeIconSpinnerAdapter(getContext(),
				BitmapUtils.loadTypeIcons(getContext())));

		Button createTypeButton = (Button) view.findViewById(R.id.create_type_button);
		createTypeButton.setOnClickListener((v) -> onCreateTypeButtonClick(v));

		EditText nameEditText = (EditText) view.findViewById(R.id.type_name_edit_text);
		nameEditText.setOnFocusChangeListener((v, f) -> {
			if (!f) {
				InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
			}
		});

		Button clearOlderThanButton = (Button) view.findViewById(R.id.clear_button);
		clearOlderThanButton.setOnClickListener((v) -> onClearButtonClick(v));

		Switch showUsageSwitch = (Switch) view.findViewById(R.id.show_unused_types_switch);
		showUsageSwitch.setOnCheckedChangeListener((c, b) -> onShowUsageSwitchChange(c, b));

		return view;
	}

	public void onCreateTypeButtonClick(View v) {
		EditText nameEditText = (EditText) getView().findViewById(R.id.type_name_edit_text);
		Spinner iconSpinner = (Spinner) getView().findViewById(R.id.type_icon_spinner);

		if (nameEditText.length() == 0) {
			nameEditText.setError("No name");
		} else if (DatabaseManager.getInstance().typeExists(nameEditText.getText().toString())) {
			nameEditText.setError("Already exists");
		} else {
			Bitmap icon = (Bitmap) iconSpinner.getSelectedItem();
			assert (icon != null);
			DatabaseManager.getInstance().createType(
					new Type(nameEditText.getText().toString(), icon));
			Toast.makeText(getContext(), "Created a type", Toast.LENGTH_SHORT).show();
			nameEditText.setText("");
			iconSpinner.setSelection(0);
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
		}
	}

	public void onClearButtonClick(View view) {
		DateEditText dateEditText = (DateEditText) getView().findViewById(R.id.date_edit_text);
		DateTime date = new DateTime(
				dateEditText.getYear(),
				dateEditText.getMonth(),
				dateEditText.getDay(),
				0,
				0);
		int deletedHobbyNbr = DatabaseManager.getInstance().deleteHobbiesOlderThan(date);
		int deletedEvenNbr = DatabaseManager.getInstance().deleteEventsOlderThan(date);
		Toast.makeText(getContext(),
				""
				+ deletedEvenNbr + " events and "
				+ deletedHobbyNbr + " hobbies deleted",
				Toast.LENGTH_SHORT).show();
	}

	public void onShowUsageSwitchChange(CompoundButton compoundButton, boolean b) {
		RecyclerView typeRecyclerView = (RecyclerView) getView().findViewById(R.id.type_recycler_view);
		TypeRecyclerViewAdapter adapter = (TypeRecyclerViewAdapter)typeRecyclerView.getAdapter();
		adapter.setShowUsage(b);
	}
}
