package org.myhouse.FuelManager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import org.apache.http.message.BasicNameValuePair;
// import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyDatabase extends SQLiteOpenHelper
{
     private Context context;
     //static variables
     
     //table names
     private static final String vehicle_table = "vehicles";
     private static final String mileage_table = "fuel_mileage";
     private static final String temp_mileage_table = "temp_fuel_mileage";
     
     //vehicle table column names
     private static final String vehicle_id = "id";
     private static final String vehicle_description = "description";
     
     //fuel_mileage column names
     private static final String mileage_uid = "id";
     private static final String mileage_id = "vehicle_id";
     private static final String mileage_miles = "miles";
     private static final String mileage_gallons = "gallons";
     private static final String mileage_cost = "cost";
     private static final String mileage_date = "fillup_date";
     
     private DecimalFormat mpgFormat;
     private DecimalFormat milesFormat;
     private DecimalFormat costFormat;     
     
     //constructor
     public MyDatabase(Context context)
     {
          super(context,"fuelmanager.db",null,1);
          this.context = context;
          
          mpgFormat = new DecimalFormat("0.000");
          milesFormat = new DecimalFormat("#,###.00");
          costFormat = new DecimalFormat("\u00A4" + "#,###.00");
     }
     
     @Override
     public void onCreate(SQLiteDatabase dbase)
     {
          //create statement
          String createVehicleTable = "create table if not exists "+vehicle_table+" "
               + " ("+ vehicle_id +" integer primary key"
               + " on conflict fail autoincrement, "
               + " " + vehicle_description +" text )";
          dbase.execSQL(createVehicleTable);
          
          String createFuelMileageTable = "create table if not exists " + mileage_table 
               + " (id integer primary key on conflict fail autoincrement,  "
               + " " + mileage_miles + " real, "
               + " " + mileage_gallons + " real, "
               + " " + mileage_cost + " real, "
               + " " + mileage_date + " text, "
               + " " + mileage_id + " integer not null, "
               + " foreign key (" + mileage_id + ") references " + vehicle_table + "(" + vehicle_id + ") "
               + ")";
          dbase.execSQL(createFuelMileageTable);
          
          String createTempFuelMileageTable = "create table if not exists " + temp_mileage_table
               + " (id integer primary key on conflict fail autoincrement,  "
               + " " + mileage_miles + " real, "
               + " " + mileage_gallons + " real, "
               + " " + mileage_cost + " real, "
               + " " + mileage_date + " text, "
               + " " + mileage_id + " integer not null, "
               + " foreign key (" + mileage_id + ") references " + vehicle_table + "(" + vehicle_id + ") "
               + ")";
          dbase.execSQL(createTempFuelMileageTable);


          //turn on foreign keys
          String fk = "pragma foreign_keys = on";
          dbase.execSQL(fk);
          
     }
     
     @Override
     public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
     {
          onCreate(db);
     }
     
     
     /** all the other dbase operations are here*/
     public long addVehicle(String d)
     {
          long retVal = 0;
          SQLiteDatabase db = this.getWritableDatabase();
          ContentValues v = new ContentValues();
          v.put(vehicle_description,d);
          retVal = db.insert(vehicle_table,null,v);
          db.close();          
          return retVal;
     }
     
     public String getVehicleDesc(int vid)
     {
          SQLiteDatabase db = this.getReadableDatabase();
          //table, columns, selection,selectionArgs, groupBy, having, orderBy
          Cursor cursor = db.query(vehicle_table,
                                   new String[]{vehicle_description},
                                   vehicle_id+"=?",
                                   new String[]{String.valueOf(vid)},
                                   null,
                                   null,
                                   null,
                                   null
          );
          String desc = "";
          if (cursor.moveToFirst())
          {
               desc = cursor.getString(0);
          }
          db.close();
          return desc;
     }
     
     public Vehicle getVehicle(int vid)
     {
          SQLiteDatabase db = this.getReadableDatabase();
          Vehicle v = new Vehicle();
          //table, columns, selection,selectionArgs, groupBy, having, orderBy
          Cursor cursor = db.query(vehicle_table,
                                   new String[]{vehicle_id,vehicle_description},
                                   vehicle_id+"=?",
                                   new String[]{String.valueOf(vid)},
                                   null,
                                   null,
                                   null,
                                   null
          );
          if (cursor.moveToFirst())
          {
               v = new Vehicle(cursor);
          }
          db.close();
          return v;
     }
     
     public ArrayList<Vehicle> getAllVehicles()
     {
          SQLiteDatabase db = this.getReadableDatabase();
          ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>(0);
          //table, columns, selection,selectionArgs, groupBy, having, orderBy
          Cursor cursor = db.query(vehicle_table,
                                   new String[]{vehicle_id,vehicle_description},
                                   null,
                                   null,
                                   null,
                                   null,
                                   vehicle_id
          );
          if (cursor.moveToFirst())
          {
               do
               {
                    Vehicle v = new Vehicle(cursor);
                    vehicles.add(v);
               }while(cursor.moveToNext());
          }
          db.close();
          return vehicles;
     }     

     public ArrayList<String> getAllVehicleString()
     {
          SQLiteDatabase db = this.getReadableDatabase();
          ArrayList<String> vehicles = new ArrayList<String>(0);
          //table, columns, selection,selectionArgs, groupBy, having, orderBy
          Cursor cursor = db.query(vehicle_table,
                                   new String[]{vehicle_id,vehicle_description},
                                   null,
                                   null,
                                   null,
                                   null,
                                   vehicle_id
          );
          if (cursor.moveToFirst())
          {
               do
               {
                    vehicles.add(cursor.getString(0));
               }while(cursor.moveToNext());
          }
          db.close();
          return vehicles;
     }     
     
     
     public int getVehicleId(String v)
     {
          SQLiteDatabase db = this.getReadableDatabase();
          //table, columns, selection,selectionArgs, groupBy, having, orderBy
          Cursor cursor = db.query(vehicle_table,
                                   new String[]{vehicle_id},
                                   vehicle_description+"=?",
                                   new String[]{v},
                                   null,
                                   null,
                                   null,
                                   null
          );
          int id = -1;
          if (cursor.moveToFirst())
          {
               id = cursor.getInt(0);
          }
          db.close();
          return id;
     }
     
     public ArrayList<Mileage> getMileage(int vid)
     {
          SharedPreferences settings = context.getSharedPreferences("settings",Activity.MODE_PRIVATE);
          String whichTable = "";
          if(settings.contains("dbase_location"))
          {
               if (settings.getString("dbase_location",null).equals("local"))
               {
                    whichTable = mileage_table;
               }
               if (settings.getString("dbase_location",null).equals("remote"))
               {
                    whichTable = temp_mileage_table;
               }
          }
          ArrayList<Mileage> items = new ArrayList<Mileage>(0);
          if(!whichTable.equals(""))
          {
               SQLiteDatabase db = this.getReadableDatabase();
     //           query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
               Cursor cursor = db.query(whichTable,
                                        new String[]{mileage_uid,mileage_miles,mileage_gallons,
                                             mileage_cost,mileage_date,mileage_id},
                                        mileage_id + "= ?",
                                        new String[]{String.valueOf(vid)},
                                        null,
                                        null,
                                        mileage_date
                              );
               if(cursor.moveToFirst())
               {
                    do
                    {
                         Mileage m = new Mileage(cursor);
                         items.add(m);
                    }while(cursor.moveToNext());
               }
               else
               {
                    Log.d("getMileage","move to first was false");
               }
               db.close();
          }
          return items;
     }
     
     public int updateRecord(Mileage m)
     {
          SharedPreferences settings = context.getSharedPreferences("settings",Activity.MODE_PRIVATE);
          String whichTable = "";
          if(settings.contains("dbase_location"))
          {
               if (settings.getString("dbase_location",null).equals("local"))
               {
                    whichTable = mileage_table;
               }
               if (settings.getString("dbase_location",null).equals("remote"))
               {
                    whichTable = temp_mileage_table;
               }
          }
          int retVal = -1;
          if(!whichTable.equals(""))
          {
               SQLiteDatabase db = this.getWritableDatabase();
               ContentValues v = new ContentValues();
               v.put(mileage_miles,m.getMiles());
               v.put(mileage_gallons,m.getGallons());
               v.put(mileage_cost,m.getCost());
               v.put(mileage_date,m.getDate());
               //update (String table, ContentValues values, String whereClause, String[] whereArgs)
               retVal = db.update(whichTable,v,mileage_uid + "= ?",new String[]{Integer.toString(m.getId())});
               db.close();
          }
          return retVal;
     }
     
     /** gets the count of all the records in the temp_mileage_table */
     public int getTempRecordCount()
     {
          SQLiteDatabase db = this.getReadableDatabase();
          String q = "select count(*) from " + temp_mileage_table;
          Cursor c = db.rawQuery(q,null);
          int count = 0;
          if (c.moveToFirst())
               count = c.getInt(0);
          db.close();
          return count;
     }
     
//      public long addData(int i, String m, String g,String c, String d)
     public long addData(Mileage m)
     {
          SharedPreferences settings = context.getSharedPreferences("settings",Activity.MODE_PRIVATE);
          String whichTable = "";
          if(settings.contains("dbase_location"))
          {
               if (settings.getString("dbase_location",null).equals("local"))
               {
                    whichTable = mileage_table;
               }
               if (settings.getString("dbase_location",null).equals("remote"))
               {
                    whichTable = temp_mileage_table;
               }
          }

          SQLiteDatabase db = this.getWritableDatabase();
          ContentValues v = new ContentValues();
          
          //check to see where the "real" database is
          long retval=0;
          if(!whichTable.equals(""))
          {
               v.put(mileage_id,m.getVehicleId());     //vehicle id
               v.put(mileage_miles,m.getMiles());      //miles
               v.put(mileage_gallons,m.getGallons());  //gallons
               v.put(mileage_cost,m.getCost());        //cost
               v.put(mileage_date,m.getDate());        //fillup date
               retval = db.insert(whichTable,null,v);
          }
          else
          {
               retval = -1;
          }
          db.close();
          return retval;
     }//end addData
     
     public int updateVehicle(Vehicle vehicle)
     {
          SQLiteDatabase db = this.getWritableDatabase();
          ContentValues v = new ContentValues();
          v.put(vehicle_description,vehicle.getVehicleDescription());
          int retval = -1;
          retval = db.update(vehicle_table,v,vehicle_id+"= ?",new String[]{Integer.toString(vehicle.getVehicleId())});
          db.close();
          return retval;
     }
     
     public ArrayList<String> getYears(Vehicle v)
     {
          SQLiteDatabase db = this.getReadableDatabase();
          ArrayList<String> years = new ArrayList<String>();
          Cursor cursor = db.rawQuery("select distinct strftime(\"%Y\",fillup_date) from " + mileage_table + 
          " where strftime(\"%Y\",fillup_date) != '0000' " + " and " + mileage_id + " =?" ,
                      new String[]{Integer.toString(v.getVehicleId())});
          if(cursor.moveToFirst())
          {
               do
               {
                    String y = cursor.getString(0);
                    years.add(y);
               }while(cursor.moveToNext());
          }
          db.close();
          return years;
     }
     
     public ArrayList<ArrayList<String>> getMonthlyStats(Vehicle v)
     {
          ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
          String months[] = new String[]{
               "01","02","03","04","05","06","07","08","09","10","11","12"
          };
          String selectStuff[] = new String[]{
               "count(id)","sum(miles)","sum(gallons)","sum(cost)","sum(miles)/sum(gallons)"
          };
          String where = mileage_id +"=? and strftime(\"%Y\",fillup_date)=? " +  
                         " and strftime(\"%m\",fillup_date)=? "
          ;
          ArrayList<String> years = getYears(v);
          SQLiteDatabase db = this.getReadableDatabase();
          for(String month:months)
          {
               ArrayList<String>row = new ArrayList<String>();
               for(String year:years)
               {
                    //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
                    Cursor c = db.query(mileage_table,
                                        selectStuff,
                                        where,
                                        new String[]{Integer.toString(v.getVehicleId()),
                                             year,month
                                        },null,null,null);
                    if(c.moveToFirst())
                    {
                         String sFillup = "";
                         String sMiles = "";
                         String sGallons = "";
                         String sCost = "";
                         String sMpg = "";
                         
                         if(!c.isNull(0))
                         {
                              sFillup = c.getString(0);
                         }
                         if (sFillup.equals("0"))
                              sFillup = "";
                         if (!c.isNull(1))
                         {
                              sMiles = c.getString(1);
                         }
                         if (!c.isNull(2))
                         {
                              sGallons = c.getString(2);
                         }
                         if(!c.isNull(3))
                         {
                              sCost = c.getString(3);
                         }
                         if(!c.isNull(4))
                         {
                              sMpg = c.getString(4);
                         }
                         //DecimalFormat, setRoundingMode(RoundingMode.UP);  //round up
                         try
                         {
                              if (!sMiles.equals(""))
                              {
                                   sMiles = milesFormat.format(Double.parseDouble(sMiles));
                              }
                              if (!sGallons.equals(""))
                              {
                                   sCost = costFormat.format(Double.parseDouble(sCost));
                              }
                              if (!sMpg.equals(""))
                              {
                                   sMpg = mpgFormat.format(Double.parseDouble(sMpg));
                              }
                         }
                         catch(Exception e)
                         {
                              Log.d("exception",e.toString());
                         }
                         String t = "";
                         t += "Fillups: " + sFillup + "\n";
                         t += "Miles: " + sMiles + "\n";
                         t += "Gallons: " + sGallons + "\n";
                         t += "Cost: " + sCost + "\n";
                         t += "MPG: " + sMpg;
                         
//                          if(year.equals("2007") && month.equals("01"))
//                               Log.d("test",t);
                         row.add(t);
                    }
                    else
                    {
                         Log.d("something bad","getmonthlystats crashed and burned");
                    }
               }//end years
               data.add(row);
          }//end months
          db.close();
          return data;
     }
     
     public ArrayList<ArrayList<String>> getYearlyStats(Vehicle v)
     {
          ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
          SQLiteDatabase db = this.getReadableDatabase();          

          String selectStuff[] = new String[]{
               "strftime(\"%Y\",fillup_date)",
               "count(id)","sum(miles)","sum(gallons)","sum(cost)","sum(miles)/sum(gallons)"
          };
          String where = mileage_id +"=? and strftime(\"%Y\",fillup_date)!='0000' ";
          Cursor c = db.query(mileage_table,
                              selectStuff,
                              where,
                              new String[]{Integer.toString(v.getVehicleId())},
                              "strftime(\"%Y\",fillup_date)",
                              null,null);
          if(c.moveToFirst())
          {
               do
               {
                    ArrayList<String>row = new ArrayList<String>();
                    String sYear = "";
                    String sFillup = "";
                    String sMiles = "";
                    String sGallons = "";
                    String sCost = "";
                    String sMpg = "";

                    if(!c.isNull(0))
                    {
                         sYear = c.getString(0);
                    }
                    if(!c.isNull(1))
                    {
                         sFillup = c.getString(1);
                    }
                    if (sFillup.equals("0"))
                         sFillup = "";
                    if (!c.isNull(2))
                    {
                         sMiles = c.getString(2);
                    }
                    if (!c.isNull(3))
                    {
                         sGallons = c.getString(3);
                    }
                    if(!c.isNull(4))
                    {
                         sCost = c.getString(4);
                    }
                    if(!c.isNull(5))
                    {
                         sMpg = c.getString(5);
                    }
                    //DecimalFormat, setRoundingMode(RoundingMode.UP);  //round up
                    try
                    {
                         if (!sMiles.equals(""))
                         {
                              sMiles = milesFormat.format(Double.parseDouble(sMiles));
                         }
                         if (!sGallons.equals(""))
                         {
                              sGallons = milesFormat.format(Double.parseDouble(sGallons));
                         }
                         if (!sCost.equals(""))
                         {
                              sCost = costFormat.format(Double.parseDouble(sCost));
                         }
                         if (!sMpg.equals(""))
                         {
                              sMpg = mpgFormat.format(Double.parseDouble(sMpg));
                         }
                    }
                    catch(Exception e)
                    {
                         Log.d("exception",e.toString());
                    }
                    row.add(sFillup);
                    row.add(sMiles);
                    row.add(sGallons);
                    row.add(sCost);
                    row.add(sMpg);
                    
                    data.add(row);
               }while(c.moveToNext());
          }
          else
          {
               Log.d("something bad","getYearlyStats crashed and burned");
          }
          db.close();
          return data;
     }//end getYearlyStats
     
     
     public ArrayList<String> getLifetimeStats(Vehicle v)
     {
          ArrayList<String> data = new ArrayList<String>();
          SQLiteDatabase db = this.getReadableDatabase();          

          String selectStuff[] = new String[]{
               "count(id)","sum(miles)","sum(gallons)","sum(cost)","sum(miles)/sum(gallons)"
          };
          String where = mileage_id +"=? ";
          
          ArrayList<String>row = new ArrayList<String>();
          //query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
          Cursor c = db.query(mileage_table,
                              selectStuff,
                              where,
                              new String[]{Integer.toString(v.getVehicleId())
                              },null,null,null);
          if(c.moveToFirst())
          {
               String sFillup = "";
               String sMiles = "";
               String sGallons = "";
               String sCost = "";
               String sMpg = "";
               
               if(!c.isNull(0))
               {
                    sFillup = c.getString(0);
               }
               if (sFillup.equals("0"))
                    sFillup = "";
               if (!c.isNull(1))
               {
                    sMiles = c.getString(1);
               }
               if (!c.isNull(2))
               {
                    sGallons = c.getString(2);
               }
               if(!c.isNull(3))
               {
                    sCost = c.getString(3);
               }
               if(!c.isNull(4))
               {
                    sMpg = c.getString(4);
               }
               //DecimalFormat, setRoundingMode(RoundingMode.UP);  //round up
               try
               {
                    if (!sMiles.equals(""))
                    {
                         sMiles = milesFormat.format(Double.parseDouble(sMiles));
                    }
                    if (!sGallons.equals(""))
                    {
                         sGallons = milesFormat.format(Double.parseDouble(sGallons));
                    }
                    if (!sCost.equals(""))
                    {
                         sCost = costFormat.format(Double.parseDouble(sCost));
                    }
                    if (!sMpg.equals(""))
                    {
                         sMpg = mpgFormat.format(Double.parseDouble(sMpg));
                    }
               }
               catch(Exception e)
               {
                    Log.d("exception",e.toString());
               }
               if(!sFillup.equals("") && !sMiles.equals("") && !sGallons.equals("") 
                    && !sCost.equals("") && !sMpg.equals(""))
                    
               {
                    data.add(sFillup);
                    data.add(sMiles);
                    data.add(sGallons);
                    data.add(sCost);
                    data.add(sMpg);
               }
          }
          else
          {
               Log.d("something bad","getLifetimeStats crashed and burned");
          }

          db.close();
          return data;          
     }//end LifetimeStats
     
     
     public int addVehicleFromRemote(String s)
     {
          Log.d("inside mydatabase","add this stuff from the remote side: " + s);
          SQLiteDatabase db = this.getWritableDatabase();
          long retVal = -1;
          boolean checkme = true;
          int bad = -1;
          //parse
          if (!s.equals(""))
          {
               //empty the current table first
               Log.d("inside mydbase", "empty the current table first");
               //remove all the vehicles
               db.delete(vehicle_table,null,null);
               
               try
               {
                    JSONObject json = new JSONObject(s);
                    JSONArray jArray = json.getJSONArray("vehicles");
                    for (int i = 0; i < jArray.length(); i++) 
                    {
                         JSONObject e = jArray.getJSONObject(i);

                         ContentValues v = new ContentValues();
                         v.put(vehicle_id,e.getString("id"));
                         v.put(vehicle_description,e.getString("description"));

                         retVal = db.insert(vehicle_table,null,v);
                         if(retVal != -1 && checkme)
                         {
                              checkme = false;
                              bad = 1;
                         }
                    }
               }
               catch(JSONException jsone)
               {
               }
          }
          db.close();
          return bad;
     }
     
     public String getBiggestMileageId()
     {
          String id = "";
          SQLiteDatabase db = this.getReadableDatabase();
          String q = "select max("+mileage_uid + ") from " + mileage_table;
          Cursor cursor = db.rawQuery(q,null);
          if(cursor.moveToFirst())
          {
               if(cursor.getType(0) != Cursor.FIELD_TYPE_NULL)
                    id = cursor.getString(0);
          }
          db.close();
          return id;
     }
     
     public int addMileageDataFromRemote(String result)
     {
//           Log.d("mydatabase addMileageDataFromRemote","remote side: " + result);
          SQLiteDatabase db = this.getWritableDatabase();
          //clean out the table first
          db.delete(mileage_table,null,null);
          //parse
          long retVal = -1;
          int good = 0;
          boolean checkme = true;
          try
          {
               JSONObject json = new JSONObject(result);
               JSONArray jArray = json.getJSONArray("fuel_mileage");
               for (int i = 0; i < jArray.length(); i++) 
               {
                    JSONObject e = jArray.getJSONObject(i);

                    ContentValues v = new ContentValues();
                    v.put(mileage_uid,e.getString("id"));
                    v.put(mileage_miles,e.getString("miles"));
                    v.put(mileage_gallons,e.getString("gallons"));
                    v.put(mileage_cost,e.getString("cost"));
                    v.put(mileage_date,e.getString("fillup_date"));
                    v.put(mileage_id,e.getString("vehicle_id"));

                    retVal = db.insert(mileage_table,null,v);
                    if (retVal == -1 && checkme)
                    {
                         checkme = false;
                         good = -1;
                    }
               }
          }
          catch(JSONException jsone)
          {
          }
          db.close();
          return good;
     }
     
     /** gets all the records from the temp_mileage_table */
     public JSONObject getTempMileage()
     {
          SQLiteDatabase db = this.getReadableDatabase();
          
          String[] columns = {mileage_uid,mileage_id,mileage_miles,mileage_gallons,mileage_cost,mileage_date};
          //table, columns, selection,selectionArgs, groupBy, having, orderBy
          Cursor cursor = db.query(temp_mileage_table,columns,null,null,null,null,mileage_date);
          JSONArray sendMe = new JSONArray();
          
          if (cursor.moveToFirst())
          {
               do
               {
                    try
                    {  
                         JSONObject little = new JSONObject();
                         little.put(mileage_uid,cursor.getString(0));
                         little.put(mileage_id,cursor.getString(1));
                         little.put(mileage_miles,cursor.getString(2));
                         little.put(mileage_gallons,cursor.getString(3));
                         little.put(mileage_cost,cursor.getString(4));
                         little.put(mileage_date,cursor.getString(5));
                         
                         sendMe.put(little);
                    }
                    catch(Exception e)
                    {
                         Log.d("getTempMileage",e.toString());
                    }
               }while(cursor.moveToNext());
          }
          db.close();
          JSONObject t = new JSONObject();
          try
          {
               t.put("testing",sendMe);
          }
          catch(Exception e)
          {
          }
          return t;
     }     
     
     public int cleanUploadedMileage(String result)
     {
          SQLiteDatabase db = this.getWritableDatabase();
          String[]separated = result.split(",");
          int retVal = -1;
          int val = -1;
          boolean checkme = true;
          for(String i:separated)
          {
               retVal = db.delete(temp_mileage_table,mileage_uid+"=?",new String[]{i});
               if(retVal == 0 && checkme)
               {
                    checkme = false;
                    val = retVal;
               }
          }
          db.close();
          return val;
     }
}//end class

