package org.myhouse.FuelManager;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Config extends Dashboard implements OnCheckedChangeListener
{
     /**
      * onCreate - called when activity is first created
      * 
      * onStart() gets called next
      */
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.config);
          
          //get the text fields that won't be shown unless that radio button is selected
          hostname = (EditText)findViewById(R.id.hostname);
          database = (EditText)findViewById(R.id.database);
          
          //check the options
          SharedPreferences settings = getSharedPreferences("settings",Activity.MODE_PRIVATE);
          if(settings.contains("dbase_location"))
          {
               if (settings.getString("dbase_location",null).equals("local"))
               {
                    ((RadioButton)findViewById(R.id.local)).setChecked(true);
                    hostname.setVisibility(View.GONE);
                    database.setVisibility(View.GONE);
               }
               if (settings.getString("dbase_location",null).equals("remote"))
               {
                    ((RadioButton)findViewById(R.id.remote)).setChecked(true);
                    hostname.setVisibility(View.VISIBLE);
                    database.setVisibility(View.VISIBLE);
               }
          }
          else
          {
               hostname.setVisibility(View.GONE);
               database.setVisibility(View.GONE);
          }
          if (settings.contains("hostname"))
          {
               ((EditText)findViewById(R.id.hostname)).setText(settings.getString("hostname",null));
          }
          if (settings.contains("database"))
          {
               ((EditText)findViewById(R.id.database)).setText(settings.getString("database",null));
          }
          
          RadioGroup rg = (RadioGroup) findViewById(R.id.dbase_location);
          rg.setOnCheckedChangeListener(this);
          
     }
     
     /**
      * onDestroy - last call before activity is destroyed
      */
     protected void onDestroy()
     {
          super.onDestroy();
     }
     
     /**
      * onPause - called when the system is about to resume another activity
      */
     protected void onPause()
     {
          super.onPause();
     }
     
     /**
      * onRestart() - called after activity has been stopped, right before its started again
      */
     protected void onRestart()
     {
          super.onRestart();
     }
     
     /**
      * onResume - called when the activity will start user interaction
      */
     protected void onResume()
     {
          super.onResume();
     }
     
     /**
      * onStart - called when activity is becoming visible to user
      */
     protected void onStart()
     {
          super.onStart();
     }
     
     /**
      * onStop - called when activity isn't visible to the user
      */
     protected void onStop()
     {
          super.onStop();
     }
          
     /**
      * what to do when a button gets checked
      */
     public void onCheckedChanged(RadioGroup g,int checkedId)
     {
          RadioButton checked = (RadioButton) findViewById(checkedId);
          switch(checkedId)
          {
               case R.id.local:
                    hostname.setVisibility(View.GONE);
                    database.setVisibility(View.GONE);
               break;
               case R.id.remote:
                    hostname.setVisibility(View.VISIBLE);
                    database.setVisibility(View.VISIBLE);
               break;
               default:
          }
     }

     /**
      * write the changes to the saved preferences
      */
     public void saveChanges(View v)
     {
          SharedPreferences settings = getSharedPreferences("settings",0);
          SharedPreferences.Editor editor = settings.edit();

          RadioGroup rg = (RadioGroup)findViewById(R.id.dbase_location);
          int selectedId = rg.getCheckedRadioButtonId();
          int ok = 1;
          switch (selectedId)
          {
               case R.id.local:
                    editor.putString("dbase_location","local");
                    editor.remove("hostname");
                    editor.remove("database");
                    editor.commit();
                    toast("Your changes were saved!");
               break;
               case R.id.remote:
                    String message ="Please Fill in:\n";
                    if(hostname.getText().toString().equals(""))
                    {
                         message += "hostname\n";
                         ok = 0;
                    }
                    if (database.getText().toString().equals(""))
                    {
                         message += "database";
                         ok = 0;
                    }
                    if (ok == 0)
                    {
                         toast(message);
                    }
                    else
                    {
                         editor.putString("dbase_location","remote");
                         String fixed = "";
                         String h = hostname.getText().toString();
                         if (h.startsWith("http://"))
                              fixed = h;
                         else
                              fixed = "http://" + h;
                         editor.putString("hostname",fixed);
                         editor.putString("database",database.getText().toString());
                         
                         //write out the paths to the remote locations
                         editor.putString("syncvehicle","/connector/get_vehicles.php");
                         editor.putString("uploadmileage","/connector/upload_mileage.php");
                         editor.putString("syncmileage","/connector/get_mileage.php");
                         
                         editor.commit();
                         toast("Your changes were saved!");
                    }
               break;
               default:
          }//end switch
          if (ok != 0)
               finish();
     }//end saveChanges     

     private EditText hostname;
     private EditText database;
     
}//end Config
