package com.example.nahidhossain.poctabedlayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomerAdapterForTagList extends BaseAdapter {


    private Context context;
    private ArrayList<TagModel> tagModelArrayList;

    public CustomerAdapterForTagList(Context context, ArrayList<TagModel> contactModelArrayList) {

        this.context = context;
        this.tagModelArrayList = contactModelArrayList;
    }

    //@Override
    //public int getViewTypeCount() {
      //  return getCount();
   // }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return tagModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return tagModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CustomerAdapterForTagList.ViewHolder holder;

        if (convertView == null) {
            holder = new CustomerAdapterForTagList.ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lv_item_tag, null, true);

            holder.tagName = (TextView) convertView.findViewById(R.id.tag_name);
            convertView.setTag(holder);

        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (CustomerAdapterForTagList.ViewHolder) convertView.getTag();
        }

        holder.tagName.setText(tagModelArrayList.get(position).getTag_name());
       // holder.tvnumber.setText(tagModelArrayList.get(position).getNumber());
        return convertView;
    }

    private class ViewHolder {

        protected TextView tagName;

    }


}
