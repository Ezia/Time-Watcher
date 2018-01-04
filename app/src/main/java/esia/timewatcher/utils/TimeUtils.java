package esia.timewatcher.utils;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.PeriodFormatterBuilder;

public class TimeUtils {

	public static String timeToString(DateTime time) {
		DateTimeFormatterBuilder formatBuilder = new DateTimeFormatterBuilder();

		formatBuilder.appendHourOfDay(2);
		formatBuilder.appendLiteral(":");
		formatBuilder.appendMinuteOfHour(2);

		return time.toString(formatBuilder.toFormatter());
	}

	public static DateTime stringToTime(String stringDate) {
		DateTimeFormatterBuilder formatBuilder = new DateTimeFormatterBuilder();

		formatBuilder.appendHourOfDay(2);
		formatBuilder.appendLiteral(":");
		formatBuilder.appendMinuteOfHour(2);

		return formatBuilder.toFormatter().parseDateTime(stringDate);
	}

	public static String dateToString(DateTime time) {
		DateTimeFormatterBuilder formatBuilder = new DateTimeFormatterBuilder();

		formatBuilder.appendDayOfMonth(2);
		formatBuilder.appendLiteral(" ");
		formatBuilder.appendMonthOfYearShortText();
		formatBuilder.appendLiteral(" ");
		formatBuilder.appendYear(4, 4);

		return time.toString(formatBuilder.toFormatter());
	}

	public static DateTime stringToDate(String stringDate) {
		DateTimeFormatterBuilder formatBuilder = new DateTimeFormatterBuilder();

		formatBuilder.appendDayOfMonth(2);
		formatBuilder.appendLiteral(" ");
		formatBuilder.appendMonthOfYearShortText();
		formatBuilder.appendLiteral(" ");
		formatBuilder.appendYear(4, 4);

		return formatBuilder.toFormatter().parseDateTime(stringDate);
	}

	public static String toSimpleString(DateTime time) {
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

	public static String toSimpleString(Period time) {
		Duration duration = time.toDurationTo(DateTime.now());

		PeriodFormatterBuilder formatBuilder = new PeriodFormatterBuilder();

		formatBuilder
				.appendYears()
				.appendSuffix(" year")
				.appendSeparator(" ")
				.appendMonths()
				.appendSuffix(" months")
				.appendSeparator(" ")
				.appendDays()
				.appendSuffix(" days")
				.appendSeparator(" ");

		if (duration.isShorterThan(Duration.standardDays(2))) {
			formatBuilder
					.appendHours()
					.appendSuffix(" h")
					.appendSeparator(" ");
			if (duration.isShorterThan(Duration.standardDays(1))) {
				formatBuilder
						.appendMinutes()
						.appendSuffix(" min")
						.appendSeparator(" ");
				if (duration.isShorterThan(Duration.standardHours(1))) {
					formatBuilder
							.appendSeconds()
							.appendSuffix(" sec");
				}
			}
		}

		return formatBuilder.toFormatter().print(time);
	}
}
