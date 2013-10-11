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
          
          theView = (WebView) findViewById(R.id.myView);
          theView.getSettings().setBuiltInZoomControls(true);
          theView.getSettings().setDisplayZoomControls(false);

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
          
          ArrayList<String> headerNames = new ArrayList<String>();
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


          ArrayList<String> data = dbase.getLifetimeStats(currentVehicle);
          String r = "<tr>";
          for(String item:data)
          {
               r += "<td>" + item + "</td>";
          }
          r += "</tr>\n";
          
          //add the row to the table
          thepage += r;
          
          //add the ending now
          thepage += ending;
          
          //clear the page first
          theView.loadUrl("about:blank");
          //put the page onto the webview 
          theView.loadData(thepage, "text/html", null);
          
     }//end showStats
     
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
     private ArrayList<String> years;
     private WebView theView;
     
}//end LifetimeStats
