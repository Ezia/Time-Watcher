package esia.timewatcher.utils;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.PeriodFormatterBuilder;

public class TimeUtils {

	public static String toString(DateTime time) {
		DateTimeFormatterBuilder formatBuilder = new DateTimeFormatterBuilder();
		DateTime now = DateTime.now();

		if (time.getYear() != now.getYear()) {
			formatBuilder.appendDayOfMonth(2);
			formatBuilder.appendLiteral(" ");
			formatBuilder.appendMonthOfYearShortText();
			formatBuilder.appendLiteral(" ");
			formatBuilder.appendYear(4, 4);
		} else if (time.getMonthOfYear() != now.getMonthOfYear()
				|| Math.abs(time.getDayOfMonth() - now.getDayOfMonth()) > 1) {
			formatBuilder.appendDayOfMonth(2);
			formatBuilder.appendLiteral(" ");
			formatBuilder.appendMonthOfYearShortText();
		} else if (Math.abs(time.getDayOfMonth() - now.getDayOfMonth()) == 1) {
			formatBuilder.appendLiteral("yesterday ");
			formatBuilder.appendHourOfDay(2);
			formatBuilder.appendLiteral(":");
			formatBuilder.appendMinuteOfHour(2);
		} else {
			formatBuilder.appendLiteral("today ");
			formatBuilder.appendHourOfDay(2);
			formatBuilder.appendLiteral(":");
			formatBuilder.appendMinuteOfHour(2);
		}

		return time.toString(formatBuilder.toFormatter());
	}

	public static String toString(Period time) {
		DateTime now = DateTime.now();

		PeriodFormatterBuilder formatBuilder = new PeriodFormatterBuilder();
		formatBuilder.printZeroAlways();

		if (time.getYears() != 0) {
			formatBuilder.appendYears();
			formatBuilder.appendLiteral(" years ");
			formatBuilder.appendDays();
			formatBuilder.appendLiteral(" days");
		} else if (time.getDays() > 1) {
			formatBuilder.appendDays();
			formatBuilder.appendLiteral(" days");
		} else if (time.getDays() == 1) {
			formatBuilder.appendDays();
			formatBuilder.appendLiteral(" days ");
			formatBuilder.appendHours();
			formatBuilder.appendLiteral(" h");
		} else if (time.getHours() != 0){
			formatBuilder.appendHours();
			formatBuilder.appendLiteral(" h ");
			formatBuilder.appendMinutes();
			formatBuilder.appendLiteral(" min");
		} else if (time.getMinutes() != 0){
			formatBuilder.appendMinutes();
			formatBuilder.appendLiteral(" min ");
			formatBuilder.appendSeconds();
			formatBuilder.appendLiteral(" sec");
		} else {
			formatBuilder.appendSeconds();
			formatBuilder.appendLiteral(" sec");
		}

		return formatBuilder.toFormatter().print(time);
	}
}
