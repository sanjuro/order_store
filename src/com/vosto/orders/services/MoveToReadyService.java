package com.vosto.orders.services;

import android.provider.Settings;
import android.util.Log;
import com.vosto.VostoBaseActivity;
import com.vosto.services.OnRestReturn;
import com.vosto.services.RequestMethod;
import com.vosto.services.RestService;
import com.vosto.services.ResultType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.vosto.utils.CommonUtilities.SERVER_URL;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 2013/05/03
 * Time: 1:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class MoveToReadyService extends RestService {

    private int orderId;
    private String timeToReady;
    private String storeOrderNumber;
    private VostoBaseActivity context;


    public MoveToReadyService(OnRestReturn listener, VostoBaseActivity context, int orderId){
        super(SERVER_URL + "/orders/" + orderId + "/ready", RequestMethod.POST, ResultType.MOVE_TO_READY, listener, context);
        this.context = context;
        Log.d("ORDER", "Move Order to Ready " + SERVER_URL + "/orders/" + orderId + "/ready");
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getTimeToReady() {
        return timeToReady;
    }

    public void setTimeToReady(String timeToReady) {
        this.timeToReady = timeToReady;
    }

    public String getStoreOrderNumber() {
        return storeOrderNumber;
    }

    public void setStoreOrderNumber(String storeOrderNumber) {
        this.storeOrderNumber = storeOrderNumber;
    }

    public String getRequestJson(){

        try{
            JSONObject root = new JSONObject();
            root.put("authentication_token", this.context.getAuthenticationToken());
            root.put("time_to_ready", this.timeToReady);
            root.put("store_order_number", this.storeOrderNumber);
            Log.d("JSON", "Order Request JSON: " + root.toString());
            return root.toString();
        }catch(JSONException e){
            e.printStackTrace();
            return "";
        }
    }
}
