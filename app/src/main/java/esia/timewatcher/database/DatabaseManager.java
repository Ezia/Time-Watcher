package esia.timewatcher.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import esia.timewatcher.structures.Event;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.structures.OccupationType;

public class DatabaseManager extends SQLiteOpenHelper {
    private static DatabaseManager instance = null;

    private static final int DATABASE_VERSION = 4;

    public static final String DATABASE_NAME = "time_history";

    private static class HobbyTable {
        private static final String TABLE_NAME = "hobby_table";

        private static final String KEY_ID = "hobby_id";
        private static final String KEY_START_DATE = "hobby_start_date";
        private static final String KEY_END_DATE = "hobby_end_date";
        private static final String KEY_TYPE = "hobby_type";
    }

    private static class EventTable {
        private static final String TABLE_NAME = "event_table";

        private static final String KEY_ID = "event_id";
        private static final String KEY_DATE = "event_start_date";
        private static final String KEY_TYPE = "event_type";
    }

    private static class OccupationTypeTable {
        private static final String TABLE_NAME = "occupation_table";

        private static final String KEY_ID = "occupation_id";
        private static final String KEY_NAME = "occupation_name";
        private static final String KEY_ICON = "occupation_icon";
    }

    ///// LIFECYCLE /////

    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Must be called by the application before performing operations
     * @param context Application context
     */
    public static void initializeInstance(Context context) {
        instance = new DatabaseManager(context.getApplicationContext());
    }

    public static DatabaseManager getInstance() {
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_OCCUPATION_TYPES_TABLE =
                "CREATE TABLE " + OccupationTypeTable.TABLE_NAME + "("
                    + OccupationTypeTable.KEY_ID + " INTEGER PRIMARY KEY,"
                    + OccupationTypeTable.KEY_NAME + " TEXT UNIQUE,"
                    + OccupationTypeTable.KEY_ICON + " BLOB"
                + ")";

        String CREATE_HOBBIES_TABLE =
                "CREATE TABLE " + HobbyTable.TABLE_NAME + "("
                    + HobbyTable.KEY_ID + " INTEGER PRIMARY KEY,"
                    + HobbyTable.KEY_TYPE + " INTEGER,"
                    + HobbyTable.KEY_START_DATE + " INTEGER,"
                    + HobbyTable.KEY_END_DATE + " INTEGER,"
                    + "FOREIGN KEY(" + HobbyTable.KEY_TYPE + ")"
                        + " REFERENCES " + OccupationTypeTable.TABLE_NAME
                        + "(" + OccupationTypeTable.KEY_ID + ")"
                + ")";

        String CREATE_EVENTS_TABLE =
                "CREATE TABLE " + EventTable.TABLE_NAME + "("
                    + EventTable.KEY_ID + " INTEGER PRIMARY KEY,"
                    + EventTable.KEY_TYPE + " INTEGER,"
                    + EventTable.KEY_DATE + " INTEGER,"
                    + "FOREIGN KEY(" + EventTable.KEY_TYPE + ")"
                        + " REFERENCES " + OccupationTypeTable.TABLE_NAME
                        + "(" + OccupationTypeTable.KEY_ID + ")"
                + ")";

        db.execSQL(CREATE_OCCUPATION_TYPES_TABLE);
        db.execSQL(CREATE_HOBBIES_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + OccupationTypeTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EventTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HobbyTable.TABLE_NAME);

        // Creating tables again
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + OccupationTypeTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EventTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + HobbyTable.TABLE_NAME);

