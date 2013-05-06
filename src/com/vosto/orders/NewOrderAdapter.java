package com.vosto.orders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.vosto.R;
import com.vosto.orders.vos.OrderVo;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 2013/05/01
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class NewOrderAdapter extends ArrayAdapter<OrderVo> {

    Context context;
    int layoutResourceId;
    OrderVo[] orders = null;

    public NewOrderAdapter(Context context, int layoutResourceId, OrderVo[] orders) {
        super(context, layoutResourceId, orders);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.orders = orders;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        OrderHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new OrderHolder();
            holder.lblOrderNumber = (TextView)row.findViewById(R.id.mainCustomerName);
            holder.lblDateOrdered = (TextView)row.findViewById(R.id.mainCustomerDetail);
            row.setTag(holder);
        }
        else
        {
            holder = (OrderHolder)row.getTag();
        }

        OrderVo order = orders[position];

        if (order.getStoreOrderNumber() == null){
            holder.lblOrderNumber.setText(order.getNumber());
        }else{
            holder.lblOrderNumber.setText(order.getStoreOrderNumber());
        }

        SimpleDateFormat format = new SimpleDateFormat("HH:mm, d MMMM yyyy", Locale.US);

        holder.lblDateOrdered.setText("Ordered at: " + format.format(order.getCreatedAt()));
        holder.lblDateOrdered.setTypeface(null, Typeface.ITALIC);


        return row;
    }

    static class OrderHolder
    {
        TextView lblOrderNumber;
        TextView lblDateOrdered;
        ImageView lblOrderStatusBadge;
    }
}
