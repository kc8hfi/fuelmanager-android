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

public class MonthlyStats extends Dashboard
{
     /**
      * onCreate 
      */
     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.monthly_stats);
          
          //get the saved vehicle in the preferences
          currentVehicle = getSavedVehicle();
          
          selectVehicleButton = (Button) findViewById(R.id.selectVehicle);
          selectVehicleButton.setText(currentVehicle.getVehicleDescription());

          months = new String[]{
               "January","February","March","April","May","June","July","August","September","October","November","December"
          };

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
      * show the vehicle selection dialog
      */
     public void selectVehicle(View v)
     {
          showVehicleDialog(clicker);
     }
     
     /**
      * display the statistics
      */
     public void showStats()
     {
          //get the currently saved vehicle in preferences
          currentVehicle = getSavedVehicle();
          
          //empty the table
          scroller.removeAllViews();
          
          //get a new list of all the years
          years = dbase.getYears(currentVehicle);
          
          TableRow headerRow = new TableRow(this);
          headerRow.setLayoutParams(tableRowParams);
          
          TextView headerv = new TextView(this);
          headerv.setLayoutParams(firstRowMargin);
          headerv.setPadding(3,0,3,0);
          headerv.setText("");
          headerv.setBackgroundResource(R.color.alternate);
          headerRow.addView(headerv);
          for(String year:years)
          {
               headerv = new TextView(this);
               headerv.setLayoutParams(firstRowMargin);
               headerv.setPadding(3,0,3,0);
               headerv.setText(year);
               headerv.setTextColor(Color.BLACK);
               headerv.setBackgroundResource(R.color.alternate);
               headerRow.addView(headerv);
          }
          scroller.addView(headerRow);
          
          ArrayList<ArrayList<String>> data = dbase.getMonthlyStats(currentVehicle);
          int i = 0;          
          for(ArrayList<String> r:data)
          {
               TableRow row = new TableRow(this);
               row.setLayoutParams(tableRowParams);

               TextView text = new TextView(this);
               if (i == 0)
                    text.setLayoutParams(firstRowMargin);
               if (i <= data.size()-1)
                    text.setLayoutParams(insideRowMargin);
               text.setPadding(3,0,3,0);
               text.setTextColor(Color.BLACK);
               text.setText(months[i]+"\n\n\n\n");
               //alternate the row color
               if ((i % 2) == 0)
               {
                    text.setBackgroundResource(R.color.white);
               }
               else
               {
                    text.setBackgroundResource(R.color.alternate);
               }
               
               row.addView(text);
               for(String eachpart:r)
               {
                    TextView monthyear = new TextView(this);
                    if (i == 0)
                         monthyear.setLayoutParams(firstRowMargin);
                    if (i <= data.size()-1)
                         monthyear.setLayoutParams(insideRowMargin);

                    //left, top, right, bottom)
                    monthyear.setPadding(3,0,3,1);
                    monthyear.setTextColor(Color.BLACK);
                    monthyear.setText(eachpart);
                    
                    if ((i % 2) == 0)
                    {
                         monthyear.setBackgroundResource(R.color.white);
                    }
                    else
                    {
                         monthyear.setBackgroundResource(R.color.alternate);
                    }
                    row.addView(monthyear);
               }
               scroller.addView(row);
               i++;
          }
     }//end showStats
     
     /**
      * clicker handler for the vehicle selection dialog box
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
     private String months[];
     private TableLayout scroller;
     private TableLayout.LayoutParams tableRowParams;
     private TableRow.LayoutParams firstRowMargin;
     private TableRow.LayoutParams insideRowMargin;
     private ArrayList<String> years;
     
}//end MonthlyStats