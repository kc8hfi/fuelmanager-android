package org.myhouse.FuelManager;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.StringBuilder;
import java.util.ArrayList;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.ResponseHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

//params, progress, result are the 3 things
public class SyncVehicleTask extends AsyncTask<String, Integer, String>
{
     public SyncVehicleTask(Dashboard d)
     {
          dashBoard = d;
     }
     
     @Override
     protected void onPreExecute()
     {
     }
     
     @Override
     protected String doInBackground(String... params)
     {
          String responseBody = "";
          String username = params[0];
          String password = params[1];
          String hostname = params[2];
          String dbasename = params[3];
          try
          {
               HttpClient httpclient = new DefaultHttpClient();
               HttpPost httppost = new HttpPost(hostname + "/get_vehicles.php");
               
               ArrayList<NameValuePair> credentials = new ArrayList<NameValuePair>();
               
               BasicNameValuePair u = new BasicNameValuePair("username",username);
               BasicNameValuePair p = new BasicNameValuePair("password",password);
               BasicNameValuePair d = new BasicNameValuePair("database",dbasename);
               credentials.add(u);
               credentials.add(p);
               credentials.add(d);
               
               UrlEncodedFormEntity encoder = new UrlEncodedFormEntity(credentials);
               httppost.setEntity(encoder);
               
               ResponseHandler<String> responseHandler = new BasicResponseHandler();
               responseBody = httpclient.execute(httppost,responseHandler);               
          }
          catch (IllegalArgumentException badArgument)
          {
               Log.d("syncvehicles","bad url to httppost");
          }
          catch(UnsupportedEncodingException badEncoding)
          {
               Log.d("syncvehicles","bad encoding to urlencodedformentity");
          }
          catch(IOException badio)
          {
               Log.d("syncvehicles","bad io: " + badio.toString());
          }
          Log.d("sync vehicles from web page",responseBody);
          return responseBody;
     }//end doInBackground
     
     @Override
     protected void onProgressUpdate(Integer... progress)
     {
     }
     
     @Override
     protected void onPostExecute(String result)
     {
          //callback and add the stuff to the database
          int retVal = dashBoard.getDatabase().addVehicleFromRemote(result);
          if(retVal != -1)
               dashBoard.toast("vehicles were downloaded successfully!");
          
     }//end onPostExecute
     
     private Dashboard dashBoard;
     
}//end SyncVehicleTask