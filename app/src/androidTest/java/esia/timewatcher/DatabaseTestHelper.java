package esia.timewatcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;

import org.joda.time.DateTime;
import java.util.LinkedList;

import esia.timewatcher.structures.Event;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.structures.OccupationType;

class DatabaseTestHelper {

	 static OccupationType getValidOccupationType1() {
		return new OccupationType("test1",
				BitmapFactory.decodeResource(
						InstrumentationRegistry.getTargetContext().getResources(),
						R.drawable.lan
				)
		);
	}

	 static OccupationType getValidOccupationType2() {
		return new OccupationType("test2",
				BitmapFactory.decodeResource(
						InstrumentationRegistry.getTargetContext().getResources(),
						R.drawable.twitter
				)
		);
	}

	 static OccupationType getInvalidOccupationType() {
		return new OccupationType("",
				BitmapFactory.decodeResource(
						InstrumentationRegistry.getTargetContext().getResources(),
						R.drawable.lan
				)
		);
	}

	static Event getValidEvent1() {
		return new Event(new DateTime(0));
	}

	static Event getValidEvent2() {
		return new Event(new DateTime(1));
	}

 	static Event getInvalidEvent() {
		return new Event((DateTime) null);
	}

	static Hobby getValidHobby1() { return new Hobby(new DateTime(10));}

	static Hobby getValidHobby2() { return new Hobby(new DateTime(20), new DateTime(30));}

	static Hobby getInvalidHobby() { return new Hobby((DateTime) null);}

	static LinkedList<OccupationType> getListOfTypes(int number) {
	 	LinkedList<OccupationType> list = new LinkedList<>();
		Bitmap icon = BitmapFactory.decodeResource(
				InstrumentationRegistry.getTargetContext().getResources(),
				R.drawable.lan
		);

	 	for (int i = 0; i < number; i++) {
			list.add(new OccupationType("test"+i, icon));
		}

		return list;
	}

	static LinkedList<Hobby> getListOfHobbies(int runningNumber, int finishedNumber) {
		LinkedList<Hobby> list = new LinkedList<>();

		for (int i = 0; i < runningNumber; i++) {
			list.add(new Hobby(new DateTime(i+1)));
		}

		for (int i = 0; i < finishedNumber; i++) {
			list.add(new Hobby(new DateTime(i+1), new DateTime(i+2)));
		}

		return list;
	}

	static LinkedList<Hobby> getListOfStoppedHobbies(int number, boolean sortedByAscendantStartDate) {
		LinkedList<Hobby> list = new LinkedList<>();

		if (sortedByAscendantStartDate) {
			for (int i = 0; i < number; i++) {
				list.add(new Hobby(new DateTime(i+1), new DateTime(i+2)));
			}
		} else {
			for (int i = number-1; i >= 0; i--) {
				list.add(new Hobby(new DateTime(i+1), new DateTime(i+2)));
			}
		}

		return list;
	}

	static LinkedList<Hobby> getListOfRunningHobbies(int number, boolean sortedByAscendantStartDate) {
		LinkedList<Hobby> list = new LinkedList<>();

		if (sortedByAscendantStartDate) {
			for (int i = 0; i < number; i++) {
				list.add(new Hobby(new DateTime(i+1)));
			}
		} else {
			for (int i = number-1; i >= 0; i--) {
				list.add(new Hobby(new DateTime(i+1)));
			}
		}

		return list;
	}

	static LinkedList<Event> getListOfEvents(int number, boolean sortByChronologicalOrder) {
		LinkedList<Event> list = new LinkedList<>();

		if (sortByChronologicalOrder) {
			for (int i = 0; i < number; i++) {
				list.add(new Event(new DateTime(i+1)));
			}
		} else {
			for (int i = number-1; i >= 0; i--) {
				list.add(new Event(new DateTime(i+1)));
			}
		}

		return list;
	}
}
