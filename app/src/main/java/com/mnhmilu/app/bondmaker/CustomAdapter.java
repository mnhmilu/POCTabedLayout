package com.mnhmilu.app.bondmaker;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Parsania Hardik on 11-May-17.
 */
public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ContactModel> contactModelArrayList;

    public CustomAdapter(Context context, ArrayList<ContactModel> contactModelArrayList) {

        this.context = context;
        this.contactModelArrayList = contactModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
      //  return  0;

        if(contactModelArrayList!=null) {

            if(contactModelArrayList.size()==0)
                return 1;
            else
                return contactModelArrayList.size();
        }
        else
       {return  1;}
    }

    @Override
    public Object getItem(int position) {
        return contactModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_item, null, true);

            holder.tvname = (TextView) convertView.findViewById(R.id.name);
            holder.tvnumber = (TextView) convertView.findViewById(R.id.number);
            holder.tvlastcalldate= (TextView) convertView.findViewById(R.id.lastCallDate);



            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }

        if(contactModelArrayList!=null) {
            if(contactModelArrayList.size()>0) {
                holder.tvname.setText(contactModelArrayList.get(position).getName());
                holder.tvnumber.setText(contactModelArrayList.get(position).getNumber());
            }
        }
        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.GRAY);
        } else {
            convertView.setBackgroundColor(Color.WHITE);
        }



        return convertView;
    }



    private class ViewHolder {

        protected TextView tvname, tvnumber,tvlastcalldate,tvelapsedDay;

    }

}