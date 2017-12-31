package esia.timewatcher.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

import esia.timewatcher.database.Data;
import esia.timewatcher.database.DatabaseListener;

public abstract class SimpleRecyclerViewAdapter
		<D extends Data, VH extends SimpleRecyclerViewAdapter.SimpleViewHolder>
		extends RecyclerView.Adapter<VH>
		implements DatabaseListener {

	protected LinkedList<D> dataList = new LinkedList<>();
	protected final Context context;
	protected final int viewResource;

	public SimpleRecyclerViewAdapter(Context context, int viewRessource) {
		this.context = context;
		this.viewResource = viewRessource;
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
	public int getItemCount() {
		return dataList.size();
	}

	public abstract class SimpleViewHolder extends RecyclerView.ViewHolder {

		public SimpleViewHolder(View itemView) {
			super(itemView);
		}

		public abstract void set(Data data);
	}
}
