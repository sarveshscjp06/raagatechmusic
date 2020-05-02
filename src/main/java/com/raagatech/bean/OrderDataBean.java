/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.bean;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Sarvesh
 */
public class OrderDataBean {

    private int order_id;

    private long customer_id;

    private int vendor_id;

    private Date order_date;

    private Date delivery_date;

    private String delivery_time;

    private String delivery_person;
    
    private String delivery_address;

    private double price;

    private String status;

    private String comments;

    private ArrayList<OrderedItemBean> items;

    private CustomerAddressBean customerAddress;
    
    private String couponCode;

    /**
     * @return the order_id
     */
    public int getOrder_id() {
        return order_id;
    }

    /**
     * @param order_id the order_id to set
     */
    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    /**
     * @return the customer_id
     */
    public long getCustomer_id() {
        return customer_id;
    }

    /**
     * @param customer_id the customer_id to set
     */
    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    /**
     * @return the order_date
     */
    public Date getOrder_date() {
        return order_date;
    }

    /**
     * @param order_date the order_date to set
     */
    public void setOrder_date(Date order_date) {
        this.order_date = order_date;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the comments
     */
    public String getComments() {
        return comments;
    }

    /**
     * @param comments the comments to set
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * @return the items
     */
    public ArrayList<OrderedItemBean> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(ArrayList<OrderedItemBean> items) {
        this.items = items;
    }

    /**
     * @return the customerAddress
     */
    public CustomerAddressBean getCustomerAddress() {
        return customerAddress;
    }

    /**
     * @param customerAddress the customerAddress to set
     */
    public void setCustomerAddress(CustomerAddressBean customerAddress) {
        this.customerAddress = customerAddress;
    }

    /**
     * @return the vendor_id
     */
    public int getVendor_id() {
        return vendor_id;
    }

    /**
     * @param vendor_id the vendor_id to set
     */
    public void setVendor_id(int vendor_id) {
        this.vendor_id = vendor_id;
    }

    /**
     * @return the delivery_date
     */
    public Date getDelivery_date() {
        return delivery_date;
    }

    /**
     * @param delivery_date the delivery_date to set
     */
    public void setDelivery_date(Date delivery_date) {
        this.delivery_date = delivery_date;
    }

    /**
     * @return the delivery_time
     */
    public String getDelivery_time() {
        return delivery_time;
    }

    /**
     * @param delivery_time the delivery_time to set
     */
    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    /**
     * @return the deliveryPerson
     */
    public String getDelivery_person() {
        return delivery_person;
    }

    /**
     * @param delivery_person the delivery_person to set
     */
    public void setDelivery_person(String delivery_person) {
        this.delivery_person = delivery_person;
    }

    /**
     * @return the delivery_address
     */
    public String getDelivery_address() {
        return delivery_address;
    }

    /**
     * @param delivery_address the delivery_address to set
     */
    public void setDelivery_address(String delivery_address) {
        this.delivery_address = delivery_address;
    }

    /**
     * @return the couponCode
     */
    public String getCouponCode() {
        return couponCode;
    }

    /**
     * @param couponCode the couponCode to set
     */
    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
    
    
}
