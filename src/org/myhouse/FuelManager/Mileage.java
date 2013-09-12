package org.myhouse.FuelManager;

import android.widget.TextView;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * object to hold a single record
 */
public class Mileage
{
     public Mileage()
     {
          id = -1;
          miles = "";
          gallons = "";
          cost = "";
          mpg = "";
          date = "";
          vehicle_id = -1;
     }

     public Mileage(int i, String m, String g, String c, String d, int vid)
     {
          id = i;
          miles = m;
          gallons = g;
          cost = c;
          if (Double.parseDouble(gallons) != 0)
          {
               Double t = Double.parseDouble(miles) / Double.parseDouble(gallons);
               mpg = String.format("%.2f",t);
          }
          else
          {
               mpg = "0";
          }
          date = d;
          vehicle_id = vid;
     }

     public Mileage(String m, String g, String c, String d, int vid)
     {
          id = -1;
          miles = m;
          gallons = g;
          cost = c;
          if (Double.parseDouble(gallons) != 0)
          {
               Double t = Double.parseDouble(miles) / Double.parseDouble(gallons);
               mpg = String.format("%.2f",t);
          }
          else
          {
               mpg = "0";
          }
          date = d;
          vehicle_id = vid;
     }
     
     
     public Mileage(Bundle b)
     {
          id = b.getInt("id");
          miles = b.getString("miles");
          gallons = b.getString("gallons");
          cost = b.getString("cost");
          if (Double.parseDouble(gallons) != 0)
          {
               Double t = Double.parseDouble(miles) / Double.parseDouble(gallons);
               mpg = String.format("%.2f",t);
          }
          else
          {
               mpg = "0";
          }
          date = b.getString("date");
          vehicle_id = b.getInt("vehicle_id");
     }
     
     public Mileage(Cursor c)
     {
          id = c.getInt(0);
          miles = c.getString(1);
          gallons = c.getString(2);
          cost = c.getString(3);
          if (Double.parseDouble(gallons) != 0)
          {
               Double t = Double.parseDouble(miles) / Double.parseDouble(gallons);
               mpg = String.format("%.2f",t);
          }
          else
          {
               mpg = "0";
          }
          date = c.getString(4);
          vehicle_id = c.getInt(5);
     }

     public String toString()
     {
          String s = "";
          s += "id: '" + Integer.toString(id) + "' ";
          s += "miles: '" + miles + "' ";
          s += "gallons: '" + gallons + "' ";
          s += "cost: '" + cost + "' ";
          s += "mpg: '" + mpg + "' ";
          s += "date: '" + date + "' ";
          s += "vehicle_id: ' " + Integer.toString(vehicle_id) + "' ";
          return s;
     }

     public Bundle toBundle()
     {
          Bundle b = new Bundle();
          b.putInt("id",id);
          b.putString("miles",miles);
          b.putString("gallons",gallons);
          b.putString("cost",cost);
          b.putString("date",date);
          b.putString("mpg",mpg);
          b.putInt("vehicle_id",vehicle_id);
          return b;
     }
     
     public int getId()
     {
          return id;
     }
     
     public String getMiles()
     {
          return miles;
     }
     
     public String getGallons()
     {
          return gallons;
     }
     
     public String getCost()
     {
          return cost;
     }
     
     public String getDate()
     {
          return date;
     }
     
     public String getMpg()
     {
          return mpg;
     }
     
     public int getVehicleId()
     {
          return vehicle_id;
     }
     
     public void setId(int i)
     {
          id = i ;
     }
     
     public void setMiles(String m)
     {
          miles = m;
     }
     
     public void setGallons(String g)
     {
          gallons = g;
     }
     
     public void setCost(String c)
     {
          cost = c;
     }
     
     public void setMpg(String m)
     {
          mpg = m;
     }
     
     public void setDate(String d)
     {
          date = d;
     }
     
     public void setVehicleId(int v)
     {
          vehicle_id = v;
     }
     
     private int id;
     private String miles;
     private String gallons;
     private String cost;
     private String mpg;
     private String date;
     private int vehicle_id;
}