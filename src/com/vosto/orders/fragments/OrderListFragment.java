package com.vosto.orders.fragments;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.app.Fragment;

import android.widget.LinearLayout;
import android.widget.TextView;
import com.vosto.R;
import com.vosto.orders.vos.OrderVo;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 2013/05/04
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class OrderListFragment extends Fragment{

    private OnItemSelectedListener listener;
    private LinearLayout lstNewOrders;
    private View baseView;
    private LayoutInflater inflater;
    private View block;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.baseView = inflater.inflate(R.layout.orderlist_fragment, container, false);
        this.lstNewOrders = (LinearLayout) baseView.findViewById(R.id.lstNewOrders);
        this.inflater = inflater;

        return baseView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement OrderListFragment.OnItemSelectedListener");
        }
    }

    public interface OnItemSelectedListener {
        public void onOrderItemSelected(OrderVo order);
    }

    // May also be triggered from the Activity
    public void updateDetail(OrderVo order) {
        // Send data to Activity
        listener.onOrderItemSelected(order);
    }

    public void updateList(OrderVo[] newOrders){

        this.lstNewOrders.removeAllViews();
        for (int i = 0; i < newOrders.length; i++) {

            block = this.inflater.inflate(R.layout.order_item_list, null);

            final OrderVo currentOrder = newOrders[i];

            TextView orderNumber = (TextView)block.findViewById(R.id.orderNumber);

            Button viewOrder = (Button)block.findViewById(R.id.viewOrder);

            viewOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateDetail(currentOrder);
                }
            });

            orderNumber.setText(currentOrder.getNumber());
            this.lstNewOrders.addView(block);
        }

   }


}
