package esia.timewatcher.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TableRow;
import android.widget.TextView;

import esia.timewatcher.R;

public class StatView extends TableRow {

	private String statName = null;
	private String statValue = null;

	public StatView(Context context) {
		super(context);
		init(context, null);
	}

	public StatView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public String getStatName() {
		return statName;
	}

	public String getStatValue() {
		return statValue;
	}

	public void setStatName(String statName) {
		this.statName = statName;
		((TextView)getChildAt(0)).setText(statName);
	}

	public void setStatValue(String statValue) {
		this.statValue = statValue;
		((TextView)getChildAt(1)).setText(statValue);
	}

	private void init(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray a = context.getTheme().obtainStyledAttributes(
					attrs,
					R.styleable.StatView,
					0, 0);

			try {
				statName = a.getString(R.styleable.StatView_statNameText);
				statValue = a.getString(R.styleable.StatView_statValueText);
			} finally {
				a.recycle();
			}
		}

		TextView nameView = new TextView(context);
		TextView valueView = new TextView(context);
		nameView.setText(statName);
		valueView.setText(statValue);
		addView(nameView);
		addView(valueView);
	}
}
