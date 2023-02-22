package com.cs360.gunnardullecs360project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

//LoginActivity Gunnar Dulle CS-360

public class LoginActivity extends AppCompatActivity {

    Activity activity;
    Button LoginButton, NewAccountButton, ForgotPassButton;
    EditText UserName, Password;
    String UserNameHolder, PhoneNumberHolder, PasswordHolder;
    Boolean EmptyHolder;
    PopupWindow popWindow;
    SQLiteDatabase db;
    UsersSqlite handler;
    String TempPassword = "NOT_FOUND";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        activity = this;

        LoginButton = findViewById(R.id.buttonLogin);
        NewAccountButton = findViewById(R.id.buttonCreateAccount);
        ForgotPassButton = findViewById(R.id.buttonForgotPassword);
        UserName = findViewById(R.id.editTextUsername);
        Password = findViewById(R.id.editTextPassword);
        handler = new UsersSqlite(this);

        //Click listener to sign in
        LoginButton.setOnClickListener(view -> {
            //Call login function
            LoginFunction();
        });

        //Click listener to add new account
        NewAccountButton.setOnClickListener(view -> {
            //Open new NewUserActivity using intent on NewAccountButton click
            Intent intent = new Intent(LoginActivity.this, NewUserActivity.class);
            startActivity(intent);
        });

        //Click listener for ForgotPassButton
        ForgotPassButton.setOnClickListener(view -> {
            UserNameHolder = UserName.getText().toString().trim();

            if (!UserNameHolder.isEmpty()) {
                forgotPassPopup();
            } else {
                Toast.makeText(LoginActivity.this, "Username is empty", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Login function
    public void LoginFunction() {
        String message = CheckEditTextNotEmpty();

        if (!EmptyHolder) {

            //Opening SQLite database write permission
            db = handler.getWritableDatabase();

            //Add search username query to cursor
            Cursor cursor = db.query(UsersSqlite.UserData.TABLE_NAME, null, " " + UsersSqlite.UserData.COLUMN_1_NAME + "=?", new String[]{UserNameHolder}, null, null, null);

            while (cursor.moveToNext()) {
                if (cursor.isFirst()) {
                    cursor.moveToFirst();

                    //Storing Password and phone associated with entered username
                    TempPassword = cursor.getString(cursor.getColumnIndexOrThrow(UsersSqlite.UserData.COLUMN_2_PASSWORD));
                    PhoneNumberHolder = cursor.getString(cursor.getColumnIndexOrThrow(UsersSqlite.UserData.COLUMN_3_PHONE_NUMBER));

                    //Close cursor
                    cursor.close();
                }
            }
            handler.close();

            //Call method to check final result
            CheckFinalResult();
        } else {
            //If any login EditText is empty call made to CheckEditTextNotEmpty made and correct message displayed
            Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
        }
    }

    //Method to check that EditText fields are not empty
    private String CheckEditTextNotEmpty() {

        //Get values from fields and stores them in string variable
        String message = "";
        UserNameHolder = UserName.getText().toString().trim();
        PasswordHolder = Password.getText().toString().trim();

        if (UserNameHolder.isEmpty()) {
            UserName.requestFocus();
            EmptyHolder = true;
            message = "Username is empty";
        } else if (PasswordHolder.isEmpty()) {
            Password.requestFocus();
            EmptyHolder = true;
            message = "User Password is empty";
        } else {
            EmptyHolder = false;
        }
        return message;
    }

    //Check that entered password matches with username in database
    public void CheckFinalResult() {
        if (TempPassword.equalsIgnoreCase(PasswordHolder)) {
            Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

            //Sends name to ListActivity using intent
            Bundle bundle = new Bundle();
            bundle.putString("user_name", UserNameHolder);
            bundle.putString("user_phone", PhoneNumberHolder);

            //Send user to ListActivity after login success message
            Intent intent = new Intent(LoginActivity.this, ListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            this.finish();

            //Empty EditText and close database after successful login
            EmptyEditTextAfterDataInsert();
        } else {

            //Display error message if credentials not correct
            Toast.makeText(LoginActivity.this, "Incorrect Username or Password or User Not Registered", Toast.LENGTH_LONG).show();
        }
        TempPassword = "NOT_FOUND";
    }

    //Empty EditText after successful login
    public void EmptyEditTextAfterDataInsert() {
        UserName.getText().clear();
        Password.getText().clear();
    }

    //Popup for forgotten password
    public void forgotPassPopup() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_forgotpass_popup, activity.findViewById(R.id.activity_forgotpass_popup), false);

        //Create window for popup
        popWindow = new PopupWindow(layout, 800, 800, true);
        popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        EditText name = layout.findViewById(R.id.editTextForgotUserName);
        EditText phone = layout.findViewById(R.id.editTextForgotPhone);
        TextView password = layout.findViewById(R.id.textViewPassDisplay);

        //Opening SQLite database write
        db = handler.getWritableDatabase();

        //Search username query added to cursor
        Cursor cursor = db.query(UsersSqlite.UserData.TABLE_NAME, null, " " + UsersSqlite.UserData.COLUMN_1_NAME + "= ?", new String[]{UserNameHolder}, null, null, null);

        while (cursor.moveToNext()) {
            if (cursor.isFirst()) {
                cursor.moveToFirst();

                //Store information associated with username
                UserNameHolder = cursor.getString(cursor.getColumnIndexOrThrow(UsersSqlite.UserData.COLUMN_1_NAME));
                PhoneNumberHolder = cursor.getString(cursor.getColumnIndexOrThrow(UsersSqlite.UserData.COLUMN_3_PHONE_NUMBER));
                TempPassword = cursor.getString(cursor.getColumnIndexOrThrow(UsersSqlite.UserData.COLUMN_2_PASSWORD));

                //Close cursor
                cursor.close();
            }
        }
        handler.close();

        //Retrieve and cancel buttons
        Button retrieve = layout.findViewById(R.id.forgotRetrieveButton);
        Button cancel = layout.findViewById(R.id.forgotCancelButton);

        retrieve.setOnClickListener(view -> {
            String verifyName = name.getText().toString();
            String verifyPhone = phone.getText().toString();

            //To get password username and password must be correct
            if (verifyPhone.equals(PhoneNumberHolder) && verifyName.equals(UserNameHolder)) {
                password.setText(TempPassword);

                new android.os.Handler().postDelayed(() -> popWindow.dismiss(), 3000);
            } else {
                Toast.makeText(activity, "Phone Number or Name is invalid", Toast.LENGTH_LONG).show();
            }
        });

        cancel.setOnClickListener(view -> {
            Toast.makeText(activity, "Forgot password cancelled", Toast.LENGTH_SHORT).show();

            new android.os.Handler().postDelayed(() -> popWindow.dismiss(), 1500);
        });
    }
}