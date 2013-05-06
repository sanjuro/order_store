package com.vosto;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vosto.orders.vos.OrderVo;

/**
 * The overridden application context, so that we can store the cart, authtoken, order, etc in the app
 * while it is running. This should be used whenever an application context is required.
 *
 */
public class VostoCustomerApp extends Application {
  private OrderVo currentOrder;

  public void saveCurrentOrder(OrderVo order){
	  if(order != null){
		  this.currentOrder = order;
		  Gson gson = new Gson();
		  SharedPreferences settings = getSharedPreferences("VostoPreferences", 0);
		  SharedPreferences.Editor editor = settings.edit();
		  editor.putString("currentOrderJson", gson.toJson(this.currentOrder));
		  editor.commit();
	  }else{
		  this.currentOrder = null;
		  SharedPreferences settings = getSharedPreferences("VostoPreferences", 0);
		  SharedPreferences.Editor editor = settings.edit();
		  editor.remove("currentOrderJson");
		  editor.commit();
	  }
  }
  
  public boolean hasPendingOrder(){
	  return this.currentOrder != null;
  }
  
  public OrderVo getCurrentOrder(){
	  Gson gson = new Gson();
	  SharedPreferences settings = getSharedPreferences("VostoPreferences", 0);
	  if(!settings.contains("currentOrderJson")){
		  return null;
	  }
	  try{
		  return gson.fromJson(settings.getString("currentOrderJson", ""), OrderVo.class);
	  }catch(JsonSyntaxException e){
		  e.printStackTrace();
		  return null;
	  }
  }
  
  public String getAuthenticationToken(){
	  SharedPreferences settings = getSharedPreferences("VostoPreferences", 0);
	  String token = settings.getString("userToken", "DXTTTTED2ASDBSD3");
	  if(token.trim().equals("")){
		token = "DXTTTTED2ASDBSD3";
	  }
	  return token;
  }
  
}
