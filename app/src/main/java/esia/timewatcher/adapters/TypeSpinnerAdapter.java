package esia.timewatcher.adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.LinkedList;

import esia.timewatcher.R;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.TypeData;
import esia.timewatcher.structures.Type;

public class TypeSpinnerAdapter implements SpinnerAdapter {

	private Context context;
	private LinkedList<TypeData> items;

	public TypeSpinnerAdapter(Context context) {
		this.context = context;
		items = DatabaseManager.getInstance().requestAllTypes();
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View newView;
		if (convertView == null) {
			newView = View.inflate(context, R.layout.type_dropdown_view, null);
		} else {
			newView = convertView;
		}

		Type type = items.get(position).getType();
		ImageView icon = newView.findViewById(R.id.icon);
		TextView name = newView.findViewById(R.id.name);
		icon.setImageBitmap(type.getIcon());
		name.setText(type.getName());

		return newView;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		// TODO
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		// TODO
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return items.get(position).getId();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View newView;
		if (convertView == null) {
			newView = View.inflate(context, R.layout.type_view, null);
		} else {
			newView = convertView;
		}

		Type type = items.get(position).getType();
		ImageView icon = newView.findViewById(R.id.icon);
		TextView name = newView.findViewById(R.id.name);
		icon.setImageBitmap(type.getIcon());
		name.setText(type.getName());

		return newView;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}
}
