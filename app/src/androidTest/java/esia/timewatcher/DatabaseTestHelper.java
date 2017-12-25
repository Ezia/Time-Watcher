package esia.timewatcher;

import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;

import java.util.Date;

import esia.timewatcher.structures.Event;
import esia.timewatcher.structures.OccupationType;

public class DatabaseTestHelper {

	public static OccupationType getValidOccupationType1() {
		return new OccupationType("test1",
				BitmapFactory.decodeResource(
						InstrumentationRegistry.getTargetContext().getResources(),
						R.drawable.lan
				)
		);
	}

	public static OccupationType getValidOccupationType2() {
		return new OccupationType("test2",
				BitmapFactory.decodeResource(
						InstrumentationRegistry.getTargetContext().getResources(),
						R.drawable.twitter
				)
		);
	}

	public static OccupationType getInvalidOccupationType() {
		return new OccupationType("",
				BitmapFactory.decodeResource(
						InstrumentationRegistry.getTargetContext().getResources(),
						R.drawable.lan
				)
		);
	}

	public static Event getValidEvent1() {
		return new Event(new Date(0));
	}

	public static Event getValidEvent2() {
		return new Event(new Date(1));
	}

	public static Event getInvalidEvent() {
		return new Event((Date) null);
	}
}
