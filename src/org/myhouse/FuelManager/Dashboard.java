package org.myhouse.FuelManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Dashboard extends Activity
{
     /**
      * onCreate - called when activity is first created
      * 
      * onStart() gets called next
      */
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);

          //edit the preferences and set the login to be nothing
          SharedPreferences loginStatus = getSharedPreferences("login",Activity.MODE_PRIVATE);
          SharedPreferences.Editor editor = loginStatus.edit();
          editor.putString("login","");
          editor.putString("item","");
          editor.commit();
          
          //initialize the database
          dbase = new MyDatabase(this);
          
          //get all the vehicles
          names = dbase.getAllVehicles();
          
          //set up the adapter for the listview
          adapter = new VehicleAdapter(this,names);

     }
     
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
     
     /**
      * go back to the home activity
      */
     public void goHome(Context c)
     {
          final Intent intent = new Intent(c,FuelManager.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
          c.startActivity(intent);
     }

     /**
      * what to do when an option gets clicked
      */
     
     public void onClickOption(View v)
     {
          int id = v.getId();
          switch(id)
          {
               case R.id.entry_form:
                    startActivity(new Intent(getApplicationContext(), EntryForm.class));
               break;
               case R.id.edit_data:
                    startActivity(new Intent(getApplicationContext(),EditMileageData.class));
               break;
               case R.id.monthly_stats:
                    startActivity(new Intent(getApplicationContext(),MonthlyStats.class));
               break;
               case R.id.yearly_stats:
                    startActivity(new Intent(getApplicationContext(),YearlyStats.class));
               break;
               case R.id.lifetime_stats:
                    startActivity(new Intent(getApplicationContext(),LifetimeStats.class));
               break;
               case R.id.about:
                    startActivity(new Intent(getApplicationContext(),About.class));
               break;
               default:
          }
     }
     
     
     /**
      * show a toast message
      */
     public void toast (String msg)
     {
          Toast.makeText (getApplicationContext(), msg, Toast.LENGTH_SHORT).show ();
     } // end toast     
     
     /**
      * print something to the debug log and display it on the screen
      */
     public void trace (String msg) 
     {
          Log.d("fuelmanager", msg);
          toast (msg);
     }     
     
     /**
      * show the menu
      */
     @Override
     public boolean onCreateOptionsMenu(Menu menu)
     {
          MenuInflater menuInflater = getMenuInflater();
          menuInflater.inflate(R.layout.menu, menu);
          
          return true;
     }
     
     /**
      * change the options menu dynamically
      */
     @Override
     public boolean onPrepareOptionsMenu(Menu menu)
     {
          //check the options
          SharedPreferences settings = getSharedPreferences("settings",Activity.MODE_PRIVATE);
          
          if(settings.contains("dbase_location"))
          {
               //hide the options menu if everything is remote
               if (settings.getString("dbase_location",null).equals("local"))
               {
                    menu.findItem(R.id.menu_tools).setVisible(false);
                    menu.findItem(R.id.menu_options).setVisible(true);
               }
               //hide the tools menu if everything is local
               if (settings.getString("dbase_location",null).equals("remote"))
               {
                    menu.findItem(R.id.menu_tools).setVisible(true);
                    menu.findItem(R.id.menu_options).setVisible(false);
               }
          }
          else
          {
               //hide all the menus
               menu.findItem(R.id.menu_options).setVisible(false);
               menu.findItem(R.id.menu_tools).setVisible(false);
          }
          
          return true;
     }
     
     /**
      * What to do when a menu option gets selected
      */
     @Override
     public boolean onOptionsItemSelected(MenuItem item)
     {
          SharedPreferences loginStuff = getSharedPreferences("login",Activity.MODE_PRIVATE);
          String loggedIn = loginStuff.getString("login","");
          switch (item.getItemId())
          {
               case R.id.menu_settings:
                    startActivity(new Intent(getApplicationContext(), Config.class));
                    return true;
               case R.id.menu_add_vehicle:
                    startActivity(new Intent(getApplicationContext(), AddVehicle.class));
                    return true;
               case R.id.menu_edit_vehicle:
                    startActivity(new Intent(getApplicationContext(), EditVehicle.class));
                    return true;
               case R.id.menu_sync_vehicles:
                    if (!loggedIn.equals("logged in"))
                    {
                         Authentication.setWhatToDo("sync vehicles");
                         startActivity(new Intent(getApplicationContext(), Login.class));
                    }
                    else
                    {
                         syncVehicles();
                    }
                    return true;
               case R.id.menu_upload:
                    int tempMileageCount = dbase.getTempRecordCount();
                    if (tempMileageCount >0)
                    {
                         if (!loggedIn.equals("logged in"))
                         {
                              Authentication.setWhatToDo("upload mileage");
                              startActivity(new Intent(getApplicationContext(), Login.class));
                         }
                         else
                         {
                              uploadData();
                         }
                    }
                    else
                    {
                         toast("nothing to upload!");
                    }
                    return true;
               case R.id.menu_sync_data:
                    if (!loggedIn.equals("logged in"))
                    {
                         Authentication.setWhatToDo("sync data");
                         startActivity(new Intent(getApplicationContext(), Login.class));
                    }
                    else
                    {
                         syncData();
                    }
                    return true;
               default:
                    return super.onOptionsItemSelected(item);
          }
     }//end onOptionsItemSelected     
     
     /**
      * get and store the chosen vehicle from their saved preferences
      */
     public Vehicle getSavedVehicle()
     {
          SharedPreferences settings = getSharedPreferences("settings",Activity.MODE_PRIVATE);
          Vehicle v = new Vehicle();
          if(settings.contains("vehicle"))
          {
               int vehicleId = settings.getInt("vehicle",-1);
               v = dbase.getVehicle(vehicleId);
          }
          else
          {
               v.setVehicleDescription("Select a vehicle");
          }
          return v;
     }
     
     /**
      * store the chosen vehicle back into the saved preferences
      */
     public void saveVehiclePreference(int vehicleId)
     {
          SharedPreferences settings = getSharedPreferences("settings",0);
          SharedPreferences.Editor editor = settings.edit();
          editor.putInt("vehicle",vehicleId);
          editor.commit();
     }
     
     /**
      * show the vehicle selection dialog box
      */
     public void showVehicleDialog(DialogInterface.OnClickListener clicker)
     {
          refreshVehicleList();
          if (names.size() > 0)
          {
               AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_TRADITIONAL);
               builder.setAdapter(adapter,clicker);  
               builder.setTitle("Pick one");
               builder.show();
          }
          else
          {
               toast("Go to the menu and add some vehicles first!");
          }
     }
     
     /**
      * refresh the list of vehicles
      */
     public void refreshVehicleList()
     {
          //refresh the list first
          names.clear();
          names.addAll(dbase.getAllVehicles());
          adapter.notifyDataSetChanged();
     }
     
     /**
      * fix the date before it goes into the database
      */
     public String formatInsertDate(String f)
     {
          String fixed = "";
          try
          {
               SimpleDateFormat df = new SimpleDateFormat("MMMM d, yyyy");
               Date nd = df.parse(f);
               df = new SimpleDateFormat("yyyy-MM-dd");
               fixed = df.format(nd);
          }
          catch(ParseException pe)
          {
          }
          return fixed;
     }
     
     /**
      * fix the date so it looks good on the screen
      */
     public String formatDisplayDate(String f)
     {
          String fixed = "";
          try
          {
               SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
               Date nd = df.parse(f);
               df = new SimpleDateFormat("MMMM d, yyyy");
               fixed = df.format(nd);
          }
          catch(ParseException pe)
          {
          }
          return fixed;
     }

     /**
      * CheckLogin calls this function
      * once they are logged in, do whatever they wanted to do
      */
     public void loginResult(String result)
     {
          if(result.equals("success"))
          {
               //go back and set the login preferences to be logged in if result = success
               SharedPreferences loginStatus = getSharedPreferences("login",Activity.MODE_PRIVATE);
               SharedPreferences.Editor editor = loginStatus.edit();

               editor.putString("login","logged in");
               editor.commit();

               if(loginStatus.contains("item"))
               {
                    String whatToDo = Authentication.getWhatToDo();
                    if(whatToDo.equals("sync vehicles"))
                    {
                         syncVehicles();
                    }
                    else if (whatToDo.equals("upload mileage"))
                    {
                         uploadData();
                    }
                    else if(whatToDo.equals("sync data"))
                    {
                         syncData();
                    }
               }
          }//result was success
          else
          {
               //unset the user and pass 
               Authentication.setUser("");
               Authentication.setPass("");
               toast("Couldn't connect and/or login!  Check your configuration!");
          }
     }
     
     /**
      * gets all the vehicles on the remote side, and adds them locally
      */
     private void syncVehicles()
     {
          SharedPreferences settings = getSharedPreferences("settings",Activity.MODE_PRIVATE);
          String host = settings.getString("hostname","");
          String db = db = settings.getString("database","");
          String user = Authentication.getUser();
          String pass = Authentication.getPass();
          
          SyncVehicleTask svt = new SyncVehicleTask(this);
          svt.execute(user,pass,host,db);
     }

     /**
      * sends all the temp mileage data to the remote side
      */
     private void uploadData()
     {
          SharedPreferences settings = getSharedPreferences("settings",Activity.MODE_PRIVATE);
          String host = settings.getString("hostname","");
          String db = db = settings.getString("database","");
          String user = Authentication.getUser();
          String pass = Authentication.getPass();
          
          int tempMileageCount = dbase.getTempRecordCount();
          if (tempMileageCount >0)
          {
               JSONObject stuff = dbase.getTempMileage();
               UploadMileageDataTask umdt = new UploadMileageDataTask(this); 
               umdt.execute(user,pass,host,db,stuff.toString());
          }
     }
     
     /**
      * gets all the mileage data from the remote side
      */
     private void syncData()
     {
          SharedPreferences settings = getSharedPreferences("settings",Activity.MODE_PRIVATE);
          String host = settings.getString("hostname","");
          String db = db = settings.getString("database","");
          String user = Authentication.getUser();
          String pass = Authentication.getPass();
          
          int tempMileageCount = dbase.getTempRecordCount();
          if (tempMileageCount < 1)
          {
               String lastId = dbase.getBiggestMileageId();
               //trace("the last id is: " + lastId);
               SyncMileageDataTask smdt = new SyncMileageDataTask(this);               
               smdt.execute(user,pass,host,db,lastId);
          }
          else
          {
               toast("Upload your data first!");
          }
     }
     
     /**
      * returns the MyDatabase object
      */
     public MyDatabase getDatabase()
     {
          return dbase;
     }
     
     protected ArrayList<Vehicle> names;
     protected VehicleAdapter adapter;
     protected MyDatabase dbase;
     
}//end class FuelManager
