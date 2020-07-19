/**
 * Copyright (C) 2017, raagatech.
 *
 * All rights reserved under the Terms and Conditions of
 * raagatech by Jagriti Jan Kalyan Samiti, Allahabad.
 *
 * $Id: Register.java, v 1.1 Exp $
 *
 * Date Author Changes
 * Jun 18, 2017, 6:17:09 PM, Sarvesh created.
 */
package com.raagatech.aspirant.service.rest.impl;

import com.raagatech.bean.InquiryBean;
import com.raagatech.commons.EmailUtils;
import com.raagatech.commons.SMSUtils;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.raagatech.commons.Utility;
import com.raagatech.data.source.DatabaseConnection;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * RESTful web services
 *
 * @author <a href=mailto:sarveshtripathi@raagatech.com>Sarvesh</a>
 * @version $Revision: 1.1 Jun 18, 2017 6:17:09 PM
 * @see com.raagatech.aspirant.service.rest.impl.Register
 */
@Path("/register")
public class Register {

    @GET
    @Path("/doregister")
    @Produces(MediaType.APPLICATION_JSON)
    public String doRegister(@QueryParam("username") String username, @QueryParam("password") String password,
            @QueryParam("email") String email, @QueryParam("mobile") String mobileNo) {
        String response;
        int result = registerUser(username, password, email, Long.valueOf(mobileNo));
        if (result == 0) {
            response = Utility.constructJSON("register", true);
        } else {
            response = Utility.constructJSON("register", false, "sql insertion error occurred");
        }
        return response;
    }

