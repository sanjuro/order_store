package com.vosto.orders.services;

import com.vosto.VostoBaseActivity;
import com.vosto.services.OnRestReturn;
import com.vosto.services.RequestMethod;
import com.vosto.services.RestService;
import com.vosto.services.ResultType;

import static com.vosto.utils.CommonUtilities.SERVER_URL;


public class GetOrderByIdService extends RestService {
	
	public GetOrderByIdService(OnRestReturn listener, VostoBaseActivity context, int orderId){
		super(SERVER_URL + "/orders/" + orderId + "?authentication_token=" + context.getAuthenticationToken(), RequestMethod.GET, ResultType.GET_ORDER_BY_ID, listener, context);
	}

}