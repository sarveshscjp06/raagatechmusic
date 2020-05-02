/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.data.source;

/**
 *
 * @author STripathi
 */
public class SamcrmDataFactory {

    private static VendorDataInterface vendorDataInterface;
    private static UserDataInterface userDataInterface;
    private static OrderBookingInterface orderInterface;

    public static VendorDataInterface getVendorDataInstance() {
        if (vendorDataInterface == null) {
            vendorDataInterface = new VendorDataDAO();
        }
        return vendorDataInterface;
    }

    public static UserDataInterface getUserDataInstance() {
        if (userDataInterface == null) {
            userDataInterface = new UserDataDAO();
        }
        return userDataInterface;
    }

    public static OrderBookingInterface getOrderBookingInstance() {
        if (orderInterface == null) {
            orderInterface = new OrderBookingDAO();
        }
        return orderInterface;
    }
}
