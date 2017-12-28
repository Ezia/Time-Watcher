package esia.timewatcher.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.LinkedList;

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

    public long getTypeNumber() {
        SQLiteDatabase db = this.getWritableDatabase();
        long numberOfRows = DatabaseUtils.queryNumEntries(db, OccupationTypeTable.TABLE_NAME);
        db.close();

        return numberOfRows;
    }

    public boolean typeExists(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long numberOfRows = DatabaseUtils.queryNumEntries(db,
                OccupationTypeTable.TABLE_NAME,
                OccupationTypeTable.KEY_ID + "=?",
                new String[] {String.valueOf(id)});
        db.close();

        return numberOfRows == 1;
    }

    public boolean typeExists(String name) throws IllegalArgumentException {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        long numberOfRows = DatabaseUtils.queryNumEntries(db,
                OccupationTypeTable.TABLE_NAME,
                OccupationTypeTable.KEY_NAME + "=?",
                new String[] {String.valueOf(name)});
        db.close();

        return numberOfRows == 1;
    }

    public OccupationTypeData createType(OccupationType type)
            throws IllegalArgumentException, SQLException {
        if (type == null || !type.isValid()) {
            throw new IllegalArgumentException();
        } else if (typeExists(type.getName())) {
            throw new EntryAlreadyExistsException();
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OccupationTypeTable.KEY_NAME, type.getName());
        values.put(OccupationTypeTable.KEY_ICON, bitmapToBytes(type.getIcon()));

        long id = db.insert(OccupationTypeTable.TABLE_NAME, null, values);

        db.close();

        if (id == -1) {
            throw new SQLException();
        }

        return new OccupationTypeData(id, new OccupationType(type));
    }

    public OccupationTypeData requestType(long id)
            throws IllegalArgumentException, SQLException {
        if (!typeExists(id)) {
            throw new IllegalArgumentException();
        }

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

        if (cursor == null) {
            db.close();
            throw new SQLException();
        } else if (cursor.getCount() <= 0) {
            cursor.close();
            db.close();
            throw new SQLException();
        }

        cursor.moveToFirst();
        OccupationType occupationType = new OccupationType(
                cursor.getString(cursor.getColumnIndex(OccupationTypeTable.KEY_NAME)),
                bytesToBitmap(cursor.getBlob(cursor.getColumnIndex(OccupationTypeTable.KEY_ICON)))
        );
        cursor.close();

        db.close();

        return new OccupationTypeData(id, occupationType);
    }

    public OccupationTypeData requestType(String name)
            throws IllegalArgumentException, SQLException {
        if (name == null || name.isEmpty() || !typeExists(name)) {
            throw new IllegalArgumentException();
        }

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(OccupationTypeTable.TABLE_NAME,
                new String[] { OccupationTypeTable.KEY_ID,
                        OccupationTypeTable.KEY_NAME, OccupationTypeTable.KEY_ICON },
                OccupationTypeTable.KEY_NAME + "=?",
                new String[] { name },
                null,
                null,
                null,
                null);

        if (cursor == null) {
            db.close();
            throw new SQLException();
        } else if (cursor.getCount() <= 0) {
            cursor.close();
            db.close();
            throw new SQLException();
        }

        cursor.moveToFirst();
        long id = cursor.getLong(cursor.getColumnIndex(OccupationTypeTable.KEY_ID));
        OccupationType occupationType = new OccupationType(
                cursor.getString(cursor.getColumnIndex(OccupationTypeTable.KEY_NAME)),
                bytesToBitmap(cursor.getBlob(cursor.getColumnIndex(OccupationTypeTable.KEY_ICON)))
        );
        cursor.close();

        db.close();

        return new OccupationTypeData(id, occupationType);
    }

	public long requestTypeId(String name)
			throws IllegalArgumentException, SQLException {
		if (name == null || name.isEmpty() || !typeExists(name)) {
			throw new IllegalArgumentException();
		}

		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(OccupationTypeTable.TABLE_NAME,
				new String[] { OccupationTypeTable.KEY_ID },
				OccupationTypeTable.KEY_NAME + "=?",
				new String[] { name },
				null,
				null,
				null,
				null);

		if (cursor == null) {
			db.close();
			throw new SQLException();
		} else if (cursor.getCount() <= 0) {
			cursor.close();
			db.close();
			throw new SQLException();
		}

		cursor.moveToFirst();
		long id = cursor.getLong(cursor.getColumnIndex(OccupationTypeTable.KEY_ID));
		cursor.close();

		db.close();

		return id;
	}

    public OccupationTypeData updateType(long id, OccupationType type)
            throws IllegalArgumentException, SQLException {
        if (type == null || !type.isValid() || !typeExists(id)
                || typeExists(type.getName()) && requestType(type.getName()).getId() != id) {
            throw new IllegalArgumentException();
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OccupationTypeTable.KEY_NAME, type.getName());
        values.put(OccupationTypeTable.KEY_ICON, bitmapToBytes(type.getIcon()));

        int affectedRowsNbr = db.update(OccupationTypeTable.TABLE_NAME, values,
                OccupationTypeTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();

        if (affectedRowsNbr >= 1) {
            return new OccupationTypeData(id, new OccupationType(type));
        } else {
            throw  new SQLException();
        }
    }

    public void deleteType(long id)
            throws IllegalArgumentException, SQLException {
        if (!typeExists(id)) {
            throw new IllegalArgumentException();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRowNbr = db.delete(OccupationTypeTable.TABLE_NAME,
                OccupationTypeTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();

        if (affectedRowNbr <= 0) {
            throw new SQLException();
        }
    }

    ///// EVENT /////

    public long getEventNumber() {
        SQLiteDatabase db = this.getWritableDatabase();

        long numberOfRows = DatabaseUtils.queryNumEntries(db, EventTable.TABLE_NAME);

        db.close();

        return numberOfRows;
    }

	public boolean eventExists(long id) {
		SQLiteDatabase db = this.getWritableDatabase();
		long numberOfRows = DatabaseUtils.queryNumEntries(db,
				EventTable.TABLE_NAME,
				EventTable.KEY_ID + "=?",
				new String[] {String.valueOf(id)});
		db.close();

		return numberOfRows == 1;
	}

    public EventData createEvent(Event event, long typeId)
			throws IllegalArgumentException, SQLException {
        if (event == null || !event.isValid() || !typeExists(typeId)) {
            throw new IllegalArgumentException();
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventTable.KEY_TYPE, typeId);
        values.put(EventTable.KEY_DATE, event.getDate().getMillis());

        long id = db.insert(EventTable.TABLE_NAME, null, values);
        db.close();

        if (id == -1) {
            throw new SQLException();
        }

        return new EventData(
                id,
                new Event(event),
                requestType(typeId)
        );
    }

    public EventData requestEvent(long id)
			throws IllegalArgumentException, SQLException {
    	if (!eventExists(id)) {
    		throw new IllegalArgumentException();
		}

        String query = "SELECT * FROM " + EventTable.TABLE_NAME
                + " INNER JOIN " + OccupationTypeTable.TABLE_NAME
                + " ON " + EventTable.KEY_TYPE + " = " + OccupationTypeTable.KEY_ID
                + " WHERE " + EventTable.KEY_ID + "=?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id)});

		if (cursor == null) {
			db.close();
			throw new SQLException();
		} else if (cursor.getCount() <= 0) {
			cursor.close();
			db.close();
			throw new SQLException();
		}

		cursor.moveToFirst();
		OccupationType occupationType = new OccupationType(
				cursor.getString(cursor.getColumnIndex(OccupationTypeTable.KEY_NAME)),
				bytesToBitmap(cursor.getBlob(cursor.getColumnIndex(OccupationTypeTable.KEY_ICON)))
		);
		Event event = new Event(
				new DateTime(cursor.getLong(cursor.getColumnIndex(EventTable.KEY_DATE)))
		);
		long typeId = cursor.getLong(cursor.getColumnIndex(EventTable.KEY_TYPE));
		cursor.close();
        db.close();

        return new EventData(
                id,
                event,
                new OccupationTypeData(typeId, occupationType)
        );
    }

    public EventData updateEvent(long id, Event event, long typeId)
			throws IllegalArgumentException, SQLException {
    	if (event == null || !event.isValid() || !eventExists(id) || !typeExists(typeId)) {
    		throw new IllegalArgumentException();
		}

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventTable.KEY_DATE, event.getDate().getMillis());
        values.put(EventTable.KEY_TYPE, typeId);

        int affectedRowNbr = db.update(EventTable.TABLE_NAME, values,
                EventTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();

        if (affectedRowNbr >= 1) {
            return new EventData(id, new Event(event), requestType(typeId));
        } else {
            throw new SQLException();
        }
    }

    public void deleteEvent(long id)
			throws IllegalArgumentException, SQLException {
    	if (!eventExists(id)) {
    		throw new IllegalArgumentException();
		}

        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRowNbr = db.delete(EventTable.TABLE_NAME,
                EventTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();

        if (affectedRowNbr <= 0) {
            throw new SQLException();
        }
    }

    ///// HOBBY /////

    public long getHobbyNumber() {
        SQLiteDatabase db = this.getWritableDatabase();

        long numberOfRows = DatabaseUtils.queryNumEntries(db, HobbyTable.TABLE_NAME);

        db.close();

        return numberOfRows;
    }

    public boolean hobbyExists(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long numberOfRows = DatabaseUtils.queryNumEntries(db,
                HobbyTable.TABLE_NAME,
                HobbyTable.KEY_ID + "=?",
                new String[] {String.valueOf(id)});
        db.close();

        return numberOfRows == 1;
    }

    public HobbyData createHobby(Hobby hobby, long typeId)
            throws IllegalArgumentException, SQLException {
        if (hobby == null || !hobby.isValid() || !typeExists(typeId)) {
            throw new IllegalArgumentException();
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HobbyTable.KEY_TYPE, typeId);
        values.put(HobbyTable.KEY_START_DATE, hobby.getStartDate().getMillis());
        values.put(HobbyTable.KEY_END_DATE, hobby.getEndDate().getMillis());

        long id = db.insert(HobbyTable.TABLE_NAME, null, values);
        db.close();

        if (id == -1) {
            throw new SQLException();
        }

        return new HobbyData(
                id,
                new Hobby(hobby),
                requestType(typeId)
        );
    }

    public HobbyData requestHobby(long id)
            throws IllegalArgumentException, SQLException {
        if (!hobbyExists(id)) {
            throw new IllegalArgumentException();
        }

        String query = "SELECT * FROM " + HobbyTable.TABLE_NAME
                + " INNER JOIN " + OccupationTypeTable.TABLE_NAME
                + " ON " + HobbyTable.KEY_TYPE + " = " + OccupationTypeTable.KEY_ID
                + " WHERE " + HobbyTable.KEY_ID + "=?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id)});

        if (cursor == null) {
            db.close();
            throw new SQLException();
        } else if (cursor.getCount() <= 0) {
            cursor.close();
            db.close();
            throw new SQLException();
        }

        cursor.moveToFirst();
        OccupationType occupationType = new OccupationType(
                cursor.getString(cursor.getColumnIndex(OccupationTypeTable.KEY_NAME)),
                bytesToBitmap(cursor.getBlob(cursor.getColumnIndex(OccupationTypeTable.KEY_ICON)))
        );
        Hobby hobby = new Hobby(
                new DateTime(cursor.getLong(cursor.getColumnIndex(HobbyTable.KEY_START_DATE))),
                new DateTime(cursor.getLong(cursor.getColumnIndex(HobbyTable.KEY_END_DATE)))
        );
        long typeId = cursor.getLong(cursor.getColumnIndex(HobbyTable.KEY_TYPE));
        cursor.close();
        db.close();

        return new HobbyData(
                id,
                hobby,
                new OccupationTypeData(typeId, occupationType)
        );
    }

    public HobbyData updateHobby(long id, Hobby hobby, long typeId)
            throws IllegalArgumentException, SQLException {
        if (hobby == null || !hobby.isValid() || !hobbyExists(id) || !typeExists(typeId)) {
            throw new IllegalArgumentException();
        }

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HobbyTable.KEY_START_DATE, hobby.getStartDate().getMillis());
        values.put(HobbyTable.KEY_END_DATE, hobby.getEndDate().getMillis());
        values.put(HobbyTable.KEY_TYPE, typeId);

        int affectedRowNbr = db.update(HobbyTable.TABLE_NAME, values,
                HobbyTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();

        if (affectedRowNbr >= 1) {
            return new HobbyData(id, new Hobby(hobby), requestType(typeId));
        } else {
            throw new SQLException();
        }
    }

    public void deleteHobby(long id)
            throws IllegalArgumentException, SQLException {
        if (!hobbyExists(id)) {
            throw new IllegalArgumentException();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRowNbr = db.delete(HobbyTable.TABLE_NAME,
                HobbyTable.KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();

        if (affectedRowNbr <= 0) {
            throw new SQLException();
        }
    }

    ///// SPECIFIC REQUESTS /////

    public LinkedList<OccupationTypeData> requestAllTypes()
            throws SQLException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(OccupationTypeTable.TABLE_NAME,
                new String[] { OccupationTypeTable.KEY_ID,
                        OccupationTypeTable.KEY_NAME, OccupationTypeTable.KEY_ICON },
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor == null) {
            db.close();
            throw new SQLException();
        }

        LinkedList<OccupationTypeData> dataList = new LinkedList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(OccupationTypeTable.KEY_ID));
            OccupationType type = new OccupationType(
                    cursor.getString(cursor.getColumnIndex(OccupationTypeTable.KEY_NAME)),
                    bytesToBitmap(cursor.getBlob(cursor.getColumnIndex(OccupationTypeTable.KEY_ICON)))
            );
            dataList.add(new OccupationTypeData(id, type));
        }

        cursor.close();
        db.close();

        return dataList;
    }

    public LinkedList<OccupationTypeData> requestTypes(int maxNumber)
                    throws IllegalArgumentException, SQLException {
        if (maxNumber <= 0) {
            throw new IllegalArgumentException();
        }

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(OccupationTypeTable.TABLE_NAME,
                new String[] { OccupationTypeTable.KEY_ID,
                        OccupationTypeTable.KEY_NAME, OccupationTypeTable.KEY_ICON },
                null,
                null,
                null,
                null,
                null,
                String.valueOf(maxNumber));

        if (cursor == null) {
            db.close();
            throw new SQLException();
        }

        LinkedList<OccupationTypeData> dataList = new LinkedList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndex(OccupationTypeTable.KEY_ID));
            OccupationType type = new OccupationType(
                    cursor.getString(cursor.getColumnIndex(OccupationTypeTable.KEY_NAME)),
                    bytesToBitmap(cursor.getBlob(cursor.getColumnIndex(OccupationTypeTable.KEY_ICON)))
            );
            dataList.add(new OccupationTypeData(id, type));
        }

        cursor.close();
        db.close();

        return dataList;
    }

    public LinkedList<HobbyData> requestRunningHobbies()
            throws SQLException {
        String query = "SELECT * FROM " + HobbyTable.TABLE_NAME
                + " INNER JOIN " + OccupationTypeTable.TABLE_NAME
                + " ON " + HobbyTable.KEY_TYPE + " = " + OccupationTypeTable.KEY_ID
                + " WHERE " + HobbyTable.KEY_END_DATE + "=?";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(0)});

        if (cursor == null) {
            db.close();
            throw new SQLException();
        }

        LinkedList<HobbyData> dataList = new LinkedList<>();
        while (cursor.moveToNext()) {
            OccupationTypeData typeData = new OccupationTypeData(
                    cursor.getLong(cursor.getColumnIndex(HobbyTable.KEY_TYPE)),
                    new OccupationType(
                            cursor.getString(cursor.getColumnIndex(OccupationTypeTable.KEY_NAME)),
                            bytesToBitmap(cursor.getBlob(cursor.getColumnIndex(OccupationTypeTable.KEY_ICON)))
                    )
            );

            HobbyData hobbyData = new HobbyData(
                    cursor.getLong(cursor.getColumnIndex(HobbyTable.KEY_START_DATE)),
                    new Hobby(
                            new DateTime(cursor.getLong(cursor.getColumnIndex(HobbyTable.KEY_START_DATE))),
                            new DateTime(cursor.getLong(cursor.getColumnIndex(HobbyTable.KEY_END_DATE)))
                    ),
                    typeData
            );

            dataList.add(hobbyData);
        }

        cursor.close();
        db.close();

        return dataList;
    }

    ///// BITMAP UTILS /////

    // convert from bitmap to byte array
    private static byte[] bitmapToBytes(Bitmap bitmap) {
        assert(bitmap != null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    private static Bitmap bytesToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}