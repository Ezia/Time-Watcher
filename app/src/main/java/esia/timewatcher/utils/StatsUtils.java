package esia.timewatcher.utils;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;

import esia.timewatcher.database.EventData;
import esia.timewatcher.database.HobbyData;

public class StatsUtils {

	public static double computeEventFrequency(ArrayList<EventData> events, Duration duration) {
		if (events.size() > 0) {
			DateTime earliest = events.get(0).getEvent().getDate();
			DateTime latest = earliest;

			for (EventData ed : events) {
				DateTime edDate = ed.getEvent().getDate();
				if (edDate.isBefore(earliest)) {
					earliest = edDate;
				} else if (edDate.isAfter(latest)) {
					latest = edDate;
				}
			}

			Duration fullDuration = new Duration(earliest, latest);

			if (fullDuration.getMillis() <= duration.getMillis()) {
				return events.size();
			} else {
				return events.size() * duration.getMillis() / fullDuration.getMillis();
			}
		} else {
			return 0;
		}
	}

	public static Duration computeHobbyDurationFrequency(ArrayList<HobbyData> hobbies, Duration duration) {
		if (hobbies.size() > 0) {
			DateTime earliest = hobbies.get(0).getHobby().getStartDate();
			DateTime latest = hobbies.get(0).getHobby().getEndDate();
			Duration totalHobbyDuration = new Duration(0);

			for (HobbyData hd : hobbies) {
				if (earliest.isAfter(hd.getHobby().getStartDate())) {
					earliest = hd.getHobby().getStartDate();
				} else if (latest.isBefore(hd.getHobby().getEndDate())) {
					latest = hd.getHobby().getEndDate();
				}
				totalHobbyDuration = totalHobbyDuration.withDurationAdded(
						new Duration(hd.getHobby().getStartDate(), hd.getHobby().getEndDate()),
						1
				);
			}

			Duration fullDuration = new Duration(earliest, latest);

			if (fullDuration.getMillis() <= duration.getMillis()) {
				return totalHobbyDuration;
			} else {
				Duration result = totalHobbyDuration
						.multipliedBy(duration.getMillis())
						.dividedBy(fullDuration.getMillis());
				return result;
			}
		} else {
			return new Duration(0);
		}
	}
}
