package com.vosto;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.vosto.orders.activities.DetailActivity;
import com.vosto.orders.fragments.OrderListFragment;
import com.vosto.orders.fragments.OrderDetailFragment;
import com.vosto.orders.services.GetNewOrdersResult;
import com.vosto.orders.services.GetNewOrdersService;
import com.vosto.orders.services.GetOrderByIdService;
import com.vosto.orders.vos.OrderVo;
import com.vosto.services.OnRestReturn;
import com.vosto.services.RestResult;

import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.DialogInterface.OnDismissListener;

public class HomeActivity extends VostoBaseActivity  implements OnRestReturn, OnItemClickListener, OnDismissListener, OnClickListener, OrderListFragment.OnItemSelectedListener{

    private static OrderVo[] newOrders;
    private LinearLayout lstNewOrders;
    protected static Dialog d;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView headerStoreDetails = (TextView) findViewById(R.id.headerStoreDetails);

        SharedPreferences settings = getSharedPreferences("VostoPreferences", 0);
        String userName = settings.getString("userName", "");

        headerStoreDetails.setText("Signed In as: " + userName);

        // initialize();
        fetchNewOrders();
    }

    @Override
    public void onOrderItemSelected(OrderVo order) {
        OrderDetailFragment detailFragment = (OrderDetailFragment) getFragmentManager()
                .findFragmentById(R.id.detailFragment);

        if (detailFragment != null && detailFragment.isInLayout()) {
            detailFragment.setOrder(order);
        } else {
            Intent intent = new Intent(getApplicationContext(),
                    DetailActivity.class);
            intent.putExtra("order", order);
            startActivity(intent);

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

            OrderListFragment listFragment = (OrderListFragment) getFragmentManager()
                    .findFragmentById(R.id.listFragment);

            if (listFragment != null && listFragment.isInLayout()) {
                listFragment.updateList(this.newOrders);
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
            Log.d("STO","Reload Orders");
            fetchNewOrders();
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
        fetchNewOrders();
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