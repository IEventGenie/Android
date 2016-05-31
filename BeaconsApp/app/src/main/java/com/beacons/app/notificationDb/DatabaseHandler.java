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

    // Table name
    private static final String TABLE_CONFIRM_CODE_EVENTS = "confimation_code_events";
    private static final String TABLE_NOTIFICATION = "notifications";

    //Confirmation Events Table Columns names
    private static final String KEY_ID = "_id";
    private static final String KEY_CONFIRMATION_CODE = "confirm_code";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EV_ID = "ev_id";
    private static final String KEY_EV_ADDRESS_TXT = "ev_addr_txt";
    private static final String KEY_EV_CITY_TXT = "ev_city_txt";
    private static final String KEY_EV_IMG_URL = "ev_img_url";
    private static final String KEY_EV_LOC_TXT = "ev_loc_txt";
    private static final String KEY_EV_NAME = "ev_name";
    private static final String KEY_ENABLE_PRECHECK_IN = "enable_prechk_in";
    private static final String KEY_ATTENDEE_ID = "attendee_id";
    private static final String KEY_CHKIN_START_DATE = "chk_start_date";
    private static final String KEY_CHKIN_END_DATE = "chk_end_date";
    private static final String KEY_PRE_CHKIN_START_DATE = "pre_chk_start_date";
    private static final String KEY_PRE_CHKIN_END_DATE = "pre_chk_end_date";
    private static final String KEY_ENABLE_CHKIN = "enable_chk_in";
    private static final String KEY_EVENT_STATUS = "event_status";
    private static final String KEY_EVENT_PRECHECKED_IN_ST = "event_prechkedin_status";
    private static final String KEY_EVENT_START_DATE = "event_start_date";
    private static final String KEY_EVENT_END_DATE = "event_end_date";
    private static final String KEY_EVENT_PRE_CHKIN_STATUS = "event_prechkin_status";
    private static final String KEY_EVENT_DETAIL_JSON = "event_detail_json";

    // Contacts Table Columns names
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DATE = "created_at";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONFIRMATION_CODE_EVENT_TABLE = "CREATE TABLE " +TABLE_CONFIRM_CODE_EVENTS + "( "+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                KEY_CONFIRMATION_CODE + " TEXT," +
                KEY_LAST_NAME + " TEXT," +
                KEY_EV_ID + " TEXT," +
                KEY_EV_ADDRESS_TXT + " TEXT," +
                KEY_EV_CITY_TXT + " TEXT," +
                KEY_EV_IMG_URL + " TEXT," +
                KEY_EV_LOC_TXT + " TEXT," +
                KEY_EV_NAME + " TEXT," +
                KEY_ENABLE_PRECHECK_IN + " TEXT," +
                KEY_ATTENDEE_ID + " TEXT," +
                KEY_CHKIN_START_DATE +" TEXT," +
                KEY_CHKIN_END_DATE +" TEXT,"+
                KEY_PRE_CHKIN_START_DATE +" TEXT,"+
                KEY_PRE_CHKIN_END_DATE +" TEXT,"+
                KEY_ENABLE_CHKIN +" TEXT,"+
                KEY_EVENT_STATUS +" TEXT,"+
                KEY_EVENT_PRECHECKED_IN_ST+" TEXT,"+
                KEY_EVENT_START_DATE+" TEXT,"+
                KEY_EVENT_END_DATE+" TEXT,"+
                //KEY_EVENT_PRE_CHKIN_STATUS+" TEXT,"+
                KEY_EVENT_DETAIL_JSON+" TEXT "+
                ")";


        String CREATE_CONTACTS_TABLE = "CREATE TABLE " +TABLE_NOTIFICATION + "( "+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                KEY_TITLE + " TEXT," + KEY_TYPE + " TEXT," + KEY_DATE +" DATETIME DEFAULT CURRENT_TIMESTAMP " + ")";

        db.execSQL(CREATE_CONFIRMATION_CODE_EVENT_TABLE);
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONFIRM_CODE_EVENTS);
        // Create tables again
        onCreate(db);
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    // Adding new Event
    public void addEventDetails(EventDetailDBModel evdet) {

        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<EventDetailDBModel> evList = getAllEvents();
        String id = "";
        String evPrechk = "";
        boolean doInsert = true;
        for(EventDetailDBModel model : evList){
            if(model.getEvId().equals(evdet.getEvId())){
                id = model.getId();
                evPrechk = model.getEvPreChkinStatus();
                doInsert = false;
            }
        }

        ContentValues values = new ContentValues();
        values.put(KEY_CONFIRMATION_CODE, evdet.getConfirmCode());
        values.put(KEY_LAST_NAME, evdet.getLastName());
        values.put(KEY_EV_ID, evdet.getEvId());
        values.put(KEY_EV_ADDRESS_TXT, evdet.getEvAddrTxt());
        values.put(KEY_EV_CITY_TXT, evdet.getEvCityTxt());
        values.put(KEY_EV_IMG_URL, evdet.getEvImgUrl());
        values.put(KEY_EV_LOC_TXT, evdet.getEvLocTxt());
        values.put(KEY_EV_NAME, evdet.getEvName());
        values.put(KEY_ENABLE_PRECHECK_IN, evdet.getEnablePrechkIn());
        values.put(KEY_ATTENDEE_ID, evdet.getAttendeeId());
        values.put(KEY_CHKIN_START_DATE, evdet.getChkinStartDate());
        values.put(KEY_CHKIN_END_DATE, evdet.getChkInEndDate());
        values.put(KEY_PRE_CHKIN_START_DATE, evdet.getPreChkInStrtDate());
        values.put(KEY_PRE_CHKIN_END_DATE, evdet.getPreChkInEndDate());
        values.put(KEY_ENABLE_CHKIN, evdet.getEnablCheckin());
        values.put(KEY_EVENT_STATUS, evdet.getEventStatus());
        values.put(KEY_EVENT_PRECHECKED_IN_ST, evdet.getEvPreChkinStatus());
        values.put(KEY_EVENT_START_DATE, evdet.getEvStartDate());
        values.put(KEY_EVENT_END_DATE, evdet.getEvEndDate());
        //values.put(KEY_EVENT_PRE_CHKIN_STATUS, evdet.getEvPreChkinStatus());
        String jsontosave = evdet.getEventDetailJson();
        values.put(KEY_EVENT_DETAIL_JSON, jsontosave);

        if(doInsert) {
            // Inserting Row
            long res = db.insert(TABLE_CONFIRM_CODE_EVENTS, null, values);
            db.close(); // Closing database connection
        }else{
            //UPDATE ROW
            //add Existing id
            values.put(KEY_ID,id);
            values.put(KEY_EVENT_PRECHECKED_IN_ST,evPrechk);
            String where = KEY_EV_ID+"=?";
            String[] whereArgs = new String[]{String.valueOf(evdet.getEvId())};
            long res = db.update(TABLE_CONFIRM_CODE_EVENTS,values,where,whereArgs);
            db.close(); // Closing database connection
        }
    }

    public int deleteEvent(String evId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int res = db.delete(TABLE_CONFIRM_CODE_EVENTS, KEY_EV_ID + " = ?",
                new String[] { evId });
        db.close();
        return res;
    }

    // Getting All EVENTS
    public ArrayList<EventDetailDBModel> getAllEvents() {
        ArrayList<EventDetailDBModel> eventList = new ArrayList<EventDetailDBModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONFIRM_CODE_EVENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EventDetailDBModel event = new EventDetailDBModel();
                event.setId(cursor.getString(0));
                event.setConfirmCode(cursor.getString(1));
                event.setLastName(cursor.getString(2));
                event.setEvId(cursor.getString(3));
                event.setEvAddrTxt(cursor.getString(4));
                event.setEvCityTxt(cursor.getString(5));
                event.setEvImgUrl(cursor.getString(6));
                event.setEvLocTxt(cursor.getString(7));
                event.setEvName(cursor.getString(8));
                event.setEnablePrechkIn(cursor.getString(9));
                event.setAttendeeId(cursor.getString(10));
                event.setChkinStartDate(cursor.getString(11));
                event.setChkInEndDate(cursor.getString(12));
                event.setPreChkInStrtDate(cursor.getString(13));
                event.setPreChkInEndDate(cursor.getString(14));
                event.setEnablCheckin(cursor.getString(15));
                event.setEventStatus(cursor.getString(16));
                event.setEvPreChkinStatus(cursor.getString(17));
                event.setEvStartDate(cursor.getString(18));
                event.setEvEndDate(cursor.getString(19));
                //event.setPreCheckinStatus(cursor.getString(20));
                event.setEventDetailJson(cursor.getString(20));

                // Adding events to list
                eventList.add(event);
            } while (cursor.moveToNext());
        }

        // return events list
        return eventList;
    }

    // Getting Active EVENTS
    public ArrayList<EventDetailDBModel> getActiveEvents() {
        ArrayList<EventDetailDBModel> eventList = new ArrayList<EventDetailDBModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONFIRM_CODE_EVENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EventDetailDBModel event = new EventDetailDBModel();
                event.setId(cursor.getString(0));
                event.setConfirmCode(cursor.getString(1));
                event.setLastName(cursor.getString(2));
                event.setEvId(cursor.getString(3));
                event.setEvAddrTxt(cursor.getString(4));
                event.setEvCityTxt(cursor.getString(5));
                event.setEvImgUrl(cursor.getString(6));
                event.setEvLocTxt(cursor.getString(7));
                event.setEvName(cursor.getString(8));
                event.setEnablePrechkIn(cursor.getString(9));
                event.setAttendeeId(cursor.getString(10));
                event.setChkinStartDate(cursor.getString(11));
                event.setChkInEndDate(cursor.getString(12));
                event.setPreChkInStrtDate(cursor.getString(13));
                event.setPreChkInEndDate(cursor.getString(14));
                event.setEnablCheckin(cursor.getString(15));
                event.setEventStatus(cursor.getString(16));
                event.setEvPreChkinStatus(cursor.getString(17));
                event.setEvStartDate(cursor.getString(18));
                event.setEvEndDate(cursor.getString(19));
                //event.setPreCheckinStatus(cursor.getString(20));
                event.setEventDetailJson(cursor.getString(20));

                // Adding contact to list
                if(!event.getEventStatus().equals("Closed")) {
                    eventList.add(event);
                }
            } while (cursor.moveToNext());
        }

        // return contact list
        return eventList;
    }

    // Getting Past EVENTS
    public ArrayList<EventDetailDBModel> getPastEvents() {
        ArrayList<EventDetailDBModel> eventList = new ArrayList<EventDetailDBModel>();

        SQLiteDatabase db = this.getWritableDatabase();
        // Select All Query
        Cursor cursor = db.query(TABLE_CONFIRM_CODE_EVENTS, new String[] { KEY_ID, KEY_CONFIRMATION_CODE, KEY_LAST_NAME, KEY_EV_ID,
                        KEY_EV_ADDRESS_TXT,KEY_EV_CITY_TXT,KEY_EV_IMG_URL,KEY_EV_LOC_TXT,KEY_EV_NAME,KEY_ENABLE_PRECHECK_IN,KEY_ATTENDEE_ID,
                        KEY_CHKIN_START_DATE,KEY_CHKIN_END_DATE,KEY_PRE_CHKIN_START_DATE,KEY_PRE_CHKIN_END_DATE,KEY_ENABLE_CHKIN,KEY_EVENT_STATUS,KEY_EVENT_PRECHECKED_IN_ST,KEY_EVENT_START_DATE,KEY_EVENT_END_DATE,KEY_EVENT_DETAIL_JSON}, KEY_EVENT_STATUS + "=?",
                new String[] { "Closed" }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        /*String selectQuery = "SELECT  * FROM " + TABLE_CONFIRM_CODE_EVENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);*/

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                EventDetailDBModel event = new EventDetailDBModel();
                event.setId(cursor.getString(0));
                event.setConfirmCode(cursor.getString(1));
                event.setLastName(cursor.getString(2));
                event.setEvId(cursor.getString(3));
                event.setEvAddrTxt(cursor.getString(4));
                event.setEvCityTxt(cursor.getString(5));
                event.setEvImgUrl(cursor.getString(6));
                event.setEvLocTxt(cursor.getString(7));
                event.setEvName(cursor.getString(8));
                event.setEnablePrechkIn(cursor.getString(9));
                event.setAttendeeId(cursor.getString(10));
                event.setChkinStartDate(cursor.getString(11));
                event.setChkInEndDate(cursor.getString(12));
                event.setPreChkInStrtDate(cursor.getString(13));
                event.setPreChkInEndDate(cursor.getString(14));
                event.setEnablCheckin(cursor.getString(15));
                event.setEventStatus(cursor.getString(16));
                event.setEvPreChkinStatus(cursor.getString(17));
                event.setEvStartDate(cursor.getString(18));
                event.setEvEndDate(cursor.getString(19));
                event.setEventDetailJson(cursor.getString(20));

                // Adding event to list
                eventList.add(event);

            } while (cursor.moveToNext());
        }

        // return contact list
        return eventList;
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