/**
 * Copyright (C) 2017, raagatech.
 *
 * All rights reserved under the Terms and Conditions of
 * raagatech by Jagriti Jan Kalyan Samiti, Allahabad.
 *
 * $Id: Login.java, v 1.1 Exp $
 *
 * Date Author Changes
 * Jun 18, 2017, 5:59:04 PM, Sarvesh created.
 */
package com.raagatech.aspirant.service.rest.impl;

import com.raagatech.bean.ProductCategoryBean;
import com.raagatech.bean.VendorDataBean;
import com.raagatech.commons.EmailUtils;
import com.raagatech.commons.SMSUtils;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.raagatech.commons.Utility;
import com.raagatech.data.source.DatabaseConnection;
import com.raagatech.data.source.SamcrmDataFactory;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;
import javax.ws.rs.POST;
import org.json.JSONObject;

/**
 * TODO User authentication and authorization
 *
 * @author <a href=mailto:sarveshtripathi@raagatech.com>Sarvesh</a>
 * @version $Revision: 1.1 Jun 18, 2017 5:59:04 PM
 * @see com.raagatech.aspirant.service.rest.impl.Login
 */
@Path("/login")
public class Login {

    @GET
    @Path("/dologin")
    @Produces(MediaType.APPLICATION_JSON)
    public String doLogin(@QueryParam("email") String userName, @QueryParam("mobile") String password) {
        String response;

        if (checkCredentials(userName, Long.valueOf(password))) {
            response = Utility.constructJSON("login", true);
        } else {
            response = Utility.constructJSON("login", false, "Invalid email or password");
        }

        return response;
    }

