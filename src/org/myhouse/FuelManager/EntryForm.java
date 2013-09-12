package org.myhouse.FuelManager;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EntryForm extends Dashboard implements DatePickerDialog.OnDateSetListener
{
     @Override
     /**
          onCreate 
     */
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.entry_form);
          
          vehicleButton = (Button) findViewById(R.id.selectVehicle);
          dateButton = (Button) findViewById(R.id.date);
          statusText = (TextView) findViewById(R.id.status);
          
          //get the saved preference vehicle
          currentVehicle = getSavedVehicle();
          vehicleButton.setText(currentVehicle.getVehicleDescription());
          
          SimpleDateFormat formatted = new SimpleDateFormat("MMMM d, yyyy");
          Date today = new Date();
          dateButton.setText(formatted.format(today));
     }
     
     /** 
      * show the vehicle selection dialog box 
      */
     public void selectVehicle(View v)
     {
          showVehicleDialog(clicker);
     }//end selectVehicle
     
     /**
      *   listener to handle the dialog box click
      */
     private DialogInterface.OnClickListener clicker = new DialogInterface.OnClickListener()
     {
          public void onClick(DialogInterface dialog, int item)
          {
               Vehicle v = names.get(item);
               vehicleButton.setText(v.getVehicleDescription());
               
               //maybe write the new vehicle to the saved preferences?
               saveVehiclePreference(v.getVehicleId());
          }
     };
     
     /**
      * show the date picker dialog box
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
      * what to do when they select a dateButton
      */
     public void onDateSet(DatePicker view, int year, int month, int day)
     {
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
               
          }
     }
     
     /** 
      * save the stuff to the database
      */
     public void saveMileageData(View view)
     {
          //check the options
          EditText m = (EditText) findViewById(R.id.miles);
          EditText g = (EditText) findViewById(R.id.gallons);
          EditText c = (EditText) findViewById(R.id.cost);
          String date = formatInsertDate(dateButton.getText().toString());
          
          String message = "Please Fill out:\n";
          boolean ok = true;

          if (vehicleButton.getText().equals(""))
          {
               message += "vehicle\n";
               ok = false;
          }
          if (m.getText().toString().equals(""))
          {
               message += "miles\n";
               ok = false;
          }
          if (g.getText().toString().equals(""))
          {
               message += "gallons\n";
               ok = false;
          }
          if (c.getText().toString().equals(""))
          {
               message += "cost\n";
               ok = false;
          }
          if (date.equals(""))
          {
               message += "date";
               ok = false;
          }
          if (!ok)
          {
               toast(message);
          }
          else //everything is ok, insert the record
          {
               currentVehicle = getSavedVehicle();
               Mileage md = new Mileage(m.getText().toString(),
                                       g.getText().toString(),
                                       c.getText().toString(),
                                       date,
                                       currentVehicle.getVehicleId()
               );
               long returnVal = dbase.addData(md);
               if (returnVal != -1)
               {
                    //tell them what was inserted
                    statusText.setText("miles: " + m.getText().toString() + ", " +
                                   "gallons: " + g.getText().toString() + ", " +
                                   "cost: " + c.getText().toString() + ", " +
                                   "mpg: " + md.getMpg() + ", " + 
                                   "date: " + dateButton.getText().toString());
                    //clear the fields 
                    m.setText("");
                    g.setText("");
                    c.setText("");
                    m.requestFocus();
               }
               else
               {
                    statusText.setText("Adding record failed!");
               }

          }
     }//end saveMileageData
     
     private Button vehicleButton;
     private Button dateButton;
     private TextView statusText;
     private Vehicle currentVehicle;
     
}//end class EntryForm