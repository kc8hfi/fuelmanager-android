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
import android.webkit.WebView;

public class YearlyStats extends Dashboard
{
     /**
      * onCreate
      */
     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.yearly_stats);
          
          //get the saved vehicle in the preferences
          currentVehicle = getSavedVehicle();
          
          selectVehicleButton = (Button) findViewById(R.id.selectVehicle);
          selectVehicleButton.setText(currentVehicle.getVehicleDescription());
          
          theView = (WebView) findViewById(R.id.myView);
          theView.getSettings().setBuiltInZoomControls(true);
          theView.getSettings().setDisplayZoomControls(false);

          showStats();
     }//end onCreate
     
     /**
      * show the vehicle selection box
      */
     public void selectVehicle(View v)
     {
          showVehicleDialog(clicker);
     }
     
     /**
      * show the statistics
      */
     public void showStats()
     {
          //get the currently saved vehicle in preferences
          currentVehicle = getSavedVehicle();
          
          ArrayList<String> headerNames = new ArrayList<String>();
          headerNames.add("");
          headerNames.add("Fillups");
          headerNames.add("Miles");
          headerNames.add("Gallons");
          headerNames.add("Cost");
          headerNames.add("MPG");
          
          String thepage = beginning;
          
          //add the table column headers
          thepage += "<tr class=\"bgcolor\">";
          for (String s:headerNames)
          {
               thepage += "<th>" + s + "</th>";
          }
          thepage += "</tr>\n";
          
          ArrayList<ArrayList<String>> data = dbase.getYearlyStats(currentVehicle);
          int i = 1;          
          for(ArrayList<String> row:data)
          {
               String r="";
               if ( (i%2) == 0)
               {
                    r += "<tr class=\"bgcolor\">";
               }
               else
               {
                    r += "<tr>";
               }
               //each item in the row goes in td tags
               for(String eachpart:row)
               {
                    r += "<td>" + eachpart + "</td>";
               }
               r += "</tr>\n";
               
               //add the row to the rest now
               thepage += r;
               i++;
          }
          //add the ending now
          thepage += ending;
          
          //clear the page first
          theView.loadUrl("about:blank");
          //put the page onto the webview 
          theView.loadData(thepage, "text/html", null);
          
//           trace (currentVehicle.toString());
//           trace(thepage);
          
     }//end showStats
     
     /**
      * vehicle selection dialog click handler
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
     private ArrayList<String> years;
     
     private WebView theView;
     
}//end YearlyStats