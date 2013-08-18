package com.vosto.orders.services;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.vosto.services.IRestResult;
import com.vosto.services.RestResult;

import com.vosto.orders.vos.OrderVo;
import com.vosto.orders.vos.LineItemVo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MoveToInProgressResult extends RestResult implements IRestResult {

    private JSONObject jsonObj;
    private OrderVo order;
    private boolean orderUpdated;
    private String errorMessage;

    public MoveToInProgressResult(){
        super();
    }

    public MoveToInProgressResult(int responseCode, String responseJson){
        super(responseCode, responseJson);
    }

    public JSONObject getJSONObject(){
        return this.jsonObj;
    }

    public OrderVo getOrder(){
        return this.order;
    }

    public boolean wasUpdateSuccessful(){
        return this.orderUpdated;
    }

    public String getErrorMessage(){
        return this.errorMessage;
    }

    public boolean processJsonAndPopulate(){

        if(this.getResponseJson().toLowerCase(Locale.getDefault()).contains("couldn't find order")){
            this.order = null;
            return true;
        }

        Log.d("ORD", "Order by id response json: " + this.getResponseJson());

        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(this.getResponseJson());
            if(jsonObj.isNull("number")){
                this.orderUpdated = false;
                if(!jsonObj.isNull("detail")){
                    this.errorMessage = jsonObj.getString("detail");
                }
                return false;
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            this.orderUpdated = true;
            this.order = new OrderVo();
            this.order.setId(jsonObj.getInt("id"));
            this.order.setNumber(jsonObj.getString("number"));
            this.order.setStoreOrderNumber(jsonObj.getString("store_order_number"));
            this.order.setTimeToReady(jsonObj.getString("time_to_ready"));
            this.order.setStore_id(jsonObj.getInt("store_id"));
            this.order.setState(jsonObj.getString("state"));
            this.order.setCreatedAt(dateFormat.parse(jsonObj.getString("created_at")));
            this.order.setTotal(jsonObj.getString("total"));


            JSONArray lineItemsArr = jsonObj.getJSONArray("line_items");
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

        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }catch(ParseException pe){
            pe.printStackTrace();
        }

        return true;
    }

}