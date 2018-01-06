package esia.timewatcher.adapters.spinner;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.LinkedList;

import esia.timewatcher.R;
import esia.timewatcher.database.DatabaseListener;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.TypeData;
import esia.timewatcher.structures.Type;

public class TypeSpinnerAdapter implements SpinnerAdapter, DatabaseListener {

	private Context context;
	private LinkedList<TypeData> dataList;
	private LinkedList<DataSetObserver> observers = new LinkedList<>();

	public TypeSpinnerAdapter(Context context) {
		this.context = context;
		dataList = DatabaseManager.getInstance().requestAllTypes();
		DatabaseManager.getInstance().addListener(this);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View newView;
		if (convertView == null) {
			newView = View.inflate(context, R.layout.type_spinner_view, null);
		} else {
			newView = convertView;
		}

		Type type = dataList.get(position).getType();
		ImageView icon = (ImageView) newView.findViewById(R.id.icon);
		TextView name = (TextView) newView.findViewById(R.id.name);
		icon.setImageBitmap(type.getIcon());
		name.setText(type.getName());

		return newView;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		observers.add(observer);
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		observers.remove(observer);
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		try {
			return dataList.get(position).getId();
		} catch (Exception e) {
			return -1;
		}
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View newView;
		if (convertView == null) {
			newView = View.inflate(context, R.layout.type_spinner_view, null);
		} else {
			newView = convertView;
		}

		Type type = dataList.get(position).getType();
		ImageView icon = (ImageView) newView.findViewById(R.id.icon);
		TextView name = (TextView) newView.findViewById(R.id.name);
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
		return dataList.isEmpty();
	}

	@Override
	public void onDatabaseChange() {
		dataList = DatabaseManager.getInstance().requestAllTypes();
		for (DataSetObserver o: observers) {
			o.onChanged();
		}
	}

	public int getItemPosition(long id) {
		for (int i = 0; i < getCount(); ++i) {
			if (dataList.get(i).getId() == id) {
				return i;
			}
		}
		return -1;
	}
}
