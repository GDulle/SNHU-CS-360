package com.cs360.gunnardullecs360project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EventsSQLite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EventData.db";
    private static final int DATABASE_VERSION = 1;

    public static final class EventData {
        public static final String TABLE_NAME = "EventsTable";
        public static final String COLUMN_0_ID = "_id";
        public static final String COLUMN_1_NAME = "name";
        public static final String COLUMN_2_DATE = "date";
        public static final String COLUMN_3_TIME = "time";
        public static final String COLUMN_4_DESCRIPTION = "description";
    }

    public EventsSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + EventsSQLite.EventData.TABLE_NAME + " (" +
                EventsSQLite.EventData.COLUMN_0_ID + " integer primary key autoincrement, " +
                EventsSQLite.EventData.COLUMN_1_NAME + " text, " +
                EventsSQLite.EventData.COLUMN_2_DATE + " text, " +
                EventsSQLite.EventData.COLUMN_3_TIME + " text, " +
                EventsSQLite.EventData.COLUMN_4_DESCRIPTION + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + EventsSQLite.EventData.TABLE_NAME);
        onCreate(db);
    }

    //Database CRUD (Create, Read, Update, Delete) Operations

    //Add event to database (Create)
    public void createEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventData.COLUMN_1_NAME, event.getUserName());
        values.put(EventData.COLUMN_2_DATE, event.getDate());
        values.put(EventData.COLUMN_3_TIME, event.getTime());
        values.put(EventData.COLUMN_4_DESCRIPTION, event.getDescription());

        db.insert(EventData.TABLE_NAME, null, values);
        db.close();
    }

    //Read event from database
    public Event readEvent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(EventData.TABLE_NAME,
                new String[]{EventData.COLUMN_0_ID, EventData.COLUMN_1_NAME, EventData.COLUMN_2_DATE, EventData.COLUMN_3_TIME, EventData.COLUMN_4_DESCRIPTION}, EventData.COLUMN_0_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Event event = new Event(Integer.parseInt(Objects.requireNonNull(cursor).getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));

        cursor.close();

        return event;
    }

    //Update event from database
    public void updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EventData.COLUMN_1_NAME, event.getUserName());
        values.put(EventData.COLUMN_2_DATE, event.getDate());
        values.put(EventData.COLUMN_3_TIME, event.getTime());
        values.put(EventData.COLUMN_4_DESCRIPTION, event.getDescription());

        db.update(EventData.TABLE_NAME, values, EventData.COLUMN_0_ID + " = ?", new String[]{String.valueOf(event.getId())});
    }

    //Delete event from database
    public void deleteEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(EventData.TABLE_NAME, EventData.COLUMN_0_ID + " = ?", new String[]{String.valueOf(event.getId())});
        db.close();
    }

    //Global Database Operations

    //Get All Events
    public List<Event> getAllEvents() {
        List<Event> eventList = new ArrayList<>();

        //Select All Query
        String selectQuery = "SELECT * FROM " + EventData.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                Event event = new Event();
                event.setId(Integer.parseInt(cursor.getString(0)));
                event.setUserName(cursor.getString(1));
                event.setDate(cursor.getString(2));
                event.setTime(cursor.getString(3));
                event.setDescription(cursor.getString(4));

                eventList.add(event);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return eventList;
    }

    //Delete all events
    public void deleteAllEvents() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EventData.TABLE_NAME, null, null);
        db.close();
    }

    //Get count of events
    public int getEventsCount() {
        String countQuery = "SELECT * FROM " + EventData.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int eventsTotal = cursor.getCount();
        cursor.close();

        return eventsTotal;
    }
}
