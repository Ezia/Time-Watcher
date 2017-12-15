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

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "time_history";

    // Tables names
    private static final String TABLE_OCCUPATION_TYPES = "occupation_types";
    private static final String TABLE_HOBBIES = "hobbies";
    private static final String TABLE_EVENTS = "events";

    // Tables column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ICON = "icon";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";
    private static final String KEY_DATE = "date";
    private static final String KEY_TYPE = "date";

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
                    + KEY_ID + " INTEGER,"
                    + KEY_NAME + " TEXT,"
                    + KEY_ICON + " BLOB,"
                    + "PRIMARY KEY("
                        + KEY_ID + ","
                        + KEY_NAME
                    + ")"
                + ")";
        String CREATE_HOBBIES_TABLE =
                "CREATE TABLE " + TABLE_HOBBIES + "("
                    + KEY_ID + " INTEGER,"
                    + KEY_TYPE + " INTEGER,"
                    + KEY_START_DATE + " INTEGER,"
                    + KEY_END_DATE + " INTEGER,"
                    + "FOREIGN KEY(" + KEY_TYPE + ")"
                        + " REFERENCES " + TABLE_OCCUPATION_TYPES + "(" + KEY_ID + "),"
                    + "PRIMARY KEY(" + KEY_ID + ")"
                + ")";
        String CREATE_EVENTS_TABLE =
                "CREATE TABLE " + TABLE_EVENTS + "("
                    + KEY_ID + " INTEGER,"
                    + KEY_TYPE + " INTEGER,"
                    + KEY_DATE + " INTEGER,"
                    + "FOREIGN KEY(" + KEY_TYPE + ")"
                        + " REFERENCES " + TABLE_OCCUPATION_TYPES + "(" + KEY_ID + "),"
                    + "PRIMARY KEY(" + KEY_ID + ")"
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

    ///// OCCUPATION TYPES /////

    public OccupationTypeData createOccupationType(OccupationType occupationType) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, occupationType.getName()); // Contact Name
        values.put(KEY_ICON, bitmapToBytes(occupationType.getIcon())); // Contact Phone Number

        // Inserting Row
        long id = db.insert(TABLE_OCCUPATION_TYPES, null, values);
        db.close(); // Closing database connection

        if (id > -1) {
            return new OccupationTypeData(id, occupationType);
        }
        return null;
    }

    public void requestOccupationType(OccupationTypeData data) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_OCCUPATION_TYPES,
                new String[] { KEY_ID, KEY_NAME, KEY_ICON },
                KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) },
                null,
                null,
                null,
                null);
        db.close();

        if (cursor != null) {
            cursor.moveToFirst();
            data.setOccupationType(
                    new OccupationType(cursor.getString(1),
                    bytesToBitmap(cursor.getBlob(2)))
            );
        }
    }

    public void updateOccupationType(OccupationTypeData data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, data.getOccupationType().getName());
        values.put(KEY_ICON, bitmapToBytes(data.getOccupationType().getIcon()));

        // updating row
        db.update(TABLE_OCCUPATION_TYPES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
        db.close();
    }

    public void deleteOccupationType(OccupationTypeData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_OCCUPATION_TYPES, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
        db.close();
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