package com.vosto.orders.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.LinkedList;
import java.util.Collections;

import com.vosto.orders.vos.CustomerVo;
import org.joda.money.Money;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.vosto.orders.vos.LineItemVo;
import com.vosto.orders.vos.OrderVo;
import com.vosto.orders.vos.AddressVo;
import com.vosto.services.IRestResult;
import com.vosto.services.RestResult;

public class CancelOrderResult extends RestResult implements IRestResult {

    private OrderVo order;

    public CancelOrderResult(){
        super();
    }

    public CancelOrderResult(int responseCode, String responseJson){
        super(responseCode, responseJson);
    }

    public OrderVo getOrder(){
        return this.order;
    }

    @Override
    public boolean processJsonAndPopulate(){
        try{

            if(this.getResponseJson().toLowerCase(Locale.getDefault()).contains("couldn't find order")){
                this.order = null;
                return true;
            }

            Log.d("ORD", "Order by id response json: " + this.getResponseJson());

            this.order = new OrderVo();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);

            JSONObject orderObj = new JSONObject(this.getResponseJson());
            this.order.setNumber(orderObj.getString("number"));
            this.order.setStoreOrderNumber(orderObj.getString("store_order_number"));
            this.order.setTimeToReady(orderObj.getString("time_to_ready"));
            this.order.setCreatedAt(dateFormat.parse(orderObj.getString("created_at")));
            this.order.setTotal(orderObj.getDouble("total"));
            this.order.setStore_id(orderObj.getInt("store_id"));
            this.order.setState(orderObj.getString("state"));

            if(!orderObj.isNull("adjustment_total")){
                this.order.setAdjustmentTotal(Money.parse("ZAR " + orderObj.getDouble("adjustment_total")));
            }

            //Add line items:
            JSONArray lineItemsArr = orderObj.getJSONArray("line_items");
            LineItemVo[] lineItems = new LineItemVo[lineItemsArr.length()];
            for(int i = 0; i<lineItemsArr.length(); i++){
                JSONObject lineItemObj = lineItemsArr.getJSONObject(i);
                LineItemVo lineItem = new LineItemVo();
                lineItem.setId(lineItemObj.getInt("id"));
                lineItem.setName(lineItemObj.getString("name"));
                lineItem.setOption_values(lineItemObj.getString("option_values"));
                lineItem.setOrder_id(lineItemObj.getInt("order_id"));
                lineItem.setQuantity(lineItemObj.getInt("quantity"));
                lineItem.setPrice(lineItemObj.getDouble("price"));
                lineItem.setSku(lineItemObj.getString("sku"));
                lineItem.setSpecialInstructions(lineItemObj.getString("special_instructions"));
                lineItem.setVariant_id(lineItemObj.getInt("variant_id"));
                lineItems[i] = lineItem;
            }

            this.order.setLineItems(lineItems);

            // Delivery Address:
            if(!orderObj.isNull("address")){
                AddressVo address = new AddressVo();
                JSONObject addressObj = orderObj.getJSONObject("address");
                address.setAddress1(addressObj.getString("address1"));
                address.setAddress2(addressObj.getString("address2"));
                address.setSuburb(addressObj.getString("suburb"));
                address.setSuburb_id(!addressObj.isNull("suburb_id") ? addressObj.getInt("suburb_id") : 1);
                address.setCity(addressObj.getString("city"));
                address.setState(addressObj.getString("state"));
                address.setCountry(addressObj.getString("country"));
                address.setZipcode(addressObj.getString("zip"));
                this.order.setDeliveryAddress(address);
            }

            return true;
        }catch(JSONException e){
            e.printStackTrace();
            return false;
        }catch(ParseException e){
            e.printStackTrace();
            return false;
        }
    }
}
