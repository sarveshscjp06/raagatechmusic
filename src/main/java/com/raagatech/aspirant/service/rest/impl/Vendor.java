/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.aspirant.service.rest.impl;

import com.raagatech.bean.OrderDataBean;
import com.raagatech.bean.ProductCategoryBean;
import com.raagatech.bean.ProductsAndServicesBean;
import com.raagatech.bean.SliderImageBean;
import com.raagatech.bean.VendorDataBean;
import com.raagatech.commons.EmailUtils;
import com.raagatech.commons.SMSUtils;
import com.raagatech.commons.Utility;
import com.raagatech.data.source.DatabaseConnection;
import com.raagatech.data.source.SamcrmDataFactory;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;
import javax.ws.rs.Consumes;
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
@Path("/vendor")
public class Vendor {

    @GET
    @Path("/samcrmSelectVendor")
    @Produces(MediaType.APPLICATION_JSON)
    public String doSelectVendor() throws Exception {
        String response;
        LinkedHashMap<Integer, String> categoryMap = SamcrmDataFactory.getVendorDataInstance().selectVendorCategory();
        if (!categoryMap.isEmpty()) {
            response = Utility.constructJSON("selectvendor", categoryMap);
        } else {
            response = Utility.constructJSON("selectvendor", false, "no data available");
        }
        return response;
    }

    @GET
    @Path("/samcrmSelectSubtype")
    @Produces(MediaType.APPLICATION_JSON)
    public String doSelectSubtype() throws Exception {
        String response;
        LinkedHashMap<Integer, String> subtypeMap = SamcrmDataFactory.getVendorDataInstance().selectVendorSubtype();
        if (!subtypeMap.isEmpty()) {
            response = Utility.constructJSON("selectsubtype", subtypeMap);
        } else {
            response = Utility.constructJSON("selectsubtype", false, "no data available");
        }
        return response;
    }

    @GET
    @Path("/samcrmVendorData")
    @Produces(MediaType.APPLICATION_JSON)
    public String getVendorData(@QueryParam("vendorcategory") String vendorCategory, @QueryParam("vendorsubtype") String vendorSubType, @QueryParam("vendorid") String vendorId) throws Exception {
        String response;
        LinkedHashMap<Integer, String> categoryMap = SamcrmDataFactory.getVendorDataInstance().selectVendorCategory();
        if (!categoryMap.isEmpty()) {
            response = Utility.constructJSON("selectvendor", categoryMap);
        } else {
            response = Utility.constructJSON("selectvendor", false, "no data available");
        }
        return response;
    }

    @GET
    @Path("/samcrmSliderImage")
    @Produces(MediaType.APPLICATION_JSON)
    public String getSamcrmSliderImage() throws Exception {
        String response;
        ArrayList<SliderImageBean> sliderImageList = DatabaseConnection.listSliderImage();
        if (!sliderImageList.isEmpty()) {
            response = Utility.constructJSON_ForSliderImage("samcrmsliderimage", sliderImageList);
        } else {
            response = Utility.constructJSON("samcrmsliderimage", false, "no data available");
        }
        return response;
    }

    @POST
    @Path("/samcrmProductCategory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getProductCategory(String mRequestBody) throws Exception {
        String response;
        JSONObject data = new JSONObject(mRequestBody);
        String vendorId = data.getString("vendorId");
        ArrayList<ProductCategoryBean> productCategoryList = SamcrmDataFactory.getVendorDataInstance().selectProductCategoryDetails(vendorId);
        if (!productCategoryList.isEmpty()) {
            JSONObject object = new JSONObject();
            object.put("samcrmProductCategory", productCategoryList);

            for (ProductCategoryBean productCategoryBean : productCategoryList) {
                ArrayList<ProductsAndServicesBean> productsList = SamcrmDataFactory.getVendorDataInstance().selectProductsAndServicesDetails(productCategoryBean.getProductCategoryId());
                if (!productsList.isEmpty()) {
                    object.put(productCategoryBean.getProductCategoryName(), productsList);
                } else {
                    object.put(productCategoryBean.getProductCategoryName(), new ArrayList<>());
                }
            }
            response = Utility.constructJSON("samcrmProductCategory", true, object.toString());
        } else {
            response = Utility.constructJSON("samcrmProductCategory", false, "no data available");
        }
        return response;
    }

