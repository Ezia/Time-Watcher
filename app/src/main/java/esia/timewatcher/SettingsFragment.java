package esia.timewatcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import esia.timewatcher.adapters.spinner.TypeIconSpinnerAdapter;
import esia.timewatcher.adapters.recycler.TypeRecyclerViewAdapter;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.structures.Type;
import esia.timewatcher.utils.BitmapUtils;


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

		return view;
	}

	public void onCreateTypeButtonClick(View v) {
		EditText nameEditText = getView().findViewById(R.id.type_name_edit_text);
		Spinner iconSpinner = getView().findViewById(R.id.type_icon_spinner);

		if (nameEditText.length() == 0) {
			nameEditText.setError("No name");
		} else if (DatabaseManager.getInstance().typeExists(nameEditText.getText().toString())) {
			nameEditText.setError("Already exists");
		} else {
			VectorDrawable selectedIcon = (VectorDrawable) iconSpinner.getSelectedItem();
			assert (selectedIcon != null);
			Bitmap icon =
					BitmapUtils.getBitmapFromVectorDrawable(getContext(), selectedIcon);
			DatabaseManager.getInstance().createType(
					new Type(nameEditText.getText().toString(), icon));
			Toast.makeText(getContext(), "Created a type", Toast.LENGTH_SHORT).show();
		}

	}
}
