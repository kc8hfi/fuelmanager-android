package org.myhouse.FuelManager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditRecord extends Dashboard implements DatePickerDialog.OnDateSetListener
{
     /**
      * onCreate - called when activity is first created
      */
     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.show_edit_record);
          
          //get the item from the intent
          Intent intent = getIntent();
          Bundle b = intent.getExtras();

          //make an item from the bundle of extra stuff
          item = new Mileage(b);
          
          miles = (EditText) findViewById(R.id.miles);
          miles.setText(item.getMiles());
          
          gallons = (EditText) findViewById(R.id.gallons);
          gallons.setText(item.getGallons());
          
          cost = (EditText) findViewById(R.id.cost);
          cost.setText(item.getCost());
          
          dateButton = (Button) findViewById(R.id.date);
          dateButton.setText(formatDisplayDate(item.getDate()));
     }
     
     /**
      * set up the date
      */
     @Override
     public void onDateSet(DatePicker view, int year, int month, int day)
     {
          //do something with the selected date
          String r = "";
          int m = month + 1;
          if (m < 10)
               r = r + "0" + Integer.toString(m);
          else
               r = r + Integer.toString(m);
          if(day < 10)
               r = r + "0" + Integer.toString(day);
          else
               r = r + Integer.toString(day);
          r = r + Integer.toString(year);
          try
          {
               SimpleDateFormat df = new SimpleDateFormat("MMddyyyy");
               Date nd = df.parse(r);
               df = new SimpleDateFormat("MMMM d, yyyy");
               String t = df.format(nd);
               dateButton.setText(t);
          }
          catch(ParseException pe)
          {
               toast("Edit record bad date!");               
          }
     }
     
     /**
      * Show the date picker dialog box
      */
     public void showDatePicker(View v)
     {
          final Calendar c = Calendar.getInstance();
          int year = c.get(Calendar.YEAR);
          int month = c.get(Calendar.MONTH);
          int day = c.get(Calendar.DAY_OF_MONTH);
          
          DatePickerDialog picker = new DatePickerDialog(this, this, year, month, day);
          picker.show();
     }
     
     /**
      * Save their changes to the database
      */
     public void saveChanges(View v)
     {
          String date = formatInsertDate(dateButton.getText().toString());
          String message = "Please Fill out:\n";
          int ok = 0;

          if (miles.getText().toString().equals(""))
          {
               message += "miles\n";
               ok = 1;
          }
          if (gallons.getText().toString().equals(""))
          {
               message += "gallons\n";
               ok = 1;
          }
          if (cost.getText().toString().equals(""))
          {
               message += "cost\n";
               ok = 1;
          }
          if (date.equals(""))
          {
               message += "date";
               ok = 1;
          }
          if (ok == 1)
          {
               toast(message);
          }
          else //everything is ok, update the record
          {
               //fix the Mileage item
               item.setMiles(miles.getText().toString());
               item.setGallons(gallons.getText().toString());
               item.setCost(cost.getText().toString());
               if (Double.parseDouble(gallons.getText().toString()) != 0)
               {
                    Double t = Double.parseDouble(miles.getText().toString()) / Double.parseDouble(gallons.getText().toString());
                    item.setMpg(String.format("%.2f",t));
               }
               else
               {
                    item.setMpg("0");
               }
               item.setDate(date);

               int returnVal = dbase.updateRecord(item);
               if (returnVal != -1)
               {
                    Intent result = new Intent();
                    Bundle b = new Bundle();
                    b = item.toBundle();
                    result.putExtras(b);
                    setResult(RESULT_OK,result);
               }
          }
          finish();
     }

     /** 
      * don't do anything, just close
      */
     public void cancelChanges(View v)
     {
          setResult(RESULT_CANCELED);
          finish();
     }
     
     private Mileage item;
     private EditText miles;
     private EditText gallons;
     private EditText cost;
     private Button dateButton;
     
}//end class EditRecord
