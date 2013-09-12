package org.myhouse.FuelManager;

import android.app.Activity;
import android.content.SharedPreferences;
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
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

//params, progress, result are the 3 things
public class CheckLogin extends AsyncTask<ArrayList<String>, Integer, String>
{
     public CheckLogin(Dashboard d)
     {
          dashBoard = d;
     }
     
     @Override
     protected void onPreExecute()
     {
     }
     
     @Override
     protected String doInBackground(ArrayList<String>... params)
     {
          ArrayList<String> stuff = new ArrayList<String>();
          stuff = params[0];
          String result = "";
          String username = stuff.get(0);
          String password = stuff.get(1);
          String hostname = stuff.get(2);
          String dbasename = stuff.get(3);
          try
          {
               HttpClient httpclient = new DefaultHttpClient();
               HttpPost httppost = new HttpPost(hostname+"/dbase_connection.php");
              
               ArrayList<NameValuePair> credentials = new ArrayList<NameValuePair>();
              
               BasicNameValuePair u = new BasicNameValuePair("username",username);
               BasicNameValuePair p = new BasicNameValuePair("password",password);
               BasicNameValuePair d = new BasicNameValuePair("database",dbasename);
               credentials.add(u);
               credentials.add(p);
               credentials.add(d);
               
               UrlEncodedFormEntity encoder = new UrlEncodedFormEntity(credentials);
               httppost.setEntity(encoder);
               
               //send all the stuff now
               HttpResponse response = httpclient.execute(httppost);
               HttpEntity entity = response.getEntity();
               
               InputStream is = entity.getContent();
               BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
               StringBuilder sb = new StringBuilder();
               String line = null;
               while ((line = reader.readLine()) != null)
               {
                    sb.append(line);
               }
               is.close();

               result=sb.toString();
          }
          catch (IllegalArgumentException badArgument)
          {
//                Log.d("login","bad url to httppost");
               result += "checkLogin: " + badArgument.toString();
          }
          catch(UnsupportedEncodingException badEncoding)
          {
//                Log.d("login","bad encoding to urlencodedformentity");
               result += "checkLogin: " + badEncoding.toString();
          }
          catch(IOException badio)
          {
//                Log.d("login","bad io: " + badio.toString());
               result += "checkLogin: " + badio.toString();
          }
//           Log.d("new login","'"+result+"'");
          return result;
     }//end doInBackground
     
     @Override
     protected void onProgressUpdate(Integer... progress)
     {
     }
     
     @Override
     protected void onPostExecute(String result)
     {
          //callback the main activity and give it the result
          dashBoard.loginResult(result);
     }//end onPostExecute
     
     private Dashboard dashBoard;
     
}//end CheckLogin