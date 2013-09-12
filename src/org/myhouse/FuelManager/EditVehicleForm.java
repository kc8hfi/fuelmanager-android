package org.myhouse.FuelManager;

import android.os.Bundle;
import android.content.Intent;
import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;


public class EditVehicleForm extends Dashboard
{
     @Override
     /**
          onCreate 
     */
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.edit_vehicle);

          //get the item from the intent
          Intent intent = getIntent();
          Bundle b = intent.getExtras();
          
          item = new Vehicle(b);
          
          description = (EditText) findViewById(R.id.edit_vehicle_description);
          description.setText(item.getVehicleDescription());
          
     }
     
     public void saveChanges(View v)
     {
          String message = "Please Fill out:\n";
          boolean ok = true;

          if (description.getText().toString().equals(""))
          {
               message += "description\n";
               ok = false;
          }
          if (!ok)
          {
               toast(message);
          }
          else //everything is ok, update the record
          {
               //fix the item
               item.setVehicleDescription(description.getText().toString());
               
               //get the vehicle id from its description
               int returnVal = dbase.updateVehicle(item);
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
     }//end saveChanges
     
     public void cancelChanges(View v)
     {
          setResult(RESULT_CANCELED);
          finish();
     }
     
     
     private Vehicle item;
     private EditText description;
}//end EditVehicleForm