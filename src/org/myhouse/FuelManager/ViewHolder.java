package org.myhouse.FuelManager;

import android.widget.TextView;
import android.os.Bundle;
import android.widget.ImageView;

public class ViewHolder
{
     public String toString()
     {
          String s = "";
          s += "miles: '" + miles.getText().toString() + "' ";
          s += "gallons: '" + gallons.getText().toString() + "' ";
          s += "cost: '" + cost.getText().toString() + "' ";
          s += "mpg: '" + mpg.getText().toString() + "' ";
          s += "date: '" + date.getText().toString() + "' ";
          return s;
     }

     public TextView miles;
     public TextView gallons;
     public TextView cost;
     public TextView mpg;
     public TextView date;

}