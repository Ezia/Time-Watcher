package esia.timewatcher.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

import esia.timewatcher.database.exceptions.EntryAlreadyExistsException;
import esia.timewatcher.database.exceptions.UnexpectedSqlResultException;
import esia.timewatcher.structures.Event;
import esia.timewatcher.structures.Hobby;
import esia.timewatcher.structures.Type;
import esia.timewatcher.utils.BitmapUtils;

public class DatabaseManager extends SQLiteOpenHelper {

    private LinkedList<DatabaseListener> listeners = new LinkedList<>();

    ///// STATIC FIELDS /////

    private static DatabaseManager instance = null;

    private static final int DATABASE_VERSION = 2;

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

    ///// ENTRY CREATIONS /////

	public TypeData createType(Type type) {
		ContentValues values = checkAndBuildTypeContent(type);

		try(SQLiteDatabase db = this.getWritableDatabase()) {
			long id = db.insert(OccupationTypeTable.TABLE_NAME, null, values);
			if (id == -1) {
				throw new UnexpectedSqlResultException();
			} else {
				notifyDatabaseChange();
				return new TypeData(id, new Type(type));
			}
		}
	}

	public EventData createEvent(Event event, long typeId) {
		ContentValues values = checkAndBuildEventContent(event, typeId);

		try (SQLiteDatabase db = this.getWritableDatabase()) {
			long id = db.insert(EventTable.TABLE_NAME, null, values);
			if (id == -1) {
				throw new UnexpectedSqlResultException();
			} else {
				notifyDatabaseChange();
				return new EventData(id, new Event(event), requestType(typeId));
			}
		}
	}

	public HobbyData createHobby(Hobby hobby, long typeId) {
		ContentValues values = checkAndBuildHobbyContent(hobby, typeId);

		try (SQLiteDatabase db = this.getWritableDatabase()) {
			long id = db.insert(HobbyTable.TABLE_NAME, null, values);
			if (id == -1) {
				throw new UnexpectedSqlResultException();
			} else {
				notifyDatabaseChange();
				return new HobbyData(id, new Hobby(hobby), requestType(typeId));
			}
		}
	}

    ///// ENTRY ACCESSES /////

    public long getTypeNumber() {
    	try (SQLiteDatabase db = this.getWritableDatabase()) {
			long numberOfRows = DatabaseUtils.queryNumEntries(db, OccupationTypeTable.TABLE_NAME);
			return numberOfRows;
		}
    }

	public long getHobbyNumber() {
		try (SQLiteDatabase db = this.getWritableDatabase()) {
			long numberOfRows = DatabaseUtils.queryNumEntries(db, HobbyTable.TABLE_NAME);
			return numberOfRows;
		}
	}

	public long getEventNumber() {
		try (SQLiteDatabase db = this.getWritableDatabase()) {
			long numberOfRows = DatabaseUtils.queryNumEntries(db, EventTable.TABLE_NAME);
			return numberOfRows;
		}
	}

    public boolean typeExists(long id) {
    	try (SQLiteDatabase db = this.getWritableDatabase()) {
			long numberOfRows = DatabaseUtils.queryNumEntries(db,
					OccupationTypeTable.TABLE_NAME,
					OccupationTypeTable.KEY_ID + "=?",
					new String[] {String.valueOf(id)});
			return numberOfRows == 1;
		}
    }

    public boolean typeExists(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException();
        }

