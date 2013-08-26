package com.vosto.orders.vos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.joda.money.Money;
import org.json.JSONArray;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class OrderVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
    public CustomerVo customer;
	private String number;
    private String store_order_number;
	private String state;
    private String time_to_ready;
	private int store_id;
    private String store_name;
    private String store_contact;
	private Date createdAt;
	private Date completedAt;
	private LineItemVo[] line_items;
    public boolean is_delivery;
    private Money adjustmentTotal;
    public AddressVo delivery_address;
	private Money total;
    // public ArrayList<LineItemVo> line_items;
	
	public OrderVo(){
        customer = null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public CustomerVo getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerVo customer) {
        this.customer = customer;
    }

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

    public String getStoreOrderNumber() {
        return store_order_number;
    }

    public void setStoreOrderNumber(String store_order_number) {
        this.store_order_number = store_order_number;
    }

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

    public String getTimeToReady() {
        return time_to_ready;
    }

    public void setTimeToReady(String time_to_ready) {
        this.time_to_ready = time_to_ready;
    }

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

    public String getStoreName() {
        return store_name;
    }

    public void setStoreName(String store_name) {
        this.store_name = store_name;
    }

    public String getStoreContact() {
        return store_contact;
    }

    public void setStoreContact(String store_contact) {
        this.store_contact = store_contact;
    }

	public Money getTotal() {
		return total;
	}

	public void setTotal(double dblTotal) {
        this.total = Money.parse("ZAR " + dblTotal);
        this.total = this.total.withAmount(dblTotal);
	}
	
//	public void setTotal(double dblTotal){
//		this.total = Money.parse("ZAR " + dblTotal);
//		this.total = this.total.withAmount(dblTotal);
//	}

    public LineItemVo[] getLineItems() {
        return line_items;
    }

    public void setLineItems(LineItemVo[] line_items) {
        this.line_items = line_items;
    }

    public Money getAdjustmentTotal() {
        return adjustmentTotal;
    }

    public void setAdjustmentTotal(double dblAdjustmentTotal){
        this.adjustmentTotal = Money.parse("ZAR " + dblAdjustmentTotal);
        this.adjustmentTotal = this.adjustmentTotal.withAmount(dblAdjustmentTotal);
    }

    public void setAdjustmentTotal(Money adjustmentTotal) {
        this.adjustmentTotal = adjustmentTotal;
    }

    public AddressVo getDeliveryAddress() {
        return delivery_address;
    }

    public void setDeliveryAddress(AddressVo delivery_address) {
        this.delivery_address = delivery_address;
    }
}