package org.myhouse.FuelManager;

import android.database.Cursor;
import android.os.Bundle;

/**
 * A vehicle object
 */
public class Vehicle
{
     public Vehicle()
     {
          vehicleId = -1;
          vehicleDescription = "";
     }
     
     public Vehicle(Cursor c)
     {
          vehicleId = c.getInt(0);
          vehicleDescription = c.getString(1);
     }
     
     public Vehicle(Bundle b)
     {
          vehicleId = b.getInt("id");
          vehicleDescription = b.getString("description");
     }     
     
     public String toString()
     {
          String v = "";
          v += "id: '" + Integer.toString(vehicleId) + "' ";
          v += "desc: '" + vehicleDescription + "' ";
          return v;
     }
     
     public Bundle toBundle()
     {
          Bundle b = new Bundle();
          b.putInt("id",vehicleId);
          b.putString("description",vehicleDescription);
          return b;
     }
     
     public int getVehicleId()
     {
          return vehicleId;
     }
     public String getVehicleDescription()
     {
          return vehicleDescription;
     }
     
     public void setVehicleId(int i)
     {
          vehicleId = i;
     }
     
     public void setVehicleDescription(String s)
     {
          vehicleDescription = s;
     }
     
//      public String getVehicleImage()
//      {
//           return iconFile;
//      }
//      public void setVehicleImage(String i)
//      {
//           iconFile = i;
//      }
          
//      private String iconFile = "ic_pump.png";
     private int vehicleId;
     private String vehicleDescription;
     
}//end Vehicle