        try (SQLiteDatabase db = this.getWritableDatabase()) {
			long numberOfRows = DatabaseUtils.queryNumEntries(db,
					OccupationTypeTable.TABLE_NAME,
					OccupationTypeTable.KEY_NAME + "=?",
					new String[] {String.valueOf(name)});
			return numberOfRows == 1;
		}
    }

	public boolean hobbyExists(long id) {
		try (SQLiteDatabase db = this.getWritableDatabase()) {
			long numberOfRows = DatabaseUtils.queryNumEntries(db,
					HobbyTable.TABLE_NAME,
					HobbyTable.KEY_ID + "=?",
					new String[] {String.valueOf(id)});
			return numberOfRows == 1;
		}
	}

	public boolean eventExists(long id) {
		try (SQLiteDatabase db = this.getWritableDatabase()) {
			long numberOfRows = DatabaseUtils.queryNumEntries(db,
					EventTable.TABLE_NAME,
					EventTable.KEY_ID + "=?",
					new String[] {String.valueOf(id)});
			return numberOfRows == 1;
		}
	}

    public TypeData requestType(long id) {
        if (!typeExists(id)) {
            throw new IllegalArgumentException();
        }

		try (SQLiteDatabase db = this.getReadableDatabase();
				Cursor cursor = db.query(OccupationTypeTable.TABLE_NAME,
					new String[] { OccupationTypeTable.KEY_ID,
						OccupationTypeTable.KEY_NAME, OccupationTypeTable.KEY_ICON },
					OccupationTypeTable.KEY_ID + "=?",
					new String[] { String.valueOf(id) },
					null,
					null,
					null,
					null)) {
			cursor.moveToNext();
			return parseType(cursor);
		}
    }

    public TypeData requestType(String name) {
        if (name == null || name.isEmpty() || !typeExists(name)) {
            throw new IllegalArgumentException();
        }

        try (SQLiteDatabase db = this.getReadableDatabase();
			 Cursor cursor = db.query(OccupationTypeTable.TABLE_NAME,
					 new String[] { OccupationTypeTable.KEY_ID,
							 OccupationTypeTable.KEY_NAME, OccupationTypeTable.KEY_ICON },
					 OccupationTypeTable.KEY_NAME + "=?",
					 new String[] { name },
					 null,
					 null,
					 null,
					 null)) {
        	cursor.moveToNext();
        	return parseType(cursor);
		}
    }

	public long requestTypeId(String name) {
		if (name == null || name.isEmpty() || !typeExists(name)) {
			throw new IllegalArgumentException();
		}

		try (SQLiteDatabase db = this.getReadableDatabase();
			 Cursor cursor = db.query(OccupationTypeTable.TABLE_NAME,
					 new String[] { OccupationTypeTable.KEY_ID },
					 OccupationTypeTable.KEY_NAME + "=?",
					 new String[] { name },
					 null,
					 null,
					 null,
					 null)) {
			cursor.moveToFirst();
			long id = cursor.getLong(cursor.getColumnIndex(OccupationTypeTable.KEY_ID));
			return id;
		}
	}

	public EventData requestEvent(long id) {
		if (!eventExists(id)) {
			throw new IllegalArgumentException();
		}

		String query = "SELECT * FROM " + EventTable.TABLE_NAME
				+ " INNER JOIN " + OccupationTypeTable.TABLE_NAME
				+ " ON " + EventTable.KEY_TYPE + " = " + OccupationTypeTable.KEY_ID
				+ " WHERE " + EventTable.KEY_ID + "=?";

		try (SQLiteDatabase db = this.getReadableDatabase();
			 Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id)})) {
			cursor.moveToNext();
			return parseEventAndType(cursor);
		}
	}

	public HobbyData requestHobby(long id) {
		if (!hobbyExists(id)) {
			throw new IllegalArgumentException();
		}

		String query = "SELECT * FROM " + HobbyTable.TABLE_NAME
				+ " INNER JOIN " + OccupationTypeTable.TABLE_NAME
				+ " ON " + HobbyTable.KEY_TYPE + " = " + OccupationTypeTable.KEY_ID
				+ " WHERE " + HobbyTable.KEY_ID + "=?";

		try (SQLiteDatabase db = this.getReadableDatabase();
			 Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(id)})) {
			cursor.moveToNext();
			return parseHobbyAndType(cursor);
		}
	}

	///// ENTRY UPDATES /////

    public TypeData updateType(long id, Type type) {
		ContentValues values = checkAndBuildTypeContent(id, type);

		try (SQLiteDatabase db = this.getWritableDatabase()) {
			int affectedRowsNbr = db.update(OccupationTypeTable.TABLE_NAME, values,
					OccupationTypeTable.KEY_ID + "=?",
					new String[] { String.valueOf(id) });
			if (affectedRowsNbr == 1) {
				notifyDatabaseChange();
				return new TypeData(id, new Type(type));
			} else {
				throw new UnexpectedSqlResultException();
			}
		}
    }

	public EventData updateEvent(long id, Event event, long typeId) {
		ContentValues values = checkAndBuildEventContent(id, event, typeId);

		try (SQLiteDatabase db = this.getWritableDatabase()) {
			int affectedRowNbr = db.update(EventTable.TABLE_NAME, values,
					EventTable.KEY_ID + "=?",
					new String[] { String.valueOf(id) });
			if (affectedRowNbr == 1) {
				notifyDatabaseChange();
				return new EventData(id, new Event(event), requestType(typeId));
			} else {
				throw new UnexpectedSqlResultException();
			}
		}
	}

	public HobbyData updateHobby(long id, Hobby hobby, long typeId) {
		ContentValues values = checkAndBuildHobbyContent(id, hobby, typeId);

		try (SQLiteDatabase db = this.getWritableDatabase()) {
			int affectedRowNbr = db.update(HobbyTable.TABLE_NAME, values,
					HobbyTable.KEY_ID + "=?",
					new String[] { String.valueOf(id) });
			if (affectedRowNbr == 1) {
				notifyDatabaseChange();
				return new HobbyData(id, new Hobby(hobby), requestType(typeId));
			} else {
				throw new UnexpectedSqlResultException();
			}
		}
	}

    ///// ENTRY DELETION /////

    public void deleteType(long id) {
        if (!typeExists(id) || isTypeUsed(id)) {
            throw new IllegalArgumentException();
        }

        try (SQLiteDatabase db = this.getWritableDatabase()) {
			int affectedRowNbr = db.delete(OccupationTypeTable.TABLE_NAME,
					OccupationTypeTable.KEY_ID + "=?",
					new String[] { String.valueOf(id) });
			if (affectedRowNbr != 1) {
				throw new UnexpectedSqlResultException();
			} else {
				notifyDatabaseChange();
			}
		}
    }

    public void deleteEvent(long id) {
    	if (!eventExists(id)) {
    		throw new IllegalArgumentException();
		}

        try (SQLiteDatabase db = this.getWritableDatabase()) {
			int affectedRowNbr = db.delete(EventTable.TABLE_NAME,
					EventTable.KEY_ID + "=?",
					new String[] { String.valueOf(id) });
			if (affectedRowNbr != 1) {
				throw new UnexpectedSqlResultException();
			} else {
				notifyDatabaseChange();
			}
		}
	}

	public void deleteHobby(long id) {
        if (!hobbyExists(id)) {
            throw new IllegalArgumentException();
        }

        try (SQLiteDatabase db = this.getWritableDatabase()) {
			int affectedRowNbr = db.delete(HobbyTable.TABLE_NAME,
					HobbyTable.KEY_ID + "=?",
					new String[] { String.valueOf(id) });
			if (affectedRowNbr != 1) {
				throw new UnexpectedSqlResultException();
			} else {
				notifyDatabaseChange();
			}
		}
    }

    ///// SPECIFIC REQUESTS /////

    public ArrayList<TypeData> requestAllTypes() {
    	String query = "SELECT * FROM " + OccupationTypeTable.TABLE_NAME;

		try(SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(query, new String[] {})) {
			ArrayList<TypeData> typeDataList = new ArrayList<>();
			while (cursor.moveToNext()) {
				typeDataList.add(parseType(cursor));
			}
			return typeDataList;
		}
    }

    public ArrayList<TypeData> requestTypes(int maxNumber) {
        if (maxNumber <= 0) {
            throw new IllegalArgumentException();
        }

        String query = "SELECT * FROM " + OccupationTypeTable.TABLE_NAME
				+ " LIMIT " + maxNumber;

		try(SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(query, new String[] {})) {
			ArrayList<TypeData> typeDataList = new ArrayList<>();
			while (cursor.moveToNext()) {
				typeDataList.add(parseType(cursor));
			}
			return typeDataList;
		}
    }

    public ArrayList<HobbyData> requestRunningHobbies(boolean orderByDescendantStartDate) {
        String query = "SELECT * FROM " + HobbyTable.TABLE_NAME
                + " INNER JOIN " + OccupationTypeTable.TABLE_NAME
                + " ON " + HobbyTable.KEY_TYPE + " = " + OccupationTypeTable.KEY_ID
                + " WHERE " + HobbyTable.KEY_END_DATE + "=?";

        if (orderByDescendantStartDate) {
            query += "ORDER BY " + HobbyTable.KEY_START_DATE + " DESC";
        } else {
            query += "ORDER BY " + HobbyTable.KEY_START_DATE + " ASC";
        }

		try(SQLiteDatabase db= this.getReadableDatabase();
			Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(0)})) {
			ArrayList<HobbyData> hobbyDataList = new ArrayList<>();
			while (cursor.moveToNext()) {
				hobbyDataList.add(parseHobbyAndType(cursor));
			}
			return hobbyDataList;
		}
    }

    public ArrayList<HobbyData> requestStoppedHobbies(boolean orderByDescendantStartDate) {
        String query = "SELECT * FROM " + HobbyTable.TABLE_NAME
                + " INNER JOIN " + OccupationTypeTable.TABLE_NAME
                + " ON " + HobbyTable.KEY_TYPE + " = " + OccupationTypeTable.KEY_ID
                + " WHERE " + HobbyTable.KEY_END_DATE + "<>?";

        if (orderByDescendantStartDate) {
			query += "ORDER BY " + HobbyTable.KEY_START_DATE + " DESC";
		} else {
			query += "ORDER BY " + HobbyTable.KEY_START_DATE + " ASC";
		}

        try(SQLiteDatabase db= this.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, new String[] {String.valueOf(0)})) {
        	ArrayList<HobbyData> hobbyDataList = new ArrayList<>();
        	while (cursor.moveToNext()) {
        		hobbyDataList.add(parseHobbyAndType(cursor));
			}
			return hobbyDataList;
        }
    }

	public ArrayList<EventData> requestEvents(boolean orderByChronologicalDate) {
		String query = "SELECT * FROM " + EventTable.TABLE_NAME
				+ " INNER JOIN " + OccupationTypeTable.TABLE_NAME
				+ " ON " + EventTable.KEY_TYPE + " = " + OccupationTypeTable.KEY_ID;

		if (orderByChronologicalDate) {
			query += " ORDER BY " + EventTable.KEY_DATE + " ASC";
		} else {
			query += " ORDER BY " + EventTable.KEY_DATE + " DESC";
		}

		try(SQLiteDatabase db= this.getReadableDatabase();
			Cursor cursor = db.rawQuery(query, new String[] {})) {
			ArrayList<EventData> eventDataList = new ArrayList<>();
			while (cursor.moveToNext()) {
				eventDataList.add(parseEventAndType(cursor));
			}
			return eventDataList;
		}
	}

	public int deleteHobbiesOlderThan(DateTime oldestEndDate) {
    	if (oldestEndDate == null || oldestEndDate.getMillis() == 0) {
    		throw new IllegalArgumentException();
		}

		try (SQLiteDatabase db = this.getWritableDatabase()) {
			int affectedRowNbr = db.delete(HobbyTable.TABLE_NAME,
					HobbyTable.KEY_END_DATE + "<>?"
					+ " AND " + HobbyTable.KEY_END_DATE + "<?",
					new String[] { String.valueOf(0), String.valueOf(oldestEndDate.getMillis()) });
			if (affectedRowNbr != 0) {
				notifyDatabaseChange();
			}
			return affectedRowNbr;
		}
	}

	public int deleteEventsOlderThan(DateTime oldestDate) {
		if (oldestDate == null) {
			throw new IllegalArgumentException();
		}

		try (SQLiteDatabase db = this.getWritableDatabase()) {
			int affectedRowNbr = db.delete(EventTable.TABLE_NAME,
					EventTable.KEY_DATE + "<?",
					new String[] { String.valueOf(oldestDate.getMillis()) });
			if (affectedRowNbr != 0) {
				notifyDatabaseChange();
			}
			return affectedRowNbr;
		}
	}

	public boolean isTypeUsed(long typeId) {
    	if (!typeExists(typeId)) {
    		throw new IllegalArgumentException();
		}

		try (SQLiteDatabase db = this.getWritableDatabase()) {
			long numberOfEvents = DatabaseUtils.queryNumEntries(db,
					EventTable.TABLE_NAME,
					EventTable.KEY_TYPE + "=?",
					new String[] {String.valueOf(typeId)});

			if (numberOfEvents == 0) {
				long numberOfHobbies = DatabaseUtils.queryNumEntries(db,
						HobbyTable.TABLE_NAME,
						HobbyTable.KEY_TYPE + "=?",
						new String[] {String.valueOf(typeId)});

				if (numberOfHobbies == 0) {
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}
		}
	}

	///// OPERATION UTILS /////

	private ContentValues checkAndBuildTypeContent(Type type) {
    	if (type == null || !type.isValid()) {
    		throw new  IllegalArgumentException();
		} else if(typeExists(type.getName())) {
			throw new EntryAlreadyExistsException();
		}

		ContentValues values = new ContentValues();
		values.put(OccupationTypeTable.KEY_NAME, type.getName());
		values.put(OccupationTypeTable.KEY_ICON, BitmapUtils.bitmapToBytes(type.getIcon()));

		return values;
	}

	private ContentValues checkAndBuildTypeContent(long id, Type type) {
		if (type == null || !type.isValid() || !typeExists(id)) {
			throw new  IllegalArgumentException();
		} else if(typeExists(type.getName()) && requestType(type.getName()).getId() != id) {
			throw new EntryAlreadyExistsException();
		}

		ContentValues values = new ContentValues();
		values.put(OccupationTypeTable.KEY_NAME, type.getName());
		values.put(OccupationTypeTable.KEY_ICON, BitmapUtils.bitmapToBytes(type.getIcon()));

		return values;
	}

	private ContentValues checkAndBuildHobbyContent(Hobby hobby, long typeId) {
		if (hobby == null || !hobby.isValid() || !typeExists(typeId)) {
			throw new IllegalArgumentException();
		}

		ContentValues values = new ContentValues();
		values.put(HobbyTable.KEY_TYPE, typeId);
		values.put(HobbyTable.KEY_START_DATE, hobby.getStartDate().getMillis());
		values.put(HobbyTable.KEY_END_DATE, hobby.getEndDate().getMillis());

		return values;
	}
	
	private ContentValues checkAndBuildHobbyContent(long hobbyId, Hobby hobby, long typeId) {
		if (hobby == null || !hobby.isValid() || !hobbyExists(hobbyId) || !typeExists(typeId)) {
			throw new IllegalArgumentException();
		}

		ContentValues values = new ContentValues();
		values.put(HobbyTable.KEY_TYPE, typeId);
		values.put(HobbyTable.KEY_START_DATE, hobby.getStartDate().getMillis());
		values.put(HobbyTable.KEY_END_DATE, hobby.getEndDate().getMillis());

		return values;
	}

	private ContentValues checkAndBuildEventContent(Event event, long typeId) {
    	if (event == null || !event.isValid() || !typeExists(typeId)) {
			throw new IllegalArgumentException();
		}

		ContentValues values = new ContentValues();
		values.put(EventTable.KEY_TYPE, typeId);
		values.put(EventTable.KEY_DATE, event.getDate().getMillis());

		return values;
	}

	private ContentValues checkAndBuildEventContent(long eventId, Event event, long typeId) {
		if (event == null || !event.isValid() || !eventExists(eventId) || !typeExists(typeId)) {
			throw new IllegalArgumentException();
		}

		ContentValues values = new ContentValues();
		values.put(EventTable.KEY_TYPE, typeId);
		values.put(EventTable.KEY_DATE, event.getDate().getMillis());

		return values;
	}

	private TypeData parseType(Cursor cursor) {
		if (cursor == null || cursor.isClosed() || cursor.getPosition() == -1) {
			throw new IllegalArgumentException();
		}

		return new TypeData(
				cursor.getLong(cursor.getColumnIndexOrThrow(OccupationTypeTable.KEY_ID)),
				new Type(
						cursor.getString(
								cursor.getColumnIndexOrThrow(OccupationTypeTable.KEY_NAME)),
						BitmapUtils.bytesToBitmap(cursor.getBlob(
								cursor.getColumnIndexOrThrow(OccupationTypeTable.KEY_ICON)))
				)
		);
	}

	private EventData parseEventAndType(Cursor cursor) {
    	if (cursor == null || cursor.isClosed() || cursor.getPosition() == -1) {
			throw new IllegalArgumentException();
		}

		return new EventData(
				cursor.getLong(cursor.getColumnIndexOrThrow(EventTable.KEY_ID)),
				new Event(
						new DateTime(
								cursor.getLong(cursor.getColumnIndexOrThrow(EventTable.KEY_DATE)))
				),
				parseType(cursor)
		);
	}

    private HobbyData parseHobbyAndType(Cursor cursor) {
        if (cursor == null || cursor.isClosed() || cursor.getPosition() == -1) {
            throw new IllegalArgumentException();
        }

        return new HobbyData(
                cursor.getLong(cursor.getColumnIndexOrThrow(HobbyTable.KEY_ID)),
                new Hobby(
                        new DateTime(cursor.getLong(
                                cursor.getColumnIndexOrThrow(HobbyTable.KEY_START_DATE))),
                        new DateTime(cursor.getLong(
                                cursor.getColumnIndexOrThrow(HobbyTable.KEY_END_DATE)))
                ),
                parseType(cursor)
        );
    }

    ///// LISTENERS /////

    public void addListener(DatabaseListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DatabaseListener listener) {
        listeners.remove(listener);
    }

    private void notifyDatabaseChange() {
    	for (DatabaseListener l : listeners) {
			l.onDatabaseChange();
		}
    }
}