    @POST
    @Path("/samcrmProductCategorysOnly")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getProductCategorysOnly(String mRequestBody) throws Exception {
        String response;
        JSONObject data = new JSONObject(mRequestBody);
        String vendorId = data.getString("vendorId");

        ArrayList<ProductCategoryBean> productCategoryList = new ArrayList<>();

        LinkedHashMap<String, String> individualData = SamcrmDataFactory.getUserDataInstance().checkSamcrmLogin(null, null, vendorId);
        if (individualData.isEmpty()) {
            response = Utility.constructProductCategoryJSON(productCategoryList,
                    false, "Kindly login to the SAM-CRM App.");
            return response;
        }

        productCategoryList = SamcrmDataFactory.getVendorDataInstance().selectProductCategoryDetails(vendorId);
        if (!productCategoryList.isEmpty()) {
            response = Utility.constructProductCategoryJSON(productCategoryList,
                    true, "This service provides vendor specific product categories.");
        } else {
            response = Utility.constructProductCategoryJSON(productCategoryList,
                    false, "no data available");
        }
        return response;
    }

    @POST
    @Path("/samcrmProductItems")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String getProductItems(String mRequestBody) throws Exception {
        String response;
        JSONObject data = new JSONObject(mRequestBody);
        String vendorId = data.getString("vendorId");
        String productCategoryId = data.getString("productCategoryId");

        ArrayList<ProductsAndServicesBean> itemsList = SamcrmDataFactory.getVendorDataInstance().selectProductsAndServicesDetails(productCategoryId);
        if (!itemsList.isEmpty()) {
            response = Utility.constructProductItemsJSON(itemsList,
                    true, "This service provides product category specific items.");
        } else {
            response = Utility.constructProductItemsJSON(itemsList,
                    false, "no data available");
        }
        return response;
    }

    @GET
    @Path("/samcrmProductsDetails")
    @Produces(MediaType.APPLICATION_JSON)
    public String getProductsAndServicesDetails(@QueryParam("vendorcategory") String vendorCategory, @QueryParam("vendorsubtype") String vendorSubType, @QueryParam("vendorid") String vendorId) throws Exception {
        String response;
        ArrayList<ProductsAndServicesBean> productsList = SamcrmDataFactory.getVendorDataInstance().selectProductsAndServicesDetails(vendorCategory);
        if (!productsList.isEmpty()) {
            JSONArray object = new JSONArray();
            object.put(productsList);
            response = object.toString();
        } else {
            response = Utility.constructJSON("samcrmProductsDetails", false, "no data available");
        }
        return response;
    }

    @POST
    @Path("/samcrmAddContact")
    @Produces(MediaType.APPLICATION_JSON)
    public String addSamcrmContact(String params) {

        JSONObject data = new JSONObject(params);
        String contactId = data.getString("customerId");
        String name = data.getString("name");
        String email = data.getString("email");
        String mobile = data.getString("mobile");
        String zipCode = data.getString("zipCode");
        String dob = data.getString("dob");
        String address = data.getString("address");
        String comment = data.getString("comment");
        String message = data.getString("message");
        String vendor_group_id = data.getString("vendor_group_id");

        String response = Utility.constructJSON("register", false, "SAM-CRM adding contact failed");
        if (Utility.isNotNull(mobile)) {
            try {
                int insertStatus = SamcrmDataFactory.getVendorDataInstance().addContact(name, email, mobile, dob, address, zipCode,
                        comment, vendor_group_id, contactId, message);
                if (insertStatus >= 1) {
                    response = Utility.constructJSON("register", true, "SAM-CRM contact added / updated successfully!");
                }
            } catch (Exception e) {
            }
        }
        return response;
    }

    @POST
    @Path("/samcrmListContacts")
    @Produces(MediaType.APPLICATION_JSON)
    public String getContactsList(String params) throws Exception {
        String vendor_group_id = new JSONObject(params).getString("vendor_group_id");
        String response;
        ArrayList<VendorDataBean> contactList = SamcrmDataFactory.getVendorDataInstance().selectContacts(vendor_group_id);
        if (!contactList.isEmpty()) {
            response = Utility.constructContactJSON("listcontact", true, contactList);
        } else {
            response = Utility.constructContactJSON("listcontact", false, contactList);
        }
        return response;
    }

