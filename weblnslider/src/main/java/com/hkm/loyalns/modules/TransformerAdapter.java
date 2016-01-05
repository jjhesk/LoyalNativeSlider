package com.hkm.loyalns.modules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hkm.loyalns.R;
import com.hkm.slider.TransformerL;


/**
 * Created by daimajia on 14-5-29.
 */
public class TransformerAdapter extends BaseAdapter{
    private Context mContext;
    public TransformerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return TransformerL.values().length;
    }

    @Override
    public Object getItem(int position) {
        return TransformerL.values()[position].toString();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView t = (TextView)LayoutInflater.from(mContext).inflate(R.layout.item,null);
        t.setText(getItem(position).toString());
        return t;
    }
}
