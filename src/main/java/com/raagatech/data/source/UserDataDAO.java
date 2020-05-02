/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.data.source;

import com.raagatech.bean.VendorDataBean;
import static com.raagatech.data.source.DatabaseConnection.FORMATTER;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author STripathi
 */
public class UserDataDAO implements UserDataInterface {

    @Override
    public int getSamcrmUserStatus(String mobileNo, String password, String ipAddress) throws Exception {
        //0 not available or 8 not individual, 1 not active, 2 not vendor, 
        //4 not loggedin, 5 not on promotion, 6 not from same device, 7 mobile or password not correct
        int samcrmUserStatus = 0;
        Connection connection;
        Statement statement;
        try {
            connection = DatabaseConnection.createConnection();
            statement = connection.createStatement();
            String queryCheckLogin = "SELECT su.is_active, su.is_loggedin, su.is_vendor, su.is_on_promotion, su.ip_address "
                    + "FROM samcrm_users su join samcrm_individual si on su.mobile = si.mobile "
                    + "WHERE su.mobile = " + mobileNo + " and si.password = '" + password + "' and su.ip_address = '" + ipAddress + "' ";
            ResultSet result = statement.executeQuery(queryCheckLogin);
            while (result.next()) {
                if (result.getInt("is_active") == 0) {
                    samcrmUserStatus = 1;
                } else if (result.getInt("is_loggedin") == 0) {
                    samcrmUserStatus = 4;
                } else if (result.getInt("is_vendor") == 0) {
                    samcrmUserStatus = 2;
                } else if (result.getInt("is_on_promotion") == 0) {
                    samcrmUserStatus = 5;
                }
            }

            if (samcrmUserStatus == 0) {
                queryCheckLogin = "SELECT individual_id FROM samcrm_individual WHERE mobile = " + mobileNo + " AND password is null";
                result = statement.executeQuery(queryCheckLogin);
                while (result.next()) {
                    if (result.getInt("individual_id") > 0) {
                        samcrmUserStatus = 8;
                    }
                }
            }

            if (samcrmUserStatus == 0) {
                queryCheckLogin = "SELECT individual_id FROM samcrm_individual WHERE mobile = " + mobileNo + " and password = '" + password + "'";
                result = statement.executeQuery(queryCheckLogin);
                while (result.next()) {
                    if (result.getInt("individual_id") > 0) {
                        samcrmUserStatus = 6;
                    }
                }
            }

            if (samcrmUserStatus == 0) {
                queryCheckLogin = "SELECT individual_id FROM samcrm_individual WHERE mobile = " + mobileNo + " OR password = '" + password + "'";
                result = statement.executeQuery(queryCheckLogin);
                while (result.next()) {
                    if (result.getInt("individual_id") > 0) {
                        samcrmUserStatus = 7;
                    }
                }
            }
            result.close();
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return samcrmUserStatus;
    }

    @Override
    public int addUpdateSamcrmUser(String name, String email, String mobile, String zipCode, String password, String ipAddress,
            String vendorCategoryId, String vendorSubtypeId, String vendorTitle, String vendorRegistrationNo, String individual_id,
            String vendorDescription, String pushMessage, String emailCampaignText, String bulkSmsText, String address) throws Exception {
        int records = 0;
        Connection connection;
        Statement statement;
        try {

            if ((individual_id != null && !individual_id.isEmpty()) || getSamcrmUserStatus(mobile, password, ipAddress) == 8) {
                connection = DatabaseConnection.createConnection();
                statement = connection.createStatement();
                String queryUpdateUser = "UPDATE samcrm_individual SET email = '" + email + "' ";
                if (name != null) {
                    queryUpdateUser += ", name = '" + name + "' ";
                }
                if (zipCode != null) {
                    queryUpdateUser += ", zipcode = '" + zipCode + "' ";
                }
                if (password != null) {
                    queryUpdateUser += ", password = '" + password + "' ";
                }
                if (address != null) {
                    queryUpdateUser += ", address = '" + address + "' ";
                }
                queryUpdateUser += " WHERE mobile = " + mobile + " OR individual_id = " + individual_id;
                records = statement.executeUpdate(queryUpdateUser);

                if (records == 1 && vendorCategoryId != null && !vendorCategoryId.isEmpty()
                        && vendorSubtypeId != null && !vendorSubtypeId.isEmpty()) {
                    int vendor = 0;
                    String queryVendorId = "SELECT id FROM samcrm_vendor_category_subtype WHERE mobile = " + mobile;
// commented to prevent multiple vendor_group_id for now.
//                            + " AND vendor_category_id = " + vendorCategoryId
//                            + " AND vendor_subtype_id = " + vendorSubtypeId;
                    ResultSet result = statement.executeQuery(queryVendorId);
                    while (result.next()) {
                        vendor = result.getInt("id");
                        break;
                    }
                    result.close();
                    if (vendor > 0) {
                        statement = connection.createStatement();
                        String queryUpdateVendorStatus = "UPDATE samcrm_vendor_category_subtype set "
                                + "vendor_title ='" + vendorTitle + "', vendor_registration_no = '" + vendorRegistrationNo + "'";
                        if (vendorDescription != null) {
                            queryUpdateVendorStatus += ", description = '" + vendorDescription + "'";
                        }
                        if (pushMessage != null) {
                            queryUpdateVendorStatus += ", push_message = '" + pushMessage + "'";
                        }
                        if (emailCampaignText != null) {
                            queryUpdateVendorStatus += ", email_campaign_text = '" + emailCampaignText + "'";
                        }
                        if (bulkSmsText != null) {
                            queryUpdateVendorStatus += ", bulk_sms_text = '" + bulkSmsText + "'";
                        }
                        queryUpdateVendorStatus += " WHERE id = " + vendor;
                        records = statement.executeUpdate(queryUpdateVendorStatus);
                    } else {
                        statement = connection.createStatement();
                        String queryInsertVendorStatus = "INSERT into samcrm_vendor_category_subtype (mobile, vendor_category_id, vendor_subtype_id, vendor_title, vendor_registration_no, "
                                + "description, push_message, email_campaign_text, bulk_sms_text) "
                                + "VALUES (" + mobile + ", " + Integer.parseInt(vendorCategoryId) + ", " + Integer.parseInt(vendorSubtypeId)
                                + ", '" + vendorTitle + "', '" + vendorRegistrationNo + "', "
                                + " '" + vendorDescription + "', '" + pushMessage + "', '" + emailCampaignText + "', '" + bulkSmsText + "')";
                        records = statement.executeUpdate(queryInsertVendorStatus);

                        queryVendorId = "SELECT id FROM samcrm_vendor_category_subtype WHERE mobile = " + mobile;
                        result = statement.executeQuery(queryVendorId);
                        while (result.next()) {
                            vendor = result.getInt("id");
                        }
                        result.close();

                        statement = connection.createStatement();
                        String queryUpdateUserStatus = "UPDATE samcrm_users SET is_vendor = " + vendor + " WHERE mobile = " + mobile;
                        records = statement.executeUpdate(queryUpdateUserStatus);
                    }
                }

            } else {
                int key = DatabaseConnection.generateNextPrimaryKey("samcrm_individual", "individual_id");
                connection = DatabaseConnection.createConnection();
                statement = connection.createStatement();
                String queryInsertUser = "INSERT into samcrm_individual (individual_id, name, email, mobile, zipcode, password, country_code, creation_date, address) "
                        + "VALUES (" + key + ", '" + name + "', '" + email + "', " + mobile + ", '" + zipCode + "', '" + password + "', 091, '" + FORMATTER.format(new Date()) + "', '" + address + "')";
                records = statement.executeUpdate(queryInsertUser);

                if (records == 1) {
                    int vendor = 0;
                    if (vendorCategoryId != null && !vendorCategoryId.isEmpty()
                            && vendorSubtypeId != null && !vendorSubtypeId.isEmpty()) {
                        statement = connection.createStatement();
                        String queryInsertVendorStatus = "INSERT into samcrm_vendor_category_subtype (mobile, vendor_category_id, vendor_subtype_id, vendor_title, vendor_registration_no, "
                                + "description, push_message, email_campaign_text, bulk_sms_text) "
                                + "VALUES (" + mobile + ", " + Integer.parseInt(vendorCategoryId) + ", " + Integer.parseInt(vendorSubtypeId)
                                + "'" + vendorTitle + "', '" + vendorRegistrationNo + "', "
                                + "'" + vendorDescription + "', '" + pushMessage + "', '" + emailCampaignText + "', '" + bulkSmsText + "')";
                        records = statement.executeUpdate(queryInsertVendorStatus);

                        String queryVendorId = "SELECT id FROM samcrm_vendor_category_subtype WHERE mobile = " + mobile;
                        ResultSet result = statement.executeQuery(queryVendorId);
                        while (result.next()) {
                            vendor = result.getInt("id");
                        }
                        result.close();
                    }
                    statement = connection.createStatement();
                    String queryInsertUserStatus = "INSERT into samcrm_users (mobile, is_active, is_vendor, is_loggedin, is_on_promotion, ip_address) "
                            + "VALUES (" + mobile + ", 1, " + vendor + ", 1, 1,'" + ipAddress + "')";
                    records = statement.executeUpdate(queryInsertUserStatus);

                    if (vendor == 1) {
                        statement = connection.createStatement();
                        String insertQuery = "insert into samcrm_creditpoint(vendor_id, credit_date, credit_reward_points, coupon_code, unit) "
                                + "values(" + key + ", '" + FORMATTER.format(new Date()) + "', 10, 'V" + key + "C0O0%10', '%')";
                        records = statement.executeUpdate(insertQuery);
                    }
                }
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return records;
    }

    @Override
    public int updateSamcrmUserStatus(String mobile, String ipAddress, int userStatus) throws Exception {
        int records = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String queryInsertUserStatus = "UPDATE samcrm_users set is_active = 1, is_loggedin = 1 WHERE ip_address = '" + ipAddress + "' "
                    + " and mobile = " + mobile;
            records = statement.executeUpdate(queryInsertUserStatus);
            
            if (records == 0 && userStatus == 6) {
                ResultSet rs = statement.executeQuery("SELECT is_vendor from samcrm_users WHERE mobile = " + mobile);
                int vendor = 0;
                while (rs.next()) {
                    vendor = rs.getInt("is_vendor");
                    break;
                }
                rs.close();
                queryInsertUserStatus = "INSERT into samcrm_users (mobile, is_active, is_vendor, is_loggedin, is_on_promotion, ip_address) "
                        + "VALUES (" + mobile + ", 1, " + vendor + ", 1, 1,'" + ipAddress + "')";
                records = statement.executeUpdate(queryInsertUserStatus);
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return records;
    }

    @Override
    public LinkedHashMap<String, String> checkSamcrmLogin(String mobileNo, String ipAddress, String individualId) throws Exception {
        LinkedHashMap<String, String> individualData = new LinkedHashMap<>();
        Connection connection;
        Statement statement;
        try {
            connection = DatabaseConnection.createConnection();
            statement = connection.createStatement();
            String queryCheckLogin = "SELECT si.individual_id, si.name, si.mobile, si.email, si.zipcode, si.profile_pic, si.profile_color, si.password, si.address, "
                    + "su.is_loggedin, su.is_vendor, svcs.vendor_category_id, svcs.vendor_subtype_id, svcs.vendor_registration_no, svcs.description, "
                    + "svcs.vendor_title, svcs.push_message, svcs.email_campaign_text, svcs.bulk_sms_text, svcs.id, "
                    + "soo.order_id, count(item_id) as cart_count "
                    + "from samcrm_individual si inner join samcrm_users su on su.mobile = si.mobile ";

            if (individualId != null && !individualId.isEmpty()) {
                queryCheckLogin = queryCheckLogin + " and si.individual_id = " + individualId;
            } else {
                queryCheckLogin = queryCheckLogin + " and su.mobile = " + mobileNo;
            }

            queryCheckLogin = queryCheckLogin + " left join samcrm_vendor_category_subtype svcs on svcs.mobile = si.mobile "
                    + "left join samcrm_obm_orders soo on soo.status = 'open' AND soo.customer_id = si.individual_id "
                    + "left join samcrm_obm_items soi on soi.order_id = soo.order_id where " //"su.ip_address = '" + ipAddress + "' and "
                    + "su.is_loggedin = 1 ";
            if (mobileNo != null && !mobileNo.isEmpty()) {
                queryCheckLogin = queryCheckLogin + " and su.mobile = " + mobileNo;
            }
            queryCheckLogin = queryCheckLogin + " group by si.mobile";
            ResultSet result = statement.executeQuery(queryCheckLogin);
            while (result.next()) {
                individualData.put("individual_id", result.getString("individual_id"));
                String name = result.getString("name");
                name = name.replaceAll("\\s+", "_");
                individualData.put("name", name);
                individualData.put("mobile", result.getString("mobile"));
                individualData.put("email", result.getString("email"));
                individualData.put("zipCode", result.getString("zipcode"));
                individualData.put("profilePic", result.getString("profile_pic"));
                individualData.put("profileColor", result.getString("profile_color"));
                individualData.put("password", result.getString("password"));
                individualData.put("address", result.getString("address"));
                individualData.put("isVendor", result.getString("is_vendor"));
                individualData.put("vendorTitle", result.getString("vendor_title"));
                individualData.put("vendorRegistrationNo", result.getString("vendor_registration_no"));
                individualData.put("vendorDescription", result.getString("description"));
                individualData.put("pushMessage", result.getString("push_message"));
                individualData.put("emailCampaignText", result.getString("email_campaign_text"));
                individualData.put("bulkSmsText", result.getString("bulk_sms_text"));
                individualData.put("vendorCategoryId", result.getString("vendor_category_id"));
                individualData.put("vendorSubtypeId", result.getString("vendor_subtype_id"));
                individualData.put("vendor_group_id", result.getString("id"));
                individualData.put("order_id", result.getString("order_id"));
                break;
            }
            result.close();
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return individualData;
    }

    @Override
    public int logoutSamcrmUser(String mobile, String ipAddress) throws Exception {
        int records = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String queryInsertUserStatus = "UPDATE samcrm_users set is_loggedin = 0"//, ip_address = '" + ipAddress + "' "
                    + " WHERE mobile = " + mobile;
            records = statement.executeUpdate(queryInsertUserStatus);
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return records;
    }

    @Override
    public int deactivateCustomer(int vendorId, int cusomerId, int mobileNo, int isVendor) throws Exception {
        int records = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String queryInsertUserStatus = "UPDATE samcrm_users set is_active = 0 AND is_loggedin = 0 WHERE is_vendor = " + isVendor + " AND mobile = " + mobileNo;
            records = statement.executeUpdate(queryInsertUserStatus);
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return records;
    }

    @Override
    public VendorDataBean selectUserData(long user_id) throws Exception {

        VendorDataBean userData = null;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String selectQuery = "SELECT si.individual_id, si.name, si.email, si.mobile, si.zipcode, si.address, si.creation_date, si.country_code, si.date_of_birth "
                    + "FROM samcrm_individual si join samcrm_users su on si.mobile = su.mobile su.is_active = 1 where si.individual_id = " + user_id;
            try (ResultSet result = statement.executeQuery(selectQuery)) {
                while (result.next()) {
                    userData = new VendorDataBean();
                    userData.setIndividual_id(result.getString("individual_id"));
                    userData.setName(result.getString("name"));
                    userData.setEmail(result.getString("email"));
                    userData.setMobile(result.getString("mobile"));
                    userData.setZipcode(result.getString("zipcode"));
                    userData.setAddress(result.getString("address"));
                    userData.setCreation_date(result.getString("creation_date"));
                    userData.setCountry_code(result.getString("country_code"));
                    userData.setDob(result.getString("date_of_birth"));
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return userData;
    }

}
