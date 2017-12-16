package esia.timewatcher.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

import esia.timewatcher.structures.Hobby;
import esia.timewatcher.structures.OccupationType;

public class DatabaseManager extends SQLiteOpenHelper {
    private static DatabaseManager instance = null;

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "time_history";

    private static final String TABLE_OCCUPATION_TYPES = "occupation_types";
    private static final String TABLE_HOBBIES = "hobbies";
    private static final String TABLE_EVENTS = "events";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ICON = "icon";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";
    private static final String KEY_DATE = "date";
    private static final String KEY_TYPE = "type";

    ///// LIFECYCLE /////

    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Must be called by the application before performing operations
     * @param context
     * @return
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
                "CREATE TABLE " + TABLE_OCCUPATION_TYPES + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_NAME + " TEXT UNIQUE,"
                    + KEY_ICON + " BLOB"
                + ")";
        String CREATE_HOBBIES_TABLE =
                "CREATE TABLE " + TABLE_HOBBIES + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TYPE + " INTEGER,"
                    + KEY_START_DATE + " INTEGER,"
                    + KEY_END_DATE + " INTEGER,"
                    + "FOREIGN KEY(" + KEY_TYPE + ")"
                        + " REFERENCES " + TABLE_OCCUPATION_TYPES + "(" + KEY_ID + ")"
                + ")";
        String CREATE_EVENTS_TABLE =
                "CREATE TABLE " + TABLE_EVENTS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_TYPE + " INTEGER,"
                    + KEY_DATE + " INTEGER,"
                    + "FOREIGN KEY(" + KEY_TYPE + ")"
                        + " REFERENCES " + TABLE_OCCUPATION_TYPES + "(" + KEY_ID + ")"
                + ")";

        db.execSQL(CREATE_OCCUPATION_TYPES_TABLE);
        db.execSQL(CREATE_HOBBIES_TABLE);
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OCCUPATION_TYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOBBIES);

        // Creating tables again
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OCCUPATION_TYPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOBBIES);

        // Creating tables again
        onCreate(db);
    }

    ///// OCCUPATION TYPES /////

    public long createOccupationType(OccupationType occupationType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, occupationType.getName());
        values.put(KEY_ICON, bitmapToBytes(occupationType.getIcon()));

        long id = db.insert(TABLE_OCCUPATION_TYPES, null, values);
        db.close();

        return id;
    }

    public OccupationType requestOccupationType(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_OCCUPATION_TYPES,
                new String[] { KEY_ID, KEY_NAME, KEY_ICON },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null,
                null,
                null,
                null);

        OccupationType occupationType = null;
        if (cursor != null) {
            cursor.moveToFirst();
            occupationType = new OccupationType(cursor.getString(1),
                    bytesToBitmap(cursor.getBlob(2))
            );
            cursor.close();
        }
        db.close();

        return occupationType;
    }

    public boolean updateOccupationType(long id, OccupationType type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, type.getName());
        values.put(KEY_ICON, bitmapToBytes(type.getIcon()));

        int affectedRowNbr = db.update(TABLE_OCCUPATION_TYPES, values, KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();

        if (affectedRowNbr == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteOccupationType(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int affectedRowNbr = db.delete(TABLE_OCCUPATION_TYPES, KEY_ID + "=?",
                new String[] { String.valueOf(id) });
        db.close();
        if (affectedRowNbr == 1) {
            return true;
        } else {
            return false;
        }
    }

    ///// BITMAP UTILS /////

    // convert from bitmap to byte array
    public static byte[] bitmapToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap bytesToBitmap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}