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
import android.widget.ListView;
import android.widget.TextView;
import com.vosto.R;
import com.vosto.orders.LineItemAdapter;
import com.vosto.orders.vos.CustomerVo;
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
        View baseView = inflater.inflate(R.layout.orderlist_fragment,
                container, false);

        this.lstNewOrders = (LinearLayout) baseView.findViewById(R.id.lstNewOrders);
        this.inflater = inflater;
        return baseView;
    }

    public interface OnItemSelectedListener {
        public void onRssItemSelected(String link);
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

    public void updateList(OrderVo[] newOrders){

        Log.d("PREV", "Num new orders: " + newOrders.length);

        for (int i = 0; i < newOrders.length; i++) {

            block = this.inflater.inflate(R.layout.order_item_block, null);

            final OrderVo currentOrder =  newOrders[i];

            Button moveOrder = (Button)block.findViewById(R.id.moveOrder);

            moveOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            TextView orderNumber = (TextView)block.findViewById(R.id.orderNumber);
            TextView mainCustomerName = (TextView)block.findViewById(R.id.mainCustomerName);
            TextView mainCustomerDetail = (TextView)block.findViewById(R.id.mainCustomerDetail);
            CustomerVo currentCustomer = currentOrder.getCustomer();

            orderNumber.setText(currentOrder.getNumber());
            mainCustomerDetail.setText(currentCustomer.getMobileNumber() + " | " + currentCustomer.getEmail());
            mainCustomerName.setText(currentCustomer.getName());

            ListView lilist = (ListView) block.findViewById(R.id.lineItemList);
            lilist.setAdapter(new LineItemAdapter(this.getActivity(), R.layout.line_item_list, currentOrder.getLineItems()));

            this.lstNewOrders.addView(block);

        }
    }


    // May also be triggered from the Activity
    public void updateDetail() {
        // Create fake data
        String newTime = String.valueOf(System.currentTimeMillis());
        // Send data to Activity
        listener.onRssItemSelected(newTime);
    }
}
