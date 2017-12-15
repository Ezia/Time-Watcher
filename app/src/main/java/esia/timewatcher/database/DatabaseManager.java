package esia.timewatcher.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {
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

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_OCCUPATION_TYPES_TABLE =
                "CREATE TABLE " + TABLE_OCCUPATION_TYPES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_ICON + " BLOB)";
        String CREATE_HOBBIES_TABLE =
                "CREATE TABLE " + TABLE_HOBBIES + "("
                        + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + KEY_TYPE + " INT,"
                        + KEY_START_DATE + " INT,"
                        + KEY_END_DATE + " INT)";
        String CREATE_EVENTS_TABLE =
                "CREATE TABLE " + TABLE_EVENTS + "("
                        + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + KEY_TYPE + " INT,"
                        + KEY_DATE + " INT)";

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
}