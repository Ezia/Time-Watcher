package esia.timewatcher;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.EventData;
import esia.timewatcher.database.TypeData;
import esia.timewatcher.structures.Event;
import esia.timewatcher.structures.Type;

@RunWith(AndroidJUnit4.class)
public class TestEventDatabase {

	///// BEFORE /////

	@Before
	public void resetDatabase() {
		InstrumentationRegistry.getTargetContext().deleteDatabase(DatabaseManager.DATABASE_NAME);
		DatabaseManager.initializeInstance(InstrumentationRegistry.getTargetContext());
	}

	///// CREATE /////

	@Test
	public void createValidEvent() throws Exception {
		long initEventTableSize = DatabaseManager.getInstance().getEventNumber();
		Event event = DatabaseTestHelper.getValidEvent1();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		EventData data2 = DatabaseManager.getInstance().createEvent(event, data1.getId());

		Assert.assertNotNull(data2);
		Assert.assertEquals(data2.getEvent(), event);
		Assert.assertEquals(data2.getTypeData().getId(), data1.getId());
		Assert.assertEquals(DatabaseManager.getInstance().getEventNumber(), initEventTableSize+1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createInvalidEvent() throws Exception {
		Event event = DatabaseTestHelper.getInvalidEvent();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		DatabaseManager.getInstance().createEvent(event, data1.getId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNullEvent() throws Exception {
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		DatabaseManager.getInstance().createEvent(null, data1.getId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNonexistentTypeEvent() throws Exception {
		Event event = DatabaseTestHelper.getValidEvent1();
		DatabaseManager.getInstance().createEvent(event, 1);
	}

	///// REQUEST /////

	@Test
	public void requestExistentEvent() throws Exception {
		long initEventTableSize = DatabaseManager.getInstance().getEventNumber();
		Event event = DatabaseTestHelper.getValidEvent1();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		EventData data2 = DatabaseManager.getInstance().createEvent(event, data1.getId());
		EventData data3 = DatabaseManager.getInstance().requestEvent(data2.getId());

		Assert.assertNotNull(data3);
		Assert.assertEquals(data3.getEvent(), event);
		Assert.assertEquals(data2.getId(), data3.getId());
		Assert.assertEquals(data3.getTypeData().getId(), data1.getId());
		Assert.assertEquals(DatabaseManager.getInstance().getEventNumber(), initEventTableSize+1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void requestNonexistentEvent() throws Exception {
		DatabaseManager.getInstance().requestEvent(1);
	}

	///// UPDATE /////

	@Test
	public void updateValidExistentEvent() throws Exception {
		long initTypeSize = DatabaseManager.getInstance().getEventNumber();
		Event event1 = DatabaseTestHelper.getValidEvent1();
		Event event2 = DatabaseTestHelper.getValidEvent2();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		EventData data2 = DatabaseManager.getInstance().createEvent(event1, data1.getId());
		EventData data3 = DatabaseManager.getInstance().updateEvent(data2.getId(), event2, data1.getId());

		Assert.assertNotNull(data3);
		Assert.assertEquals(data2.getId(), data3.getId());
		Assert.assertEquals(data3.getEvent(), event2);
		Assert.assertEquals(data3.getTypeData().getId(), data1.getId());
		Assert.assertEquals(DatabaseManager.getInstance().getEventNumber(), initTypeSize+1);
	}

	@Test
	public void updateExistentEventType() throws Exception {
		long initEventSize = DatabaseManager.getInstance().getEventNumber();
		Event event1 = DatabaseTestHelper.getValidEvent1();
		Type type1 = DatabaseTestHelper.getValidOccupationType1();
		Type type2 = DatabaseTestHelper.getValidOccupationType2();

		TypeData data1 = DatabaseManager.getInstance().createType(type1);
		TypeData data2 = DatabaseManager.getInstance().createType(type2);
		EventData data3 = DatabaseManager.getInstance().createEvent(event1, data1.getId());
		EventData data4 = DatabaseManager.getInstance().updateEvent(data3.getId(), event1, data2.getId());

		Assert.assertNotNull(data4);
		Assert.assertEquals(data3.getId(), data4.getId());
		Assert.assertEquals(data4.getEvent(), event1);
		Assert.assertEquals(data4.getTypeData().getId(), data2.getId());
		Assert.assertEquals(DatabaseManager.getInstance().getEventNumber(), initEventSize+1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateNonexistentEvent() throws Exception {
		Event event1 = DatabaseTestHelper.getValidEvent1();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		DatabaseManager.getInstance().updateEvent(1, event1, data1.getId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateExistentEventNonexistentType() throws Exception {
		Event event1 = DatabaseTestHelper.getValidEvent1();
		Event event2 = DatabaseTestHelper.getValidEvent2();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		EventData data2 = DatabaseManager.getInstance().createEvent(event1, data1.getId());
		DatabaseManager.getInstance().updateEvent(data2.getId(), event2, data1.getId()+1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateInvalidExistentEvent() throws Exception {
		Event event1 = DatabaseTestHelper.getValidEvent1();
		Event event2 = DatabaseTestHelper.getInvalidEvent();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		EventData data2 = DatabaseManager.getInstance().createEvent(event1, data1.getId());
		DatabaseManager.getInstance().updateEvent(data2.getId(), event2, data1.getId());
	}

	@Test(expected = IllegalArgumentException.class)
	public void updateNullExistentEvent() throws Exception {
		Event event1 = DatabaseTestHelper.getValidEvent1();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		EventData data2 = DatabaseManager.getInstance().createEvent(event1, data1.getId());
		DatabaseManager.getInstance().updateEvent(data2.getId(), null, data1.getId());
	}

	///// DELETE /////

	@Test
	public void deleteExistentEvent() throws Exception {
		long initEventTableSize = DatabaseManager.getInstance().getEventNumber();
		Event event = DatabaseTestHelper.getValidEvent1();
		Type type = DatabaseTestHelper.getValidOccupationType1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		EventData data2 = DatabaseManager.getInstance().createEvent(event, data1.getId());
		DatabaseManager.getInstance().deleteEvent(data2.getId());

		Assert.assertEquals(DatabaseManager.getInstance().getEventNumber(), initEventTableSize);
	}

	@Test(expected = IllegalArgumentException.class)
	public void deleteNonexistentEvent() throws Exception {
		DatabaseManager.getInstance().deleteEvent(1);
	}

	///// EXISTENCE /////

	@Test
	public void testExistentEvent() throws Exception {
		long initEventSize = DatabaseManager.getInstance().getEventNumber();
		Type type = DatabaseTestHelper.getValidOccupationType1();
		Event event = DatabaseTestHelper.getValidEvent1();

		TypeData data1 = DatabaseManager.getInstance().createType(type);
		EventData data2 = DatabaseManager.getInstance().createEvent(event, data1.getId());
		boolean exists = DatabaseManager.getInstance().eventExists(data2.getId());

		Assert.assertTrue(exists);
		Assert.assertEquals(DatabaseManager.getInstance().getEventNumber(), initEventSize+1);
	}

	@Test
	public void testNonexistentEvent() throws Exception {
		long initEventSize = DatabaseManager.getInstance().getEventNumber();

		boolean exists = DatabaseManager.getInstance().eventExists(1);

		Assert.assertFalse(exists);
		Assert.assertEquals(DatabaseManager.getInstance().getEventNumber(), initEventSize);
	}

	///// TABLE SIZE /////

	@Test
	public void emptyTable() throws Exception {
		Assert.assertEquals(DatabaseManager.getInstance().getEventNumber(), 0);
	}
}
