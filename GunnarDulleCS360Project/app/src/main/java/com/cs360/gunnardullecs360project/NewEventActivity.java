package com.cs360.gunnardullecs360project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicReference;

//Class for adding events
public class NewEventActivity extends AppCompatActivity {

    String userNameHolder, DateHolder, TimeHolder, DescHolder;
    EditText DateValue, TimeValue, DescValue;
    Button AddEventButton, CancelButton;
    Boolean EmptyHolder;
    EventsSQLite db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        //Initiate buttons, editText, and database
        DateValue = findViewById(R.id.editNewEventDate);
        TimeValue = findViewById(R.id.editNewEventTime);
        DescValue = findViewById(R.id.editNewEventDesc);
        AddEventButton = findViewById(R.id.addEventButton);
        CancelButton = findViewById(R.id.cancelAddEventButton);
        db = new EventsSQLite(this);

        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());

        //Receiving username from ListActivity
        userNameHolder = intent.get().getStringExtra(ListActivity.UserName);

        //Click listener for CancelButton
        CancelButton.setOnClickListener(view -> {
            //Return to ListActivity after cancel
            Intent add = new Intent();
            setResult(0, add);
            this.finish();
        });

        //Click listener for addEventButton, passes data to ListActivity
        AddEventButton.setOnClickListener(view -> InsertEventIntoDatabase());
    }

    //Add event to database and send data to ListActivity
    public void InsertEventIntoDatabase() {
        String message = CheckEditTextNotEmpty();

        if (!EmptyHolder) {
            String date = DateHolder;
            String time = TimeHolder;
            String description = DescHolder;

            Event event = new Event(date, time, description);
            db.createEvent(event);

            //Display message after added to database
            Toast.makeText(this, "Event has been added", Toast.LENGTH_LONG).show();

            //Close NewEventActivity
            Intent add = new Intent();
            setResult(RESULT_OK, add);
            this.finish();
        } else {
            //Display message if item description empty and focus the field
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    //Check that event description not empty
    public String CheckEditTextNotEmpty() {
        //Get value from fields and store in string variable
        String message = "";
        DateHolder = DateValue.getText().toString().trim();
        TimeHolder = TimeValue.getText().toString().trim();
        DescHolder = DescValue.getText().toString().trim();

        //Check if fields are empty
        if (DateHolder.isEmpty()) {
            DateValue.requestFocus();
            EmptyHolder = true;
            message = "Event \"Date\" is empty";
        } else if (TimeHolder.isEmpty()) {
            TimeValue.requestFocus();
            EmptyHolder = true;
            message = "Event \"Time\" is empty";
        } else if (DescHolder.isEmpty()) {
            DescValue.requestFocus();
            EmptyHolder = true;
            message = "Event \"Description\" is empty";
        } else {
            EmptyHolder = false;
        }
        return message;
    }
}