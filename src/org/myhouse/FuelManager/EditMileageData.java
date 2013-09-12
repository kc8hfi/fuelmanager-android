package org.myhouse.FuelManager;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditMileageData extends Dashboard
{
     protected Object mActionMode;
     public int selectedItem = -1;
     
     /** Called when the activity is first created. */
     @Override
     public void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.show_edit_data);
          
          //get the saved preferences vehicle
          vehicle = getSavedVehicle();
          
          //get the mileage info for the vehicle
          values = new ArrayList<Mileage>();
          values = dbase.getMileage(vehicle.getVehicleId());
          
          //set the button's title
          vehicleButton = (Button) findViewById(R.id.selectVehicle);
          vehicleButton.setText(vehicle.getVehicleDescription());
          
          //set up the adapter for the listview
          adapter = new MyPerformanceBaseAdapter(this,values);
          
          listView = (ListView) findViewById(android.R.id.list);
          
          listView.setOnItemLongClickListener(new OnItemLongClickListener()
          {
               public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
               {
                    if(mActionMode != null)
                    {
                         return false;
                    }
                    if(position != 0)   //0 is the column headers, don't mess with them
                    {
                         selectedItem = position;
                         ViewHolder item = (ViewHolder)view.getTag();
                         
                         //send the clicked item to another activity to be edited ( or not edited)
                         Intent intent = new Intent(getBaseContext(),EditRecord.class);
                         Bundle b = new Bundle();
                         Mileage mitem = values.get(selectedItem-1);
                         b = mitem.toBundle();
                         intent.putExtras(b);
                         startActivityForResult(intent,EDIT_MILEAGE_DATA);
                         adapter.notifyDataSetChanged();
                    }
                    return true;
               }
          });
          
//           listView.setOnItemClickListener(new OnItemClickListener()
//           {
//                public void onItemClick(AdapterView<?> parent, View v,int pos,long id)
//                {
//                     //leave the column header alone, which is position 0
//                     if(pos !=0)
//                     {
//                          v.setBackgroundResource(R.color.red);
//                     }
//                }
//           });

          listView.setOnItemSelectedListener(new OnItemSelectedListener()
          {
               public void onItemSelected(AdapterView<?> parent, View v,int position,long id)
               {
                    //leave the column header alone, which is position 0
//                     if(position !=0)
//                     {
//                          if ( (position %2) == 0)
//                          {
//                               v.setBackgroundResource(R.color.white);
//                          }
//                          else
//                          {
//                               v.setBackgroundResource(R.color.alternate);
//                          }
//                     }
               }
               public void onNothingSelected(AdapterView<?> parent)
               {
//                     if(pos !=0)
//                     {
//                          v.setBackgroundResource(R.color.white);
//                     }
               }
          });

          
          View header = getLayoutInflater().inflate(R.layout.show_edit_data_headers,null);
          
          //set the background color
          listView.setBackgroundColor(Color.WHITE);
          
          //add a header row
          listView.addHeaderView(header);
          listView.setAdapter(adapter);
     }
     
     /** 
      * show the vehicle selection dialog box 
      */
     public void selectVehicle(View v)
     {
          showVehicleDialog(clicker);
     }
     
     /**
      * if they edited the record, update the dataset
      */
     @Override
     protected void onActivityResult(int requestCode,int resultCode,Intent data)
     {
          if(requestCode == EDIT_MILEAGE_DATA)
          {
               if(resultCode == RESULT_OK)
               {
                    Bundle b = data.getExtras();
                    Mileage item = new Mileage(b);
                    //find the id in the current array and replace it with item
                    for(int i=0;i<values.size();i++)
                    {
                         Mileage t = values.get(i);
                         if(t.getId() == item.getId())
                         {
                              //update this one
                              t.setMiles(item.getMiles());
                              t.setGallons(item.getGallons());
                              t.setCost(item.getCost());
                              t.setMpg(item.getMpg());
                              t.setDate(item.getDate());
                              break;
                         }
                    }
                    //update the view
                    adapter.notifyDataSetChanged();
               }
          }
     }//end onActivityResult
     
     /**
      * click handler for the listview
      */
     private DialogInterface.OnClickListener clicker = new DialogInterface.OnClickListener()
     {
          public void onClick(DialogInterface dialog, int item)
          {
               vehicle = names.get(item);
               vehicleButton.setText(vehicle.getVehicleDescription());
               
               //maybe write the new vehicle to the saved preferences?
               saveVehiclePreference(vehicle.getVehicleId());
               
               //refresh the list of data
               values.clear();
               values.addAll(dbase.getMileage(vehicle.getVehicleId()));
               adapter.notifyDataSetChanged();
          }
     };   

     
     private Vehicle vehicle;
     private ArrayList<Mileage> values;  
     private MyPerformanceBaseAdapter adapter;
     private Button vehicleButton;
     private ListView listView;
     
     private static final int EDIT_MILEAGE_DATA = 1;
}
