package esia.timewatcher;

import android.support.test.InstrumentationRegistry;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.database.OccupationTypeData;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.structures.OccupationType;

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
		LinkedList<OccupationType> types = DatabaseTestHelper.getListOfTypes(10);

		for (OccupationType type: types) {
			DatabaseManager.getInstance().createType(type);
		}

		LinkedList<OccupationTypeData> typeData = DatabaseManager.getInstance().requestTypes(5);

		Assert.assertEquals(typeData.size(), 5);
	}

	@Test
	public void testRequestMoreTypes() throws Exception {
		LinkedList<OccupationType> types = DatabaseTestHelper.getListOfTypes(10);

		for (OccupationType type: types) {
			DatabaseManager.getInstance().createType(type);
		}

		LinkedList<OccupationTypeData> typeData = DatabaseManager.getInstance().requestTypes(15);

		Assert.assertEquals(typeData.size(), 10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRequestInvalidNumberOfTypes() throws Exception {
		DatabaseManager.getInstance().requestTypes(0);
	}

	@Test
	public void testRequestTypesOnEmptyTable() throws Exception {
		LinkedList<OccupationTypeData> typeData = DatabaseManager.getInstance().requestTypes(15);
		Assert.assertEquals(typeData.size(), 0);
	}

	@Test
	public void testRequestAllTypes() throws Exception {
		LinkedList<OccupationType> types = DatabaseTestHelper.getListOfTypes(10);

		for (OccupationType type: types) {
			DatabaseManager.getInstance().createType(type);
		}

		LinkedList<OccupationTypeData> typeData = DatabaseManager.getInstance().requestAllTypes();

		Assert.assertEquals(typeData.size(), 10);
	}

	@Test
	public void testRequestAllTypesOnEmptyTable() throws Exception {
		LinkedList<OccupationTypeData> typeData = DatabaseManager.getInstance().requestAllTypes();
		Assert.assertEquals(typeData.size(), 0);
	}

	///// REQUEST RUNNING HOBBIES /////

	@Test
	public void testRequestRunningHobbies() throws Exception {
		OccupationType type = DatabaseTestHelper.getValidOccupationType1();
		LinkedList<Hobby> hobbies = DatabaseTestHelper.getListOfHobbies(5, 7);

		OccupationTypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : hobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestRunningHobbies(true);

		Assert.assertEquals(hobbyData.size(), 5);
	}

	@Test
	public void testRequestRunningHobbiesByAscendantOrder() throws Exception {
		OccupationType type = DatabaseTestHelper.getValidOccupationType1();
		LinkedList<Hobby> orderedHobbies = DatabaseTestHelper.getListOfRunningHobbies(5, true);
		LinkedList<Hobby> shuffledHobbies = new LinkedList<>(orderedHobbies);
		Collections.shuffle(shuffledHobbies);

		OccupationTypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : shuffledHobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestRunningHobbies(false);

		for (int i = 0; i < orderedHobbies.size(); ++i) {
			Assert.assertEquals(orderedHobbies.get(i), hobbyData.get(i).getHobby());
		}
	}

	@Test
	public void testRequestRunningHobbiesByDescendantOrder() throws Exception {
		OccupationType type = DatabaseTestHelper.getValidOccupationType1();
		LinkedList<Hobby> orderedHobbies = DatabaseTestHelper.getListOfRunningHobbies(5, false);
		LinkedList<Hobby> shuffledHobbies = new LinkedList<>(orderedHobbies);
		Collections.shuffle(shuffledHobbies);

		OccupationTypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : shuffledHobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestRunningHobbies(true);

		for (int i = 0; i < orderedHobbies.size(); ++i) {
			Assert.assertEquals(orderedHobbies.get(i), hobbyData.get(i).getHobby());
		}
	}

	///// REQUEST STOPPED HOBBIES /////

	@Test
	public void testRequestRunningHobbiesOnEmptyTable() throws Exception {
		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestRunningHobbies(true);
		Assert.assertEquals(hobbyData.size(), 0);
	}

	@Test
	public void testRequestStoppedHobbies() throws Exception {
		OccupationType type = DatabaseTestHelper.getValidOccupationType1();
		LinkedList<Hobby> hobbies = DatabaseTestHelper.getListOfHobbies(5, 7);

		OccupationTypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : hobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestStoppedHobbies(true);

		Assert.assertEquals(hobbyData.size(), 7);
	}

	@Test
	public void testRequestStoppedHobbiesOnEmptyTable() throws Exception {
		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestStoppedHobbies(true);
		Assert.assertEquals(hobbyData.size(), 0);
	}

	@Test
	public void testRequestStoppedHobbiesByAscendantOrder() throws Exception {
		OccupationType type = DatabaseTestHelper.getValidOccupationType1();
		LinkedList<Hobby> orderedHobbies = DatabaseTestHelper.getListOfStoppedHobbies(5, true);
		LinkedList<Hobby> shuffledHobbies = new LinkedList<>(orderedHobbies);
		Collections.shuffle(shuffledHobbies);

		OccupationTypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : shuffledHobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestStoppedHobbies(false);

		for (int i = 0; i < orderedHobbies.size(); ++i) {
			Assert.assertEquals(orderedHobbies.get(i), hobbyData.get(i).getHobby());
		}
	}

	@Test
	public void testRequestStoppedHobbiesByDescendantOrder() throws Exception {
		OccupationType type = DatabaseTestHelper.getValidOccupationType1();
		LinkedList<Hobby> orderedHobbies = DatabaseTestHelper.getListOfStoppedHobbies(5, false);
		LinkedList<Hobby> shuffledHobbies = new LinkedList<>(orderedHobbies);
		Collections.shuffle(shuffledHobbies);

		OccupationTypeData typeData = DatabaseManager.getInstance().createType(type);
		for (Hobby hobby : shuffledHobbies) {
			DatabaseManager.getInstance().createHobby(hobby, typeData.getId());
		}

		LinkedList<HobbyData> hobbyData = DatabaseManager.getInstance().requestStoppedHobbies(true);

		for (int i = 0; i < orderedHobbies.size(); ++i) {
			Assert.assertEquals(orderedHobbies.get(i), hobbyData.get(i).getHobby());
		}
	}
}
