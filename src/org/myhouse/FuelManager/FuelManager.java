package org.myhouse.FuelManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FuelManager extends Dashboard
{
     /**
      * onCreate - called when activity is first created
      * 
      * onStart() gets called next
      */
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.main);
          
          //check the options
          SharedPreferences settings = getSharedPreferences("settings",Activity.MODE_PRIVATE);
          //if  they haven't configured anything, show the config activity
          if(!settings.contains("dbase_location"))
          {
               startActivity(new Intent(getApplicationContext(), Config.class));
          }
     }//end onCreate
     
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
     
}//end class FuelManager
