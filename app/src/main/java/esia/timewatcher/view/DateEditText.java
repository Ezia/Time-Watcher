package esia.timewatcher.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import org.joda.time.DateTime;

import esia.timewatcher.R;
import esia.timewatcher.utils.TimeUtils;

public class DateEditText extends AppCompatEditText {
	private int year;
	private int month;
	private int day;

	private void init() {
		setFocusable(false);
		setInputType(InputType.TYPE_NULL);
		setOnClickListener(view -> onClick());
		DateTime now = DateTime.now();
		setDate(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth());
	}

	public DateEditText(Context context) {
		super(context);
		init();
	}

	public DateEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DateEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void setDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
		setText(TimeUtils.dateToString(
				new DateTime(year, month, day, 0, 0)));
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public void onClick() {
		new DatePickerDialog(getContext(),
				(datePicker, y, m, d) -> setDate(y, m+1, d),
				year, month-1, day).show();
	}
}