    @POST
    @Path("/samcrmUpdateProductCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateProductCategory(String params) {

        JSONObject data = new JSONObject(params);
        String categoryId = data.getString("categoryId");
        String categoryName = data.getString("categoryName");
        String categoryDescription = data.getString("categoryDescription");
        String categorySequence = data.getString("categorySequence");
        String vendor_group_id = data.getString("vendor_group_id");

        String response = Utility.constructJSON("update", false, "SAM-CRM Product Category");
        if (Utility.isNotNull(vendor_group_id)) {
            try {
                String vendorName;
                if (categoryId != null && Integer.parseInt(categoryId) > 0) {
                    vendorName = SamcrmDataFactory.getVendorDataInstance().editCategory(Integer.parseInt(categoryId), categoryName, categoryDescription, vendor_group_id, Integer.parseInt(categorySequence));
                } else {
                    vendorName = SamcrmDataFactory.getVendorDataInstance().addCategory(categoryName, categoryDescription, vendor_group_id, Integer.parseInt(categorySequence));
                }
                if (vendorName != null) {
                    response = Utility.constructJSON("update",
                            true, "Hi " + vendorName + ", you have added / updated a product category!");
                }
            } catch (Exception e) {
            }
        }
        return response;
    }

    @POST
    @Path("/samcrmUpdateProductAndService")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateProductAndService(String params) {

        JSONObject data = new JSONObject(params);
        String productId = data.getString("categoryId");
        String productName = data.getString("categoryName");
        String productDescription = data.getString("categoryDescription");
        String productSequence = data.getString("categorySequence");
        String vendor_group_id = data.getString("vendor_group_id");
        int product_category_id = Integer.parseInt(data.getString("product_category_id"));
        int price = Integer.parseInt(data.getString("price"));
        int discount = Integer.parseInt(data.getString("discount"));

        String response = Utility.constructJSON("update", false, "SAM-CRM Product And Service Updation");
        if (Utility.isNotNull(vendor_group_id)) {
            try {
                String vendorName;
                if (productId != null && Integer.parseInt(productId) > 0) {
                    vendorName = SamcrmDataFactory.getVendorDataInstance().editProductAndService(Integer.parseInt(productId), productName, productDescription, vendor_group_id,
                            Integer.parseInt(productSequence), price, discount, product_category_id);
                } else {
                    vendorName = SamcrmDataFactory.getVendorDataInstance().addProductAndService(productName, productDescription, vendor_group_id, price, discount, product_category_id, Integer.parseInt(productSequence));
                }
                if (vendorName != null) {
                    response = Utility.constructJSON("update", true, vendorName);
                    EmailUtils.pushSalesAndServicesViaEmail("SAM-CRM", "sarvesh.new@gmail.com", "sarveshscjp06@yahoo.co.in", "New Product Offer!", "in daily email /sms, the vendor can share about the new products and / or services", "SCJP06");//"I'm xyz, My shop is here: wer  rwerwe. Below is the offer description: "
                }
            } catch (Exception e) {
            }
        }
        return response;
    }

    @GET
    @Path("/samcrmInventoryEntry")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateProductServiceInventory(@QueryParam("productId") int productId, @QueryParam("quantity") int quantity, @QueryParam("unit") String unit, @QueryParam("vendorId") String vendor_group_id) {

        String response = Utility.constructJSON("update", false, "SAM-CRM Product Service Inventory Entry");
        if (Utility.isNotNull(vendor_group_id)) {
            try {
                String inventoryStatus;
                if (productId > 0) {
                    inventoryStatus = SamcrmDataFactory.getVendorDataInstance().updateProductServiceInventory(productId, quantity, unit);
                    response = Utility.constructJSON("update", true, inventoryStatus);
                }
            } catch (Exception e) {
            }
        }
        return response;
    }

    @GET
    @Path("/samcrmCheckInventory")
    @Produces(MediaType.APPLICATION_JSON)
    public String CheckInventory(@QueryParam("productId") int productId, @QueryParam("vendorId") String vendor_group_id) {

        String response = Utility.constructJSON("check-inventory", false, "SAM-CRM Check Inventory");
        if (Utility.isNotNull(vendor_group_id)) {
            try {
                if (productId > 0) {
                    int inventoryStatus = SamcrmDataFactory.getVendorDataInstance().checkInventory(productId);
                    response = Utility.constructJSON("check-inventory", true, String.valueOf(inventoryStatus));
                }
            } catch (Exception e) {
            }
        }
        return response;
    }

