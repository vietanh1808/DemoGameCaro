package com.example.administrator.cocaro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 3/14/2018.
 */

public class AdapterGridViewReplay extends BaseAdapter{
    Context myContext;
    int myLayout;
    ArrayList<Integer> list;

    public AdapterGridViewReplay(Context myContext, int myLayout, ArrayList<Integer> list) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(myLayout, null);

        CustomTextView customTextView = view.findViewById(R.id.custom_text);
        customTextView.setBackgroundResource(R.drawable.oco);

        if (list.get(i)==1) customTextView.setBackgroundResource(R.drawable.ocox);
        if (list.get(i)==-1) customTextView.setBackgroundResource(R.drawable.ocoo);

        return view;
    }
}
