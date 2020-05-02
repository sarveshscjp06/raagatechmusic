/**
 * Copyright (C) 2017, raagatech.
 *
 * All rights reserved under the Terms and Conditions of
 * raagatech by Jagriti Jan Kalyan Samiti, Allahabad.
 *
 * $Id: DatabaseConnection.java, v 1.1 Exp $
 *
 * Date Author Changes
 * Jun 18, 2017, 1:47:54 PM, Sarvesh created.
 */
package com.raagatech.data.source;

import com.google.appengine.api.utils.SystemProperty;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.raagatech.bean.InquiryBean;
import com.raagatech.bean.SliderImageBean;
import java.util.LinkedHashMap;

/**
 * TODO provides the database connection and some primary execution.
 *
 * @author <a href=mailto:sarveshtripathi@raagatech.com>Sarvesh</a>
 * @version $Revision: 1.1 Jun 18, 2017 1:47:54 PM
 * @see com.raagatech.data.source.DatabaseConnection
 */
public class DatabaseConnection {

    protected static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    public static Connection createConnection() throws Exception {

        Connection connection = null;
        try {
            Class.forName(Constants.dbDriverClass);
            if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
                connection = DriverManager.getConnection(Constants.dbUrl_production, Constants.dbUsername_production, Constants.dbPassword_production);

            } else if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Development) {
                connection = DriverManager.getConnection(Constants.dbUrl, Constants.dbUsername, Constants.dbPassword);
            }

        } catch (ClassNotFoundException | SQLException e) {
        }
        return connection;
    }

    public static boolean checkLogin(String userName, long password) throws Exception {

        boolean isUserAvalable = Boolean.FALSE;
        Connection connection;
        Statement statement;
        try {

            connection = DatabaseConnection.createConnection();
            statement = connection.createStatement();
            String queryCheckLogin = "SELECT * FROM users WHERE email = '" + userName + "' "
                    + "AND mobile = " + password;
            ResultSet result = statement.executeQuery(queryCheckLogin);
            while (result.next()) {
                isUserAvalable = Boolean.TRUE;
                break;
            }
            result.close();
            connection.close();
        } catch (SQLException sqle) {
        }
        return isUserAvalable;
    }

    public static boolean insertUser(String username, String password, String email, long mobileNo) throws Exception {

        boolean insertStatus = Boolean.FALSE;
        Connection connection;
        try {

            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String queryInsertUser = "INSERT into users (username, password, creation_date, email, country_code, mobile) "
                    + "VALUES ('" + username + "','" + password + "','" + FORMATTER.format(new Date()) + "', '" + email + "', 091, " + mobileNo + ")";
            int records = statement.executeUpdate(queryInsertUser);
            if (records > 0) {
                insertStatus = Boolean.TRUE;
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return insertStatus;
    }

    public static boolean insertInquiry(String inquiryname, int inspirationid, String email, long mobileNo,
            int levelid, String address, String followupDetails) throws Exception {

        boolean insertStatus = Boolean.FALSE;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String queryInsertInquiry = "INSERT into inquiry (firstname, inspiration_id, inquiry_date, email, mobile"
                    + ", level_id, address_line1) "
                    + "VALUES ('" + inquiryname + "'," + inspirationid + ",'" + FORMATTER.format(new Date()) + "', '" + email + "', " + mobileNo + ","
                    + levelid + ", '" + address + "')";
            int records = statement.executeUpdate(queryInsertInquiry);
            if (records > 0) {
                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT MAX(inquiry_id) from inquiry");
                int inquiry_id = 0;
                while (rs.next()) {
                    inquiry_id = rs.getInt(1);
                }
                rs.close();
                statement = connection.createStatement();
                String queryInsertFollowupDetails = "INSERT into followupdetails (inquiry_id, inquirystatus_id, followup_details, followup_date) "
                        + "VALUES (" + inquiry_id + ", 1, '" + followupDetails + "','" + FORMATTER.format(new Date()) + "')";
                records = statement.executeUpdate(queryInsertFollowupDetails);
                if (records > 0) {
                    insertStatus = Boolean.TRUE;
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return insertStatus;
    }

    public static LinkedHashMap<Integer, String> selectLevel() throws Exception {

        LinkedHashMap<Integer, String> levelMap = new LinkedHashMap<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectLevel = "SELECT level_id, levelname FROM level order by level_id";
            ResultSet result = statement.executeQuery(querySelectLevel);
            while (result.next()) {
                int key = result.getInt("level_id");
                String value = result.getString("levelname");
                levelMap.put(key, value);
            }
            result.close();
            connection.close();
        } catch (SQLException sqle) {
        }
        return levelMap;
    }

    public static LinkedHashMap<Integer, String> selectInspiration() throws Exception {

        LinkedHashMap<Integer, String> inspirationMap = new LinkedHashMap<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectInspiration = "SELECT inspiration_id, inspirationname FROM inspiration";
            ResultSet result = statement.executeQuery(querySelectInspiration);
            while (result.next()) {
                int key = result.getInt("inspiration_id");
                String value = result.getString("inspirationname");
                inspirationMap.put(key, value);
            }
            result.close();
            connection.close();
        } catch (SQLException sqle) {
        }
        return inspirationMap;
    }

    public static ArrayList<InquiryBean> listInquiry() throws Exception {

        ArrayList<InquiryBean> inquiryList = new ArrayList<>();
        Connection connection;
        try {

            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectInquiry = "select i.inquiry_id, i.firstname, i.inquiry_date, i.email, i.mobile, i.address_line1, "
                    + "i.inspiration_id, i.level_id, f.followup_details, f.inquirystatus_id, "
                    + "s.label_text, s.label_color from inquiry i join followupdetails f on i.inquiry_id = f.inquiry_id "
                    + "join inquirystatusmaster s on f.inquirystatus_id = s.inquirystatus_id "
                    + "where f.followup_id = (select max(d.followup_id) from followupdetails d where d.inquiry_id = i.inquiry_id)";
            ResultSet result = statement.executeQuery(querySelectInquiry);
            while (result.next()) {
                InquiryBean inquiryBean = new InquiryBean();
                inquiryBean.setInquiry_id(result.getInt("inquiry_id"));
                inquiryBean.setFirstname(result.getString("firstname"));
                inquiryBean.setInquiry_date(result.getDate("inquiry_date"));
                inquiryBean.setEmail(result.getString("email"));
                inquiryBean.setMobile(result.getLong("mobile"));
                inquiryBean.setAddress_line1(result.getString("address_line1"));
                inquiryBean.setInspiration_id(result.getInt("inspiration_id"));
                inquiryBean.setLevel_id(result.getInt("level_id"));
                inquiryBean.setFollowup_details(result.getString("followup_details"));
                inquiryBean.setInquirystatus_id(result.getInt("inquirystatus_id"));
                inquiryBean.setLabel_text(result.getString("label_text"));
                inquiryBean.setLabel_color(result.getString("label_color"));
                inquiryList.add(inquiryBean);
                //inquiryList.add(result.getString("email")+"<x>"+result.getString("mobile"));
            }
            result.close();
            connection.close();
        } catch (SQLException sqle) {
        }
        return inquiryList;
    }

    public static boolean updateInquiry(int inquiry_id, String inquiryname, int inspirationid, String email, long mobileNo,
            int levelid, String address, String followupDetails) throws Exception {

        boolean updateStatus = Boolean.FALSE;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String queryUpdateInquiry = "UPDATE inquiry SET firstname = '" + inquiryname + "', email = '" + email + "', mobile = " + mobileNo
                    + ", address_line1 =  '" + address + "', inspiration_id = " + inspirationid + " , level_id = " + levelid + " where inquiry_id = " + inquiry_id;
            int records = statement.executeUpdate(queryUpdateInquiry);
            if (records > 0) {
                statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT MAX(followup_id) from followupdetails where inquiry_id = " + inquiry_id);
                int followup_id = 0;
                while (rs.next()) {
                    followup_id = rs.getInt(1);
                }
                rs.close();
                if (followup_id > 0) {
                    statement = connection.createStatement();
                    String queryUpdateFollowupDetails = "UPDATE followupdetails SET followup_details = '" + followupDetails + "' "
                            + " where followup_id = " + followup_id;
                    records = statement.executeUpdate(queryUpdateFollowupDetails);
                    updateStatus = Boolean.TRUE;
                } else {
                    updateStatus = updateFollowup(inquiry_id, 1, followupDetails);
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return updateStatus;
    }

    public static LinkedHashMap<Integer, String> selectInquiryStatus() throws Exception {

        LinkedHashMap<Integer, String> inquiryStatusMap = new LinkedHashMap<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectLevel = "SELECT inquirystatus_id, label_text FROM inquirystatusmaster order by inquirystatus_id";
            ResultSet result = statement.executeQuery(querySelectLevel);
            while (result.next()) {
                int key = result.getInt("inquirystatus_id");
                String value = result.getString("label_text");
                inquiryStatusMap.put(key, value);
            }
            result.close();
            connection.close();
        } catch (SQLException sqle) {
        }
        return inquiryStatusMap;
    }

    public static boolean updateFollowup(int inquiry_id, int inquirystatus_id, String followupDetails) throws Exception {

        boolean updateStatus = Boolean.FALSE;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("INSERT into followupdetails (inquiry_id, inquirystatus_id, followup_details, followup_date) "
                    + "VALUES (" + inquiry_id + ", " + inquirystatus_id + ", '" + followupDetails + "','" + FORMATTER.format(new Date()) + "')");
            updateStatus = Boolean.TRUE;
            connection.close();
        } catch (SQLException sqle) {
        }
        return updateStatus;
    }

    public static InquiryBean getInquiryById(int inquiryId) throws Exception {

        InquiryBean inquiryBean = null;
        Connection connection;
        try {

            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectInquiry = "select i.firstname, i.inquiry_date, i.email, i.mobile from inquiry i where i.inquiry_id = " + inquiryId;
            ResultSet result = statement.executeQuery(querySelectInquiry);
            while (result.next()) {
                inquiryBean = new InquiryBean();
                inquiryBean.setFirstname(result.getString("firstname"));
                inquiryBean.setInquiry_date(result.getDate("inquiry_date"));
                inquiryBean.setEmail(result.getString("email"));
                inquiryBean.setMobile(result.getLong("mobile"));
            }
            result.close();
            connection.close();
        } catch (SQLException sqle) {
        }
        return inquiryBean;
    }

    public static int generateNextPrimaryKey(String tableName, String columnName) throws Exception {

        int nextInt = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String selectQuery = "select COALESCE(max(" + columnName + "), 0) + 1 as primary_key from " + tableName;
            ResultSet rs = statement.executeQuery(selectQuery);
            while (rs.next()) {
                nextInt = rs.getInt("primary_key");
            }
            rs.close();
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return nextInt;
    }

    public static ArrayList<SliderImageBean> listSliderImage() {

        ArrayList<SliderImageBean> sliderImageList = new ArrayList<>();
        int imageId = 1;
        do {
            SliderImageBean bean = new SliderImageBean();
            bean.setId(String.valueOf(imageId));
            bean.setTitle("SliderImage " + imageId);
            bean.setDescription("This is Slider Image example!");
            if (imageId == 1) {
                bean.setImage_url("http:\\/\\/localhost:8888\\/images\\/stories\\/facebookIcon.png");
            }
            if (imageId == 2) {
                bean.setImage_url("http:\\/\\/localhost:8888\\/images\\/stories\\/facebookIcon.png");
            }
            if (imageId == 3) {
                bean.setImage_url("http:\\/\\/localhost:8888\\/images\\/stories\\/facebookIcon.png");
            }
            sliderImageList.add(bean);
            imageId++;
        } while (imageId < 4);
        return sliderImageList;
    }

    public static int selectInquiry(String email, long mobileNo) throws Exception {

        int inquiry_id = 0;
        try {
            Connection connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectInquiry = "select inquiry_id from inquiry WHERE email = '" + email + "' AND mobile = " + mobileNo;
            ResultSet result = statement.executeQuery(querySelectInquiry);
            while (result.next()) {
                inquiry_id = result.getInt("inquiry_id");
            }
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
        }
        return inquiry_id;
    }
}
