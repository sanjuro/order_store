package com.vosto.orders.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.content.DialogInterface.OnDismissListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.vosto.R;
import com.vosto.VostoBaseActivity;
import com.vosto.HomeActivity;
import com.vosto.orders.services.CancelOrderService;
import com.vosto.services.OnRestReturn;
import com.vosto.services.RestResult;

import com.vosto.orders.vos.OrderVo;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 2013/08/18
 * Time: 11:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class CancelOrderActivity extends VostoBaseActivity implements OnRestReturn, OnItemClickListener, OnDismissListener, OnClickListener {

    private OrderVo order;

    @Override
    public void onCreate(Bundle args)
    {
        super.onCreate(args);
        setContentView(R.layout.activity_cancel_order);

        this.order = (OrderVo)getIntent().getSerializableExtra("order");


        Button cancelButton = (Button)findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                cancelledPressed(v);
            }
        });
    }

    /**
     * Called from within the base RestService after a rest call completes.
     * @param result Can be any result type. This function should check the type and handle accordingly.
     */
    @Override
    public void onRestReturn(RestResult result) {
        // send toast message
        CharSequence text = "Order has been cancelled";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();

        Intent intent = new Intent(this, HomeActivity.class);
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

    public void cancelledPressed(View v){
        OrderVo order = (OrderVo)getIntent().getSerializableExtra("order");
        Log.d("ORDER", "Cancelling Order ID " + order.getId());
        //Make the REST call:
        CancelOrderService service = new CancelOrderService(this, this, order.getId());
        service.execute();
    }

}
