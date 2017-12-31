package esia.timewatcher.adapters.spinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

import esia.timewatcher.R;

public class TypeIconSpinnerAdapter implements SpinnerAdapter {

	private Context context;
	private int typedArrayId = R.array.type_icons;

	public TypeIconSpinnerAdapter(Context context) {
		this.context = context;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View newView;
		if (convertView == null) {
			newView = View.inflate(context, R.layout.type_icon_view, null);
		} else {
			newView = convertView;
		}

		TypedArray typedArray = context.getResources().obtainTypedArray(typedArrayId);
		Drawable icon = typedArray.getDrawable(position);
		typedArray.recycle();
		ImageView iconView = newView.findViewById(R.id.icon);
		iconView.setImageDrawable(icon);

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
		TypedArray typedArray = context.getResources().obtainTypedArray(typedArrayId);
		int len = typedArray.length();
		typedArray.recycle();
		return len;
	}

	@Override
	public Object getItem(int position) {
		TypedArray typedArray = context.getResources().obtainTypedArray(typedArrayId);
		Drawable dr = typedArray.getDrawable(position);
		typedArray.recycle();
		return dr;
	}

	@Override
	public long getItemId(int position) {
		TypedArray typedArray = context.getResources().obtainTypedArray(typedArrayId);
		long id = typedArray.getResourceId(position, 0);
		typedArray.recycle();
		return id;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getDropDownView(position, convertView, parent);
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
		TypedArray typedArray = context.getResources().obtainTypedArray(typedArrayId);
		boolean empty = typedArray.getIndexCount() == 0;
		typedArray.recycle();
		return empty;
	}
}
