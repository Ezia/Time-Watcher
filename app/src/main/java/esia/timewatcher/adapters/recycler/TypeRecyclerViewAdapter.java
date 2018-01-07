package esia.timewatcher.adapters.recycler;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
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

	public TypeRecyclerViewAdapter(Context context, DialogListener dialogListener) {
		super(context, dialogListener, R.layout.type_view);
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
		ImageButton moreButton;
		private boolean deletable = false;

		public TypeViewHolder(View itemView) {
			super(itemView);
			icon = (ImageView) itemView.findViewById(R.id.icon);
			name = (TextView) itemView.findViewById(R.id.name);

			moreButton = (ImageButton) itemView.findViewById(R.id.more_button);

			moreButton.setOnClickListener(v -> {
				PopupMenu popup = new PopupMenu(context, v);
				popup.inflate(R.menu.modify_menu);
				if (deletable) {
					popup.inflate(R.menu.deletion_menu);
				}
				popup.setOnMenuItemClickListener(item -> onPopupMenuItemClick(item));
				popup.show();
			});
		}

		private boolean onPopupMenuItemClick(MenuItem item) {
			switch (item.getItemId()) {
				case R.id.delete_menu_item:
					DatabaseManager.getInstance().deleteType(getItemId());
					Toast.makeText(context, "Type deleted", Toast.LENGTH_SHORT).show();
					return true;
				case R.id.modify_menu_item:
					ModifyTypeDialogFragment frag = ModifyTypeDialogFragment.newInstance(getItemId());
					notifyDialogRequest(frag);
					return true;
			}
			return false;
		}

		@Override
		public void set(Data data) {
			TypeData typeData = (TypeData) data;
			assert(typeData != null);
			icon.setImageBitmap(typeData.getType().getIcon());
			name.setText(typeData.getType().getName());
			if (DatabaseManager.getInstance().isTypeUsed(typeData.getId())) {
				deletable = false;
				if (showUsage) {
					itemView.setBackgroundTintList(context.getResources().
							getColorStateList(R.color.secondaryDarkColor,
									context.getTheme()));
				} else {
					itemView.setBackgroundTintList(context.getResources().
							getColorStateList(R.color.primaryLightColor,
									context.getTheme()));
				}
			} else {
				deletable = true;
				itemView.setBackgroundTintList(context.getResources().
						getColorStateList(R.color.primaryLightColor,
								context.getTheme()));
			}
		}
	}

}
