package com.vosto;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.vosto.orders.LineItemAdapter;
import com.vosto.orders.activities.InProgressActivity;
import com.vosto.orders.fragments.OrderListFragment;
import com.vosto.orders.fragments.OrderDetailFragment;
import com.vosto.orders.services.GetNewOrdersResult;
import com.vosto.orders.services.GetNewOrdersService;
import com.vosto.orders.services.GetOrderByIdService;
import com.vosto.orders.vos.CustomerVo;
import com.vosto.orders.vos.OrderVo;
import com.vosto.services.OnRestReturn;
import com.vosto.services.RestResult;

import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

public class HomeActivity extends VostoBaseActivity  implements OnRestReturn, OnItemClickListener, OnDismissListener, OnClickListener, OrderListFragment.OnItemSelectedListener{

    private static OrderVo[] newOrders;
    private LinearLayout lstNewOrders;
    private View block;
    protected static Dialog d;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initialize();
    }

    // if the wizard generated an onCreateOptionsMenu you can delete
    // it, not needed for this tutorial

    @Override
    public void onRssItemSelected(String link) {
        OrderDetailFragment fragment = (OrderDetailFragment) getFragmentManager()
                .findFragmentById(R.id.detailFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.setText(link);
        }
    }

    /**
     * Called from within the base RestService after a rest call completes.
     * @param result Can be any result type. This function should check the type and handle accordingly.
     */
    @Override
    public void onRestReturn(RestResult result) {
        if(result == null){
            return;
        }

        if(result instanceof GetNewOrdersResult){
            // We received a list of previous orders:
            GetNewOrdersResult ordersResult = (GetNewOrdersResult)result;
            this.newOrders = ordersResult.getOrders();
            if(this.newOrders == null){
                this.newOrders = new OrderVo[0];
            }
            Log.d("PREV", "Num new orders: " + this.newOrders.length);


            this.lstNewOrders = (LinearLayout) findViewById(R.id.lstNewOrders);

            for (int i = 0; i < this.newOrders.length; i++) {

                block = getLayoutInflater().inflate(R.layout.order_item_block, null);

                final OrderVo currentOrder =  this.newOrders[i];

                Button moveOrder = (Button)block.findViewById(R.id.moveOrder);

                moveOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), InProgressActivity.class);
                        intent.putExtra("order", currentOrder);
                        startActivity(intent);
                        finish();
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
                lilist.setAdapter(new LineItemAdapter(this, R.layout.line_item_list, currentOrder.getLineItems()));

                this.lstNewOrders.addView(block);
            }

        }

    }

    /**
     * If an order_id has been passed through the intent, it means we are coming from a notification,
     * so we must fetch and display that specific order.
     * Otherwise, if we have an order stored on the device, we display that order.
     * Otherwise we have no order to display, so we just display the previous orders list.
     */
    private void initialize(){
        if(getIntent().hasExtra("order_id") && getIntent().getIntExtra("order_id", -1) > 0){
            // User has clicked a notification. Load the specified order:
            if(this.pleaseWaitDialog != null && this.pleaseWaitDialog.isShowing()){
                this.pleaseWaitDialog.dismiss();
            }
            GetOrderByIdService service = new GetOrderByIdService(this, this, getIntent().getIntExtra("order_id", -1));
            service.execute();
        }else{
            // We have no specific order to display. Just show the new orders list:
            this.newOrders = new OrderVo[0];
            if(this.pleaseWaitDialog != null && this.pleaseWaitDialog.isShowing()){
                //If the dialog is already showing, dismiss it otherwise we will have a duplicate dialog.
                this.pleaseWaitDialog.dismiss();
            }
            fetchNewOrders();
        }
    }

    /**
     * Reload the list of previous orders from Vosto.
     */
    private void fetchNewOrders(){
        if(this.pleaseWaitDialog != null && this.pleaseWaitDialog.isShowing()){
            this.pleaseWaitDialog.dismiss();
        }

        SharedPreferences settings = getSharedPreferences("VostoPreferences", 0);
        String storeId = settings.getString("userStoreId", "1");
        Log.d("STO","Fetch Orders for Store: " + storeId);
        GetNewOrdersService service = new GetNewOrdersService(this, this, storeId);
        service.execute();
    }

    public void getNewOrders(View v) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

    }

    @Override
    public void onClick(View v) {

    }

    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return newOrders.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View retval = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_block, null);
            TextView title = (TextView) retval.findViewById(R.id.mainCustomerName);
            title.setText(newOrders[position].getNumber());

            return retval;
        }


    };

}