    @GET
    @Path("/samcrmGenerateCoupon")
    @Produces(MediaType.APPLICATION_JSON)
    public String generateCoupon(@QueryParam("vendorId") int vendor_id, @QueryParam("productId") int productId, @QueryParam("customerId") int customerId) {
        String response = Utility.constructJSON("generate-coupon", false);
        boolean isCouponGenerated = Boolean.FALSE;
        if (vendor_id > 0) {
            try {
                if (productId > 0) {
                    //isCouponGenerated = SamcrmDataFactory.getVendorDataInstance().productwiseCouponGeneration(productId);
                    if (isCouponGenerated) {
                        response = Utility.constructJSON("productwise_coupon_generation", true);
                    }
                } else if (customerId > 0) {
                    //isCouponGenerated = SamcrmDataFactory.getVendorDataInstance().customerwiseCouponGeneration(customerId);
                    if (isCouponGenerated) {
                        response = Utility.constructJSON("customerwise_coupon_generation", true);
                    }
                } else //isCouponGenerated = SamcrmDataFactory.getVendorDataInstance().commonCouponGeneration();
                if (isCouponGenerated) {
                    response = Utility.constructJSON("common_coupon_generation", true);
                }
            } catch (Exception e) {
            }
        }

        return response;
    }

    @GET
    @Path("/samcrmUrgentService")
    @Produces(MediaType.APPLICATION_JSON)
    public String urgentService(@QueryParam("vendorId") int vendor_id, @QueryParam("name") String name, @QueryParam("mobile") long mobile, @QueryParam("address") String address, @QueryParam("request") String request) {
        String response = Utility.constructJSON("Urgent - Service", false);
        try {
            ArrayList<OrderDataBean> ordersList = SamcrmDataFactory.getOrderBookingInstance().getOrdersList(String.valueOf(mobile), vendor_id, "open");

            if (vendor_id > 0 && ordersList.isEmpty()) {
                long customerId = SamcrmDataFactory.getVendorDataInstance().checkUserExistance(mobile);
                String contactNo = SamcrmDataFactory.getVendorDataInstance().getUrgentServiceContactNo(vendor_id);
                if (!contactNo.isEmpty()) {

                    OrderDataBean orderDataBean = new OrderDataBean();
                    orderDataBean.setVendor_id(vendor_id);
                    orderDataBean.setCustomer_id(customerId);
                    orderDataBean.setDelivery_address(address);
                    orderDataBean.setComments(request);
                    String otpText = "";
                    int otpNumber = 1111;
                    if (customerId == 0) {
                        Random random = new Random();
                        int max = 9999, min = 1000;
                        otpNumber = random.nextInt(max - min + 1) + min;
                        otpText = otpNumber + " is One Time Password for SAM-CRM urgent service authentication. \nKindly don't share this with others. \n Vendor will ask you this OTP before initiating service.";
                        String thanks = "\n Thanks. For further inquiry please call : " + contactNo;
                        otpText = otpText + thanks;
                        customerId = SamcrmDataFactory.getVendorDataInstance().addContact(name, "info@raagatech.com", String.valueOf(mobile), "", address, "", request, String.valueOf(vendor_id), "0", thanks);
                        orderDataBean.setCustomer_id(customerId);
                    }
                    orderDataBean.setCouponCode(String.valueOf(otpNumber));
                    SamcrmDataFactory.getOrderBookingInstance().createOrder(orderDataBean);
                    if (otpText != null && !otpText.isEmpty()) {
                        SMSUtils.sendSamcrmOtp(String.valueOf(mobile), otpText);
                    }
                }
                response = Utility.constructJSON("Urgent - Service", true);
            } else {
                response = Utility.constructJSON("First, confirm OR cancel the OPEN order then use Urgent - Service. Thanks!", false);
            }
        } catch (Exception e) {
        }

        return response;
    }

