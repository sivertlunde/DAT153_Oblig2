package com.example.oblig1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomList extends ArrayAdapter<Image>{

    private final Activity context;

    public CustomList(Activity context, ArrayList<Image> images) {
        super(context, R.layout.list_single);
        this.context = context;
        this.addAll(images);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.list_single, null, true);

        TextView txtTitle = (TextView) view.findViewById(R.id.txt);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        txtTitle.setText(this.getItem(position).getName());
        imageView.setImageBitmap(this.getItem(position).getBitmap());
        return view;
    }

}