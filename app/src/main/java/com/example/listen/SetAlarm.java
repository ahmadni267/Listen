package com.example.listen;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class SetAlarm extends AppCompatActivity{
    Button timeBtnOn, timeBtnOff;
    int hour, minute;
    EditText bhs;
    Button simpan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
        timeBtnOn = findViewById(R.id.timeButtonOn);
        timeBtnOff = findViewById(R.id.timeButtonOff);
        simpan = findViewById(R.id.btnsimpan);
        bhs = findViewById(R.id.dropdownbhs);
        bhs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDropdownMenu();
            }
        });
    }

    private void showDropdownMenu() {
        ListPopupWindow dropDownMenu = new ListPopupWindow(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, getResources().getStringArray(R.array.dropdown_items));
        dropDownMenu.setAdapter(adapter);
        dropDownMenu.setAnchorView(bhs);
        dropDownMenu.setWidth(bhs.getWidth());
        dropDownMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = adapter.getItem(position);
                bhs.setText(selectedItem);
                dropDownMenu.dismiss();
            }
        });
        dropDownMenu.show();
    }

    public void popTimePickerOn(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeBtnOn.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,style, onTimeSetListener, hour, minute,true);
        timePickerDialog.setTitle("Aktif");
        timePickerDialog.show();
    }
    public void popTimePickerOff(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeBtnOff.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));
            }
        };
        int style = AlertDialog.THEME_HOLO_DARK;
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,style, onTimeSetListener, hour, minute,true);
        timePickerDialog.setTitle("Aktif");
        timePickerDialog.show();
    }
}