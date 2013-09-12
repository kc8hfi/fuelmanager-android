package org.myhouse.FuelManager;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

// public class EditVehicle extends ListActivity
public class EditVehicle extends Dashboard
{
     /**
      * onCreate - called when activity is first created
      * 
      * onStart() gets called next
      */
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.vehicle_list);
          
          ListView list = (ListView)findViewById(R.id.list);
          
          list.setOnItemLongClickListener(new OnItemLongClickListener()
          {
               public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
               {
                    if(mActionMode != null)
                    {
                         return false;
                    }
                    Vehicle v = names.get(position);
                    Log.d("vehicle: " , v.toString());
                    Bundle b = v.toBundle();
                    Intent intent = new Intent(getBaseContext(),EditVehicleForm.class);
                    intent.putExtras(b);
                    startActivityForResult(intent,1);
                    return true;
               }
          });
          
          //refresh the list of vehicles first
          refreshVehicleList();
          
          //set the adapter to this listview
          list.setAdapter(adapter);
     }//end onCreate
     
     /**
      * after the edit vehicle form gets finished, do this
      */
     @Override
     protected void onActivityResult(int requestCode,int resultCode,Intent data)
     {
//           Log.d("request code",Integer.toString(requestCode));
          if(requestCode == 1)
          {
               if(resultCode == RESULT_OK)
               {
                    Bundle b = data.getExtras();
                    Vehicle item = new Vehicle(b);
                    //find the id in the current array and replace it with item
                    for(int i=0;i<names.size();i++)
                    {
                         Vehicle t = names.get(i);
                         if(t.getVehicleId() == item.getVehicleId())
                         {
                              //update this one
                              t.setVehicleDescription(item.getVehicleDescription());
                              break;
                         }
                    }
                    //update the view
                    adapter.notifyDataSetChanged();
               }
          }
     }//end onActivityResult     
     
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
     
     protected Object mActionMode;
     
}//end EditVehicle
