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


public class CancelOrderService extends RestService {

    private int orderId;
    private VostoBaseActivity context;

    public CancelOrderService(OnRestReturn listener, VostoBaseActivity context, int orderId){
        super(SERVER_URL + "/orders/" + orderId + "/cancel?authentication_token=" + context.getAuthenticationToken(), RequestMethod.POST, ResultType.CANCEL_ORDER, listener, context);
        this.context = context;
        this.orderId = orderId;
        Log.d("PREV", "Previous orders url: " + SERVER_URL + "/orders/" + orderId + "/cancel?authentication_token=" + context.getAuthenticationToken());
    }



    public String getRequestJson(){
       return this.getCancelOrderRequestJson();
    }

    private String getCancelOrderRequestJson(){
        try{
            JSONObject root = new JSONObject();
            root.put("authentication_token", this.context.getAuthenticationToken());
            root.put("id", this.orderId);
            Log.d("jsontest", "Cancel Order JSON  :" + root.toString());
            return root.toString();
        }catch(JSONException e){
            e.printStackTrace();
            return "";
        }

    }

}
