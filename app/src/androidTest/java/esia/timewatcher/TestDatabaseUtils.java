package esia.timewatcher;

import android.support.test.InstrumentationRegistry;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.EventData;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.database.TypeData;
import esia.timewatcher.structures.Event;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.structures.Type;

@RunWith(JUnit4.class)
public class TestDatabaseUtils {
	///// BEFORE /////

	@Before
	public void resetDatabase() {
		InstrumentationRegistry.getTargetContext().deleteDatabase(DatabaseManager.DATABASE_NAME);
		DatabaseManager.initializeInstance(InstrumentationRegistry.getTargetContext());
	}

	///// REQUEST TYPES /////

	@Test
	public void testRequestLessTypes() throws Exception {
		LinkedList<Type> types = DatabaseTestHelper.getListOfTypes(10);

		for (Type type: types) {
			DatabaseManager.getInstance().createType(type);
		}

		ArrayList<TypeData> typeData = DatabaseManager.getInstance().requestTypes(5);

		Assert.assertEquals(typeData.size(), 5);
		for (TypeData data : typeData) {
			Assert.assertTrue(types.contains(data.getType()));
		}
	}

	@Test
	public void testRequestMoreTypes() throws Exception {
		LinkedList<Type> types = DatabaseTestHelper.getListOfTypes(10);

		for (Type type: types) {
			DatabaseManager.getInstance().createType(type);
		}

		ArrayList<TypeData> typeData = DatabaseManager.getInstance().requestTypes(15);

		Assert.assertEquals(typeData.size(), 10);
		for (TypeData data : typeData) {
			Assert.assertTrue(types.contains(data.getType()));
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRequestInvalidNumberOfTypes() throws Exception {
		DatabaseManager.getInstance().requestTypes(0);
	}

	@Test
	public void testRequestTypesOnEmptyTable() throws Exception {
		ArrayList<TypeData> typeData = DatabaseManager.getInstance().requestTypes(15);
		Assert.assertEquals(typeData.size(), 0);
	}

	@Test
	public void testRequestAllTypes() throws Exception {
		LinkedList<Type> types = DatabaseTestHelper.getListOfTypes(10);

		for (Type type: types) {
			DatabaseManager.getInstance().createType(type);
		}

		ArrayList<TypeData> typeData = DatabaseManager.getInstance().requestAllTypes();

		Assert.assertEquals(typeData.size(), 10);
		for (TypeData data : typeData) {
			Assert.assertTrue(types.contains(data.getType()));
		}
	}

	@Test
	public void testRequestAllTypesOnEmptyTable() throws Exception {
		ArrayList<TypeData> typeData = DatabaseManager.getInstance().requestAllTypes();
		Assert.assertEquals(typeData.size(), 0);
	}

	///// REQUEST RUNNING HOBBIES /////

	@Test
	public void testRequestRunningHobbiesByAscendantOrder() throws Exception {
		Type type = DatabaseTestHelper.getValidType1();
		LinkedList<Hobby> orderedHobbies = DatabaseTestHelper.getListOfRunningHobbies(5, true);
		LinkedList<Hobby> shuffledHobbies = new LinkedList<>(orderedHobbies);
		Collections.shuffle(shuffledHobbies);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : shuffledHobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		ArrayList<HobbyData> hobbyData = DatabaseManager.getInstance().requestRunningHobbies(false);

		Assert.assertEquals(hobbyData.size(), 5);
		for (int i = 0; i < orderedHobbies.size(); ++i) {
			Assert.assertEquals(orderedHobbies.get(i), hobbyData.get(i).getHobby());
		}
	}

	@Test
	public void testRequestRunningHobbiesByDescendantOrder() throws Exception {
		Type type = DatabaseTestHelper.getValidType1();
		LinkedList<Hobby> orderedHobbies = DatabaseTestHelper.getListOfRunningHobbies(5, false);
		LinkedList<Hobby> shuffledHobbies = new LinkedList<>(orderedHobbies);
		Collections.shuffle(shuffledHobbies);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : shuffledHobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		ArrayList<HobbyData> hobbyData = DatabaseManager.getInstance().requestRunningHobbies(true);

		Assert.assertEquals(hobbyData.size(), 5);
		for (int i = 0; i < orderedHobbies.size(); ++i) {
			Assert.assertEquals(orderedHobbies.get(i), hobbyData.get(i).getHobby());
		}
	}

	@Test
	public void testRequestRunningHobbiesOnEmptyTable() throws Exception {
		ArrayList<HobbyData> hobbyData = DatabaseManager.getInstance().requestRunningHobbies(true);
		Assert.assertEquals(hobbyData.size(), 0);
	}

	///// REQUEST STOPPED HOBBIES /////

	@Test
	public void testRequestStoppedHobbiesOnEmptyTable() throws Exception {
		ArrayList<HobbyData> hobbyData = DatabaseManager.getInstance().requestStoppedHobbies(true);
		Assert.assertEquals(hobbyData.size(), 0);
	}

	@Test
	public void testRequestStoppedHobbiesByAscendantOrder() throws Exception {
		Type type = DatabaseTestHelper.getValidType1();
		LinkedList<Hobby> orderedHobbies = DatabaseTestHelper.getListOfStoppedHobbiesByStartDate(5, true);
		LinkedList<Hobby> shuffledHobbies = new LinkedList<>(orderedHobbies);
		Collections.shuffle(shuffledHobbies);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : shuffledHobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		ArrayList<HobbyData> hobbyData = DatabaseManager.getInstance().requestStoppedHobbies(false);

		Assert.assertEquals(hobbyData.size(), 5);
		for (int i = 0; i < orderedHobbies.size(); ++i) {
			Assert.assertEquals(orderedHobbies.get(i), hobbyData.get(i).getHobby());
		}
	}

	@Test
	public void testRequestStoppedHobbiesByDescendantOrder() throws Exception {
		Type type = DatabaseTestHelper.getValidType1();
		LinkedList<Hobby> orderedHobbies = DatabaseTestHelper.getListOfStoppedHobbiesByStartDate(5, false);
		LinkedList<Hobby> shuffledHobbies = new LinkedList<>(orderedHobbies);
		Collections.shuffle(shuffledHobbies);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : shuffledHobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		ArrayList<HobbyData> hobbyData = DatabaseManager.getInstance().requestStoppedHobbies(true);

		Assert.assertEquals(hobbyData.size(), 5);
		for (int i = 0; i < orderedHobbies.size(); ++i) {
			Assert.assertEquals(orderedHobbies.get(i), hobbyData.get(i).getHobby());
		}
	}
	
	///// REQUEST EVENTS /////

	@Test
	public void testRequestEventsOnEmptyTable() throws Exception {
		ArrayList<EventData> eventData = DatabaseManager.getInstance().requestEvents(true);
		Assert.assertEquals(eventData.size(), 0);
	}

	@Test
	public void testRequestEventsByChronologicalOrder() throws Exception {
		Type type = DatabaseTestHelper.getValidType1();
		LinkedList<Event> orderedEvents = DatabaseTestHelper.getListOfEvents(5, true);
		LinkedList<Event> shuffledEvents = new LinkedList<>(orderedEvents);
		Collections.shuffle(shuffledEvents);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Event event : shuffledEvents) {
			DatabaseManager.getInstance().createEvent(event, typeData.getId());
		}

		ArrayList<EventData> eventData = DatabaseManager.getInstance().requestEvents(true);

		Assert.assertEquals(eventData.size(), 5);
		for (int i = 0; i < orderedEvents.size(); ++i) {
			Assert.assertEquals(orderedEvents.get(i), eventData.get(i).getEvent());
		}
	}

	@Test
	public void testRequestEventsByNonchronologicalOrder() throws Exception {
		Type type = DatabaseTestHelper.getValidType1();
		LinkedList<Event> orderedEvents = DatabaseTestHelper.getListOfEvents(5, false);
		LinkedList<Event> shuffledEvents = new LinkedList<>(orderedEvents);
		Collections.shuffle(shuffledEvents);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Event event : shuffledEvents) {
			DatabaseManager.getInstance().createEvent(event, typeData.getId());
		}

		ArrayList<EventData> eventData = DatabaseManager.getInstance().requestEvents(false);

		Assert.assertEquals(eventData.size(), 5);
		for (int i = 0; i < orderedEvents.size(); ++i) {
			Assert.assertEquals(orderedEvents.get(i), eventData.get(i).getEvent());
		}
	}

