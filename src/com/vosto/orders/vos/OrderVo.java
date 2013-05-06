package com.vosto.orders.vos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.joda.money.Money;

import com.vosto.orders.vos.CustomerVo;

public class OrderVo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
    public CustomerVo customer;
	private String number;
    private String store_order_number;
	private String state;
    private String time_to_ready;
	private int store_id;
	private Date createdAt;
	private Date completedAt;
	private LineItemVo[] line_items;
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

	public Money getTotal() {
		return total;
	}

	public void setTotal(Money total) {
		this.total = total;
	}
	
	public void setTotal(double dblTotal){
		this.total = Money.parse("ZAR " + dblTotal);
		this.total = this.total.withAmount(dblTotal);
	}

    public LineItemVo[] getLineItems() {
        return line_items;
    }

    public void setLineItems(LineItemVo[] line_items) {
        this.line_items = line_items;
    }
}