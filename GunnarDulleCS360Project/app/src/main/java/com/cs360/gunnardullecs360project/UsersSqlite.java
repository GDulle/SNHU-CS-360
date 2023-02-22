package com.cs360.gunnardullecs360project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//SQLite Handler class for UserData database
public class UsersSqlite extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "UserData.db";
    private static final int DATABASE_VERSION = 1;

    public static final class UserData {
        public static final String TABLE_NAME = "UsersTable";
        public static final String COLUMN_0_ID = "_id";
        public static final String COLUMN_1_NAME = "name";
        public static final String COLUMN_2_PASSWORD = "password";
        public static final String COLUMN_3_PHONE_NUMBER = "phone_number";
    }

    public UsersSqlite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + UserData.TABLE_NAME + " (" +
                UserData.COLUMN_0_ID + " integer primary key autoincrement, " +
                UserData.COLUMN_1_NAME + " text, " +
                UserData.COLUMN_2_PASSWORD + " text, " +
                UserData.COLUMN_3_PHONE_NUMBER + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + UserData.TABLE_NAME);
        onCreate(db);
    }

    //Database CRUD (Create, Read, Update, Delete) Functions

    //Add User to Database (Create)
    public void createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserData.COLUMN_1_NAME, user.getUserName());
        values.put(UserData.COLUMN_2_PASSWORD, user.getPassword());
        values.put(UserData.COLUMN_3_PHONE_NUMBER, user.getPhoneNumber());

        db.insert(UserData.TABLE_NAME, null, values);
        db.close();
    }

    //Read User from Database (Not Used)
    public User readUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(UserData.TABLE_NAME,
                new String[]{UserData.COLUMN_0_ID, UserData.COLUMN_1_NAME, UserData.COLUMN_2_PASSWORD, UserData.COLUMN_3_PHONE_NUMBER}, UserData.COLUMN_0_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User(Integer.parseInt(Objects.requireNonNull(cursor).getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3));

        cursor.close();

        return user;
    }

    //Update User in Database
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserData.COLUMN_1_NAME, user.getUserName());
        values.put(UserData.COLUMN_2_PASSWORD, user.getPassword());
        values.put(UserData.COLUMN_3_PHONE_NUMBER, user.getPhoneNumber());

        return db.update(UserData.TABLE_NAME, values, UserData.COLUMN_0_ID + " = ?", new String[]{String.valueOf(user.getId())});
    }

    //Delete User in Database
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(UserData.TABLE_NAME, UserData.COLUMN_0_ID + " = ?", new String[]{String.valueOf(user.getId())});
        db.close();
    }

    //Global Database Operations

    //Get All Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();

        //Select all query
        String selectQuery = "SELECT * FROM " + UserData.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //Loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUserName(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setPhoneNumber(cursor.getString(3));

                userList.add(user);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return userList;
    }

    //Delete All Users
    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UserData.TABLE_NAME, null, null);
        db.close();
    }

    //Get User Count
    public int getUserCount() {
        String countQuery = "SELECT * FROM " + UserData.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int usersTotal = cursor.getCount();
        cursor.close();

        return usersTotal;
    }
}
