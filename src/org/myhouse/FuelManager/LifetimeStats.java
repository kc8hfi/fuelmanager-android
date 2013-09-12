package org.myhouse.FuelManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import java.util.ArrayList;

public class LifetimeStats extends Dashboard
{

     /**
      * onCreate
      */
     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.lifetime_stats);
          
          //get the saved vehicle in the preferences
          currentVehicle = getSavedVehicle();
          
          selectVehicleButton = (Button) findViewById(R.id.selectVehicle);
          selectVehicleButton.setText(currentVehicle.getVehicleDescription());

          scroller = (TableLayout) findViewById(R.id.scroll);
          
          tableRowParams = new TableLayout.LayoutParams
                                                  (TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT);
          tableRowParams.setMargins(2,2,2,2);
          
          firstRowMargin = new TableRow.LayoutParams(
               TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
          //left, top, right ,bottom)
          firstRowMargin.setMargins(2,2,0,0);
          
          insideRowMargin = new TableRow.LayoutParams(
               TableRow.LayoutParams.WRAP_CONTENT,TableRow.LayoutParams.WRAP_CONTENT);
          //left, top, right ,bottom)
          insideRowMargin.setMargins(2,0,0,0);
          
          showStats();
     }//end onCreate
     
     /**
      * show the vehicle selection
      */
     public void selectVehicle(View v)
     {
          showVehicleDialog(clicker);
     }
     
     /**
      * show the stats
      */
     public void showStats()
     {
          //get the currently saved vehicle in preferences
          currentVehicle = getSavedVehicle();
          
          //empty the table
          scroller.removeAllViews();
          
          //column headers
          ArrayList<String> headerNames = new ArrayList<String>();
          headerNames.add("Fillups");
          headerNames.add("Miles");
          headerNames.add("Gallons");
          headerNames.add("Cost");
          headerNames.add("MPG");
          
          TableRow headerRow = new TableRow(this);
          headerRow.setLayoutParams(tableRowParams);
          
          TextView headerv = new TextView(this);
          // headerv.setLayoutParams(firstRowMargin);
          // headerv.setPadding(3,0,3,0);
          // headerv.setText("");
          // headerv.setBackgroundResource(R.color.alternate);
          // headerRow.addView(headerv);
          for(String header:headerNames)
          {
               headerv = new TextView(this);
               headerv.setLayoutParams(firstRowMargin);
               headerv.setPadding(3,0,3,0);
               headerv.setText(header);
               headerv.setTextColor(Color.BLACK);
               headerv.setBackgroundResource(R.color.alternate);
               headerRow.addView(headerv);
          }
          scroller.addView(headerRow);
          
          ArrayList<String> data = dbase.getLifetimeStats(currentVehicle);
          
          TableRow row = new TableRow(this);
          int i = 0;          
          for(String r:data)
          {
               row.setLayoutParams(tableRowParams);

               TextView monthyear = new TextView(this);
               monthyear.setLayoutParams(insideRowMargin);

               //left, top, right, bottom)
               monthyear.setPadding(3,0,3,1);
               monthyear.setTextColor(Color.BLACK);
               monthyear.setText(r);
               
               monthyear.setBackgroundResource(R.color.white);
               row.addView(monthyear);
               
               i++;
          }//end loop
          scroller.addView(row);
     }
     
     /**
      * what to do when they select a vehicle
      */
     private DialogInterface.OnClickListener clicker = new DialogInterface.OnClickListener()
     {
          public void onClick(DialogInterface dialog, int item)
          {
               Vehicle v = names.get(item);
               selectVehicleButton.setText(v.getVehicleDescription());
               
               //maybe write the new vehicle to the saved preferences?
               saveVehiclePreference(v.getVehicleId());
               
               //display the table with the new results
               showStats();
          }
     };
     
     private Button selectVehicleButton;
     private Vehicle currentVehicle;
     private TableLayout scroller;
     private TableLayout.LayoutParams tableRowParams;
     private TableRow.LayoutParams firstRowMargin;
     private TableRow.LayoutParams insideRowMargin;
     private ArrayList<String> years;
     
}//end LifetimeStats
