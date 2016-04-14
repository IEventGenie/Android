package com.beacons.app.notificationDb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "becons_notification_DB";
 
    // Contacts table name
    private static final String TABLE_NOTIFICATION = "notifications";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DATE = "created_at";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " +TABLE_NOTIFICATION + "( "+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                KEY_TITLE + " TEXT," + KEY_TYPE + " TEXT," + KEY_DATE +" DATETIME DEFAULT CURRENT_TIMESTAMP " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
 
        // Create tables again
        onCreate(db);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    // Adding new Notification
    public void addNotification(BeaconNotification notification) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, notification.getTitle());
        values.put(KEY_TYPE, notification.getType());
        values.put(KEY_DATE, getDateTime());

        // Inserting Row
        long id = db.insert(TABLE_NOTIFICATION, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single contact
    BeaconNotification getNotification(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_NOTIFICATION, new String[] { KEY_ID, KEY_TITLE, KEY_TYPE, KEY_DATE }, KEY_TITLE + "=?",
                new String[] { String.valueOf(title) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();
 
        BeaconNotification noti = new BeaconNotification(cursor.getString(1), cursor.getString(2));
        noti.setId(cursor.getString(0));
        noti.setDate(cursor.getString(3));
        // return contact
        return noti;
    }
     
    // Getting All Contacts
    public ArrayList<BeaconNotification> getAllNotification() {
        ArrayList<BeaconNotification> beaconNotificationList = new ArrayList<BeaconNotification>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATION + " ORDER BY "+ KEY_DATE +" DESC";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BeaconNotification noti = new BeaconNotification();
                noti.setId(cursor.getString(0));
                noti.setTitle(cursor.getString(1));
                noti.setType(cursor.getString(2));
                noti.setDate(cursor.getString(3));
                // Adding contact to list
                beaconNotificationList.add(noti);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return beaconNotificationList;
    }
 
    public int updateBeaconNotification(BeaconNotification noti) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID,noti.getId());
        values.put(KEY_TITLE, noti.getTitle());
        values.put(KEY_TYPE, noti.getType());
        values.put(KEY_DATE,noti.getDate());
 
        // updating row
        return db.update(TABLE_NOTIFICATION, values, KEY_ID + " = ?",
                new String[] { String.valueOf(noti.getId()) });
    }
 
    public void deleteBeaconNotification(BeaconNotification noti) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTIFICATION, KEY_ID + " = ?",
                new String[] { String.valueOf(noti.getId()) });
        db.close();
    }

    public int getBeaconNotificationCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NOTIFICATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
 
}