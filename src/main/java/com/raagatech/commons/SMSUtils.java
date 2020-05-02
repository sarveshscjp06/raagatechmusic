/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 *
 * @author Sarvesh
 */
public class SMSUtils {

    //Your authentication key
    private static final String AUTHKEY = "185479AhnoJHbbMwN5a1a53b5";
    //Sender ID,While using route4 sender id should be 6 characters long.
    private static final String SENDERID = "Raksha";
    //define route
    private static final String ROUTE = "4";

    //Send SMS API
    private static final String MAINURL = "http://api.msg91.com/api/sendhttp.php?";

    public static void sendSMS(String mobileNo, String smsText) {
        //Multiple mobiles numbers separated by comma
        String mobiles = mobileNo;
        //Your message to send, Add URL encoding here.
        String message = smsText + " Thanks. Please call or email: 9891029284 / raksha@raagatech.com";
        //encoding message
        String encoded_message = URLEncoder.encode(message);

        //Prepare parameter string
        StringBuilder sbPostData = new StringBuilder(MAINURL);
        sbPostData.append("authkey=" + AUTHKEY);
        sbPostData.append("&mobiles=").append(mobiles);
        sbPostData.append("&message=").append(encoded_message);
        sbPostData.append("&route=" + ROUTE);
        sbPostData.append("&sender=" + SENDERID);

        //Prepare Url
        URLConnection myURLConnection;
        URL myURL;
        BufferedReader reader;
        //final string
        //mainUrl = sbPostData.toString();
        try {
            //prepare connection
            myURL = new URL(sbPostData.toString());
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            //reading response
            String response;
            while ((response = reader.readLine()) != null) //print response
            {
                System.out.println(response);
            }

            //finally close connection
            reader.close();
        } catch (IOException e) {
        }
    }
    
    public static void sendSamcrmOtp(String mobileNo, String message) {
        //Multiple mobiles numbers separated by comma
        String mobiles = mobileNo;
        //Your message to send, Add URL encoding here.
        //String message = otpText + " Thanks. For further inquiry please call or email: +919891029284 / raksha@raagatech.com";
        //encoding message
        String encoded_message = URLEncoder.encode(message);

        //Prepare parameter string
        StringBuilder sbPostData = new StringBuilder(MAINURL);
        sbPostData.append("authkey=" + AUTHKEY);
        sbPostData.append("&mobiles=").append(mobiles);
        sbPostData.append("&message=").append(encoded_message);
        sbPostData.append("&route=" + ROUTE);
        sbPostData.append("&sender=SAMCRM");

        //Prepare Url
        URLConnection myURLConnection;
        URL myURL;
        BufferedReader reader;
        //final string
        //mainUrl = sbPostData.toString();
        try {
            //prepare connection
            myURL = new URL(sbPostData.toString());
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            //reading response
            String response;
            while ((response = reader.readLine()) != null) //print response
            {
                System.out.println(response);
            }

            //finally close connection
            reader.close();
        } catch (IOException e) {
        }
    }    
}
