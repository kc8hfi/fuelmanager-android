package org.myhouse.FuelManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddVehicle extends Dashboard
{
     /**
      * onCreate - called when activity is first created
      * 
      * onStart() gets called next
      */
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.add_vehicle);
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
      * save the new vehicle to the local database
      */
     public void saveChanges (View v)
     {
          EditText desc = (EditText) findViewById(R.id.vehicle_description);
          TextView status = (TextView) findViewById(R.id.status);
          
          if (!desc.getText().toString().equals(""))
          {
               long returnVal = dbase.addVehicle(desc.getText().toString());
               if (returnVal >0)
               {
                    //tell them what was added
                    status.setText(desc.getText().toString() + " was added!");
                    
                    //empty the text field
                    desc.setText("");
               }
               else
                    toast("Adding the vehicle failed!");
          }
     }//end saveChanges
     
}//end AddVehicle