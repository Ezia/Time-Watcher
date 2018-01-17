package esia.timewatcher.view;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;

import org.joda.time.DateTime;

import esia.timewatcher.utils.TimeUtils;

public class TimeEditText extends AppCompatEditText {
	private int minutes = 0;
	private int hours = 0;
	private int seconds = 0;
	private boolean now;

	private void init() {
		setFocusable(false);
		setInputType(InputType.TYPE_NULL);
		setOnClickListener(view -> onClick());
		setTimeNow();
	}

	public TimeEditText(Context context) {
		super(context);
		init();
	}

	public TimeEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TimeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void setTimeNow() {
		now = true;
		setText("now");
	}

	public void setTime(int hours, int minutes, int seconds) {
		now = false;
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		setText(TimeUtils.timeToString(
				new DateTime(0, 1, 1, hours, minutes)));
	}

	public int getMinutes() {
		if (now) {
			return DateTime.now().getMinuteOfHour();
		} else {
			return minutes;
		}
	}

	public int getHours() {
		if (now) {
			return DateTime.now().getHourOfDay();
		} else {
			return hours;
		}
	}

	public int getSeconds() {
		if (now) {
			return DateTime.now().getSecondOfMinute();
		} else {
			return seconds;
		}
	}

	public void onClick() {

		TimePickerDialog dialog;
		if (now) {
			DateTime nowTime = DateTime.now();
			dialog = new TimePickerDialog(getContext(),
					(timePicker, h, m) -> setTime(h, m, 0),
					nowTime.getHourOfDay(),
					nowTime.getMinuteOfHour(), true);
		} else {
			dialog = new TimePickerDialog(getContext(),
					(timePicker, h, m) -> setTime(h, m, 0),
					hours, minutes, true);
		}
		dialog.setButton(
				TimePickerDialog.BUTTON_NEUTRAL,
				"Reset",
				(dialog1, which) -> setTimeNow()
		);
		dialog.show();
	}
}
