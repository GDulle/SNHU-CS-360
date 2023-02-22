package com.cs360.gunnardullecs360project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

//ListActivity to display events, as well as add and delete events from the database
//Also where SMS functions are, this is the main screen of the app

public class ListActivity extends AppCompatActivity {

    public static final String UserName = "";
    private static final int USER_PERMISSIONS_REQUEST_SEND_SMS = 0;
    static String NameHolder, PhoneNumberHolder;
    private static boolean smsAuthorized = false;
    private static boolean deleteEvents = false;
    ImageButton ButtonAddEvent, ButtonSMS, ButtonDeleteEvent;
    ListView trackerListView;
    EventsSQLite db;
    AlertDialog AlertDialog = null;
    ArrayList<Event> events;
    CustomEventsList customEventsList;
    int eventsCount;

    //Receive and evaluate user response to AlertDialog to delete events
    public static void YesDeleteEvents() {
        deleteEvents = true;
    }

    public static void NoDeleteEvents() {
        deleteEvents = false;
    }

    //Receive adn evaluate user response to AlertDialog to send SMS
    public static void AllowSMS() {
        smsAuthorized = true;
    }

    public static void DenySMS() {
        smsAuthorized = false;
    }

    public static void SendSMSMessage(Context context) {
        String PhoneNum = PhoneNumberHolder;
        String smsMsg = "Upcoming event, check Event Tracker for more info.";

        //Check AlertDialog permission to send SMS
        if (smsAuthorized) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(PhoneNum, null, smsMsg, null, null);
                Toast.makeText(context, "SMS sent", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                Toast.makeText(context, "Device permission denied", Toast.LENGTH_LONG).show();
                ex.printStackTrace();
            }
        } else {
            Toast.makeText(context, "SMS alert disabled", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Initiate buttons, textViews, and extTexts
        ButtonDeleteEvent = findViewById(R.id.buttonDeleteEvent);
        ButtonAddEvent = findViewById(R.id.buttonAddEvent);
        ButtonSMS = findViewById(R.id.buttonTextMessage);
        trackerListView = findViewById(R.id.eventListView);
        db = new EventsSQLite(this);

        //Receive username and phone number from LoginActivity
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            NameHolder = bundle.getString("user_name");
            PhoneNumberHolder = bundle.getString("user_phone");
        }

        events = (ArrayList<Event>) db.getAllEvents();

        eventsCount = db.getEventsCount();

        if (eventsCount > 0) {
            customEventsList = new CustomEventsList(this, events, db);
            trackerListView.setAdapter(customEventsList);
        } else {
            Toast.makeText(this, "Database empty", Toast.LENGTH_LONG).show();
        }

        //Click listener for ButtonDeleteEvent
        ButtonDeleteEvent.setOnClickListener(view -> {
            eventsCount = db.getEventsCount();

            if (eventsCount > 0) {
                //Display Delete Alert Dialog
                AlertDialog = AlertDeleteAllEvents.doubleButton(this);
                AlertDialog.show();

                AlertDialog.setCancelable(true);
                AlertDialog.setOnCancelListener(dialog -> DeleteAllEvents());
            } else {
                Toast.makeText(this, "Database is empty", Toast.LENGTH_LONG).show();
            }
        });

        //Click listener for ButtonAddEvent
        ButtonAddEvent.setOnClickListener(view -> {
            //Opening NewEventActivity
            Intent add = new Intent(this, NewEventActivity.class);
            add.putExtra(UserName, NameHolder);
            startActivityForResult(add, 1);
        });

        //Click listener for ButtonSMS
        ButtonSMS.setOnClickListener(view -> {
            //Request SMS permissions for device
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.SEND_SMS)) {
                    Toast.makeText(this, "SMS permission is required", Toast.LENGTH_LONG).show();
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.SEND_SMS},
                            USER_PERMISSIONS_REQUEST_SEND_SMS);
                }
            } else {
                Toast.makeText(this, "Device SMS permission is allowed", Toast.LENGTH_LONG).show();
            }
            //Open SMS Alert Dialog
            AlertDialog = AlertSMSNotification.doubleButton(this);
            AlertDialog.show();
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                eventsCount = db.getEventsCount();

                if (customEventsList == null) {
                    customEventsList = new CustomEventsList(this, events, db);
                    trackerListView.setAdapter(customEventsList);
                }

                customEventsList.events = (ArrayList<Event>) db.getAllEvents();
                ((BaseAdapter) trackerListView.getAdapter()).notifyDataSetChanged();
            } else {
                Toast.makeText(this, "An event has been cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void DeleteAllEvents() {
        if (deleteEvents) {
            db.deleteAllEvents();
            Toast.makeText(this, "All events were deleted", Toast.LENGTH_SHORT).show();

            if (customEventsList == null) {
                customEventsList = new CustomEventsList(this, events, db);
                trackerListView.setAdapter(customEventsList);
            }

            customEventsList.events = (ArrayList<Event>) db.getAllEvents();
            ((BaseAdapter) trackerListView.getAdapter()).notifyDataSetChanged();
        }
    }
}