    @POST
    @Path("/samcrmAuthentication")
    @Produces(MediaType.APPLICATION_JSON)
    public String samcrmUserAuthentication(String params) {

        JSONObject data = new JSONObject(params);
        String individual_id = null;
        String ipAddress = null;
        String name = null;
        String email = null;
        String mobile = null;
        String zipCode = null;
        String password = null;
        String vendorTitle = null;
        String vendorRegistrationNo = null;
        String vendorCategoryId = null;
        String vendorSubtypeId = null;
        String mobileVerificationCode = null;
        String vendorDescription = null;
        String pushMessage = null;
        String emailCampaignText = null;
        String bulkSmsText = null;
        String address = null;
        if (data.has("individual_id")) {
            individual_id = data.optString("individual_id");
        }
        if (data.has("ipAddress")) {
            ipAddress = data.getString("ipAddress");
        }
        if (data.has("name")) {
            name = data.getString("name");
        }
        if (data.has("email")) {
            email = data.getString("email");
        }
        if (data.has("mobile")) {
            mobile = data.getString("mobile");
        }
        if (data.has("zipCode")) {
            zipCode = data.getString("zipCode");
        }
        if (data.has("password")) {
            password = data.getString("password");
        }
        if (data.has("vendorTitle")) {
            vendorTitle = data.getString("vendorTitle");
        }
        if (data.has("vendorRegistrationNo")) {
            vendorRegistrationNo = data.getString("vendorRegistrationNo");
        }
        if (data.has("vendorCategoryId")) {
            vendorCategoryId = data.getString("vendorCategoryId");
        }
        if (data.has("vendorSubtypeId")) {
            vendorSubtypeId = data.getString("vendorSubtypeId");
        }
        if (data.has("mobileVerificationCode")) {
            mobileVerificationCode = data.getString("mobileVerificationCode");
        }
        if (data.has("vendorDescription")) {
            vendorDescription = data.getString("vendorDescription");
        }
        if (data.has("pushMessage")) {
            pushMessage = data.getString("pushMessage");
        }
        if (data.has("emailCampaignText")) {
            emailCampaignText = data.getString("emailCampaignText");
        }
        if (data.has("bulkSmsText")) {
            bulkSmsText = data.getString("bulkSmsText");
        }
        if (data.has("address")) {
            address = data.getString("address");
        }
        String response = Utility.constructJSON("login", false, "SAM-CRM user authentiction failed");
        try {
            int insertStatus = 0;
//            if (Utility.isNotNull(individual_id)) {
//                insertStatus = SamcrmDataFactory.getUserDataInstance().createSamcrmUser(name, email, mobile, zipCode, password, ipAddress, vendorCategoryId, vendorSubtypeId, vendorTitle, vendorRegistrationNo, individual_id);
//            } else 
            if (Utility.isNotNull(individual_id) || (Utility.isNotNull(mobileVerificationCode) && Utility.isNotNull(name) && Utility.isNotNull(email)
                    && Utility.isNotNull(zipCode) && Utility.isNotNull(password))) {
                insertStatus = SamcrmDataFactory.getUserDataInstance().addUpdateSamcrmUser(name, email, mobile, zipCode, 
                        password, ipAddress, vendorCategoryId, vendorSubtypeId, vendorTitle, vendorRegistrationNo, 
                        individual_id, vendorDescription, pushMessage, emailCampaignText, bulkSmsText, address);
            }
            if (insertStatus == 1) {
                LinkedHashMap<String, String> individualData = SamcrmDataFactory.getUserDataInstance().checkSamcrmLogin(mobile, ipAddress, null);
                if (individualData != null && !individualData.isEmpty()) {
                    ArrayList<VendorDataBean> vendorList = new ArrayList<>();
                    String userZipCode = individualData.get("zipCode");
                    String isVendor = individualData.get("isVendor");
                    if (Integer.parseInt(isVendor) == 0) {
                        vendorList = SamcrmDataFactory.getVendorDataInstance().getAreaSpecificVendors(userZipCode, mobile, 0, 0);
                    }
                    response = Utility.constructJSON(vendorList, true, individualData);
                    EmailUtils.sendSamcrmUserAuthenticationEMail(email, name, "Congratulations! User Creation succeed!");
                } else {
                    response = Utility.constructJSON("login", false, "User Registration Failed");
                }
            } else {
                int userStatus = SamcrmDataFactory.getUserDataInstance().getSamcrmUserStatus(mobile, password, ipAddress);
                switch (userStatus) {
                    case 8:
                    case 0:
                        Random random = new Random();
                        int max = 9999,
                         min = 1000;
                        int otpNumber = random.nextInt(max - min + 1) + min;
                        String otpText = otpNumber + " is One Time Password for SAM-CRM mobile no verification. \nKindly don't share this with others.";
                        otpText = otpText + " Thanks. For further inquiry please call or email: +919891029284 / raksha@raagatech.com";
                        SMSUtils.sendSamcrmOtp(mobile, otpText);
                        response = Utility.constructJSON("login", false, String.valueOf(otpNumber));
                        break;
                    case 1:
                    case 2:
                    case 4:
                    case 5:
                    case 6:
                        SamcrmDataFactory.getUserDataInstance().updateSamcrmUserStatus(mobile, ipAddress, userStatus);
                        LinkedHashMap<String, String> individualData = SamcrmDataFactory.getUserDataInstance().checkSamcrmLogin(mobile, ipAddress, null);
                        ArrayList<VendorDataBean> vendorList = new ArrayList<>();
                        String userZipCode = individualData.get("zipCode");
                        String isVendor = individualData.get("isVendor");
                        if (Integer.parseInt(isVendor) == 0) {
                            vendorList = SamcrmDataFactory.getVendorDataInstance().getAreaSpecificVendors(userZipCode, mobile, 0, 0);
                        }
                        response = Utility.constructJSON(vendorList, true, individualData);
                        EmailUtils.sendSamcrmUserAuthenticationEMail(email, name, "Login attempted from different device and succeed!");
                        break;
                    case 7:
                        response = Utility.constructJSON("login", false, "SAM-CRM login: mobile-no OR password is wrong.");
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
        }
        return response;
    }

    private boolean checkCredentials(String userName, long password) {

        boolean result = Boolean.FALSE;

        if (Utility.isNotNull(userName) && password > 0) {
            try {
                result = DatabaseConnection.checkLogin(userName, password);
            } catch (Exception e) {
            }
        }

        return result;
    }

    @POST
    @Path("/samcrmUserData")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkSamcrmCredentials(String params) throws Exception {

        JSONObject data = new JSONObject(params);
        String mobileNo = null;
        String ipAddress = null;
        if (data.has("mobile") && !data.isNull("mobile")) {
            mobileNo = data.getString("mobile");
        }
        if (data.has("ipAddress")) {
            ipAddress = data.getString("ipAddress");
        }
        String response = Utility.constructJSONString(null, null, false);
        try {
            if (mobileNo != null && !mobileNo.isEmpty()) {
                ArrayList<VendorDataBean> vendorList = new ArrayList<>();
                LinkedHashMap<String, String> individualData = SamcrmDataFactory.getUserDataInstance().checkSamcrmLogin(mobileNo, ipAddress, null);
                if (!individualData.isEmpty()) {
                    String zipCode = individualData.get("zipCode");
                    String isVendor = individualData.get("isVendor");
                    String mobile = individualData.get("mobile");
                    if (Integer.parseInt(isVendor) == 0) {
                        vendorList = SamcrmDataFactory.getVendorDataInstance().getAreaSpecificVendors(zipCode, mobile, 0, 0);
                    }
                    response = Utility.constructJSONString(vendorList, individualData, true);
                    EmailUtils.sendSamcrmUserAuthenticationEMail(individualData.get("email"), individualData.get("name"), "Credentials Verification Succeed!");
                }
            }
        } catch (Exception e) {
        }
        return response;
    }

    @POST
    @Path("/logoutSamcrmUser")
    @Produces(MediaType.APPLICATION_JSON)
    public String logoutSamcrmUser(String params) throws Exception {

        JSONObject data = new JSONObject(params);
        String name = data.has("name") ? data.getString("name") : "";
        String email = data.has("email") ? data.getString("email") : "";
        String mobileNo = data.getString("mobile");
        String ipAddress = data.getString("ipAddress");
        String response = "";
        if (Utility.isNotNull(mobileNo)) {
            try {
                int status = SamcrmDataFactory.getUserDataInstance().logoutSamcrmUser(mobileNo, ipAddress);

                if (status >= 1) {
                    response = Utility.constructJSON("logoutsamcrmuser", true);
                    EmailUtils.sendSamcrmUserAuthenticationEMail(email, name, "You are logged-out successfully.");
                } else {
                    response = Utility.constructJSON("logoutsamcrmuser", false, "user not available");
                }
            } catch (Exception e) {
            }
        }
        return response;
    }

    @GET
    @Path("/samcrmVendorsList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getVendorsList(@QueryParam("mobile") String mobile, @QueryParam("postalCode") String zipCode, @QueryParam("vendorCategoryId") int category_id, @QueryParam("vendorSubtypeId") int subtype_id) {

        String response = Utility.constructJSONString(null, null, false);
        try {
            ArrayList<VendorDataBean> vendorList = SamcrmDataFactory.getVendorDataInstance().getAreaSpecificVendors(zipCode, mobile, category_id, subtype_id);
            response = Utility.constructJSONString(vendorList, null, true);
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmVendorCategoryList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getVendorCategoryList() {

        String response = Utility.constructProductCategoryJSON(null, false, null);
        try {
            ArrayList<ProductCategoryBean> vendorCategoryList = SamcrmDataFactory.getVendorDataInstance().selectVendorCategoryList();
            response = Utility.constructProductCategoryJSON(vendorCategoryList, true, null);
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmVendorSubTypeList")
    @Produces(MediaType.APPLICATION_JSON)
    public String getVendorSubTypeList() {

        String response = Utility.constructProductCategoryJSON(null, false, null);
        try {
            ArrayList<ProductCategoryBean> vendorSubTypeList = SamcrmDataFactory.getVendorDataInstance().selectVendorSubTypeList();
            response = Utility.constructProductCategoryJSON(vendorSubTypeList, true, null);
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmBlockCustomer")
    @Produces(MediaType.APPLICATION_JSON)
    public String blockCustomer(@QueryParam("vendor_id") int vendor_id, @QueryParam("customer_id") int customer_id, @QueryParam("mobile") int mobile) {

        String response = Utility.constructJSON("Block Customer", false);
        try {
            SamcrmDataFactory.getUserDataInstance().deactivateCustomer(vendor_id, customer_id, mobile, 0);
            response = Utility.constructJSON("Block Customer", true);
        } catch (Exception e) {
        }
        return response;
    }

    @GET
    @Path("/samcrmBlockVendor")
    @Produces(MediaType.APPLICATION_JSON)
    public String blockVendor(@QueryParam("vendor_id") int vendor_id, @QueryParam("customer_id") int customer_id, @QueryParam("mobile") int mobile) {

        String response = Utility.constructJSON("Block Vendor", false);
        try {
            SamcrmDataFactory.getUserDataInstance().deactivateCustomer(vendor_id, customer_id, mobile, 1);
            response = Utility.constructJSON("Block Vendor", true);
        } catch (Exception e) {
        }
        return response;
    }
}
