package com.mnhmilu.app.bondmaker;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mnhmilu.app.bondmaker.entity.TagModel;

import java.util.ArrayList;

public class CustomeAdapterForTagList extends BaseAdapter {


    private Activity context;
    ArrayList<TagModel> tagModelArrayList;
    PopupWindow pwindo;
    SQLiteDatabaseHandler db;
    private ListView listView;

    public CustomeAdapterForTagList(Activity context, ArrayList<TagModel> contactModelArrayList) {

        this.context = context;
        this.tagModelArrayList = contactModelArrayList;
    }

    public CustomeAdapterForTagList(Activity context, ArrayList<TagModel> tagModelArrayList, SQLiteDatabaseHandler db) {

        this.context = context;
        this.tagModelArrayList = tagModelArrayList;
        this.db=db;
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

    /*
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
    } */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            row = inflater.inflate(R.layout.lv_item_tag, null, true);

           // vh.textViewId = (TextView) row.findViewById(R.id.textViewId);
            vh.tagName = (TextView) row.findViewById(R.id.tag_name);
            vh.editButton = (Button) row.findViewById(R.id.edit);
            vh.deleteButton = (Button) row.findViewById(R.id.delete);
            // store the holder with the view.
            row.setTag(vh);
        } else {

            vh = (ViewHolder) convertView.getTag();

        }

        vh.tagName.setText(tagModelArrayList.get(position).getTag_name());
//        vh.textViewId.setText("" + tagModelArrayList.get(position).getTag_id());

        final int positionPopup = position;
        vh.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Save: ", "" + positionPopup);
                editPopUp(positionPopup);

            }
        });
        vh.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Last Index", "" + positionPopup);
                //     Integer index = (Integer) view.getTag();
                db.deleteTagModel(tagModelArrayList.get(positionPopup));

                //      countries.remove(index.intValue());
                tagModelArrayList = (ArrayList) db.getAllTagModels();
                Log.d("Tag Size", "" + tagModelArrayList.size());
                notifyDataSetChanged();
            }
        });
        return row;
    }

    private class ViewHolder {

        TextView textViewId;
        TextView tagName;
        Button editButton;
        Button deleteButton;

    }


    public void editPopUp(final int positionPopup) {
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_popup_tagedit,
                (ViewGroup) context.findViewById(R.id.popup_element_tagentry));
        pwindo = new PopupWindow(layout, 600, 670, true);
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
        final EditText tagEdit = (EditText) layout.findViewById(R.id.editTextTagName);
        tagEdit.setText(tagModelArrayList.get(positionPopup).getTag_name());

        Button save = (Button) layout.findViewById(R.id.save_popup);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tagNameString = tagEdit.getText().toString();

                TagModel tagModel = new TagModel(tagNameString);
                String tagNameStr = tagEdit.getText().toString();

                TagModel tagModelEnttity = tagModelArrayList.get(positionPopup);
                tagModelEnttity.setTag_name(tagNameStr);
                db.updateTagModel(tagModelEnttity);
                tagModelArrayList = (ArrayList) db.getAllTagModels();
                notifyDataSetChanged();
                for (TagModel item : tagModelArrayList) {
                    String log = "Id: " + item.getTag_id() + " ,Name: " + item.getTag_name();
                    // Writing Countries to log
                    Log.d("Name: ", log);
                }
                pwindo.dismiss();
            }
        });
    }


}
