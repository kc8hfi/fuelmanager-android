package org.myhouse.FuelManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.text.ParseException;
import java.util.ArrayList;

public class Login extends Dashboard 
{
     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.login);
          
          username = (EditText)findViewById(R.id.username);
          password = (EditText)findViewById(R.id.password);
          
     }
     
     /**
      * Save their changes to the database
      */
     public void doLogin(View v)
     {
          String u = username.getText().toString();
          String p = password.getText().toString();
          String h = "";
          String d = "";
          
          SharedPreferences settings = getSharedPreferences("settings",Activity.MODE_PRIVATE);
          String result = "";
          if(settings.contains("hostname"))
          {
               h = settings.getString("hostname","");
               d = settings.getString("database","");
               CheckLogin login = new CheckLogin(this);
//                trace(u+p+h+d);

               Authentication.setUser(u);
               Authentication.setPass(p);

               String[] passed = new String[4];;
               passed[0] = u;
               passed[1] = p;
               passed[2] = h;
               passed[3] = d;
               
               login.execute(passed);
               
          }
          finish();
     }

     /** 
      * don't do anything, just close
      */
     public void cancelLogin(View v)
     {
          finish();
     }
     private EditText username;
     private EditText password;
     
}//end class Login