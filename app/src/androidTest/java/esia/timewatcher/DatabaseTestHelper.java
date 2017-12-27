package esia.timewatcher;

import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;

import java.util.Date;

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
		return new Event(new Date(0));
	}

	static Event getValidEvent2() {
		return new Event(new Date(1));
	}

 	static Event getInvalidEvent() {
		return new Event((Date) null);
	}

	static Hobby getValidHobby1() { return new Hobby(new Date(10));}

	static Hobby getValidHobby2() { return new Hobby(new Date(20), new Date(30));}

	static Hobby getInvalidHobby() { return new Hobby((Date) null);}


}
