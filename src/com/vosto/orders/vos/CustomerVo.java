package com.vosto.orders.vos;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: macbookpro
 * Date: 2013/05/01
 * Time: 6:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomerVo implements Serializable {

    private int id;
    private String name;
    private String mobile_number;
    private String email;

    public CustomerVo() {
        id = 0;
        name = "";
        mobile_number = "";
        email = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobile_number;
    }

    public void setMobileNumber(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}