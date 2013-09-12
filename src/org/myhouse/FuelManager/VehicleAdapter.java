package org.myhouse.FuelManager;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;

public class VehicleAdapter extends BaseAdapter
{
     public VehicleAdapter(Activity context, ArrayList<Vehicle> names) 
     {
          this.context = context;
          this.names = names;
     }
     
     @Override
     public long getItemId(int position)
     {
          return position;
     }
      
     @Override    
     public Object getItem(int position)
     {

          return names.get(position);
     }

     @Override
     public int getCount()
     {
          return (names == null) ? 0 : names.size();
     }
  
     @Override
     public View getView(int position, View convertView, ViewGroup parent)
     {
          View rowView = convertView;
          ImageView image = null;
          TextView text = null;
          if (rowView == null)
          {
               LayoutInflater inflater = context.getLayoutInflater();
               rowView = inflater.inflate(R.layout.vehicle_layout, null);
               image = (ImageView) rowView.findViewById(R.id.imageView);
               text = (TextView) rowView.findViewById(R.id.vehicle_description);
               
               rowView.setTag(text);
          }
          image = (ImageView) rowView.findViewById(R.id.imageView);
          text = (TextView) rowView.getTag();
          Vehicle v = names.get(position);
          
          //set background color
          if ( (position %2) == 0)
          {
               rowView.setBackgroundResource(R.color.alternate);
          }
          else
          {
               rowView.setBackgroundResource(R.color.white);
          }
          if (rowView == context.getCurrentFocus())
          {
               rowView.setBackgroundResource(R.color.red);
          }
          image.setImageResource(R.drawable.ic_pump);
          text.setText(v.getVehicleDescription());
    
          rowView.setId(v.getVehicleId());
          
          return rowView;
     }
     
     private final Activity context;
     private final ArrayList<Vehicle> names;
     
}//end VehicleAdapter