package esia.timewatcher;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.EntryAlreadyExistsException;
import esia.timewatcher.database.OccupationTypeData;
import esia.timewatcher.structures.OccupationType;

@RunWith(AndroidJUnit4.class)
public class TestTypeDatabase {

    ///// BEFORE /////

    @Before
    public void resetDatabase() {
        InstrumentationRegistry.getTargetContext().deleteDatabase(DatabaseManager.DATABASE_NAME);
        DatabaseManager.initializeInstance(InstrumentationRegistry.getTargetContext());
    }

    ///// CREATE /////

	@Test
	public void createValidType() throws Exception {
		long initTypeSize = DatabaseManager.getInstance().getTypeNumber();
		OccupationType type = DatabaseTestHelper.getValidOccupationType1();

		OccupationTypeData data = DatabaseManager.getInstance().createType(type);

		Assert.assertNotNull(data);
		Assert.assertEquals(data.getOccupationType(), type);
		Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize+1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createInvalidType() throws Exception {
		OccupationType type = DatabaseTestHelper.getInvalidOccupationType();
		DatabaseManager.getInstance().createType(type);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNullType() throws Exception {
		DatabaseManager.getInstance().createType(null);
	}

    @Test(expected = EntryAlreadyExistsException.class)
    public void createExistentType() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getTypeNumber();
        OccupationType type = DatabaseTestHelper.getValidOccupationType1();

        DatabaseManager.getInstance().createType(type);
        OccupationTypeData data = DatabaseManager.getInstance().createType(type);

        Assert.assertNull(data);
        Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize+1);
    }

    ///// REQUEST /////

    @Test
    public void requestExistentTypeById() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getTypeNumber();
        OccupationType type = DatabaseTestHelper.getValidOccupationType1();

        OccupationTypeData data1 = DatabaseManager.getInstance().createType(type);
        OccupationTypeData data2 = DatabaseManager.getInstance().requestType(data1.getId());

