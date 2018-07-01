package com.nephi.getoffyourphone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xerxes on 15.01.18.
 */

public class DB_Helper extends SQLiteOpenHelper {

    // All Static variables
    private static final int DATABASE_VERSION = 6;  // Database Version
    // Database Name
    private static final String DATABASE_NAME = "SelectedApps";
    // Apps table name
    private static final String TABLE_APPS = "Apps";
    //First_Boot table name
    private static final String TABLE_FIRST_BOOT = "First_Boot";
    //Timer table name
    private static final String TABLE_TIMER = "Timer";
    //App Open Counter
    private static final String TABLE_OPEN_COUNTER = "OpenCounter";


    // Apps Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SELECTOR_ID = "s_id";
    private static final String KEY_PKG = "PKG";
    //FirstBoot
    private static final String KEY_ID_FB = "fb_id";
    private static final String KEY_FIRST_BOOT = "isFirstBoot";
    //TimerProcess
    private static final String KEY_ID_TIMER = "timer_id";
    private static final String TIMER_FINISH = "Timer_Finished";
    private static final String LOCK_TIME = "Lock_Time";
    private static final String RUNNING = "Running";
    private static final String HOURS = "hours";
    private static final String DATA = "data";
    private static final String SELECTED = "isSelected";

    //Open Counter
    private static final String KEY_OPEN_ID = "o_id";
    private static final String KEY_LOCK_COUNTER = "lock_counter";
    private static final String KEY_OPENED_TIMES = "opened_times";


