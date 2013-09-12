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

public class UploadMileageDataTask extends AsyncTask<String, Integer, String>    //params, progress, result
{
     /**
      * constructor, needs the Dashboard item to do stuff with the database
      */
     public UploadMileageDataTask(Dashboard d)
     {
          dashBoard = d;
     }
     
     /**
      * do this before execution
      */
     @Override
     protected void onPreExecute()
     {
     }
     
     /**
      * starts up 
      */
     @Override
     protected String doInBackground(String... params)
     {
          String responseBody = "";
          String username = params[0];
          String password = params[1];
          String hostname = params[2];
          String dbasename = params[3];
          String theData = params[4];   //this is a json object
          Log.d("UploadMileageDataTask","right before the  try block");
          try
          {
               HttpClient httpclient = new DefaultHttpClient();
               HttpPost httppost = new HttpPost(hostname + "/upload_mileage.php");
               ArrayList<NameValuePair> credentials = new ArrayList<NameValuePair>();
               BasicNameValuePair u = new BasicNameValuePair("username",username);
               BasicNameValuePair p = new BasicNameValuePair("password",password);
               BasicNameValuePair d = new BasicNameValuePair("database",dbasename);
               BasicNameValuePair s = new BasicNameValuePair("thedata",theData);
               credentials.add(u);
               credentials.add(p);
               credentials.add(d);
               credentials.add(s);

               UrlEncodedFormEntity encoder = new UrlEncodedFormEntity(credentials);
               httppost.setEntity(encoder);
               
               ResponseHandler<String> responseHandler = new BasicResponseHandler();
               responseBody = httpclient.execute(httppost,responseHandler);               
          }
          catch (IllegalArgumentException badArgument)
          {
               Log.d("UploadMileageDataTask","bad url to httppost");
          }
          catch(UnsupportedEncodingException badEncoding)
          {
               Log.d("UploadMileageDataTask","bad encoding to urlencodedformentity");
          }
          catch(IOException badio)
          {
               Log.d("UploadMileageDataTask","bad io: " + badio.toString());
          }
          return responseBody;
     }//end doInBackground
     
     @Override
     protected void onProgressUpdate(Integer... progress)
     {
     }

     /**
      * after the task is complete
      */
     @Override
     protected void onPostExecute(String result)
     {
          //callback and add the stuff to the database
          int retVal = dashBoard.getDatabase().cleanUploadedMileage(result);
          if (retVal == -1)
               dashBoard.toast("fuel mileage data was uploaded successfully!");
     }
     
     private Dashboard dashBoard;
     
}//end UploadMileageDataTask
