package org.myhouse.FuelManager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends Dashboard
{
     /**
      * onCreate
      */
     @Override
     protected void onCreate(Bundle savedInstanceState)
     {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.about);
          
          license = (TextView) findViewById(R.id.license);
          
          setLicenseText();
     }
     
     /**
      * set the license text, just show the web page for now
      */
     public void setLicenseText()
     {
          license.setText("http://www.gnu.org/licenses/gpl.html");
     }

     private TextView license;
}//end About