    @POST
    @Path("/samcrmPushSalesAndService")
    @Produces(MediaType.APPLICATION_JSON)
    public String pushSalesAndService(String params) throws Exception {

        JSONObject data = new JSONObject(params);
        int vendor_id = data.has("vendorId") ? Integer.parseInt(data.getString("vendorId")) : 0;
        String vendorTitle = data.has("vendorTitle") ? data.getString("vendorTitle") : "";
        String name = data.has("name") ? data.getString("name") : "";
        String email = data.has("email") ? data.getString("email") : "";
        String mobile = data.has("mobile") ? data.getString("mobile") : "";
        String message = data.has("message") ? data.getString("message") : "";
        String ipAddress = data.getString("ipAddress");
        String address = data.has("address") ? data.getString("address") : "";
        String response = Utility.constructJSON("Push Sales & Service", false);
        if (vendor_id > 0) {
            try {
                ArrayList<VendorDataBean> contactList = SamcrmDataFactory.getVendorDataInstance().selectContacts(String.valueOf(vendor_id));
                for (VendorDataBean bean : contactList) {
                    message = "Hi " + bean.getVendorTitle() + " " + bean.getName() + ",\n Greetings!\n" + message;
                    message = message + "\n Thank you. For further inquiry please call : " + mobile + " OR e-mail: " + email + " us!";
                    message = message + "\n Best Regards\n" + vendorTitle + "\n\n" + name + "\n" + address;
                    SMSUtils.sendSamcrmOtp(String.valueOf(bean.getMobile()), message);
                    EmailUtils.pushSalesAndServicesViaEmail(vendorTitle, email, bean.getEmail(), "Discount Offer!", message, bean.getName());//"I'm xyz, My shop is here: wer  rwerwe. Below is the offer description: "
                }
                response = Utility.constructJSON("Push Sales & Service", true);
            } catch (Exception e) {
            }
        }

        return response;
    }

    @POST
    @Path("/samcrmEmailCampaign")
    @Produces(MediaType.APPLICATION_JSON)
    public String emailCampaign(String params) throws Exception {

        JSONObject data = new JSONObject(params);
        int vendor_id = data.has("vendorId") ? Integer.parseInt(data.getString("vendorId")) : 0;
        String vendorTitle = data.has("vendorTitle") ? data.getString("vendorTitle") : "";
        String name = data.has("name") ? data.getString("name") : "";
        String email = data.has("email") ? data.getString("email") : "";
        String mobile = data.has("mobile") ? data.getString("mobile") : "";
        String message = data.has("message") ? data.getString("message") : "";
        String ipAddress = data.getString("ipAddress");
        String address = data.has("address") ? data.getString("address") : "";
        String response = Utility.constructJSON("E-mail Campaign", false);
        if (vendor_id > 0) {
            try {
                ArrayList<VendorDataBean> contactList = SamcrmDataFactory.getVendorDataInstance().selectContacts(String.valueOf(vendor_id));
                for (VendorDataBean bean : contactList) {
                    message = "Hi " + bean.getVendorTitle() + " " + bean.getName() + ",\n Greetings!\n" + message;
                    message = message + "\n Thank you. For further inquiry please call : " + mobile + " OR e-mail: " + email + " us!";
                    message = message + "\n Best Regards\n" + name + "\n" + address;
                    EmailUtils.pushSalesAndServicesViaEmail(vendorTitle, email, bean.getEmail(), "!Offer! for today only.", message, bean.getName());//"I'm xyz, My shop is here: wer  rwerwe. Below is the offer description: "
                }
                response = Utility.constructJSON("E-mail Campaign", true);
            } catch (Exception e) {
            }
        }

        return response;
    }

    @POST
    @Path("/samcrmBulkSms")
    @Produces(MediaType.APPLICATION_JSON)
    public String bulkSms(String params) throws Exception {

        JSONObject data = new JSONObject(params);
        int vendor_id = data.has("vendorId") ? Integer.parseInt(data.getString("vendorId")) : 0;
        String vendorTitle = data.has("vendorTitle") ? data.getString("vendorTitle") : "";
        String name = data.has("name") ? data.getString("name") : "";
        String email = data.has("email") ? data.getString("email") : "";
        String mobile = data.has("mobile") ? data.getString("mobile") : "";
        String message = data.has("message") ? data.getString("message") : "";
        String ipAddress = data.getString("ipAddress");
        String address = data.has("address") ? data.getString("address") : "";
        String response = Utility.constructJSON("SMS Campaign", false);
        if (vendor_id > 0) {
            try {
                ArrayList<VendorDataBean> contactList = SamcrmDataFactory.getVendorDataInstance().selectContacts(String.valueOf(vendor_id));
                for (VendorDataBean bean : contactList) {
                    message = "Hi " + bean.getVendorTitle() + " " + bean.getName() + ",\n Greetings!\n" + message;
                    message = message + "\n Thank you. For further inquiry please call : " + mobile + " OR e-mail: " + email + " us!";
                    message = message + "\n Best Regards\n" + vendorTitle + "\n\n" + name + "\n" + address;
                    SMSUtils.sendSamcrmOtp(String.valueOf(bean.getMobile()), message);
                }
                response = Utility.constructJSON("SMS Campaign", true);
            } catch (Exception e) {
            }
        }

        return response;
    }

}
