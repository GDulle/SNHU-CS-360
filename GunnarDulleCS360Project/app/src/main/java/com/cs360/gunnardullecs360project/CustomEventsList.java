package com.cs360.gunnardullecs360project;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

//CustomEventsList adds event rows for ListActivity

public class CustomEventsList extends BaseAdapter {

    private final Activity context;
    ArrayList<Event> events;
    EventsSQLite db;
    private PopupWindow popWindow;

    public CustomEventsList(Activity context, ArrayList<Event> events, EventsSQLite db) {
        this.context = context;
        this.events = events;
        this.db = db;
    }

    public static class ViewHolder {
        TextView textViewEventDate;
        TextView textViewEventTime;
        TextView textViewEventDescription;
        ImageButton ButtonEdit;
        ImageButton ButtonDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;

        if (convertView == null) {
            vh = new ViewHolder();
            row = inflater.inflate(R.layout.activity_tracker_template, null, true);

            vh.ButtonEdit = row.findViewById(R.id.buttonEditEvent);
            vh.textViewEventDate = row.findViewById(R.id.textViewEventDate);
            vh.textViewEventTime = row.findViewById(R.id.textViewEventTime);
            vh.textViewEventDescription = row.findViewById(R.id.textViewEventDescription);
            vh.ButtonDelete = row.findViewById(R.id.buttonDeleteEvent);

            row.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.textViewEventDate.setText(events.get(position).getDate());
        vh.textViewEventTime.setText(events.get(position).getTime());
        vh.textViewEventDescription.setText(events.get(position).getDescription());

        //Send SMS if value equals 0
        String value = vh.textViewEventTime.getText().toString().trim();
        if (value.equals("0")) {
            ListActivity.SendSMSMessage(context.getApplicationContext());
        }

        final int positionPopup = position;

        //Edit button listener
        vh.ButtonEdit.setOnClickListener(view -> editPopup(positionPopup));

        //Delete button listener
        vh.ButtonDelete.setOnClickListener(view -> {
            db.deleteEvent(events.get(positionPopup));

            events = (ArrayList<Event>) db.getAllEvents();
            notifyDataSetChanged();

            Toast.makeText(context, "Event has been deleted", Toast.LENGTH_SHORT).show();
        });

        return row;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public int getCount() {
        return events.size();
    }

    public void editPopup(final int positonPopup) {
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_editevent_popup, context.findViewById(R.id.activity_editevent_popup));

        popWindow = new PopupWindow(layout, 800, 800, true);
        popWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        final EditText editTextEditDate = layout.findViewById(R.id.editTextEditDate);
        final EditText editTextEditTime = layout.findViewById(R.id.editTextEditTime);
        final EditText editTextEditDesc = layout.findViewById(R.id.editTextEditDescription);

        editTextEditDate.setText(events.get(positonPopup).getDate());
        editTextEditTime.setText(events.get(positonPopup).getTime());
        editTextEditDesc.setText(events.get(positonPopup).getDescription());

        Button confirm = layout.findViewById(R.id.editEventConfirmButton);
        Button cancel = layout.findViewById(R.id.editEventCancelButton);

        //Confirm event editing
        confirm.setOnClickListener(view -> {
            String eventDate = editTextEditDate.getText().toString();
            String eventTime = editTextEditTime.getText().toString();
            String eventDescription = editTextEditDesc.getText().toString();

            Event event = events.get(positonPopup);
            event.setDate(eventDate);
            event.setTime(eventTime);
            event.setDescription(eventDescription);

            db.updateEvent(event);
            events = (ArrayList<Event>) db.getAllEvents();
            notifyDataSetChanged();

            Toast.makeText(context, "Event updated", Toast.LENGTH_SHORT).show();

            popWindow.dismiss();
        });

        //Cancel editing
        cancel.setOnClickListener(view -> {
            Toast.makeText(context, "Edit cancelled", Toast.LENGTH_SHORT).show();
            popWindow.dismiss();
        });
    }
}