    private int registerUser(String username, String password, String email, long mobileNo) {

        int result = 3;
        if (Utility.isNotNull(username) && Utility.isNotNull(password) && Utility.isNotNull(email)) {
            try {
                if (DatabaseConnection.insertUser(username, password, email, mobileNo)) {
                    result = 0;
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    @GET
    @Path("/doregisterinquiry")
    @Produces(MediaType.APPLICATION_JSON)
    public String doRegisterInquiry(@QueryParam("inquiryname") String inquiryname, @QueryParam("inspirationid") String inspirationid,
            @QueryParam("email") String email, @QueryParam("mobile") String mobileNo, @QueryParam("levelid") String levelid,
            @QueryParam("address") String address, @QueryParam("followupDetails") String followupDetails) {
        String response;
        int result = registerInquiry(inquiryname, Integer.valueOf(inspirationid), email, Long.valueOf(mobileNo), Integer.valueOf(levelid),
                address, followupDetails);
        if (result == 0) {
            response = Utility.constructJSON("register", true);
        } else {
            response = Utility.constructJSON("register", false, "sql insertion error occurred");
        }
        return response;
    }

    private int registerInquiry(String inquiryname, int inspirationid, String email, long mobileNo,
            int levelid, String address, String followupDetails) {

        int result = 3;
        if (Utility.isNotNull(inquiryname) && Utility.isNotNull(email)) {
            try {
                if (DatabaseConnection.insertInquiry(inquiryname, inspirationid, email, mobileNo,
                        levelid, address, followupDetails, "", "", "", "", 0, "",
                                "", "", "")) {
                    result = 0;
                    if (!email.equalsIgnoreCase("raksha@raagatech.com")) {
                        EmailUtils.sendGoogleMail(email, inquiryname, followupDetails);
                    }
                    SMSUtils.sendSMS(String.valueOf(mobileNo), followupDetails);
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    @GET
    @Path("/doselectlevel")
    @Produces(MediaType.APPLICATION_JSON)
    public String doSelectLevel() throws Exception {
        String response;
        LinkedHashMap<Integer, String> levelMap = DatabaseConnection.selectLevel();
        if (!levelMap.isEmpty()) {
            response = Utility.constructJSON("selectlevel", levelMap);
        } else {
            response = Utility.constructJSON("selectlevel", false, "no data available");
        }
        return response;
    }

    @GET
    @Path("/doselectinspiration")
    @Produces(MediaType.APPLICATION_JSON)
    public String doSelectInspiration() throws Exception {
        String response;
        LinkedHashMap<Integer, String> inspirationMap = DatabaseConnection.selectInspiration();
        if (!inspirationMap.isEmpty()) {
            response = Utility.constructJSON("selectinspiration", inspirationMap);
        } else {
            response = Utility.constructJSON("selectinspiration", false, "no data available");
        }
        return response;
    }

    @GET
    @Path("/dolistinquiry")
    @Produces(MediaType.APPLICATION_JSON)
    public String doListInquiry() throws Exception {
        String response;
        ArrayList<InquiryBean> inquiryList = DatabaseConnection.listInquiry();
        if (!inquiryList.isEmpty()) {
            response = Utility.constructJSON("listinquiry", inquiryList);
        } else {
            response = Utility.constructJSON("listinquiry", false, "no data available");
        }
        return response;
    }

    @GET
    @Path("/doupdateinquiry")
    @Produces(MediaType.APPLICATION_JSON)
    public String doUpdateInquiry(@QueryParam("inquiry_id") String inquiry_id, @QueryParam("inquiryname") String inquiryname, @QueryParam("inspirationid") String inspirationid,
            @QueryParam("email") String email, @QueryParam("mobile") String mobileNo, @QueryParam("levelid") String levelid,
            @QueryParam("address") String address, @QueryParam("followupDetails") String followupDetails) {
        String response;
        int result = updateInquiry(Integer.valueOf(inquiry_id), inquiryname, Integer.valueOf(inspirationid), email, Long.valueOf(mobileNo), Integer.valueOf(levelid),
                address, followupDetails);
        if (result == 0) {
            response = Utility.constructJSON("register", true);
        } else {
            response = Utility.constructJSON("register", false, "sql insertion error occurred");
        }
        return response;
    }

    private int updateInquiry(int inquiry_id, String inquiryname, int inspirationid, String email, long mobileNo,
            int levelid, String address, String followupDetails) {

        int result = 3;
        if (inquiry_id > 0 && Utility.isNotNull(inquiryname) && Utility.isNotNull(email)) {
            try {
                if (DatabaseConnection.updateInquiry(inquiry_id, inquiryname, inspirationid, email, mobileNo,
                        levelid, address, followupDetails, "", "", "", "", 0, "",
                                "", "", "")) {
                    result = 0;
                }
                if (!email.equalsIgnoreCase("raksha@raagatech.com")) {
                    EmailUtils.sendGoogleMail(email, inquiryname, followupDetails);
                }
                SMSUtils.sendSMS(String.valueOf(mobileNo), followupDetails);

            } catch (Exception e) {
            }
        }
        return result;
    }

    @GET
    @Path("/doselectinquirystatus")
    @Produces(MediaType.APPLICATION_JSON)
    public String doSelectInquiryStatus() throws Exception {
        String response;
        LinkedHashMap<Integer, String> inquiryStatusMap = DatabaseConnection.selectInquiryStatus();
        if (!inquiryStatusMap.isEmpty()) {
            response = Utility.constructJSON("selectlevel", inquiryStatusMap);
        } else {
            response = Utility.constructJSON("selectlevel", false, "no data available");
        }
        return response;
    }

    @GET
    @Path("/doupdatefollowup")
    @Produces(MediaType.APPLICATION_JSON)
    public String doUpdateFollowup(@QueryParam("inquiry_id") String inquiry_id, @QueryParam("inquirystatus_id") String inquirystatus_id,
            @QueryParam("followupDetails") String followupDetails) {
        String response;
        int result = updateFollowup(Integer.valueOf(inquiry_id), Integer.valueOf(inquirystatus_id), followupDetails);
        if (result == 0) {
            response = Utility.constructJSON("register", true);
        } else {
            response = Utility.constructJSON("register", false, "sql insertion error occurred");
        }
        return response;
    }

    private int updateFollowup(int inquiry_id, int inquirystatus_id, String followupDetails) {

        int result = 3;
        if (inquiry_id > 0 && Utility.isNotNull(followupDetails) && inquirystatus_id > 0) {
            try {
                if (DatabaseConnection.updateFollowup(inquiry_id, inquirystatus_id, followupDetails)) {
                    result = 0;
                }

                InquiryBean inquiryBean = DatabaseConnection.getInquiryById(inquiry_id);
                if (inquiryBean != null) {
                    if (!inquiryBean.getEmail().equalsIgnoreCase("raksha@raagatech.com")) {
                        EmailUtils.sendGoogleMail(inquiryBean.getEmail(), inquiryBean.getFirstname(), followupDetails);
                    }
                    SMSUtils.sendSMS(String.valueOf(inquiryBean.getMobile()), followupDetails);
                }
            } catch (Exception e) {
            }
        }
        return result;
    }
}
