package esia.timewatcher;

import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.OccupationTypeData;
import esia.timewatcher.structures.OccupationType;

@RunWith(AndroidJUnit4.class)
public class TestOccupationTypeDatabase {

    ///// BEFORE /////

    @Before
    public void resetDatabase() {
        InstrumentationRegistry.getTargetContext().deleteDatabase(DatabaseManager.DATABASE_NAME);
        DatabaseManager.initializeInstance(InstrumentationRegistry.getTargetContext());
    }

    ///// CREATE /////

    @Test
    public void createExistingType() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();
        OccupationType type = new OccupationType("test",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );

        DatabaseManager.getInstance().createOccupationType(type);
        OccupationTypeData data = DatabaseManager.getInstance().createOccupationType(type);

        Assert.assertNull(data);
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize+1);
    }

    @Test
    public void createValidType() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();
        OccupationType type = new OccupationType("test",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );

        OccupationTypeData data = DatabaseManager.getInstance().createOccupationType(type);

        Assert.assertNotNull(data);
        Assert.assertNotEquals(data.getId(), -1);
        Assert.assertEquals(data.getOccupationType(), type);
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize+1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createInvalidType() throws Exception {
        OccupationType type = new OccupationType(null,
                BitmapFactory.decodeResource(
                InstrumentationRegistry.getTargetContext().getResources(),
                R.drawable.lan
        ));
        DatabaseManager.getInstance().createOccupationType(type);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullType() throws Exception {
        DatabaseManager.getInstance().createOccupationType(null);
    }
    
    ///// REQUEST /////

    @Test
    public void requestExistentTypeById() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();
        OccupationType type = new OccupationType("test",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );

        OccupationTypeData data1 = DatabaseManager.getInstance().createOccupationType(type);
        OccupationTypeData data2 = DatabaseManager.getInstance().requestOccupationTypeById(data1.getId());

        Assert.assertNotNull(data2);
        Assert.assertEquals(data1.getId(), data2.getId());
        Assert.assertEquals(data2.getOccupationType(), type);
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize+1);
    }

    @Test
    public void requestExistentTypeByName() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();
        OccupationType type = new OccupationType("test",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );

        OccupationTypeData data1 = DatabaseManager.getInstance().createOccupationType(type);
        OccupationTypeData data2 = DatabaseManager.getInstance().requestOccupationTypeByName("test");

        Assert.assertNotNull(data2);
        Assert.assertEquals(data1.getId(), data2.getId());
        Assert.assertEquals(data2.getOccupationType(), type);
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize+1);
    }

    @Test
    public void requestNonexistentTypeById() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();

        OccupationTypeData data = DatabaseManager.getInstance().requestOccupationTypeById(1);

        Assert.assertNull(data);
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize);
    }

    @Test
    public void requestNonexistentTypeByValidName() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();

        OccupationTypeData data = DatabaseManager.getInstance().requestOccupationTypeByName("test");

        Assert.assertNull(data);
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestNonexistentTypeByInvalidName() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();

        DatabaseManager.getInstance().requestOccupationTypeByName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void requestNonexistentTypeByNullName() throws Exception {
        DatabaseManager.getInstance().requestOccupationTypeByName(null);
    }

    ///// UPDATE /////

    @Test
    public void updateValidExistentType() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();
        OccupationType type1 = new OccupationType("test1",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );
        OccupationType type2 = new OccupationType("test2",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );

        OccupationTypeData data1 = DatabaseManager.getInstance().createOccupationType(type1);
        OccupationTypeData data2 = DatabaseManager.getInstance().updateOccupationType(data1.getId(), type2);

        Assert.assertNotNull(data2);
        Assert.assertEquals(data1.getId(), data2.getId());
        Assert.assertEquals(data2.getOccupationType(), type2);
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize+1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateInvalidExistentType() throws Exception {
        OccupationType type1 = new OccupationType("test1",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );
        OccupationType type2 = new OccupationType("",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );

        OccupationTypeData data1 = DatabaseManager.getInstance().createOccupationType(type1);
        DatabaseManager.getInstance().updateOccupationType(data1.getId(), type2);
    }

    @Test
    public void updateValidNonexistentType() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();
        OccupationType type = new OccupationType("test1",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );

        OccupationTypeData data = DatabaseManager.getInstance().updateOccupationType(1, type);

        Assert.assertNull(data);
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize);
    }

    ///// DELETE /////

    @Test
    public void deleteExistentType() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();
        OccupationType type = new OccupationType("test",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );

        OccupationTypeData data1 = DatabaseManager.getInstance().createOccupationType(type);
        boolean deleted = DatabaseManager.getInstance().deleteOccupationType(data1.getId());
        OccupationTypeData data2 = DatabaseManager.getInstance().requestOccupationTypeById(data1.getId());

        Assert.assertNull(data2);
        Assert.assertTrue(deleted);
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize);
    }

    @Test
    public void deleteNonexistentType() throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();

        boolean deleted = DatabaseManager.getInstance().deleteOccupationType(1);

        Assert.assertFalse(deleted);
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize);
    }

    ///// MISC /////

    @Test
    public void emptyTable() throws Exception {
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), 0);
    }
}
