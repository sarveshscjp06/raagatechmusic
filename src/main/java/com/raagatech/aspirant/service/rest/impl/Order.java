/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.aspirant.service.rest.impl;

import com.raagatech.bean.CustomerAddressBean;
import com.raagatech.bean.OrderDataBean;
import com.raagatech.bean.OrderTrackerBean;
import com.raagatech.bean.OrderedItemBean;
import com.raagatech.bean.PaymentBean;
import com.raagatech.bean.VendorDataBean;
import com.raagatech.commons.EmailUtils;
import com.raagatech.commons.SMSUtils;
import com.raagatech.commons.Utility;
import com.raagatech.data.source.SamcrmDataFactory;
import java.util.ArrayList;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Sarvesh
 */
@Path("/order")
public class Order {

    @POST
    @Path("/samcrmBookOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public String bookOrder(String orderData) {

        JSONObject data = new JSONObject(orderData);
        int vendorId = data.getInt("vendorId");//1
        int customerId = data.getInt("customerId");//2
        //int addressId = data.getInt("addressId");//1
        //String deliveryDate = "2019/01/17";//data.getString("deliveryDate");//2019/01/17 
        //String deliveryTime = "12:40:23";//data.getString("deliveryTime");//12:40:23
        //String deliveryPerson = "Sarvesh";//data.getString("deliveryPerson");//Sarvesh
        //String deliveryAddress = "primary";//data.getString("deliveryAddress");//primary
        //double price = data.getDouble("price");//130.5
        //String status = data.getString("status");//null
        //String comments = "test order booking mobule";//data.getString("comments");//test order booking mobule
        JSONArray itemIdArray = data.getJSONArray("itemIds");//[0, 0];
        JSONArray itemArray = data.getJSONArray("items");//[18, 30];
        JSONArray itemCounts = data.getJSONArray("counts");//[2,5]

        String response = Utility.constructJSON("order", false, "SAM-CRM order booking failed");
        if (vendorId > 0 && itemArray.length() > 0 && itemCounts.length() == itemArray.length()) {// && !deliveryAddress.isEmpty()
            try {
                OrderDataBean orderDataBean = new OrderDataBean();
                orderDataBean.setVendor_id(vendorId);
                orderDataBean.setCustomer_id(customerId);
                //orderDataBean.setDelivery_date(new SimpleDateFormat("yyyy/MM/dd").parse(deliveryDate));
                //orderDataBean.setDelivery_time(deliveryTime);
                //orderDataBean.setDelivery_person(deliveryPerson);
                //orderDataBean.setDelivery_address(deliveryAddress);
                //orderDataBean.setPrice(price);
                //orderDataBean.setStatus(status);
                //orderDataBean.setComments(comments);
                ArrayList<OrderedItemBean> items = new ArrayList<>();
                for (int i = 0; i < itemArray.length(); i++) {
                    OrderedItemBean itemBean = new OrderedItemBean();
                    itemBean.setItem_id((int) itemIdArray.get(i));
                    itemBean.setProduct_id((int) itemArray.get(i));
                    itemBean.setQuantity((int) itemCounts.get(i));
                    items.add(itemBean);
                }
                orderDataBean.setItems(items);
//                CustomerAddressBean addressBean = new CustomerAddressBean();
//                addressBean.setAddresstype(deliveryAddress);
//                if (!deliveryAddress.equalsIgnoreCase("primary")) {
//                    addressBean.setRecepient(deliveryAddress);
//                    addressBean.setAddresstype("secondary");
//                    addressBean.setStreet(data.getString("street"));//Gaurs International school road
//                    addressBean.setCountry(data.getString("country"));//india
//                    addressBean.setZipcode(data.getString("zipcode"));//201308
//                    addressBean.setMobile(Long.valueOf("9312181442"));//9312181442//data.getLong("mobile")
//                }
//                if (addressId == 0) {
//                    SamcrmDataFactory.getOrderBookingInstance().createAddress(orderDataBean.getCustomer_id(), addressBean);
//                    orderDataBean.setCustomerAddress(addressBean);
//                    addressId = addressBean.getAddress_id();
//                }
                SamcrmDataFactory.getOrderBookingInstance().createOrder(orderDataBean);
                int orderedItems = 0;
                if (orderDataBean.getOrder_id() > 0) {
                    orderedItems = SamcrmDataFactory.getOrderBookingInstance().createItems(items, orderDataBean.getOrder_id());
                }
                if (orderedItems > 0) {
                    ArrayList<OrderDataBean> ordersList = new ArrayList<>();
                    ordersList.add(orderDataBean);
                    response = Utility.constructOrdersJSON(ordersList, null, true);
                }

            } catch (Exception e) {
                System.out.println("orderData: exception" + e);
            }
        }
        return response;
    }

