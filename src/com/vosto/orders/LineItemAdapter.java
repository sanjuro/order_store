package com.vosto.orders;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vosto.R;
import com.vosto.orders.vos.LineItemVo;

import com.vosto.utils.StringUtils;

public class LineItemAdapter extends ArrayAdapter<LineItemVo> {

    Context context;
    int layoutResourceId;
    LineItemVo[] lineItems = null;

    public LineItemAdapter(Context context, int layoutResourceId, LineItemVo[] lineItems) {
        super(context, layoutResourceId, lineItems);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.lineItems = lineItems;
    }

    public int getCount() {
        return lineItems.length;
    }

    public LineItemVo getItem(int position) {
        return lineItems[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LineItemAdapterHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new LineItemAdapterHolder();
            holder.itemname = (TextView)row.findViewById(R.id.itemName);
            holder.itemdesc = (TextView)row.findViewById(R.id.itemDescrip);
            holder.itemextras = (TextView)row.findViewById(R.id.itemExtras);
            holder.iteminstruct = (TextView)row.findViewById(R.id.itemInstruct);
            holder.itemprice = (TextView)row.findViewById(R.id.itemPrice);
            holder.itemmany = (TextView)row.findViewById(R.id.itemMany);

            row.setTag(holder);
        }
        else
        {
            holder = (LineItemAdapterHolder)row.getTag();
        }

        LineItemVo item = this.lineItems[position];

        String instruct = item.getSpecialInstructions();
        if (instruct.length()<1) {
            instruct = "None";
        }
        holder.iteminstruct.setText("Special Instructions:\n"+instruct);
        if (item.getQuantity()>1) {
            //Set the time in
            holder.itemmany.setText(String.valueOf(item.getQuantity())+" x "+item.getPrice()+" =");
        }

        holder.itemname.setText(String.valueOf(item.getQuantity())+"x "+item.getName());
        holder.itemdesc.setText(item.getDescription());
        holder.itemextras.setText(item.getOption_values());
        holder.itemprice.setText(StringUtils.asPrice(item.getPrice()));
        // total.setText(StringUtils.asPrice(li.getPrice()));

        // holder.lblPrice.setText(MoneyUtils.getRandString(item.getPrice()));
        // holder.lblQuantity.setText(item.getQuantity());

        return row;

    }

    static class LineItemAdapterHolder
    {
        TextView itemname;
        TextView itemdesc;
        TextView itemextras;
        TextView iteminstruct;
        TextView itemprice;
        TextView itemmany;
    }
}

