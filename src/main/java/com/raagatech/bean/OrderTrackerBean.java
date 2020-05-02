/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.bean;

/**
 *
 * @author STripathi
 */
public class OrderTrackerBean {
    
    private int trackerId;
    
    private int orderId;
    
    private int deliveryPersonId;
    
    private String readyTime;
    
    private String outToTime;
    
    private String deliveryTime;
    
    private String comment;
    
    private String feedback;

    /**
     * @return the trackerId
     */
    public int getTrackerId() {
        return trackerId;
    }

    /**
     * @param trackerId the trackerId to set
     */
    public void setTrackerId(int trackerId) {
        this.trackerId = trackerId;
    }

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
     * @return the deliveryPersonId
     */
    public int getDeliveryPersonId() {
        return deliveryPersonId;
    }

    /**
     * @param deliveryPersonId the deliveryPersonId to set
     */
    public void setDeliveryPersonId(int deliveryPersonId) {
        this.deliveryPersonId = deliveryPersonId;
    }

    /**
     * @return the readyTime
     */
    public String getReadyTime() {
        return readyTime;
    }

    /**
     * @param readyTime the readyTime to set
     */
    public void setReadyTime(String readyTime) {
        this.readyTime = readyTime;
    }

    /**
     * @return the outToTime
     */
    public String getOutToTime() {
        return outToTime;
    }

    /**
     * @param outToTime the outToTime to set
     */
    public void setOutToTime(String outToTime) {
        this.outToTime = outToTime;
    }

    /**
     * @return the deliveryTime
     */
    public String getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * @param deliveryTime the deliveryTime to set
     */
    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the feedback
     */
    public String getFeedback() {
        return feedback;
    }

    /**
     * @param feedback the feedback to set
     */
    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
    
}
