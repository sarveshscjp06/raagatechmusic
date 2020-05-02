/**
 * Copyright (C) 2015, raagatech.
 *
 * All rights reserved under the Terms and Conditions of
 * raagatech by Jagriti Jan Kalyan Samiti, Allahabad.
 *
 * $Id: InquiryBean.java, v 1.1 Exp $
 *
 * Date Author Changes
 * Jul 1, 2017, 11:14:18 PM, Sarvesh created.
 */
package com.raagatech.bean;

import java.util.Date;

/**
 * inquiry properties
 *
 * @author <a href=mailto:sarveshtripathi@raagatech.com>Sarvesh</a>
 * @version $Revision: 1.1 Jul 1, 2017 11:14:18 PM
 * @see com.raagatech.bean.InquiryBean
 */
public class InquiryBean {

    private Integer inquiry_id;
    private String firstname;
    private Date inquiry_date;
    private Integer inspiration_id;
    private String email;
    private Long mobile;
    private Integer level_id;
    private String address_line1;
    private String followup_details;
    private Integer inquirystatus_id;
    private String label_text;
    private String label_color;
    
    public InquiryBean() {
    }

    /**
     * @return the inquiry_id
     */
    public Integer getInquiry_id() {
        return inquiry_id;
    }

    /**
     * @param inquiry_id the inquiry_id to set
     */
    public void setInquiry_id(Integer inquiry_id) {
        this.inquiry_id = inquiry_id;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the inquiry_date
     */
    public Date getInquiry_date() {
        return inquiry_date;
    }

    /**
     * @param inquiry_date the inquiry_date to set
     */
    public void setInquiry_date(Date inquiry_date) {
        this.inquiry_date = inquiry_date;
    }

    /**
     * @return the inspiration_id
     */
    public Integer getInspiration_id() {
        return inspiration_id;
    }

    /**
     * @param inspiration_id the inspiration_id to set
     */
    public void setInspiration_id(Integer inspiration_id) {
        this.inspiration_id = inspiration_id;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the mobile
     */
    public Long getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(Long mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the level_id
     */
    public Integer getLevel_id() {
        return level_id;
    }

    /**
     * @param level_id the level_id to set
     */
    public void setLevel_id(Integer level_id) {
        this.level_id = level_id;
    }

    /**
     * @return the address_line1
     */
    public String getAddress_line1() {
        return address_line1;
    }

    /**
     * @param address_line1 the address_line1 to set
     */
    public void setAddress_line1(String address_line1) {
        this.address_line1 = address_line1;
    }

    /**
     * @return the followup_details
     */
    public String getFollowup_details() {
        return followup_details;
    }

    /**
     * @param followup_details the followup_details to set
     */
    public void setFollowup_details(String followup_details) {
        this.followup_details = followup_details;
    }

    /**
     * @return the inquirystatus_id
     */
    public Integer getInquirystatus_id() {
        return inquirystatus_id;
    }

    /**
     * @param inquirystatus_id the inquirystatus_id to set
     */
    public void setInquirystatus_id(Integer inquirystatus_id) {
        this.inquirystatus_id = inquirystatus_id;
    }

    /**
     * @return the label_text
     */
    public String getLabel_text() {
        return label_text;
    }

    /**
     * @param label_text the label_text to set
     */
    public void setLabel_text(String label_text) {
        this.label_text = label_text;
    }

    /**
     * @return the label_color
     */
    public String getLabel_color() {
        return label_color;
    }

    /**
     * @param label_color the label_color to set
     */
    public void setLabel_color(String label_color) {
        this.label_color = label_color;
    }    
}
