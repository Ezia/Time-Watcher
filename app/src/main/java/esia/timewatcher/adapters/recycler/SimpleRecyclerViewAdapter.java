package esia.timewatcher.adapters.recycler;

import android.app.DialogFragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import esia.timewatcher.database.Data;
import esia.timewatcher.database.DatabaseListener;
import esia.timewatcher.database.DatabaseManager;

public abstract class SimpleRecyclerViewAdapter
		<D extends Data, VH extends SimpleRecyclerViewAdapter.SimpleViewHolder>
		extends RecyclerView.Adapter<VH>
		implements DatabaseListener {

	protected ArrayList<D> dataList = new ArrayList<>();
	protected final Context context;
	private final int viewResource;
	protected RecyclerView recyclerView;

	public SimpleRecyclerViewAdapter(Context context,
									 DialogListener dialogListener,
									 int viewResource) {
		this.dialogListener = dialogListener;
		this.context = context;
		this.viewResource = viewResource;
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
		}

		public abstract void set(Data data);
	}

	///// dialog listener /////

	private DialogListener dialogListener;

	public interface DialogListener {
		abstract void onDialogRequest(DialogFragment dialog);
	}

	protected void notifyDialogRequest(DialogFragment dialog) {
		if (dialogListener != null) {
			dialogListener.onDialogRequest(dialog);
		}
	}
}
