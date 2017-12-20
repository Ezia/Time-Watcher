package esia.timewatcher;

import android.graphics.BitmapFactory;
import android.support.test.InstrumentationRegistry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.LinkedList;

import esia.timewatcher.database.DatabaseManager;
import esia.timewatcher.database.OccupationTypeData;
import esia.timewatcher.structures.OccupationType;

@RunWith(Parameterized.class)
public class TestOccupationTypeDatabase {

    ///// PARAMETERS /////

    private OccupationType type1;
    private OccupationType type2;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        LinkedList<Object[]> list =  new LinkedList<>();

        OccupationType type1 = new OccupationType("myName",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.twitter
                )
        );

        OccupationType type2 = new OccupationType("myName2",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );

        OccupationType type3 = new OccupationType("myName2",
                null
        );

        OccupationType type4 = new OccupationType("",
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );

        OccupationType type5 = new OccupationType(null,
                BitmapFactory.decodeResource(
                        InstrumentationRegistry.getTargetContext().getResources(),
                        R.drawable.lan
                )
        );

        OccupationType type6 = new OccupationType(null, null);

        list.add(new Object[] {type1, type2});
        list.add(new Object[] {type1, null});
        list.add(new Object[] {null, type2});
        list.add(new Object[] {null, null});
        list.add(new Object[] {type1, type3});
        list.add(new Object[] {type1, type4});
        list.add(new Object[] {type1, type5});
        list.add(new Object[] {type1, type6});
        list.add(new Object[] {type3, type1});
        list.add(new Object[] {type4, type1});
        list.add(new Object[] {type5, type1});
        list.add(new Object[] {type6, type1});
        return list;
    }

    public TestOccupationTypeDatabase(OccupationType type1, OccupationType type2) {
        this.type1 = type1;
        this.type2 = type2;
    }

    ///// BEFORE AND AFTER /////

    @Before
    public void resetDatabase() {
        InstrumentationRegistry.getTargetContext().deleteDatabase(DatabaseManager.DATABASE_NAME);
        DatabaseManager.initializeInstance(InstrumentationRegistry.getTargetContext());
    }

    ///// TEST UTILS /////

    public static OccupationTypeData createType(OccupationType type) throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();

        OccupationTypeData data = DatabaseManager.getInstance().createOccupationType(type);

        if (type == null || !type.isValid()) {
            Assert.assertNull(data);
            Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize);
        } else {
            Assert.assertNotNull(data);
            Assert.assertNotEquals(data.getId(), -1);
            Assert.assertEquals(data.getOccupationType(), type);
            Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize+1);
        }
        return data;
    }

    public static OccupationTypeData requestType(long id, boolean exists, OccupationType knownValue) throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();

        OccupationTypeData data = DatabaseManager.getInstance().requestOccupationType(id);

        if (knownValue == null || !knownValue.isValid() || !exists) {
            Assert.assertNull(data);
        } else {
            Assert.assertNotNull(data);
            Assert.assertEquals(data.getId(), id);
            Assert.assertEquals(data.getOccupationType(), knownValue);
        }
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize);
        return data;
    }

    public static OccupationTypeData updateType(long id, boolean exists, OccupationType newValue) throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();

        OccupationTypeData data = DatabaseManager.getInstance().updateOccupationType(id, newValue);

        if (newValue == null || !newValue.isValid() || !exists) {
            Assert.assertNull(data);
        } else {
            Assert.assertNotNull(data);
            Assert.assertEquals(data.getId(), id);
            Assert.assertEquals(data.getOccupationType(), newValue);
        }
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize);
        return data;
    }

    public static boolean deleteType(long id, boolean exists) throws Exception {
        long initTypeSize = DatabaseManager.getInstance().getOccupationTypeNumber();

        boolean deleted = DatabaseManager.getInstance().deleteOccupationType(id);

        if (!exists) {
            Assert.assertFalse(deleted);
            Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize);
        } else {
            Assert.assertTrue(deleted);
            Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), initTypeSize-1);
        }
        return deleted;
    }

    ///// TESTS /////


    @Test
    public void emptyTable() throws Exception {
        Assert.assertEquals(DatabaseManager.getInstance().getOccupationTypeNumber(), 0);
    }

    @Test
    public void create1Type() throws Exception {
        createType(type1);
    }

    @Test
    public void create2Types() throws Exception {
        createType(type1);
        createType(type2);
    }

    @Test
    public void createAndRequest1Type() throws Exception {
        OccupationTypeData data = createType(type1);
        if (data != null) {
            requestType(data.getId(), true, type1);
        } else {
            requestType(-1, false, type1);
        }
    }

    @Test
    public void createAndUpdate1Type() throws Exception {
        OccupationTypeData data = createType(type1);
        if (data != null) {
            updateType(data.getId(), true, type2);
        } else {
            updateType(-1, false, type2);
        }
    }

    @Test
    public void createAndDelete1Type() throws Exception {
        OccupationTypeData data = createType(type1);
        if (data != null) {
            deleteType(data.getId(), true);
        } else {
            deleteType(-1, false);
        }
    }

    @Test
    public void createDeleteAndRequest1Type() throws Exception {
        OccupationTypeData data = createType(type1);
        if (data != null) {
            deleteType(data.getId(), true);
            requestType(data.getId(), false, type1);
        } else {
            deleteType(-1, false);
            requestType(-1, false, type1);
        }
    }

    @Test
    public void createDeleteAndUpdate1Type() throws Exception {
        OccupationTypeData data = createType(type1);
        if (data != null) {
            deleteType(data.getId(), true);
            updateType(data.getId(), false, type2);
        } else {
            deleteType(-1, false);
            requestType(-1, false, type2);
        }
    }
}
