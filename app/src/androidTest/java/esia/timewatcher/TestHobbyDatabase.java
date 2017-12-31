package esia.timewatcher;

import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.HobbyData;
import esia.timewatcher.database.TypeData;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.structures.Type;

@RunWith(JUnit4.class)
public class TestHobbyDatabase {

	///// BEFORE /////

	@Before
	public void resetDatabase() {
		InstrumentationRegistry.getTargetContext().deleteDatabase(DatabaseManager.DATABASE_NAME);
		DatabaseManager.initializeInstance(InstrumentationRegistry.getTargetContext());
	}

	///// CREATE /////

	@Test
	public void createValidHobby() throws Exception {
		long initHobbySize = DatabaseManager.getInstance().getHobbyNumber();
		Hobby hobby = DatabaseTestHelper.getValidHobby1();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		HobbyData data2 = DatabaseManager.getInstance().createHobby(hobby, data1.getId());

		Assert.assertNotNull(data2);
		Assert.assertEquals(data2.getHobby(), hobby);
		Assert.assertEquals(data2.getTypeData().getId(), data1.getId());
		Assert.assertEquals(DatabaseManager.getInstance().getHobbyNumber(), initHobbySize+1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createInvalidHobby() throws Exception {
		Hobby hobby = DatabaseTestHelper.getInvalidHobby();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		DatabaseManager.getInstance().createHobby(hobby, data1.getId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNullHobby() throws Exception {
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		DatabaseManager.getInstance().createHobby(null, data1.getId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNonexistentTypeHobby() throws Exception {
		Hobby hobby = DatabaseTestHelper.getValidHobby1();
		DatabaseManager.getInstance().createHobby(hobby, 1);
	}

	///// REQUEST /////

	@Test
	public void requestExistentHobby() throws Exception {
		long initHobbyTableSize = DatabaseManager.getInstance().getHobbyNumber();
		Hobby hobby = DatabaseTestHelper.getValidHobby1();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		HobbyData data2 = DatabaseManager.getInstance().createHobby(hobby, data1.getId());
		HobbyData data3 = DatabaseManager.getInstance().requestHobby(data2.getId());

		Assert.assertNotNull(data3);
		Assert.assertEquals(data3.getHobby(), hobby);
		Assert.assertEquals(data2.getId(), data3.getId());
		Assert.assertEquals(data3.getTypeData().getId(), data1.getId());
		Assert.assertEquals(DatabaseManager.getInstance().getHobbyNumber(), initHobbyTableSize+1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void requestNonexistentHobby() throws Exception {
		DatabaseManager.getInstance().requestHobby(1);
	}

	///// UPDATE /////

	@Test
	public void updateValidExistentHobby() throws Exception {
		long initTypeSize = DatabaseManager.getInstance().getHobbyNumber();
		Hobby hobby1 = DatabaseTestHelper.getValidHobby1();
		Hobby hobby2 = DatabaseTestHelper.getValidHobby2();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		HobbyData data2 = DatabaseManager.getInstance().createHobby(hobby1, data1.getId());
		HobbyData data3 = DatabaseManager.getInstance().updateHobby(data2.getId(), hobby2, data1.getId());

		Assert.assertNotNull(data3);
		Assert.assertEquals(data2.getId(), data3.getId());
		Assert.assertEquals(data3.getHobby(), hobby2);
		Assert.assertEquals(data3.getTypeData().getId(), data1.getId());
		Assert.assertEquals(DatabaseManager.getInstance().getHobbyNumber(), initTypeSize+1);
	}

	@Test
	public void updateExistentHobbyType() throws Exception {
		long initHobbySize = DatabaseManager.getInstance().getHobbyNumber();
		Hobby hobby1 = DatabaseTestHelper.getValidHobby1();
		Type type1 = DatabaseTestHelper.getValidOccupationType1();
		Type type2 = DatabaseTestHelper.getValidOccupationType2();

		TypeData data1 = DatabaseManager.getInstance().createType(type1);
		TypeData data2 = DatabaseManager.getInstance().createType(type2);
		HobbyData data3 = DatabaseManager.getInstance().createHobby(hobby1, data1.getId());
		HobbyData data4 = DatabaseManager.getInstance().updateHobby(data3.getId(), hobby1, data2.getId());

		Assert.assertNotNull(data4);
		Assert.assertEquals(data3.getId(), data4.getId());
		Assert.assertEquals(data4.getHobby(), hobby1);
		Assert.assertEquals(data4.getTypeData().getId(), data2.getId());
		Assert.assertEquals(DatabaseManager.getInstance().getHobbyNumber(), initHobbySize+1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateNonexistentHobby() throws Exception {
		Hobby hobby1 = DatabaseTestHelper.getValidHobby1();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		DatabaseManager.getInstance().updateHobby(1, hobby1, data1.getId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateExistentHobbyNonexistentType() throws Exception {
		Hobby hobby1 = DatabaseTestHelper.getValidHobby1();
		Hobby hobby2 = DatabaseTestHelper.getValidHobby2();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		HobbyData data2 = DatabaseManager.getInstance().createHobby(hobby1, data1.getId());
		DatabaseManager.getInstance().updateHobby(data2.getId(), hobby2, data1.getId()+1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateInvalidExistentHobby() throws Exception {
		Hobby hobby1 = DatabaseTestHelper.getValidHobby1();
		Hobby hobby2 = DatabaseTestHelper.getInvalidHobby();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		HobbyData data2 = DatabaseManager.getInstance().createHobby(hobby1, data1.getId());
		DatabaseManager.getInstance().updateHobby(data2.getId(), hobby2, data1.getId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateNullExistentHobby() throws Exception {
		Hobby hobby1 = DatabaseTestHelper.getValidHobby1();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		HobbyData data2 = DatabaseManager.getInstance().createHobby(hobby1, data1.getId());
		DatabaseManager.getInstance().updateHobby(data2.getId(), null, data1.getId());
	}

	///// DELETE /////

	@Test
	public void deleteExistentHobby() throws Exception {
		long initHobbyTableSize = DatabaseManager.getInstance().getHobbyNumber();
		Hobby hobby = DatabaseTestHelper.getValidHobby1();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		HobbyData data2 = DatabaseManager.getInstance().createHobby(hobby, data1.getId());
		DatabaseManager.getInstance().deleteHobby(data2.getId());

		Assert.assertEquals(DatabaseManager.getInstance().getHobbyNumber(), initHobbyTableSize);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteNonexistentHobby() throws Exception {
		DatabaseManager.getInstance().deleteHobby(1);
	}

	///// EXISTENCE /////

	@Test
	public void testExistentHobby() throws Exception {
		long initHobbySize = DatabaseManager.getInstance().getHobbyNumber();
		Type type = DatabaseTestHelper.getValidOccupationType1();
		Hobby hobby = DatabaseTestHelper.getValidHobby1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		HobbyData data2 = DatabaseManager.getInstance().createHobby(hobby, data1.getId());
		boolean exists = DatabaseManager.getInstance().hobbyExists(data2.getId());

		Assert.assertTrue(exists);
		Assert.assertEquals(DatabaseManager.getInstance().getHobbyNumber(), initHobbySize+1);
	}

	@Test
	public void testNonexistentHobby() throws Exception {
		long initHobbySize = DatabaseManager.getInstance().getHobbyNumber();

		boolean exists = DatabaseManager.getInstance().hobbyExists(1);

		Assert.assertFalse(exists);
		Assert.assertEquals(DatabaseManager.getInstance().getHobbyNumber(), initHobbySize);
	}

	///// TABLE SIZE /////

	@Test
	public void emptyTable() throws Exception {
		Assert.assertEquals(DatabaseManager.getInstance().getHobbyNumber(), 0);
	}
}
