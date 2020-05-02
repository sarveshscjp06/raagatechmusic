/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.bean;

/**
 *
 * @author Sarvesh
 */
public class CustomerAddressBean {
    
    private int address_id;
    
    private String recepient;
    
    private String street;
    
    private String city;
    
    private String state;
    
    private String zipcode;
    
    private String country;
    
    private String addresstype;
    
    private long mobile;

    /**
     * @return the address_id
     */
    public int getAddress_id() {
        return address_id;
    }

    /**
     * @param address_id the address_id to set
     */
    public void setAddress_id(int address_id) {
        this.address_id = address_id;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the zipcode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * @param zipcode the zipcode to set
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return the addresstype
     */
    public String getAddresstype() {
        return addresstype;
    }

    /**
     * @param addresstype the addresstype to set
     */
    public void setAddresstype(String addresstype) {
        this.addresstype = addresstype;
    }

    /**
     * @return the recepient
     */
    public String getRecepient() {
        return recepient;
    }

    /**
     * @param recepient the recepient to set
     */
    public void setRecepient(String recepient) {
        this.recepient = recepient;
    } 

    /**
     * @return the mobile
     */
    public long getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(long mobile) {
        this.mobile = mobile;
    }
    
    
}
