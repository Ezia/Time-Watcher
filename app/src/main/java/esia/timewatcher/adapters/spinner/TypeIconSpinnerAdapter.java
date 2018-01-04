package esia.timewatcher.adapters.spinner;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;

import java.util.LinkedList;

import esia.timewatcher.R;

public class TypeIconSpinnerAdapter implements SpinnerAdapter {

	private Context context;
	private LinkedList<Bitmap> icons;

	public TypeIconSpinnerAdapter(Context context, LinkedList<Bitmap> icons) {
		this.context = context;
		this.icons = icons;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View newView;
		if (convertView == null) {
			newView = View.inflate(context, R.layout.type_icon_view, null);
		} else {
			newView = convertView;
		}

		ImageView iconView = (ImageView) newView.findViewById(R.id.icon);
		iconView.setImageBitmap(icons.get(position));

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
		return icons.size();
	}

	@Override
	public Object getItem(int position) {
		return icons.get(position);
	}

	@Override
	public long getItemId(int position) {
		return icons.get(position).hashCode();
	}

	@Override
	public boolean hasStableIds() {
		return true;
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
		return getCount() == 0;
	}
}