    DB_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create first boot table
        String CREATE_FIRST_BOOT_TABLE = "CREATE TABLE " + TABLE_FIRST_BOOT + "("
                + KEY_ID_FB + " INTEGER PRIMARY KEY,"
                + KEY_FIRST_BOOT + " TEXT" + ")";
        //Create Open Counter table
        String CREATE_OPEN_COUNTER = "CREATE TABLE " + TABLE_OPEN_COUNTER + "("
                + KEY_OPEN_ID + " INTEGER PRIMARY KEY,"
                + KEY_LOCK_COUNTER + " INTEGER,"
                + KEY_OPENED_TIMES + " INTEGER" + ")";
        //Create apps table
        String CREATE_APPS_TABLE = "CREATE TABLE " + TABLE_APPS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_PKG + " TEXT,"
                + KEY_SELECTOR_ID + " INTEGER" + ")";
        //Create Timer table
        String CREATE_TIMER_TABLE = "CREATE TABLE " + TABLE_TIMER + "("
                + KEY_ID_TIMER + " INTEGER PRIMARY KEY,"
                + LOCK_TIME + " TEXT,"
                + RUNNING + " TEXT,"
                + TIMER_FINISH + " INTEGER,"
                + HOURS + " TEXT,"
                + SELECTED + " INTEGER,"
                + DATA + " TEXT" + ")";
        db.execSQL(CREATE_FIRST_BOOT_TABLE);
        db.execSQL(CREATE_APPS_TABLE);
        db.execSQL(CREATE_TIMER_TABLE);
        db.execSQL(CREATE_OPEN_COUNTER);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIRST_BOOT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMER);
        db.execSQL("DROP TABLE IF EXISTS Version");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OPEN_COUNTER);
        // Create tables again
        onCreate(db);
    }

    void set_defaultOpenCounter(int openCounter, int opened_times) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOCK_COUNTER, openCounter); //Counter of Opens
        values.put(KEY_OPENED_TIMES, opened_times); //Counter of Opens

        //Inserting Row
        db.insert(TABLE_OPEN_COUNTER, null, values);

        db.close(); //closing database connetion
    }

    void set_openTimes(int openTimes) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_OPENED_TIMES, openTimes); //Hours
        //Inserting Row
        db.update(TABLE_OPEN_COUNTER, values, "o_id=?", new String[]{"1"});
        db.close(); //closing database connetion
    }

    int get_openTimes(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int openCounter = 0;
        Cursor cursor = db.query(TABLE_OPEN_COUNTER, new String[]{KEY_OPEN_ID,
                        KEY_LOCK_COUNTER, KEY_OPENED_TIMES}, KEY_OPEN_ID + "=?",
                new String[]{String.valueOf(_id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            openCounter = cursor.getInt(2);
            cursor.close();
        }
        // return App
        return openCounter;
    }

    void set_openCounter(int openCounter) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOCK_COUNTER, openCounter); //Hours
        //Inserting Row
        db.update(TABLE_OPEN_COUNTER, values, "o_id=?", new String[]{"1"});
        db.close(); //closing database connetion
    }

    int get_openCounter(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int openCounter = 0;
        Cursor cursor = db.query(TABLE_OPEN_COUNTER, new String[]{KEY_OPEN_ID,
                        KEY_LOCK_COUNTER}, KEY_OPEN_ID + "=?",
                new String[]{String.valueOf(_id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            openCounter = cursor.getInt(1);
            cursor.close();
        }
        // return App
        return openCounter;
    }

    void set_AllTimerData(String LockTime, String Running, int TimerFinish, String Hours, int Selected, String data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LOCK_TIME, LockTime); //Lock
        values.put(RUNNING, Running); //isRunning?
        values.put(TIMER_FINISH, TimerFinish); //TimerFinish
        values.put(HOURS, Hours); //Hours
        values.put(SELECTED, Selected); //SelectedApps
        values.put(DATA, data); //Date Data

        //Inserting Row
        db.insert(TABLE_TIMER, null, values);
        db.close(); //closing database connetion
    }

    void set_Data(String Data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DATA, Data); //Hours
        //Inserting Row
        db.update(TABLE_TIMER, values, "timer_id=?", new String[]{"1"});
        db.close(); //closing database connetion
    }

    String get_Data(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Data = "";
        Cursor cursor = db.query(TABLE_TIMER, new String[]{KEY_ID_TIMER,
                        LOCK_TIME, RUNNING, TIMER_FINISH, HOURS, SELECTED, DATA}, KEY_ID_TIMER + "=?",
                new String[]{String.valueOf(_id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Data = cursor.getString(6);
            cursor.close();
        }
        // return App
        return Data;
    }

    void set_Selected(int Selected) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SELECTED, Selected); // 0 No , 1 Yes
        //Inserting Row
        db.update(TABLE_TIMER, values, "timer_id=?", new String[]{"1"});
        db.close(); //closing database connetion
    }

    int get_Selected(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int Selected = 0;
        Cursor cursor = db.query(TABLE_TIMER, new String[]{KEY_ID_TIMER
                        , LOCK_TIME, RUNNING, TIMER_FINISH, HOURS, SELECTED, DATA}, KEY_ID_TIMER + "=?",
                new String[]{String.valueOf(_id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Selected = Integer.parseInt(cursor.getString(5));
            cursor.close();
        }
        // return App
        return Selected;
    }

    void set_Hours(String Hours) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HOURS, Hours); //Hours
        //Inserting Row
        db.update(TABLE_TIMER, values, "timer_id=?", new String[]{"1"});
        db.close(); //closing database connetion
    }

    String get_Hours(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Hours = "";
        Cursor cursor = db.query(TABLE_TIMER, new String[]{KEY_ID_TIMER
                        , LOCK_TIME, RUNNING, TIMER_FINISH, HOURS, SELECTED, DATA}, KEY_ID_TIMER + "=?",
                new String[]{String.valueOf(_id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Hours = cursor.getString(4);
            cursor.close();
        }
        // return App
        return Hours;
    }

    void set_TimerFinish(int Running) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TIMER_FINISH, Running); // 0 No , 1 Yes
        //Inserting Row
        db.update(TABLE_TIMER, values, "timer_id=?", new String[]{"1"});
        db.close(); //closing database connetion
    }

    int get_TimerFinish(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int TimerFinish = 0;
        Cursor cursor = db.query(TABLE_TIMER, new String[]{KEY_ID_TIMER
                        , LOCK_TIME, RUNNING, TIMER_FINISH, HOURS, SELECTED, DATA}, KEY_ID_TIMER + "=?",
                new String[]{String.valueOf(_id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            TimerFinish = Integer.parseInt(cursor.getString(3));
            cursor.close();
        }
        // return App
        return TimerFinish;
    }

    void set_Running(String Running) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RUNNING, Running); //Running Y and N
        //Inserting Row
        db.update(TABLE_TIMER, values, "timer_id=?", new String[]{"1"});
        db.close(); //closing database connetion
    }

    String get_Running(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Running = "";
        Cursor cursor = db.query(TABLE_TIMER, new String[]{KEY_ID_TIMER
                        , LOCK_TIME, RUNNING, TIMER_FINISH, HOURS, SELECTED, DATA}, KEY_ID_TIMER + "=?",
                new String[]{String.valueOf(_id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Running = cursor.getString(2);
            cursor.close();
        }
        // return App
        return Running;
    }

    void set_LockTime(String Lock_Time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LOCK_TIME, Lock_Time); //LockTime set
        //Inserting Row
        db.update(TABLE_TIMER, values, "timer_id=?", new String[]{"1"});
        db.close(); //closing database connetion
    }

    String get_LockTime(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String lock_time = "";
        Cursor cursor = db.query(TABLE_TIMER, new String[]{KEY_ID_TIMER
                        , LOCK_TIME, RUNNING, TIMER_FINISH, HOURS, SELECTED, DATA}, KEY_ID_TIMER + "=?",
                new String[]{String.valueOf(_id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            lock_time = cursor.getString(1);
            cursor.close();
        }
        // return App
        return lock_time;
    }

    public void dbClose() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }

    void set_FirstBoot(String value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_BOOT, value); //First Boot value, 0 is NO 1 is YES
        //Inserting Row
        db.insert(TABLE_FIRST_BOOT, null, values);
        db.close(); //closing database connetion
    }

    public String get_FirstBoot(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String value = "";
        Cursor cursor = db.query(TABLE_FIRST_BOOT, new String[]{KEY_ID_FB,
                        KEY_FIRST_BOOT}, KEY_ID_FB + "=?",
                new String[]{String.valueOf(_id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            value = cursor.getString(1);
            cursor.close();
        }
        // return App
        return value;
    }

    void add_apps(apps apps) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PKG, apps.get_PKG()); //PKG
        values.put(KEY_SELECTOR_ID, apps.getS_id()); //ID
        //Inserting Row
        db.insert(TABLE_APPS, null, values);
        db.close(); //closing database connetion
    }

    public void add_apps_name(apps apps) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PKG, apps.get_PKG()); //PKG
        //Inserting Row
        db.insert(TABLE_APPS, null, values);
        db.close(); //closing database connetion
    }

    public void add_apps_id(apps apps) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SELECTOR_ID, apps.getS_id()); //PKG
        //Inserting Row
        db.insert(TABLE_APPS, null, values);
        db.close(); //closing database connetion
    }

    // Getting single app
    apps get_app(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_APPS, new String[]{KEY_ID,
                        KEY_PKG, KEY_SELECTOR_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(_id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        apps contact = new apps(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getInt(2));
        // return App
        return contact;
    }

    // Getting single app
    String get_app_PKG(int _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String app_pkg = "";
        Cursor cursor = db.query(TABLE_APPS, new String[]{KEY_ID,
                        KEY_PKG, KEY_SELECTOR_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(_id)}, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            app_pkg = cursor.getString(1);
        }
        // return App
        return app_pkg;
    }

    // Getting All apps
    public List<apps> getAllApps() {
        List<apps> AppsList = new ArrayList<apps>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_APPS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                apps apps = new apps();
                apps.set_id(Integer.parseInt(cursor.getString(0)));
                apps.set_PKG(cursor.getString(1));
                apps.setS_id(cursor.getInt(2));
                // Adding contact to list
                AppsList.add(apps);
            } while (cursor.moveToNext());
        }

        // return Apps list
        return AppsList;
    }

    long getFirstBootCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_FIRST_BOOT);
        db.close();
        return cnt;
    }

    public void EmptyTableFirstBoot() {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_APPS,null,null);
        db.execSQL("delete from " + TABLE_FIRST_BOOT);
        db.execSQL("vacuum");
        db.close();
    }

    // Getting Apps Count
    long getAppsCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_APPS);
        db.close();
        return cnt;
    }

    // Getting Apps Count
    long getOpenCounter() {
        SQLiteDatabase db = this.getReadableDatabase();
        long cnt = DatabaseUtils.queryNumEntries(db, TABLE_OPEN_COUNTER);
        db.close();
        return cnt;
    }

    public void delAllEmptyPKGs(String _PKG) {
        String deleteQuery = "DELETE FROM " + TABLE_APPS + " WHERE PKG='" + _PKG + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQuery);
        db.execSQL("vacuum");
        db.close();
    }

    public void delAllEmptyS_IDS(int S_ID) {
        String deleteQuery = "DELETE FROM " + TABLE_APPS + " WHERE s_id='" + S_ID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(deleteQuery);
        db.execSQL("vacuum");
        db.close();
    }

    void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.delete(TABLE_APPS,null,null);
        db.execSQL("delete from " + TABLE_APPS);
        db.execSQL("vacuum");
        db.close();
    }
}