        // Creating tables again
        onCreate(db);
    }

    ///// OCCUPATION TYPES /////

    public long getOccupationTypeNumber() {
        SQLiteDatabase db = this.getWritableDatabase();

        long numberOfRows = DatabaseUtils.queryNumEntries(db, OccupationTypeTable.TABLE_NAME);

        db.close();

        return numberOfRows;
    }

    public OccupationTypeData createOccupationType(OccupationType occupationType) {
        if (occupationType == null || !occupationType.isValid()) {
            return null;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OccupationTypeTable.KEY_NAME, occupationType.getName());
        values.put(OccupationTypeTable.KEY_ICON, bitmapToBytes(occupationType.getIcon()));

        long id = db.insert(OccupationTypeTable.TABLE_NAME, null, values);
        db.close();

        if (id == -1) {
            return null;
        }
        return new OccupationTypeData(id, new OccupationType(occupationType));
    }

    public OccupationTypeData requestOccupationType(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(OccupationTypeTable.TABLE_NAME,
                new String[] { OccupationTypeTable.KEY_ID,
                        OccupationTypeTable.KEY_NAME, OccupationTypeTable.KEY_ICON },
                OccupationTypeTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);

        OccupationType occupationType = null;
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            occupationType = new OccupationType(
                    cursor.getString(cursor.getColumnIndex(OccupationTypeTable.KEY_NAME)),
                    bytesToBitmap(cursor.getBlob(cursor.getColumnIndex(OccupationTypeTable.KEY_ICON)))
            );
            cursor.close();
        }
        db.close();

        if (occupationType == null) {
            return null;
        }
        return new OccupationTypeData(id, occupationType);
    }

    public OccupationTypeData updateOccupationType(long id, OccupationType type) {
        if (type == null || !type.isValid()) {
            return null;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OccupationTypeTable.KEY_NAME, type.getName());
        values.put(OccupationTypeTable.KEY_ICON, bitmapToBytes(type.getIcon()));

        int affectedRowNbr = db.update(OccupationTypeTable.TABLE_NAME, values,
                OccupationTypeTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();

        if (affectedRowNbr == 1) {
            return new OccupationTypeData(id, new OccupationType(type));
        } else {
            return null;
        }
    }

    public boolean deleteOccupationType(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRowNbr = db.delete(OccupationTypeTable.TABLE_NAME,
                OccupationTypeTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();

        if (affectedRowNbr == 1) {
            return true;
        } else {
            return false;
        }
    }

    ///// EVENT /////

    public long getEventNumber() {
        SQLiteDatabase db = this.getWritableDatabase();

        long numberOfRows = DatabaseUtils.queryNumEntries(db, EventTable.TABLE_NAME);

        db.close();

        return numberOfRows;
    }

    public EventData createEvent(Event event, long typeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventTable.KEY_TYPE, typeId);
        values.put(EventTable.KEY_DATE, event.getDate().getTime());

        long id = db.insert(EventTable.TABLE_NAME, null, values);
        db.close();

        if (id == -1) {
            return null;
        }

        return new EventData(
                id,
                new Event(event),
                requestOccupationType(typeId)
        );
    }

    public EventData requestEvent(long id) {
        String query = "SELECT * FROM " + EventTable.TABLE_NAME
                + " INNER JOINT " + OccupationTypeTable.TABLE_NAME
                + " ON " + EventTable.KEY_TYPE + " = " + OccupationTypeTable.KEY_ID
                + " WHERE " + EventTable.KEY_ID + "=?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id)});

        Event event = null;
        OccupationType occupationType = null;
        long typeId = -1;
        if (cursor != null) {
            cursor.moveToFirst();
            occupationType = new OccupationType(
                    cursor.getString(cursor.getColumnIndex(OccupationTypeTable.KEY_NAME)),
                    bytesToBitmap(cursor.getBlob(cursor.getColumnIndex(OccupationTypeTable.KEY_ICON)))
            );
            event = new Event(
                    new Date(cursor.getLong(cursor.getColumnIndex(EventTable.KEY_DATE))),
                    occupationType
            );
            typeId = cursor.getLong(cursor.getColumnIndex(EventTable.KEY_TYPE));
            cursor.close();
        }
        db.close();

        if (event == null) {
            return null;
        }
        return new EventData(
                id,
                event,
                new OccupationTypeData(typeId, occupationType)
        );
    }

    public EventData updateEvent(long id, Event event, long typeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventTable.KEY_DATE, event.getDate().getTime());
        values.put(EventTable.KEY_TYPE, typeId);

        int affectedRowNbr = db.update(EventTable.TABLE_NAME, values,
                EventTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();

        if (affectedRowNbr == 1) {
            return new EventData(id, new Event(event), requestOccupationType(typeId));
        } else {
            return null;
        }
    }

    public boolean deleteEvent(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRowNbr = db.delete(EventTable.TABLE_NAME,
                EventTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();
        if (affectedRowNbr == 1) {
            return true;
        } else {
            return false;
        }
    }

    ///// HOBBY /////

    public long getHobbyNumber() {
        SQLiteDatabase db = this.getWritableDatabase();

        long numberOfRows = DatabaseUtils.queryNumEntries(db, HobbyTable.TABLE_NAME);

        db.close();

        return numberOfRows;
    }

    public HobbyData createHobby(Hobby hobby, long typeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HobbyTable.KEY_TYPE, typeId);
        values.put(HobbyTable.KEY_START_DATE, hobby.getStartDate().getTime());
        values.put(HobbyTable.KEY_END_DATE, hobby.getEndDate().getTime());

        long id = db.insert(HobbyTable.TABLE_NAME, null, values);
        db.close();

        if (id == -1) {
            return null;
        }

        return new HobbyData(
                id,
                new Hobby(hobby),
                requestOccupationType(typeId)
        );
    }

    public HobbyData requestHobby(long id) {
        String query = "SELECT * FROM " + HobbyTable.TABLE_NAME
                + " INNER JOINT " + OccupationTypeTable.TABLE_NAME
                + " ON " + HobbyTable.KEY_TYPE + " = " + OccupationTypeTable.KEY_ID
                + " WHERE " + HobbyTable.KEY_ID + "=?";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id)});

        Hobby hobby = null;
        OccupationType occupationType = null;
        long typeId = -1;
        if (cursor != null) {
            cursor.moveToFirst();
            occupationType = new OccupationType(
                    cursor.getString(cursor.getColumnIndex(OccupationTypeTable.KEY_NAME)),
                    bytesToBitmap(cursor.getBlob(cursor.getColumnIndex(OccupationTypeTable.KEY_ICON)))
            );
            hobby = new Hobby(
                    new Date(cursor.getLong(cursor.getColumnIndex(HobbyTable.KEY_START_DATE))),
                    new Date(cursor.getLong(cursor.getColumnIndex(HobbyTable.KEY_END_DATE))),
                    occupationType
            );
            typeId = cursor.getLong(cursor.getColumnIndex(HobbyTable.KEY_TYPE));
            cursor.close();
        }
        db.close();

        if (hobby == null) {
            return null;
        }
        return new HobbyData(
                id,
                hobby,
                new OccupationTypeData(typeId, occupationType)
        );
    }

    public HobbyData updateHobby(long id, Hobby hobby, long typeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HobbyTable.KEY_TYPE, typeId);
        values.put(HobbyTable.KEY_END_DATE, hobby.getEndDate().getTime());
        values.put(HobbyTable.KEY_START_DATE, hobby.getStartDate().getTime());

        int affectedRowNbr = db.update(EventTable.TABLE_NAME, values,
                EventTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();

        if (affectedRowNbr == 1) {
            return new HobbyData(id, new Hobby(hobby), requestOccupationType(typeId));
        } else {
            return null;
        }
    }

    public boolean deleteHobby(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRowNbr = db.delete(HobbyTable.TABLE_NAME,
                HobbyTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();
        if (affectedRowNbr == 1) {
            return true;
        } else {
            return false;
        }
    }

    ///// JOINT UTILS /////

    public void getEventsOfType(long occupationType) {

    }

    ///// BITMAP UTILS /////

    // convert from bitmap to byte array
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        assert(bitmap != null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap bytesToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}