package com.example.gtk.platform.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gtk.platform.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by gutia on 2017-06-16.
 */

public class MyAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private List<String> images;
    private List<String> text;
    private List<String> prices;

    public MyAdapter(Context context, List<String > images, List<String> text, List<String> prices) {
        this.images = images;
        this.text = text;
        this.prices = prices;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return prices.size();
    }

    @Override
    public Object getItem(int position) {
        return prices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = layoutInflater.inflate(R.layout.item, null);
        SimpleDraweeView sd = (SimpleDraweeView)v.findViewById(R.id.image_view);
        TextView tv1 = (TextView)v.findViewById(R.id.game_name);
        TextView tv2 = (TextView)v.findViewById(R.id.game_price);
        sd.setImageURI(images.get(position));
        tv1.setText(text.get(position));
        tv2.setText(prices.get(position));
        return v;
    }
}
