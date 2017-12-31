package esia.timewatcher;

import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.LinkedList;

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

		LinkedList<TypeData> typeData = DatabaseManager.getInstance().requestTypes(5);

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

		LinkedList<TypeData> typeData = DatabaseManager.getInstance().requestTypes(15);

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
		LinkedList<TypeData> typeData = DatabaseManager.getInstance().requestTypes(15);
		Assert.assertEquals(typeData.size(), 0);
	}

	@Test
	public void testRequestAllTypes() throws Exception {
		LinkedList<Type> types = DatabaseTestHelper.getListOfTypes(10);

		for (Type type: types) {
			DatabaseManager.getInstance().createType(type);
		}

		LinkedList<TypeData> typeData = DatabaseManager.getInstance().requestAllTypes();

		Assert.assertEquals(typeData.size(), 10);
		for (TypeData data : typeData) {
			Assert.assertTrue(types.contains(data.getType()));
		}
	}

	@Test
	public void testRequestAllTypesOnEmptyTable() throws Exception {
		LinkedList<TypeData> typeData = DatabaseManager.getInstance().requestAllTypes();
		Assert.assertEquals(typeData.size(), 0);
	}

	///// REQUEST RUNNING HOBBIES /////

	@Test
	public void testRequestRunningHobbiesByAscendantOrder() throws Exception {
		Type type = DatabaseTestHelper.getValidOccupationType1();
		LinkedList<Hobby> orderedHobbies = DatabaseTestHelper.getListOfRunningHobbies(5, true);
		LinkedList<Hobby> shuffledHobbies = new LinkedList<>(orderedHobbies);
		Collections.shuffle(shuffledHobbies);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : shuffledHobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestRunningHobbies(false);

		Assert.assertEquals(hobbyData.size(), 5);
		for (int i = 0; i < orderedHobbies.size(); ++i) {
			Assert.assertEquals(orderedHobbies.get(i), hobbyData.get(i).getHobby());
		}
	}

	@Test
	public void testRequestRunningHobbiesByDescendantOrder() throws Exception {
		Type type = DatabaseTestHelper.getValidOccupationType1();
		LinkedList<Hobby> orderedHobbies = DatabaseTestHelper.getListOfRunningHobbies(5, false);
		LinkedList<Hobby> shuffledHobbies = new LinkedList<>(orderedHobbies);
		Collections.shuffle(shuffledHobbies);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : shuffledHobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestRunningHobbies(true);

		Assert.assertEquals(hobbyData.size(), 5);
		for (int i = 0; i < orderedHobbies.size(); ++i) {
			Assert.assertEquals(orderedHobbies.get(i), hobbyData.get(i).getHobby());
		}
	}

	@Test
	public void testRequestRunningHobbiesOnEmptyTable() throws Exception {
		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestRunningHobbies(true);
		Assert.assertEquals(hobbyData.size(), 0);
	}

	///// REQUEST STOPPED HOBBIES /////

	@Test
	public void testRequestStoppedHobbiesOnEmptyTable() throws Exception {
		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestStoppedHobbies(true);
		Assert.assertEquals(hobbyData.size(), 0);
	}

	@Test
	public void testRequestStoppedHobbiesByAscendantOrder() throws Exception {
		Type type = DatabaseTestHelper.getValidOccupationType1();
		LinkedList<Hobby> orderedHobbies = DatabaseTestHelper.getListOfStoppedHobbies(5, true);
		LinkedList<Hobby> shuffledHobbies = new LinkedList<>(orderedHobbies);
		Collections.shuffle(shuffledHobbies);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : shuffledHobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestStoppedHobbies(false);

		Assert.assertEquals(hobbyData.size(), 5);
		for (int i = 0; i < orderedHobbies.size(); ++i) {
			Assert.assertEquals(orderedHobbies.get(i), hobbyData.get(i).getHobby());
		}
	}

	@Test
	public void testRequestStoppedHobbiesByDescendantOrder() throws Exception {
		Type type = DatabaseTestHelper.getValidOccupationType1();
		LinkedList<Hobby> orderedHobbies = DatabaseTestHelper.getListOfStoppedHobbies(5, false);
		LinkedList<Hobby> shuffledHobbies = new LinkedList<>(orderedHobbies);
		Collections.shuffle(shuffledHobbies);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : shuffledHobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestStoppedHobbies(true);

		Assert.assertEquals(hobbyData.size(), 5);
		for (int i = 0; i < orderedHobbies.size(); ++i) {
			Assert.assertEquals(orderedHobbies.get(i), hobbyData.get(i).getHobby());
		}
	}
	
	///// REQUEST EVENTS /////


	@Test
	public void testRequestEventsOnEmptyTable() throws Exception {
		LinkedList<EventData> eventData = DatabaseManager.getInstance().requestEvents(true);
		Assert.assertEquals(eventData.size(), 0);
	}

	@Test
	public void testRequestEventsByChronologicalOrder() throws Exception {
		Type type = DatabaseTestHelper.getValidOccupationType1();
		LinkedList<Event> orderedEvents = DatabaseTestHelper.getListOfEvents(5, true);
		LinkedList<Event> shuffledEvents = new LinkedList<>(orderedEvents);
		Collections.shuffle(shuffledEvents);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Event event : shuffledEvents) {
			DatabaseManager.getInstance().createEvent(event, typeData.getId());
		}

		LinkedList<EventData> eventData = DatabaseManager.getInstance().requestEvents(true);

		Assert.assertEquals(eventData.size(), 5);
		for (int i = 0; i < orderedEvents.size(); ++i) {
			Assert.assertEquals(orderedEvents.get(i), eventData.get(i).getEvent());
		}
	}

	@Test
	public void testRequestEventsByNonchronologicalOrder() throws Exception {
		Type type = DatabaseTestHelper.getValidOccupationType1();
		LinkedList<Event> orderedEvents = DatabaseTestHelper.getListOfEvents(5, false);
		LinkedList<Event> shuffledEvents = new LinkedList<>(orderedEvents);
		Collections.shuffle(shuffledEvents);

		TypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Event event : shuffledEvents) {
			DatabaseManager.getInstance().createEvent(event, typeData.getId());
		}

		LinkedList<EventData> eventData = DatabaseManager.getInstance().requestEvents(false);

		Assert.assertEquals(eventData.size(), 5);
		for (int i = 0; i < orderedEvents.size(); ++i) {
			Assert.assertEquals(orderedEvents.get(i), eventData.get(i).getEvent());
		}
	}
}
