package esia.timewatcher.adapters.recycler;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import esia.timewatcher.ModifyTypeDialogFragment;
import esia.timewatcher.R;
import esia.timewatcher.database.Data;
import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.TypeData;

public class TypeRecyclerViewAdapter
		extends SimpleRecyclerViewAdapter<TypeData, TypeRecyclerViewAdapter.TypeViewHolder> {

	private boolean showUsage = false;

	public TypeRecyclerViewAdapter(Context context) {
		super(context, R.layout.type_view);
		dataList = DatabaseManager.getInstance().requestAllTypes();
	}

	public void setShowUsage(boolean showUsage) {
		if (this.showUsage != showUsage) {
			notifyDataSetChanged();
		}
		this.showUsage = showUsage;
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
		public void fillPopup(PopupMenu popup) {
			if (!DatabaseManager.getInstance().isTypeUsed(getItemId())) {
				super.fillPopup(popup);
			}
			popup.inflate(R.menu.modify_menu);
		}

		@Override
		public boolean onPopupItemClick(MenuItem item) {
			if (item.getItemId() == R.id.modify_menu_item) {
				ModifyTypeDialogFragment frag = ModifyTypeDialogFragment.newInstance(getItemId());
				notifyDialogRequest(frag);
				return true;
			}

			return super.onPopupItemClick(item);
		}

		@Override
		public void deleteData() {
			DatabaseManager.getInstance().deleteType(getItemId());
			Toast.makeText(context, "Type deleted", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void set(Data data) {
			TypeData typeData = (TypeData) data;
			assert(typeData != null);
			icon.setImageBitmap(typeData.getType().getIcon());
			name.setText(typeData.getType().getName());
			if (showUsage && DatabaseManager.getInstance().isTypeUsed(typeData.getId())) {
				itemView.setBackgroundTintList(context.getResources().
						getColorStateList(R.color.secondaryColor,
						context.getTheme()));
			} else {
				itemView.setBackgroundTintList(context.getResources().
						getColorStateList(R.color.primaryLightColor,
								context.getTheme()));
			}
		}
	}

}
