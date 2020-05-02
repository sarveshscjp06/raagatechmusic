/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.bean;

import java.util.Date;

/**
 *
 * @author STripathi
 */
public class PaymentBean {
    
    private int orderId;
    
    private String couponCode;
    
    private String paymentMode;
    
    private double grossTotalPayment;
    
    private double gst;
    
    private double sgst;
    
    private double serviceCharges;
    
    private String transactionNo;
    
    private String invoiceNo;
    
    private String transactionDate;

    /**
     * @return the orderId
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    /**
     * @return the paymentMode
     */
    public String getPaymentMode() {
        return paymentMode;
    }

    /**
     * @param paymentMode the paymentMode to set
     */
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    /**
     * @return the grossTotalPayment
     */
    public double getGrossTotalPayment() {
        return grossTotalPayment;
    }

    /**
     * @param grossTotalPayment the grossTotalPayment to set
     */
    public void setGrossTotalPayment(double grossTotalPayment) {
        this.grossTotalPayment = grossTotalPayment;
    }

    /**
     * @return the gst
     */
    public double getGst() {
        return gst;
    }

    /**
     * @param gst the gst to set
     */
    public void setGst(double gst) {
        this.gst = gst;
    }

    /**
     * @return the sgst
     */
    public double getSgst() {
        return sgst;
    }

    /**
     * @param sgst the sgst to set
     */
    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    /**
     * @return the serviceCharges
     */
    public double getServiceCharges() {
        return serviceCharges;
    }

    /**
     * @param serviceCharges the serviceCharges to set
     */
    public void setServiceCharges(double serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    /**
     * @return the transactionNo
     */
    public String getTransactionNo() {
        return transactionNo;
    }

    /**
     * @param transactionNo the transactionNo to set
     */
    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    /**
     * @return the invoiceNo
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * @param invoiceNo the invoiceNo to set
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    /**
     * @return the transactionDate
     */
    public String getTransactionDate() {
        return transactionDate;
    }

    /**
     * @param transactionDate the transactionDate to set
     */
    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }
    
}
