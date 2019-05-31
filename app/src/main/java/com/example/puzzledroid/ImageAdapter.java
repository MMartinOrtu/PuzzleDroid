package com.example.puzzledroid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    // Contexto de la aplicación
    private Context mContext;
    private ArrayList<ImageData> imageChunks;

    private int imageWidth, imageHeight;




    public ImageAdapter(Context c) {
        mContext = c;
    }
    //constructor
    public ImageAdapter(Context c, ArrayList<ImageData> images ){
        mContext = c;
        imageChunks = images;
        imageWidth = images.get(0).getBitmap().getWidth();
        imageHeight = images.get(0).getBitmap().getHeight();

    }

    @Override
    public int getCount() {
        return imageChunks.size();
    }

    @Override
    public Object getItem(int position) {

        return imageChunks.get(position);
        //return null;
    }

    @Override
    public long getItemId(int position) {

        return position;
        //return 0;
    }
    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        //ImageView a retornar
        ImageView image;

        if(convertView == null){
			/*
			NOTE: I have set imageWidth - 10 and imageHeight
			as arguments to LayoutParams class.
			But you can take anything as per your requirement
			 */
            image = new ImageView(mContext);
            image.setLayoutParams(new GridView.LayoutParams(imageWidth - 0, imageHeight - 0));
            image.setPadding(0, 0, 0, 0);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);


        }else{
            image = (ImageView) convertView;

        }
        //Setear la imagen desde la array
        image.setImageBitmap(imageChunks.get(position).getBitmap());
        image.setContentDescription(Integer.toString(position));

        return image;
    }
}
