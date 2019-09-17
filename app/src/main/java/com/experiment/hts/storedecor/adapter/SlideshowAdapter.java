package com.experiment.hts.storedecor.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.experiment.hts.storedecor.R;
import com.experiment.hts.storedecor.model.SlideImage;

import java.util.ArrayList;
import java.util.List;

public class SlideshowAdapter extends BaseAdapter {

    SlideImage slideImage;
    List<Bitmap> bitmapList;
    private Context mContext;

    public SlideshowAdapter(List<Bitmap> bitmapList, SlideImage slideImage, Context mContext) {
        this.bitmapList = bitmapList;
        this.slideImage = slideImage;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return bitmapList.size();
    }

    @Override
    public Object getItem(int position) {
        return bitmapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.item_slide, parent, false);
        }

        ImageView imageView = convertView.findViewById(R.id.ivSlide);
        imageView.setImageBitmap(bitmapList.get(position));
        imageView.setClipToOutline(true);

        return convertView;
    }

}