        Assert.assertNotNull(data2);
        Assert.assertEquals(data1.getId(), data2.getId());
        Assert.assertEquals(data2.getOccupationType(), type);
        Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize+1);
    }

    @Test
    public void requestExistentTypeByName() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getTypeNumber();
        OccupationType type = DatabaseTestHelper.getValidOccupationType1();

        OccupationTypeData data1 = DatabaseManager.getInstance().createType(type);
        OccupationTypeData data2 = DatabaseManager.getInstance().requestType(type.getName());

        Assert.assertNotNull(data2);
        Assert.assertEquals(data1.getId(), data2.getId());
        Assert.assertEquals(data2.getOccupationType(), type);
        Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize+1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestNonexistentTypeById() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getTypeNumber();

        OccupationTypeData data = DatabaseManager.getInstance().requestType(1);

        Assert.assertNull(data);
        Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestNonexistentTypeByName() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getTypeNumber();

        OccupationTypeData data = DatabaseManager.getInstance().requestType("test");

        Assert.assertNull(data);
        Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestTypeByInvalidName() throws Exception {
        DatabaseManager.getInstance().requestType("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestTypeByNullName() throws Exception {
        DatabaseManager.getInstance().requestType(null);
    }

    ///// UPDATE /////

    @Test
    public void updateValidExistentType() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getTypeNumber();
        OccupationType type1 = DatabaseTestHelper.getValidOccupationType1();
        OccupationType type2 = DatabaseTestHelper.getValidOccupationType2();

        OccupationTypeData data1 = DatabaseManager.getInstance().createType(type1);
        OccupationTypeData data2 = DatabaseManager.getInstance().updateType(data1.getId(), type2);

        Assert.assertNotNull(data2);
        Assert.assertEquals(data1.getId(), data2.getId());
        Assert.assertEquals(data2.getOccupationType(), type2);
        Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize+1);
    }

	@Test(expected = IllegalArgumentException.class)
	public void updateNullExistentType() throws Exception {
		OccupationType type1 = DatabaseTestHelper.getValidOccupationType1();

		OccupationTypeData data1 = DatabaseManager.getInstance().createType(type1);
		DatabaseManager.getInstance().updateType(data1.getId(), null);
	}

    @Test(expected = IllegalArgumentException.class)
    public void updateInvalidExistentType() throws Exception {
        OccupationType type1 = DatabaseTestHelper.getValidOccupationType1();
        OccupationType type2 = DatabaseTestHelper.getInvalidOccupationType();

        OccupationTypeData data1 = DatabaseManager.getInstance().createType(type1);
        DatabaseManager.getInstance().updateType(data1.getId(), type2);
    }

	@Test(expected = IllegalArgumentException.class)
	public void updateDuplicateExistentType() throws Exception {
		OccupationType type1 = DatabaseTestHelper.getValidOccupationType1();
		OccupationType type2 = DatabaseTestHelper.getValidOccupationType2();

		OccupationTypeData data1 = DatabaseManager.getInstance().createType(type1);
		DatabaseManager.getInstance().createType(type2);
		DatabaseManager.getInstance().updateType(data1.getId(), type2);
	}

    @Test(expected = IllegalArgumentException.class)
    public void updateNonexistentType() throws Exception {
        OccupationType type = DatabaseTestHelper.getValidOccupationType1();
        DatabaseManager.getInstance().updateType(1, type);
    }

    ///// DELETE /////

    @Test
    public void deleteExistentType() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getTypeNumber();
        OccupationType type = DatabaseTestHelper.getValidOccupationType1();

        OccupationTypeData data1 = DatabaseManager.getInstance().createType(type);
        DatabaseManager.getInstance().deleteType(data1.getId());
		Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteNonexistentType() throws Exception {
        DatabaseManager.getInstance().deleteType(1);
    }

    ///// EXISTENCE /////

	@Test
	public void testExistentTypeById() throws Exception {
		long initTypeSize = DatabaseManager.getInstance().getTypeNumber();
		OccupationType type = DatabaseTestHelper.getValidOccupationType1();

		OccupationTypeData data = DatabaseManager.getInstance().createType(type);
		boolean exists = DatabaseManager.getInstance().typeExists(data.getId());

		Assert.assertTrue(exists);
		Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize+1);
	}

	@Test
	public void testNonexistentTypeById() throws Exception {
		long initTypeSize = DatabaseManager.getInstance().getTypeNumber();

		boolean exists = DatabaseManager.getInstance().typeExists(1);

		Assert.assertFalse(exists);
		Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize);
	}

	@Test
	public void testExistentTypeByName() throws Exception {
		long initTypeSize = DatabaseManager.getInstance().getTypeNumber();
		OccupationType type = DatabaseTestHelper.getValidOccupationType1();

		OccupationTypeData data = DatabaseManager.getInstance().createType(type);
		boolean exists = DatabaseManager.getInstance().typeExists(data.getOccupationType().getName());

		Assert.assertTrue(exists);
		Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize+1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTypeByInvalidName() throws Exception {
		DatabaseManager.getInstance().typeExists("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTypeByNullName() throws Exception {
		DatabaseManager.getInstance().typeExists(null);
	}

	@Test
	public void testNonexistentTypeByName() throws Exception {
		long initTypeSize = DatabaseManager.getInstance().getTypeNumber();

		boolean exists = DatabaseManager.getInstance().typeExists("test");

		Assert.assertFalse(exists);
		Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize);
	}

    ///// SIZE OF TABLE /////

    @Test
    public void emptyTable() throws Exception {
        Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), 0);
    }

    ///// ID FROM NAME /////

	@Test
	public void requestExistentIdFromName() throws Exception {
		long initTypeSize = DatabaseManager.getInstance().getTypeNumber();
		OccupationType type = DatabaseTestHelper.getValidOccupationType1();

		OccupationTypeData data1 = DatabaseManager.getInstance().createType(type);
		long id = DatabaseManager.getInstance().requestTypeId(type.getName());

		Assert.assertEquals(id, data1.getId());
		Assert.assertEquals(DatabaseManager.getInstance().getTypeNumber(), initTypeSize+1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void requestNonexistentIdFromName() throws Exception {
		DatabaseManager.getInstance().requestTypeId("test");
	}

	@Test(expected = IllegalArgumentException.class)
	public void requestIdFromNullName() throws Exception {
		DatabaseManager.getInstance().requestTypeId(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void requestIdFromInvalidName() throws Exception {
		DatabaseManager.getInstance().requestTypeId("");
	}
}
