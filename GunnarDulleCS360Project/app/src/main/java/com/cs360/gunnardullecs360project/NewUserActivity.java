package com.cs360.gunnardullecs360project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//NewUserActivity to add new user to user database

public class NewUserActivity extends AppCompatActivity {

    Button AddNewUserButton, CancelNewUserButton;
    EditText UserNameHolder, PhoneNumberHolder, PasswordHolder;
    Boolean EmptyHolder;
    SQLiteDatabase db;
    UsersSqlite handler;
    String Search_Result = "Not_Found";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        UserNameHolder = findViewById(R.id.editTextNewUsername);
        PhoneNumberHolder = findViewById(R.id.editTextNewPhoneNumber);
        PasswordHolder = findViewById(R.id.editTextNewPassword);
        AddNewUserButton = findViewById(R.id.buttonRegisterNewUser);
        CancelNewUserButton = findViewById(R.id.buttonCancelNewUser);
        handler = new UsersSqlite(this);

        //Click listener for AddNewUserButton
        AddNewUserButton.setOnClickListener(view -> {
            String message = CheckEditTextNotEmpty();

            if (!EmptyHolder) {
                //Check if phone number already in database
                CheckPhoneAlreadyExists();
                //Empty editText fields after done creating new user in database
                EmptyEditTextAfterDataInsert();
            } else {
                //Display toast message if any fields empty and focus the field
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        });

        //Click listener for cancel button
        CancelNewUserButton.setOnClickListener(view -> {
            //Send back to LoginActivity
            startActivity(new Intent(NewUserActivity.this, LoginActivity.class));
            this.finish();
        });
    }

    //Add new user to database
    public void InsertUserIntoDatabase() {
        String name = UserNameHolder.getText().toString().trim();
        String phone = PhoneNumberHolder.getText().toString().trim();
        String pass = PasswordHolder.getText().toString().trim();

        User user = new User(name, pass, phone);
        handler.createUser(user);

        //Toast message after new user inserted into database
        Toast.makeText(NewUserActivity.this, "User added!\nYou can log in now.", Toast.LENGTH_LONG).show();

        //Go back to LoginActivity after user added
        startActivity(new Intent(NewUserActivity.this, LoginActivity.class));
        this.finish();
    }

    //Check that no fields are empty
    public String CheckEditTextNotEmpty() {

        //Get value from fields and store in string variables
        String message = "";
        String name = UserNameHolder.getText().toString().trim();
        String phone = PhoneNumberHolder.getText().toString().trim();
        String pass = PasswordHolder.getText().toString().trim();

        //Check that fields are filled
        if (name.isEmpty()) {
            UserNameHolder.requestFocus();
            EmptyHolder = true;
            message = "Username is empty";
        } else if (phone.isEmpty()) {
            PhoneNumberHolder.requestFocus();
            EmptyHolder = true;
            message = "Phone Number is empty";
        } else if (pass.isEmpty()) {
            PasswordHolder.requestFocus();
            EmptyHolder = true;
            message = "Password is empty";
        } else {
            EmptyHolder = false;
        }
        return message;
    }

    //Check if user phone number is already in database
    public void CheckPhoneAlreadyExists() {
        String phone = PhoneNumberHolder.getText().toString().trim();
        db = handler.getWritableDatabase();

        //Use cursor to search for phone number
        Cursor cursor = db.query(UsersSqlite.UserData.TABLE_NAME, null, " " + UsersSqlite.UserData.COLUMN_3_PHONE_NUMBER + "=?", new String[]{phone}, null, null, null);

        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();
                //If phone number exists then set result variable as Phone Number found
                Search_Result = "Phone Number found";
                //Close cursor
                cursor.close();
            }
        }
        handler.close();

        //Call method to check results and load data into database
        CheckCredentials();
    }

    //Final check on user credentials
    public void CheckCredentials() {
        //Check whether phone number already used
        if (Search_Result.equalsIgnoreCase("Phone Number found")) {
            //Phone number exists, message displayed
            Toast.makeText(NewUserActivity.this, "Phone Number already in use.\nTry logging in\nor use Forgot Password", Toast.LENGTH_LONG).show();
        } else {
            //If phone number not in use user added to database
            InsertUserIntoDatabase();
        }
        Search_Result = "Not_Found";
    }

    //Empty EditText after done inserting into database
    public void EmptyEditTextAfterDataInsert() {
        UserNameHolder.getText().clear();
        PhoneNumberHolder.getText().clear();
        PasswordHolder.getText().clear();
    }
}