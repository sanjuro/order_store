package com.vosto.orders.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vosto.R;
import com.vosto.VostoBaseActivity;
import com.vosto.orders.LineItemAdapter;
import com.vosto.orders.services.MoveToReadyService;
import com.vosto.orders.vos.CustomerVo;
import com.vosto.orders.vos.OrderVo;
import com.vosto.orders.vos.LineItemVo;

import com.vosto.utils.MoneyUtils;
import com.vosto.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DetailActivity extends VostoBaseActivity {

    private LayoutInflater inflater;
    private LinearLayout detailOrder;
    private View block;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.inflater = getLayoutInflater();

        // Need to check if Activity has been switched to landscape mode
        // If yes, finished and go back to the start Activity
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }
        setContentView(R.layout.activity_detail);

        this.detailOrder = (LinearLayout) findViewById(R.id.detailOrder);

        OrderVo order = (OrderVo) getIntent().getSerializableExtra("order");
        if (order != null) {
            setOrder(order);
        }
    }

    public void setOrder(OrderVo order) {
        Log.d("ORD", "Got Order Detail Number " + order.getNumber());

        block = this.inflater.inflate(R.layout.order_item_block, null);

        final OrderVo currentOrder = order;

        Button moveOrder = (Button)block.findViewById(R.id.moveOrder);

        moveOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), InProgressActivity.class);
                intent.putExtra("order", currentOrder);
                startActivity(intent);
            }
        });

        Button readyOrder = (Button)block.findViewById(R.id.readyOrder);

        readyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReadyActivity.class);
                intent.putExtra("order", currentOrder);
                startActivity(intent);
            }
        });

        Button cancelOrder = (Button)block.findViewById(R.id.cancelOrder);

        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CancelOrderActivity.class);
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

        orderNumber.setText(currentOrder.getNumber());
        storeName.setText(currentOrder.getStoreName());
        storeContact.setText(currentOrder.getStoreContact());
        dateOrdered.setText("Ordered at: " + format.format(order.getCreatedAt()));
        mainCustomerDetail.setText(currentCustomer.getMobileNumber() + " | " + currentCustomer.getEmail());
        mainCustomerName.setText(currentCustomer.getName());

        // totalAmount.setText(currentOrder.getTotal());
        totalAmount.setText(MoneyUtils.getRandString(currentOrder.getTotal()));

//        LinearLayout lilist = (LinearLayout) block.findViewById(R.id.lineItemList);
//        lilist.setAdapter(new LineItemAdapter(this, R.layout.line_item_list, currentOrder.getLineItems()));

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

        this.detailOrder.addView(block);
    }
}