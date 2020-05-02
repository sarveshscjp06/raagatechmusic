/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.data.source;

import com.raagatech.bean.VendorDataBean;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author STripathi
 */
public interface UserDataInterface {
    
    public int getSamcrmUserStatus(String mobileNo, String password, String ipAddress) throws Exception;
    
    public int addUpdateSamcrmUser(String name, String email, String mobile, String zipCode, String password, String ipAddress, 
            String vendorCategoryId, String vendorSubtypeId, String vendorTitle, String vendorRegistrationNo, String individual_id, 
            String vendorDescription, String pushMessage, String emailCampaignText, String bulkSmsText, String address) throws Exception;
    
    public int updateSamcrmUserStatus(String mobile, String ipAddress, int userStatus) throws Exception;
    
    public LinkedHashMap<String, String> checkSamcrmLogin(String mobileNo, String ipAddress, String individualId) throws Exception;
    
    public int logoutSamcrmUser(String mobileNo, String ipAddress) throws Exception;
    
    public int deactivateCustomer(int vendorId, int cusomerId, int mobileNo, int isVendor) throws Exception;

    public VendorDataBean selectUserData(long user_id) throws Exception;
}
