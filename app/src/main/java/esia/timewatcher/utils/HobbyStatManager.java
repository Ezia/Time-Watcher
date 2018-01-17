package esia.timewatcher.utils;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.ArrayList;

import esia.timewatcher.database.HobbyData;

public class HobbyStatManager {

	private ArrayList<HobbyData> hobbyDataList;

	private DateTime earliestStartDate;
	private DateTime latestEndDate;
	private Duration totalHobbyDuration;

	public HobbyStatManager(ArrayList<HobbyData> hobbyDataList) {
		if (hobbyDataList == null) {
			throw new IllegalArgumentException();
		}
		this.hobbyDataList = new ArrayList<>(hobbyDataList);
		computeDates();
	}

	private void computeDates() {
		if (hobbyDataList.size() > 0) {
			DateTime earliest = hobbyDataList.get(0).getHobby().getStartDate();
			DateTime latest = hobbyDataList.get(0).getHobby().getEndDate();
			Duration totalDuration = new Duration(0);

			for (HobbyData hd : hobbyDataList) {
				if (earliest.isAfter(hd.getHobby().getStartDate())) {
					earliest = hd.getHobby().getStartDate();
				} else if (latest.isBefore(hd.getHobby().getEndDate())) {
					latest = hd.getHobby().getEndDate();
				}
				totalDuration = totalDuration.withDurationAdded(
						new Duration(hd.getHobby().getStartDate(), hd.getHobby().getEndDate()),
						1
				);
			}

			earliestStartDate = earliest;
			latestEndDate = latest;
			totalHobbyDuration = totalDuration;
		} else {
			earliestStartDate = null;
			latestEndDate = null;
			totalHobbyDuration = null;
		}
	}

	public Duration getHobbyDurationFrequency(Duration durationStep) {
		if (hobbyDataList.size() > 0) {
			Duration fullDuration = new Duration(earliestStartDate, latestEndDate);

			if (fullDuration.getMillis() <= durationStep.getMillis()) {
				return totalHobbyDuration;
			} else {
				Duration result = totalHobbyDuration
						.multipliedBy(durationStep.getMillis())
						.dividedBy(fullDuration.getMillis());
				return result;
			}
		} else {
			return new Duration(0);
		}
	}

	public DateTime getEarliestStartDate() {
		return earliestStartDate;
	}

	public DateTime getLatestEndDate() {
		return latestEndDate;
	}

	public Duration getTotalHobbyDuration() {
		return totalHobbyDuration;
	}
}
