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
import com.vosto.orders.services.MoveToInProgressService;
import com.vosto.services.OnRestReturn;
import com.vosto.services.RestResult;

import com.vosto.orders.vos.OrderVo;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 2013/05/01
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class InProgressActivity extends VostoBaseActivity implements OnRestReturn, OnItemClickListener, OnDismissListener, OnClickListener, OnSeekBarChangeListener {

    private OrderVo order;
    private SeekBar slider;
    private int mintime = 5;
    private String unit = " min";

    @Override
    public void onCreate(Bundle args)
    {
        super.onCreate(args);
        setContentView(R.layout.activity_in_progress);

        this.order = (OrderVo)getIntent().getSerializableExtra("order");

        //set click states for the up and down buttons
        final TextView txt = (TextView) findViewById(R.id.sliderValue);
        final EditText pos = (EditText) findViewById(R.id.posOrderNumber);
        pos.clearFocus();


        txt.setText(String.valueOf(mintime)+unit);

        Button confMoveProg = (Button)findViewById(R.id.confMoveProg);
        confMoveProg.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                inProgressPressed(v);
            }
        });

        this.slider = (SeekBar)findViewById(R.id.slider);
        this.slider.setOnSeekBarChangeListener(this);
    }

    /**
     * Called from within the base RestService after a rest call completes.
     * @param result Can be any result type. This function should check the type and handle accordingly.
     */
    @Override
    public void onRestReturn(RestResult result) {
        // send toast message
        CharSequence text = "Order has been updated";
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

    public void inProgressPressed(View v){
        EditText numberEditText = (EditText)findViewById(R.id.sliderValue);
        EditText posOrderNumber = (EditText)findViewById(R.id.posOrderNumber);

        String timeToReady = numberEditText.getText().toString().trim();
        String storeOrderNumber = posOrderNumber.getText().toString().trim();

        String[] nameParts = timeToReady.split("\\s");
        String timeReady = nameParts[0];

        OrderVo order = (OrderVo)getIntent().getSerializableExtra("order");
        Log.d("ORDER", "Order ID " + order.getId());
        //Make the REST call:
        MoveToInProgressService service = new MoveToInProgressService(this, this, order.getId());
        service.setOrderId(order.getId());
        service.setTimeToReady(timeReady);
        service.setStoreOrderNumber(storeOrderNumber);
        service.execute();
    }

    @Override
    public void onProgressChanged(SeekBar v, int progress, boolean isUser) {
        EditText numberEditText = (EditText)findViewById(R.id.sliderValue);
        numberEditText.setText(String.valueOf(progress)+unit);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }
}
