package com.vosto.orders.vos;

import java.io.Serializable;

import org.joda.money.Money;

/**
 * Models an order line item.
 * This is different from a CartItem because a LineItem is part of a completed order, fetched from the server
 * whereas a CartItem is created by the application before the order is placed.
 *
 */
public class LineItemVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int order_id;
    private String name;
    private String description;
    private int variant_id;
    private String option_values;
    private double price;
    private int quantity;
    private String special_instructions;
    private String sku;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getOrder_id() {
        return order_id;
    }
    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getVariant_id() {
        return variant_id;
    }
    public void setVariant_id(int variant_id) {
        this.variant_id = variant_id;
    }
    public String getOption_values() {
        return option_values;
    }
    public void setOption_values(String option_values) {
        this.option_values = option_values;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getSpecialInstructions() {
        return special_instructions;
    }
    public void setSpecialInstructions(String special_instructions) {
        this.special_instructions = special_instructions;
    }
    public String getSku() {
        return sku;
    }
    public void setSku(String sku) {
        this.sku = sku;
    }

}