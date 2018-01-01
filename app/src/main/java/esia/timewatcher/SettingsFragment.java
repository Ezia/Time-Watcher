package esia.timewatcher;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.joda.time.DateTime;

import esia.timewatcher.adapters.spinner.TypeIconSpinnerAdapter;
import esia.timewatcher.adapters.recycler.TypeRecyclerViewAdapter;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.structures.Type;
import esia.timewatcher.utils.BitmapUtils;
import esia.timewatcher.utils.TimeUtils;


public class SettingsFragment extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.settings_fragment, container, false);

		RecyclerView typeRecyclerView = view.findViewById(R.id.type_recycler_view);
		typeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		typeRecyclerView.setAdapter(new TypeRecyclerViewAdapter(getContext()));

		Spinner typeIconSpinner = view.findViewById(R.id.type_icon_spinner);
		typeIconSpinner.setAdapter(new TypeIconSpinnerAdapter(getContext()));

		Button createTypeButton = view.findViewById(R.id.create_type_button);
		createTypeButton.setOnClickListener((v) -> onCreateTypeButtonClick(v));

		EditText dateEditText = view.findViewById(R.id.date_edit_text);
		dateEditText.setInputType(InputType.TYPE_NULL);
		dateEditText.setOnFocusChangeListener((v, f) -> onDateEditTextClick(v, f));
		dateEditText.setOnClickListener((v) -> onDateEditTextClick(v, true));

		EditText nameEditText = view.findViewById(R.id.type_name_edit_text);
		nameEditText.setOnFocusChangeListener((v, f) -> {
			if (!f) {
				InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
			}
		});

		return view;
	}

	public void onDatePickerDataSet(DatePicker view, int year, int month, int day) {
		EditText dateEditText = getView().findViewById(R.id.date_edit_text);
		DateTime date = new DateTime(year, month+1, day, 0, 0);
		String stringDate = TimeUtils.dateToString(date);
		dateEditText.setText(stringDate);
	}

	public void onDateEditTextClick(View v, boolean focus) {
		if (focus) {
			EditText dateEditText = getView().findViewById(R.id.date_edit_text);
			DateTime date;
			if (dateEditText.getText().length() == 0) {
				date = DateTime.now();
			} else {
				date = TimeUtils.stringToDate(dateEditText.getText().toString());
			}
			new DatePickerDialog(
					getContext(),
					(view, y, m, d) -> onDatePickerDataSet(view, y, m, d),
					date.getYear(),
					date.getMonthOfYear()-1,
					date.getDayOfMonth()
					).show();
		}
	}

	public void onCreateTypeButtonClick(View v) {
		EditText nameEditText = getView().findViewById(R.id.type_name_edit_text);
		Spinner iconSpinner = getView().findViewById(R.id.type_icon_spinner);

		if (nameEditText.length() == 0) {
			nameEditText.setError("No name");
		} else if (DatabaseManager.getInstance().typeExists(nameEditText.getText().toString())) {
			nameEditText.setError("Already exists");
		} else {
			Drawable selectedIcon = (Drawable)iconSpinner.getSelectedItem();
			assert (selectedIcon != null);
			Bitmap icon =
					BitmapUtils.drawableToBitmap(getContext(), selectedIcon);
			DatabaseManager.getInstance().createType(
					new Type(nameEditText.getText().toString(), icon));
			Toast.makeText(getContext(), "Created a type", Toast.LENGTH_SHORT).show();
			nameEditText.setText("");
			iconSpinner.setSelection(0);
			InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
		}
	}
}