	///// DELETE HOBBIES /////

	@Test
	public void deleteHobbiesOlderThanOnEmptyTable() throws Exception {
		int deletedNbr = DatabaseManager.getInstance().deleteHobbiesOlderThan(DateTime.now());
		Assert.assertEquals(deletedNbr, 0);
		Assert.assertEquals(DatabaseManager.getInstance().getHobbyNumber(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteHobbiesOlderThanNull() throws Exception {
		DatabaseManager.getInstance().deleteHobbiesOlderThan(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteHobbiesOlderThanEpoch() throws Exception {
		DatabaseManager.getInstance().deleteHobbiesOlderThan(new DateTime(0));
	}

	private void createAndDeleteHobbiesOlderThan(
			int initStoppedNbr,
			int initRunningNbr,
			int expectedRemovedNbr) {
		assert(expectedRemovedNbr >= 0);
		assert(initRunningNbr >= 0);
		assert(initStoppedNbr >= 0);
		assert(initRunningNbr + initStoppedNbr > 0);
		assert(expectedRemovedNbr <= initStoppedNbr);

		Type type = DatabaseTestHelper.getValidType1();
		LinkedList<Hobby> hobbies = DatabaseTestHelper.
				getListOfStoppedHobbiesByStopDate(initStoppedNbr, true);
		hobbies.addAll(DatabaseTestHelper
				.getListOfRunningHobbies(initRunningNbr, true));
		DateTime limitDate;
		if (expectedRemovedNbr < initStoppedNbr) {
			 limitDate = hobbies.get(expectedRemovedNbr).getEndDate();
		} else {
			limitDate = hobbies.get(expectedRemovedNbr-1).getEndDate().plus(1);
		}
		List<Hobby> remainingHobbies = hobbies.subList(expectedRemovedNbr, initStoppedNbr+initRunningNbr);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby h : hobbies) {
			DatabaseManager.getInstance().createHobby(h, typeData.getId());
		}

		int deletedNbr = DatabaseManager.getInstance().deleteHobbiesOlderThan(limitDate);
		ArrayList<HobbyData> remainingHobbyData =
				DatabaseManager.getInstance().requestStoppedHobbies(true);
		remainingHobbyData.addAll(
				DatabaseManager.getInstance().requestRunningHobbies(true));

		Assert.assertEquals(deletedNbr, expectedRemovedNbr);
		Assert.assertEquals(remainingHobbyData.size(),
				initStoppedNbr + initRunningNbr - expectedRemovedNbr);
		for (HobbyData hd : remainingHobbyData) {
			Assert.assertTrue(remainingHobbies.contains(hd.getHobby()));
		}
	}

	@Test
	public void deleteHobbiesOlderThan() throws Exception {
		createAndDeleteHobbiesOlderThan(10, 0,4);
	}

	@Test
	public void deleteHobbiesOlderThanWithRunningHobbies() throws Exception {
		createAndDeleteHobbiesOlderThan(10, 5,4);
	}

	@Test
	public void deleteHobbiesOlderThanEarliestDate() throws Exception {
		createAndDeleteHobbiesOlderThan(9, 0, 0);
	}

	@Test
	public void deleteHobbiesOlderThanOldestDate() throws Exception {
		createAndDeleteHobbiesOlderThan(10, 0, 10);
	}

	///// DELETE EVENTS /////

	private void createAndDeleteEventsOlderThan(
			int initEventNbr,
			int expectedRemovedNbr) {
		assert(expectedRemovedNbr >= 0);
		assert(initEventNbr > 0);
		assert(expectedRemovedNbr <= initEventNbr);

		Type type = DatabaseTestHelper.getValidType1();
		LinkedList<Event> events = DatabaseTestHelper.
				getListOfEvents(initEventNbr, true);
		DateTime limitDate;
		if (expectedRemovedNbr < initEventNbr) {
			limitDate = events.get(expectedRemovedNbr).getDate();
		} else {
			limitDate = events.get(expectedRemovedNbr-1).getDate().plus(1);
		}
		List<Event> remainingEvents = events.subList(expectedRemovedNbr, initEventNbr);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Event h : events) {
			DatabaseManager.getInstance().createEvent(h, typeData.getId());
		}

		int deletedNbr = DatabaseManager.getInstance().deleteEventsOlderThan(limitDate);
		ArrayList<EventData> remainingEventsData =
				DatabaseManager.getInstance().requestEvents(true);

		Assert.assertEquals(deletedNbr, expectedRemovedNbr);
		Assert.assertEquals(remainingEventsData.size(),
				initEventNbr - expectedRemovedNbr);
		for (EventData hd : remainingEventsData) {
			Assert.assertTrue(remainingEvents.contains(hd.getEvent()));
		}
	}

	@Test
	public void deleteEventsOlderThanOnEmptyTable() throws Exception {
		int deletedNbr = DatabaseManager.getInstance().deleteEventsOlderThan(DateTime.now());
		Assert.assertEquals(deletedNbr, 0);
		Assert.assertEquals(DatabaseManager.getInstance().getHobbyNumber(), 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteEventsOlderThanNull() throws Exception {
		DatabaseManager.getInstance().deleteHobbiesOlderThan(null);
	}

	@Test
	public void deleteEventsOlderThan() throws Exception {
		createAndDeleteEventsOlderThan(9,4);
	}

	@Test
	public void deleteEventsOlderThanEarliestDate() throws Exception {
		createAndDeleteEventsOlderThan(10,0);
	}

	@Test
	public void deleteEventsOlderThanOldestDate() throws Exception {
		createAndDeleteEventsOlderThan(10,10);
	}

	///// CHECK TYPE USAGE /////

	@Test
	public void checkUnusedType() throws Exception {
		Type type = DatabaseTestHelper.getValidType1();

		TypeData data = DatabaseManager.getInstance().createType(type);
		boolean typeUsed = DatabaseManager.getInstance().isTypeUsed(data.getId());

		Assert.assertFalse(typeUsed);
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkNonexistentType() throws Exception {
		DatabaseManager.getInstance().isTypeUsed(1);
	}

	@Test
	public void checkUsedByEventType() throws Exception {
		Type type = DatabaseTestHelper.getValidType1();
		Event event = DatabaseTestHelper.getValidEvent1();

		TypeData data = DatabaseManager.getInstance().createType(type);
		DatabaseManager.getInstance().createEvent(event, data.getId());
		boolean typeUsed = DatabaseManager.getInstance().isTypeUsed(data.getId());

		Assert.assertTrue(typeUsed);
	}

	@Test
	public void checkUsedByHobbyType() throws Exception {
		Type type = DatabaseTestHelper.getValidType1();
		Hobby hobby = DatabaseTestHelper.getValidHobby1();

		TypeData data = DatabaseManager.getInstance().createType(type);
		DatabaseManager.getInstance().createHobby(hobby, data.getId());
		boolean typeUsed = DatabaseManager.getInstance().isTypeUsed(data.getId());

		Assert.assertTrue(typeUsed);
	}

	@Test
	public void checkUsedByEventAndHobbyType() throws Exception {
		Type type = DatabaseTestHelper.getValidType1();
		Event event = DatabaseTestHelper.getValidEvent1();
		Hobby hobby = DatabaseTestHelper.getValidHobby1();

		TypeData data = DatabaseManager.getInstance().createType(type);
		DatabaseManager.getInstance().createEvent(event, data.getId());
		DatabaseManager.getInstance().createHobby(hobby, data.getId());
		boolean typeUsed = DatabaseManager.getInstance().isTypeUsed(data.getId());

		Assert.assertTrue(typeUsed);
	}
}
