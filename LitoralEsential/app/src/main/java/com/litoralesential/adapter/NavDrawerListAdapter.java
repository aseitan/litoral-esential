package com.litoralesential.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.litoralesential.MainActivity;
import com.litoralesential.R;
import com.litoralesential.Utils;
import com.litoralesential.model.NavDrawerItem;

import java.io.File;
import java.util.ArrayList;

public class NavDrawerListAdapter extends BaseAdapter {
    private Context mainActivity;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(MainActivity mainActivity, ArrayList<NavDrawerItem> navDrawerItems) {
        this.mainActivity = mainActivity;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater mInflater = (LayoutInflater) mainActivity.getApplicationContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        txtTitle.setText(navDrawerItems.get(position).getTitle());

        /*
        // remove the images for the moment
        if (!navDrawerItems.get(position).isUpdatedImage())
        {
            ImageView categoryImage = (ImageView) convertView.findViewById(R.id.category_image);

            String drawerItemID = Integer.toString(navDrawerItems.get(position).getID());

            Bitmap bm;
            bm = BitmapFactory.decodeFile(Utils.externalPathRoot + File.separator + Utils.CATEGORY_IMAGE_PREFIX + drawerItemID);
            if(bm != null)
            {
                categoryImage.setImageBitmap(bm);
                navDrawerItems.get(position).setUpdatedImage(true);
            }
        }
        */
        return convertView;
    }

}
