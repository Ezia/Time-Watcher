package esia.timewatcher.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import esia.timewatcher.R;
import esia.timewatcher.database.Data;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.TypeData;

public class TypeRecyclerViewAdapter
		extends SimpleRecyclerViewAdapter<TypeData, TypeRecyclerViewAdapter.TypeViewHolder> {

	public TypeRecyclerViewAdapter(Context context) {
		super(context, R.layout.type_view);
		dataList = DatabaseManager.getInstance().requestAllTypes();
	}

	@Override
	public TypeViewHolder createViewHolder(View v) {
		return new TypeViewHolder(v);
	}

	@Override
	public void onDatabaseChange() {
		dataList = DatabaseManager.getInstance().requestAllTypes();
		notifyDataSetChanged();
	}

	public class TypeViewHolder extends SimpleRecyclerViewAdapter.SimpleViewHolder {

		ImageView icon;
		TextView name;

		public TypeViewHolder(View itemView) {
			super(itemView);
			icon = itemView.findViewById(R.id.icon);
			name = itemView.findViewById(R.id.name);
		}

		@Override
		public void set(Data data) {
			TypeData typeData = (TypeData) data;
			assert(typeData != null);
			icon.setImageBitmap(typeData.getType().getIcon());
			name.setText(typeData.getType().getName());
		}
	}
}
