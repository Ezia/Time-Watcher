package esia.timewatcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;

import org.joda.time.DateTime;
import java.util.LinkedList;

import esia.timewatcher.structures.Event;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.structures.Type;

public class DatabaseTestHelper {

	 public static Type getValidType1() {
		return new Type("test1",
				BitmapFactory.decodeResource(
						InstrumentationRegistry.getTargetContext().getResources(),
						R.drawable.lan
				)
		);
	}

	 public static Type getValidType2() {
		return new Type("test2",
				BitmapFactory.decodeResource(
						InstrumentationRegistry.getTargetContext().getResources(),
						R.drawable.twitter
				)
		);
	}

	 public static Type getInvalidType() {
		return new Type("",
				BitmapFactory.decodeResource(
						InstrumentationRegistry.getTargetContext().getResources(),
						R.drawable.lan
				)
		);
	}

	public static Event getValidEvent1() {
		return new Event(new DateTime(0));
	}

	public static Event getValidEvent2() {
		return new Event(new DateTime(1));
	}

 	public static Event getInvalidEvent() {
		return new Event((DateTime) null);
	}

	public static Hobby getValidHobby1() { return new Hobby(new DateTime(10));}

	public static Hobby getValidHobby2() { return new Hobby(new DateTime(20), new DateTime(30));}

	public static Hobby getInvalidHobby() { return new Hobby((DateTime) null);}

	public static LinkedList<Type> getListOfTypes(int number) {
	 	LinkedList<Type> list = new LinkedList<>();
		Bitmap icon = BitmapFactory.decodeResource(
				InstrumentationRegistry.getTargetContext().getResources(),
				R.drawable.lan
		);

	 	for (int i = 0; i < number; i++) {
			list.add(new Type("test"+i, icon));
		}

		return list;
	}

	public static LinkedList<Hobby> getListOfHobbies(int runningNumber, int finishedNumber) {
		LinkedList<Hobby> list = new LinkedList<>();

		for (int i = 0; i < runningNumber; i++) {
			list.add(new Hobby(new DateTime(i+1)));
		}

		for (int i = 0; i < finishedNumber; i++) {
			list.add(new Hobby(new DateTime(i+1), new DateTime(i+2)));
		}

		return list;
	}

	public static LinkedList<Hobby> getListOfStoppedHobbiesByStartDate(int number, boolean sortedByAscendantStartDate) {
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

	public static LinkedList<Hobby> getListOfStoppedHobbiesByStopDate(int number, boolean sortedByAscendantStopDate) {
		LinkedList<Hobby> list = new LinkedList<>();

		if (sortedByAscendantStopDate) {
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

	public static LinkedList<Hobby> getListOfRunningHobbies(int number, boolean sortedByAscendantStartDate) {
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

	public static LinkedList<Event> getListOfEvents(int number, boolean sortByChronologicalOrder) {
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
