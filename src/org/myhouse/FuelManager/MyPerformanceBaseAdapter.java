package org.myhouse.FuelManager;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class MyPerformanceBaseAdapter extends BaseAdapter
{
     private final Activity context;
     private final ArrayList<Mileage> names;
     
     public MyPerformanceBaseAdapter(Activity context, ArrayList<Mileage> names) 
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
          if (rowView == null)
          {
               LayoutInflater inflater = context.getLayoutInflater();
               rowView = inflater.inflate(R.layout.show_edit_data_row, null);
               ViewHolder viewHolder = new ViewHolder();
               viewHolder.miles = (TextView) rowView.findViewById(R.id.miles);
               viewHolder.gallons = (TextView) rowView.findViewById(R.id.gallons);
               viewHolder.cost = (TextView) rowView.findViewById(R.id.cost);
               viewHolder.mpg = (TextView) rowView.findViewById(R.id.mpg);
               viewHolder.date = (TextView) rowView.findViewById(R.id.fillup_date);

               rowView.setTag(viewHolder);
          }

          ViewHolder holder = (ViewHolder) rowView.getTag();
          Mileage m = names.get(position);
          //alternate the row colors
          if ( (position %2) == 0)
          {
               rowView.setBackgroundResource(R.color.white);
          }
          else
          {
               rowView.setBackgroundResource(R.color.alternate);
          }
//           if (rowView == context.getCurrentFocus())
//           {
//                Log.d("focused","rowview is the current focus");
//                rowView.setBackgroundResource(R.color.red);
//           }
          holder.miles.setText(m.getMiles());
          holder.gallons.setText(m.getGallons());
          holder.cost.setText(m.getCost());
          holder.mpg.setText(m.getMpg());
          String pretty = ((Dashboard)context).formatDisplayDate(m.getDate());
          holder.date.setText(pretty);

          rowView.setId(m.getId());
          return rowView;
     }
}//end MyPerformanceBaseAdapter