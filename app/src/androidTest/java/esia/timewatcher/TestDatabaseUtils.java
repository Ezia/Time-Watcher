package esia.timewatcher;

import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.LinkedList;

import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.OccupationTypeData;
import esia.timewatcher.structures.OccupationType;

@RunWith(JUnit4.class)
public class TestDatabaseUtils {
	///// BEFORE /////

	@Before
	public void resetDatabase() {
		InstrumentationRegistry.getTargetContext().deleteDatabase(DatabaseManager.DATABASE_NAME);
		DatabaseManager.initializeInstance(InstrumentationRegistry.getTargetContext());
	}

	///// TYPE REQUESTS /////

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
}
