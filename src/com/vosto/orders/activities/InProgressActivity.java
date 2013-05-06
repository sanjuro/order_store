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
import com.vosto.DashboardActivity;
import com.vosto.orders.services.MoveToInProgressService;
import com.vosto.services.OnRestReturn;
import com.vosto.services.RestResult;

import com.vosto.orders.vos.OrderVo;

import static com.vosto.utils.CommonUtilities.SERVER_URL;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 2013/05/01
 * Time: 5:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class InProgressActivity extends VostoBaseActivity implements OnRestReturn, OnItemClickListener, OnDismissListener, OnClickListener {

    private OrderVo order;

    @Override
    public void onCreate(Bundle args)
    {
        super.onCreate(args);
        setContentView(R.layout.activity_in_progress);

        this.order = (OrderVo)getIntent().getSerializableExtra("order");

        //set click states for the up and down buttons
        final Button up = (Button) findViewById(R.id.upButton);
        final Button down = (Button) findViewById(R.id.downButton);
        final TextView txt = (TextView) findViewById(R.id.numberEditText);
        final EditText pos = (EditText) findViewById(R.id.posOrderNumber);
        pos.clearFocus();
        final int increment = 5;
        final int maxtime = 60;
        final int mintime = 5;
        final String unit = " min";

        txt.setText(String.valueOf(mintime)+unit);

        up.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                //increase the time
                CharSequence s = txt.getText();
                String showntime = s.toString();
                showntime = showntime.substring( 0, (showntime.length()-unit.length()) );
                int time = Integer.parseInt(showntime);
                time += increment;
                if (time>maxtime) {
                    time = maxtime;
                }
                showntime = String.valueOf(time)+unit;
                txt.setText(showntime);
            }
        });

        down.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                //reduce the time
                CharSequence s = txt.getText();
                String showntime = s.toString();
                showntime = showntime.substring( 0, (showntime.length()-unit.length()) );
                int time = Integer.parseInt(showntime);
                time -= increment;
                if (time<mintime) {
                    time = mintime;
                }
                showntime = String.valueOf(time)+unit;
                txt.setText(showntime);
            }
        });

        Button confMoveProg = (Button)findViewById(R.id.confMoveProg);
        confMoveProg.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                inProgressPressed(v);
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
        CharSequence text = "Order has been updated";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();

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

    public void inProgressPressed(View v){
        EditText numberEditText = (EditText)findViewById(R.id.numberEditText);
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
}
