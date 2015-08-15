package com.shubhamdokania.mytune;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shubhamdokania.mytune.model.Datum;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubham on 15/8/15.
 */
public class JSONAdapter extends BaseAdapter {

    private static final String IMAGE_BASE_URL = "http://covers.openlibrary.org/b/id/";

    Context mContext;
    LayoutInflater mInflater;
    List<Datum> mdata;

    public JSONAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mdata = new ArrayList<Datum>();
    }

    @Override
    public int getCount() {
        return mdata.size();
        //return mJsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mdata.get(position);
        //return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_music, null);

            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.song_name);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        Datum dataObject = (Datum) getItem(position);

        /*if (jsonObject.has("image")) {
            String imageID = jsonObject.optString("image");

            String imageURL = IMAGE_BASE_URL + imageID + "-S.jpg";

            Picasso.with(mContext).load(imageURL).placeholder(R.drawable.ic_books).into(holder.thumbnailImageView);
        }
        else {
            holder.thumbnailImageView.setImageResource(R.drawable.ic_books);
        }*/

        Picasso.with(mContext).load(dataObject.getImage()).placeholder(R.drawable.ic_books).into(holder.thumbnailImageView);

        holder.titleTextView.setText(dataObject.getName());

        return convertView;
    }

    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
    }

    public void updateData(List<Datum> vals) {

        mdata = vals;
        /*for( Datum s : data ) {
            Log.d("SOMETHING", s.getName());
        }*/
        notifyDataSetChanged();
    }

}
