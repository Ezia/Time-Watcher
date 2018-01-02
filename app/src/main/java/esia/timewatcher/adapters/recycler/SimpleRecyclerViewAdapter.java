package esia.timewatcher.adapters.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.LinkedList;

import esia.timewatcher.R;
import esia.timewatcher.database.Data;
import esia.timewatcher.database.DatabaseListener;
import esia.timewatcher.database.DatabaseManager;

public abstract class SimpleRecyclerViewAdapter
		<D extends Data, VH extends SimpleRecyclerViewAdapter.SimpleViewHolder>
		extends RecyclerView.Adapter<VH>
		implements DatabaseListener {

	protected LinkedList<D> dataList = new LinkedList<>();
	protected final Context context;
	protected final int viewResource;
	protected RecyclerView recyclerView;

	public SimpleRecyclerViewAdapter(Context context, int viewRessource) {
		this.context = context;
		this.viewResource = viewRessource;
		setHasStableIds(true);
		DatabaseManager.getInstance().addListener(this);
	}

	@Override
	public VH onCreateViewHolder(ViewGroup parent, int viewType) {
		View newView = LayoutInflater.from(context)
				.inflate(viewResource, parent, false);
		return createViewHolder(newView);
	}

	public abstract VH createViewHolder(View v);

	@Override
	public void onBindViewHolder(VH holder, int position) {
		holder.set(dataList.get(position));
	}

	@Override
	public void onAttachedToRecyclerView(RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		this.recyclerView = recyclerView;
	}

	@Override
	public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
		super.onDetachedFromRecyclerView(recyclerView);
		this.recyclerView = null;
	}

	@Override
	public int getItemCount() {
		return dataList.size();
	}

	@Override
	public long getItemId(int position) {
		return dataList.get(position).getId();
	}

	public abstract class SimpleViewHolder extends RecyclerView.ViewHolder {

		public SimpleViewHolder(View itemView) {
			super(itemView);
			itemView.setOnLongClickListener(v -> onLongClick(v));
		}

		public abstract void set(Data data);

		public void fillPopup(PopupMenu popup) {
			popup.getMenuInflater().inflate(R.menu.deletion_menu, popup.getMenu());
		}

		public boolean onPopupItemClick(MenuItem item) {
			switch (item.getItemId()) {
				case R.id.delete_menu_item:
					deleteData();
					return true;
				default:
					return false;
			}
		}

		public boolean onLongClick(View v) {
			PopupMenu popup = new PopupMenu(context, itemView);
			fillPopup(popup);
			popup.setOnMenuItemClickListener(menuItem -> onPopupItemClick(menuItem));
			popup.show();
			return true;
		}

		public abstract void deleteData();
	}
}
