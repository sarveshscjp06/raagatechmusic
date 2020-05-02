/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.data.source;

import com.raagatech.bean.ProductCategoryBean;
import com.raagatech.bean.ProductsAndServicesBean;
import com.raagatech.bean.VendorDataBean;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *
 * @author STripathi
 */
public interface VendorDataInterface {

    public LinkedHashMap<Integer, String> selectVendorCategory() throws Exception;

    public LinkedHashMap<Integer, String> selectVendorSubtype() throws Exception;

    public LinkedHashMap<Integer, String> getVendorData(String vendorCategory, String vendorSubType) throws Exception;

    public ArrayList<ProductCategoryBean> selectProductCategoryDetails(String vendorId) throws Exception;

    public ArrayList<ProductsAndServicesBean> selectProductsAndServicesDetails(String vendorCategory) throws Exception;

    public int addContact(String name, String email, String mobile, String dob, String address, String zipCode, 
            String comment, String vendor_group_id, String contactId, String message) throws Exception;

    public ArrayList<VendorDataBean> selectContacts(String vendor_group_id) throws Exception;

    public String addCategory(String categoryName, String categoryDescription, String vendor_group_id, int categorySequence) throws Exception;

    public String editCategory(int categoryId, String categoryName, String categoryDescription, String vendor_group_id, int categorySequence) throws Exception;

    public String addProductAndService(String productName, String productDescription,
            String vendor_group_id, int offer_price, int discount, int productcategory_id, int productSequence) throws Exception;

    public String editProductAndService(int productId, String productName,
            String productDescription, String vendor_group_id, int productSequence, int offer_price, int discount, int productcategory_id) throws Exception;

    public ArrayList<VendorDataBean> getAreaSpecificVendors(String zipCode, String mobile, int category_id, int subtype_id) throws Exception;

    public ArrayList<VendorDataBean> getVendorwiseContactList() throws Exception;

    public ArrayList<ProductCategoryBean> selectVendorCategoryList() throws Exception;

    public ArrayList<ProductCategoryBean> selectVendorSubTypeList() throws Exception;

    public String updateProductServiceInventory(int productId, int quantity, String unit) throws Exception;

    public int checkInventory(int productId) throws Exception;

    public int getDiscount(int vendor_id, int customer_id, double total_price) throws Exception;

    public String generateCouponCode(int order_id, int vendor_id, int customer_id, int discount) throws Exception;
    
    public int updateInventoryByOrderId(int order_id) throws Exception;
    
    public String getUrgentServiceContactNo(int vendor_id) throws Exception;
    
    public long checkUserExistance(long mobile) throws Exception;

}