    @POST
    @Path("/samcrmMakePayment")
    @Produces(MediaType.APPLICATION_JSON)
    public String makePayment(String paymentData) {

        JSONObject data = new JSONObject(paymentData);
        PaymentBean paymentBean = new PaymentBean();
        paymentBean.setOrderId(data.getInt("orderId"));//
        paymentBean.setCouponCode(data.getString("couponCode"));//
        paymentBean.setPaymentMode(data.getString("paymentMode"));//cash-on-delivery OR samcrm-pay-balance OR samcrm-borrow-loan
        paymentBean.setGrossTotalPayment(data.getDouble("grossTotalPayment"));//
        paymentBean.setGst(data.getDouble("gst"));//
        paymentBean.setSgst(data.getDouble("sgst"));//
        paymentBean.setServiceCharges(data.getDouble("serviceCharges"));//

        String response = Utility.constructJSON("payment", false, "SAM-CRM payment failed");
        try {
            String transactionNo = SamcrmDataFactory.getOrderBookingInstance().completeTransaction(paymentBean);
            String invoiceNo = SamcrmDataFactory.getOrderBookingInstance().generateInvoice(transactionNo);
            response = Utility.constructJSON("payment", true, "SAM-CRM payment done! Invoice No: " + invoiceNo);
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmOrdersList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getOrdersList(@QueryParam("mobile") String mobile, @QueryParam("customerId") int customerId, @QueryParam("status") String status) {

        ArrayList<OrderDataBean> ordersList = new ArrayList<>();
        String response = Utility.constructOrdersJSON(ordersList, null, false);
        try {
            ordersList = SamcrmDataFactory.getOrderBookingInstance().getOrdersList(mobile, customerId, status);
            if (!ordersList.isEmpty()) {
                response = Utility.constructOrdersJSON(ordersList, null, true);
            }
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmCheckInventoryAndUpdateQuantity")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkInventoryAndUpdateQuantity(@QueryParam("productId") int productId, @QueryParam("orderId") int orderId, @QueryParam("quantity") int quantity, @QueryParam("unit") String unit, @QueryParam("itemId") int itemId) {

        String response = "";
        try {
            int inventoryQuantity = 0;
            if (productId > 0) {
                inventoryQuantity = SamcrmDataFactory.getVendorDataInstance().checkInventory(productId);
            }
            if (inventoryQuantity > quantity && orderId > 0) {
                int record = SamcrmDataFactory.getOrderBookingInstance().updateCartItemQuantity(itemId, quantity, orderId);
                if (record == 1) {
                    response = Utility.constructJSON("update", true, "quantity updated successfully.");
                } else {
                    response = Utility.constructJSON("update", false, "Inventory is " + inventoryQuantity + " only");
                }
            }
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmDiscountCoupon")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDiscountCoupon(@QueryParam("vendor_id") int vendor_id, @QueryParam("customer_id") int customer_id, @QueryParam("order_id") int order_id, @QueryParam("total_price") double total_price) {

        int discount = 0;
        String couponCode = null;//V4C0O0INR10
        String response = Utility.constructJSON("0", false, couponCode);
        try {
            discount = SamcrmDataFactory.getVendorDataInstance().getDiscount(vendor_id, customer_id, total_price);
            if (discount > 0) {
                couponCode = SamcrmDataFactory.getVendorDataInstance().generateCouponCode(order_id, vendor_id, customer_id, discount);
                if (couponCode != null && !couponCode.isEmpty()) {
                    response = Utility.constructJSON(String.valueOf(discount), true, couponCode);
                    VendorDataBean bean = SamcrmDataFactory.getUserDataInstance().selectUserData(customer_id);
                    String message = "Congratulations! The generated discount coupon code is "+couponCode;
                    SMSUtils.sendSamcrmOtp(bean.getMobile(), message);                    
                }
            }
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmConfirmOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public String confirmOrder(@QueryParam("vendor_id") int vendor_id, @QueryParam("customer_id") int customer_id, @QueryParam("order_id") int order_id, @QueryParam("couponCode") String couponCode, @QueryParam("total_price") double total_price) {

        String response = Utility.constructJSON("Confirm Order", false);
        try {
            boolean confirmationStatus = SamcrmDataFactory.getOrderBookingInstance().confirmOrder(vendor_id, customer_id, order_id, couponCode, total_price);
            if (confirmationStatus) {
                response = Utility.constructJSON("Confirm Order", true);
                VendorDataBean userData = SamcrmDataFactory.getUserDataInstance().selectUserData(customer_id);
                if (userData != null) {
                    SMSUtils.sendSMS(userData.getMobile(), "Thanks for confirming the order! Happy to help / serve you!");
                }
            }
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmUpdateOrderDelivery")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateOrderDelivery(@QueryParam("order_id") int order_id, @QueryParam("delivery_person_id") int delivery_person_id, @QueryParam("recepient") String recepient, @QueryParam("contact_no") long contact_no) {

        String response = Utility.constructJSON("Close Order", false);
        try {
            int customer_id = SamcrmDataFactory.getOrderBookingInstance().updateOrderDelivery(order_id, delivery_person_id, recepient, contact_no);
            if (customer_id > 0) {
                SamcrmDataFactory.getVendorDataInstance().updateInventoryByOrderId(order_id);
                response = Utility.constructJSON("Close Order", true);
                VendorDataBean userData = SamcrmDataFactory.getUserDataInstance().selectUserData(customer_id);
                if (userData != null) {
                    SMSUtils.sendSMS(userData.getMobile(), "The order is delivered! Allow me to fulfil more orders!");
                }
            }
        } catch (Exception e) {
        }
        return response;
    }

    @POST
    @Path("/samcrmNewAddress")
    @Produces(MediaType.APPLICATION_JSON)
    public String createNewAddress(String addressData) {

        JSONObject data = new JSONObject(addressData);
        int vendorId = data.getInt("vendorId");//1
        int customerId = data.getInt("customerId");//9
        int orderId = data.getInt("orderId");//1

        CustomerAddressBean addressBean = new CustomerAddressBean();
        addressBean.setAddresstype("secondary");
        addressBean.setRecepient(data.getString("recepient"));
        addressBean.setStreet(data.getString("street"));
        addressBean.setCity(data.getString("city"));
        addressBean.setState(data.getString("state"));
        addressBean.setCountry(data.getString("country"));
        addressBean.setZipcode(data.getString("zipcode"));
        addressBean.setMobile(data.getLong("contact_no"));

        String response = Utility.constructJSON("0", false, "New delivery address not added.");
        try {
            int addressId = SamcrmDataFactory.getOrderBookingInstance().createNewAddress(vendorId, customerId, orderId, addressBean);
            if (addressId != 0) {
                response = Utility.constructJSON(String.valueOf(addressId), true, "New delivery address added.");
            }
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmDeleteItem")
    @Produces(MediaType.APPLICATION_JSON)
    public String removeItem(@QueryParam("vendor_id") int vendor_id, @QueryParam("customer_id") int customer_id, @QueryParam("order_id") int order_id, @QueryParam("item_id") int item_id) {

        String response = Utility.constructJSON("Remove Item from cart.", false);
        try {
            int record = SamcrmDataFactory.getOrderBookingInstance().removeItem(order_id, vendor_id, customer_id, item_id);
            if (record == 1) {
                response = Utility.constructJSON("Remove Item from cart.", true);
            }
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmCancelOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public String cancelOrder(@QueryParam("order_id") int order_id, @QueryParam("comment") String comment, @QueryParam("delivery_person_id") int delivery_person_id) {

        String response = Utility.constructJSON("Order Cancellation Status", false);
        try {
            int cancellationStatus = SamcrmDataFactory.getOrderBookingInstance().cancelOrder(order_id, comment, delivery_person_id);
            if (cancellationStatus > 0) {
                response = Utility.constructJSON("Order Cancellation Status", true);
                ArrayList<OrderDataBean> ordersList = SamcrmDataFactory.getOrderBookingInstance().getOrdersList(null, order_id, "cancelled");
                OrderDataBean bean = ordersList.get(0);
                VendorDataBean vendorData = SamcrmDataFactory.getUserDataInstance().selectUserData(bean.getVendor_id());
                VendorDataBean customerData = SamcrmDataFactory.getUserDataInstance().selectUserData(bean.getCustomer_id());

                String subject = ":: Order Cancelled";
                String followupDetails = "Hello "+customerData.getVendorTitle()+" "+customerData.getName();
                followupDetails += "\n\nOrder Cancellation Comment:\n"+bean.getComments()+"\n Thanks.'\n\n";
                followupDetails += "From: \n"+vendorData.getName()+"\nAddress: "+vendorData.getAddress()+"\nContact No: "+vendorData.getMobile(); 

                EmailUtils.pushSalesAndServicesViaEmail(vendorData.getName(), vendorData.getEmail(), customerData.getEmail(), subject, followupDetails, customerData.getName());
                SMSUtils.sendSamcrmOtp(customerData.getMobile(), followupDetails);
            }
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmUpdateDeliveryStatus")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateDeliveryStatus(@QueryParam("order_id") int order_id, @QueryParam("delivery_person_id") int delivery_person_id, @QueryParam("step") int step) {

        String response = Utility.constructJSON("Update Delivery Status", false);
        try {
            boolean deliveryStatus = SamcrmDataFactory.getOrderBookingInstance().updateDeliveryStatus(order_id, delivery_person_id, step);
            if (deliveryStatus) {
                response = Utility.constructJSON("Update Delivery Status", deliveryStatus);
            }
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmTrackOrder")
    @Produces(MediaType.APPLICATION_JSON)
    public String trackOrder(@QueryParam("order_id") int order_id, @QueryParam("customer_id") int customer_id) {

        String response = Utility.constructJSONString(null, false, "Order Tracker Failed");
        try {
            ArrayList<OrderTrackerBean> list = SamcrmDataFactory.getOrderBookingInstance().getOrderDeliveryStatus(order_id, customer_id);
            response = Utility.constructJSONString(list, true, "Order Tracker Success");
        } catch (Exception e) {
        }
        return response;
    }
}
