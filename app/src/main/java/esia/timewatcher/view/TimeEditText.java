package esia.timewatcher.view;

import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;

import org.joda.time.DateTime;

import esia.timewatcher.utils.TimeUtils;

public class TimeEditText extends AppCompatEditText {
	private int minutes;
	private int hours;
	private int seconds;

	private void init() {
		setFocusable(false);
		setInputType(InputType.TYPE_NULL);
		setOnClickListener(view -> onClick());
		DateTime now = DateTime.now();
		setTime(now.getHourOfDay(), now.getMinuteOfHour(), now.getSecondOfMinute());
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

	public void setTime(int hours, int minutes, int seconds) {
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		setText(TimeUtils.timeToString(
				new DateTime(0, 1, 1, hours, minutes)));
	}

	public int getMinutes() {
		return minutes;
	}

	public int getHours() {
		return hours;
	}

	public int getSeconds() {
		return seconds;
	}

	public void onClick() {
		new TimePickerDialog(getContext(),
				(timePicker, h, m) -> setTime(h, m, 0),
				hours, minutes, true).show();
	}
}
