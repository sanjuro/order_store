package com.vosto.orders.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.vosto.R;
import com.vosto.orders.LineItemAdapter;
import com.vosto.orders.activities.CancelOrderActivity;
import com.vosto.orders.activities.InProgressActivity;
import com.vosto.orders.activities.ReadyActivity;
import com.vosto.orders.vos.CustomerVo;
import com.vosto.orders.vos.LineItemVo;
import com.vosto.orders.vos.OrderVo;
import com.vosto.utils.MoneyUtils;
import com.vosto.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class OrderDetailFragment extends Fragment {

    private View baseView;
    private LayoutInflater inflater;
    private LinearLayout detailOrder;
    private View block;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.baseView = inflater.inflate(R.layout.orderdetail_fragment,container, false);
        this.detailOrder = (LinearLayout) baseView.findViewById(R.id.detailOrder);
        this.inflater = inflater;
        return baseView;
    }

    public void setOrder(OrderVo order) {
        Log.d("ORD","Got Order Detail # " + order.getNumber());
        block = this.inflater.inflate(R.layout.order_item_block, null);

        final OrderVo currentOrder = order;

        Button moveOrder = (Button)block.findViewById(R.id.moveOrder);

        moveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InProgressActivity.class);
                intent.putExtra("order", currentOrder);
                startActivity(intent);
            }
        });

        Button readyOrder = (Button)block.findViewById(R.id.readyOrder);

        readyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReadyActivity.class);
                intent.putExtra("order", currentOrder);
                startActivity(intent);
            }
        });

        Button cancelOrder = (Button)block.findViewById(R.id.cancelOrder);

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CancelOrderActivity.class);
                intent.putExtra("order", currentOrder);
                startActivity(intent);
            }
        });

        TextView orderNumber = (TextView)block.findViewById(R.id.orderNumber);
        TextView storeName = (TextView)block.findViewById(R.id.storeName);
        TextView storeContact = (TextView)block.findViewById(R.id.storeContact);
        TextView orderState  = (TextView)block.findViewById(R.id.orderState);
        TextView dateOrdered = (TextView)block.findViewById(R.id.dateOrdered);
        TextView mainCustomerName = (TextView)block.findViewById(R.id.mainCustomerName);
        TextView mainCustomerDetail = (TextView)block.findViewById(R.id.mainCustomerDetail);
        CustomerVo currentCustomer = currentOrder.getCustomer();

        TextView delivMethod = (TextView)block.findViewById(R.id.delivMethod);
        TextView delivMethodAddr = (TextView)block.findViewById(R.id.delivMethodAddr);
        TextView delivMethodPrice = (TextView)block.findViewById(R.id.delivMethodPrice);

        if(currentOrder.getDeliveryAddress() != null && !currentOrder.getDeliveryAddress().isEmpty() && currentOrder.getAdjustmentTotal() != null){
            delivMethodPrice.setText(MoneyUtils.getRandString(currentOrder.getAdjustmentTotal()));
            delivMethod.setText("Delivery");
            delivMethodAddr.setText(currentOrder.getDeliveryAddress().toString());
        }else{
            delivMethodAddr.setVisibility(View.GONE);
            delivMethodPrice.setText("R0.00");
            delivMethod.setText("Collect in Store");
        }

        TextView totalAmount = (TextView)block.findViewById(R.id.totalAmnt);

        SimpleDateFormat format = new SimpleDateFormat("HH:mm, d MMMM yyyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT+4"));

        orderNumber.setText(currentOrder.getNumber());
        storeName.setText(currentOrder.getStoreName());
        storeContact.setText(currentOrder.getStoreContact());
        dateOrdered.setText("Ordered at: " + format.format(order.getCreatedAt()));
        dateOrdered.setTypeface(null, Typeface.ITALIC);

        //Show the correct status badge based on the order state:
        if(order.getState().toLowerCase(Locale.getDefault()).equals("ready")){
            orderState.setText("READY");
        }else if(order.getState().toLowerCase(Locale.getDefault()).equals("collected")){
            orderState.setText("COLLECTED");
        }else if(order.getState().toLowerCase(Locale.getDefault()).equals("in_progress")){
            orderState.setText("IN PROGRESS");
        }else if(order.getState().toLowerCase(Locale.getDefault()).equals("cancelled")){
            orderState.setText("CANCELLED");
        }else if(order.getState().toLowerCase(Locale.getDefault()).equals("not_collected")){
            orderState.setText("NOT COLLECTED");
        }else{
            orderState.setText("NEW");
        }

        mainCustomerDetail.setText(currentCustomer.getMobileNumber() + " | " + currentCustomer.getEmail());
        mainCustomerName.setText(currentCustomer.getName());
        // totalAmount.setText(currentOrder.getTotal());
        totalAmount.setText(MoneyUtils.getRandString(currentOrder.getTotal()));

        LinearLayout list = (LinearLayout) block.findViewById(R.id.lineItemList);
        for (int i=0; i < currentOrder.getLineItems().length; i++) {
            LineItemVo lineItem = currentOrder.getLineItems()[i];
            View vi = inflater.inflate(R.layout.line_item_list, null);

            TextView itemname = (TextView) vi.findViewById(R.id.itemName);
            TextView itemdesc = (TextView) vi.findViewById(R.id.itemDescrip);
            TextView itemextras = (TextView) vi.findViewById(R.id.itemExtras);
            TextView iteminstruct = (TextView) vi.findViewById(R.id.itemInstruct);
            TextView itemprice = (TextView) vi.findViewById(R.id.itemPrice);
            TextView itemmany = (TextView) vi.findViewById(R.id.itemMany);

            String instruct = lineItem.getSpecialInstructions();
            if (instruct.length()<1) {
                instruct = "None";
            }
            iteminstruct.setText("Special Instructions:\n"+instruct);
            if (lineItem.getQuantity()>1) {
                //Set the time in
                itemmany.setText(String.valueOf(lineItem.getQuantity())+" x "+lineItem.getPrice()+" =");
            }

            itemname.setText(String.valueOf(lineItem.getQuantity())+"x "+lineItem.getName());
            itemdesc.setText(lineItem.getDescription());
            itemextras.setText(lineItem.getOption_values());
            itemprice.setText(StringUtils.asPrice(lineItem.getPrice()));

            list.addView(vi);
        }

        this.detailOrder.removeAllViews();
        this.detailOrder.addView(block);
    }
}

