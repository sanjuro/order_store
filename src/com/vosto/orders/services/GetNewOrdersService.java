package com.vosto.orders.services;

import android.util.Log;

import com.vosto.VostoBaseActivity;
import com.vosto.services.OnRestReturn;
import com.vosto.services.RequestMethod;
import com.vosto.services.RestService;
import com.vosto.services.ResultType;

import static com.vosto.utils.CommonUtilities.SERVER_URL;


public class GetNewOrdersService extends RestService {

    public GetNewOrdersService(OnRestReturn listener, VostoBaseActivity context, String storeId){
        super(SERVER_URL + "/stores/new_orders?authentication_token=" + context.getAuthenticationToken(), RequestMethod.POST, ResultType.GET_NEW_ORDERS, listener, context);
        Log.d("PREV", "Previous orders url: " + SERVER_URL + "/stores/new_orders?authentication_token=" + context.getAuthenticationToken());
    }

}