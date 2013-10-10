package org.myhouse.FuelManager;

import android.app.Application;
import android.util.Log;

/**
 * need something to hold some Singleton's
 * We hang onto what they wanted to do.  Then, once they have logged in, 
 * we do the task they wanted.
 */
public class Authentication extends Application
{
     public static String getLoggedIn()
     {
          return loggedIn;
     }
     public static void setLoggedIn(String s)
     {
          loggedIn = s;
     }
     
     public static String getWhatToDo()
     {
          return whatToDo;
     }
     
     public static void setWhatToDo(String t)
     {
          whatToDo = t;
     }
     
     public static String getUser()
     {
          return user;
     }
     public static void setUser(String s)
     {
          user = s;
     }
     public static String getPass()
     {
          return pass;
     }
     public static void setPass(String p)
     {
          pass = p;
     }
     
     public static String string()
     {
          String s = "";
          s += "loggedIn: " + loggedIn + "\n";
          s += "whatToDo: " + whatToDo + "\n";
          s += "user: " + user + "\n";
          s += "pass: " + pass + "\n";
          return s;
     }
     
     private static String loggedIn = "no";
     private static String whatToDo = "nothing";
     private static String user = "";
     private static String pass = "";

}//end Authentication