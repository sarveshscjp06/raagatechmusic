/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.data.source;

import com.raagatech.bean.ProductCategoryBean;
import com.raagatech.bean.ProductsAndServicesBean;
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
public class VendorDataDAO implements VendorDataInterface {

    @Override
    public LinkedHashMap<Integer, String> getVendorData(String vendorCategory, String vendorSubType) throws Exception {

        LinkedHashMap<Integer, String> subtypeMap = new LinkedHashMap<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectSubtype = "SELECT subtype_id, label, vendorcategory_id FROM samcrm_vendorsubtype";
            try (ResultSet result = statement.executeQuery(querySelectSubtype)) {
                while (result.next()) {
                    int key = result.getInt("subtype_id");
                    String value = result.getString("label") + "<+>" + result.getInt("vendorcategory_id");
                    subtypeMap.put(key, value);
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return subtypeMap;
    }

    @Override
    public LinkedHashMap<Integer, String> selectVendorCategory() throws Exception {

        LinkedHashMap<Integer, String> categoryMap = new LinkedHashMap<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectCategory = "SELECT category_id, label FROM samcrm_vendorcategory order by category_id";
            try (ResultSet result = statement.executeQuery(querySelectCategory)) {
                while (result.next()) {
                    int key = result.getInt("category_id");
                    String value = result.getString("label");
                    categoryMap.put(key, value);
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return categoryMap;
    }

    @Override
    public LinkedHashMap<Integer, String> selectVendorSubtype() throws Exception {

        LinkedHashMap<Integer, String> subtypeMap = new LinkedHashMap<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectSubtype = "SELECT subtype_id, label, vendorcategory_id FROM samcrm_vendorsubtype";
            try (ResultSet result = statement.executeQuery(querySelectSubtype)) {
                while (result.next()) {
                    int key = result.getInt("subtype_id");
                    String value = result.getString("label") + "<+>" + result.getInt("vendorcategory_id");
                    subtypeMap.put(key, value);
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return subtypeMap;
    }

    @Override
    public ArrayList<ProductCategoryBean> selectProductCategoryDetails(String vendorId) throws Exception {

        ArrayList<ProductCategoryBean> productCategoryList = new ArrayList<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectProductCategory = "SELECT category_id, name, description, sequence FROM samcrm_productcategory "
                    + "where vendor_id = " + vendorId + " order by sequence";
            try (ResultSet result = statement.executeQuery(querySelectProductCategory)) {
                while (result.next()) {
                    ProductCategoryBean productCategoryBean = new ProductCategoryBean();
                    productCategoryBean.setProductCategoryId(result.getString("category_id"));
                    productCategoryBean.setProductCategoryName(result.getString("name"));
                    productCategoryBean.setProductCategoryDescription(result.getString("description"));
                    productCategoryBean.setSequence(result.getString("sequence"));
                    productCategoryList.add(productCategoryBean);
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return productCategoryList;
    }

    @Override
    public ArrayList<ProductsAndServicesBean> selectProductsAndServicesDetails(String productCategory) throws Exception {

        ArrayList<ProductsAndServicesBean> productsAndServicesList = new ArrayList<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectProductCategory = "SELECT psd.product_id, psd.productcategory_id, psd.name, psd.description, psd.offer_price, psd.discount, psd.sequence, psi.quantity, psi.unit, soi.item_id "
                    + "FROM samcrm_productservicedetails psd left join samcrm_productserviceinventory psi on psd.product_id = psi.product_id "
                    + "left join samcrm_obm_items soi on soi.product_id = psd.product_id left join samcrm_obm_orders soo on soi.order_id = soo.order_id and soo.status = 'open' "
                    + "where productcategory_id = " + productCategory;
            try (ResultSet result = statement.executeQuery(querySelectProductCategory)) {
                while (result.next()) {
                    ProductsAndServicesBean productsAndServicesBean = new ProductsAndServicesBean();
                    productsAndServicesBean.setProductCategoryId(result.getString("productcategory_id"));
                    productsAndServicesBean.setProductId(result.getString("product_id"));
                    productsAndServicesBean.setProductName(result.getString("name"));
                    productsAndServicesBean.setProductDescription(result.getString("description"));
                    productsAndServicesBean.setOfferPrice(result.getString("offer_price"));
                    productsAndServicesBean.setDiscount(result.getString("discount"));
                    productsAndServicesBean.setSequence(result.getString("sequence"));
                    productsAndServicesBean.setQuantity(result.getInt("quantity"));
                    productsAndServicesBean.setUnit(result.getString("unit"));
                    productsAndServicesBean.setItemId(result.getInt("item_id"));
                    productsAndServicesList.add(productsAndServicesBean);
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return productsAndServicesList;
    }

    @Override
    public int addContact(String name, String email, String mobile, String dob, String address, String zipCode,
            String comment, String vendor_group_id, String contactId, String message) throws Exception {
        int key = Integer.parseInt(contactId);
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String query = "";
            if (key > 0) {
                query = "UPDATE samcrm_contact_list SET name = '" + name + "', email = '" + email + "', zipcode = '" + zipCode + "', address = '"
                        + address + "', mobile = " + mobile + ", message = '" + message + "', comment = '" + comment + "' ";
                if (dob != null && !dob.isEmpty()) {
                    query = query.concat(", date_of_birth = '" + dob + "' ");
                }
                query = query.concat(" WHERE mobile = " + mobile + " AND vendor_group_id = " + vendor_group_id);
                statement.executeUpdate(query);
            } else {
                long individualId = checkUserExistance(Long.parseLong(mobile));
                if (individualId == 0) {
                    key = DatabaseConnection.generateNextPrimaryKey("samcrm_individual", "individual_id");
                    query = "INSERT into samcrm_individual (individual_id, name, email, mobile, zipcode, address, creation_date, country_code, date_of_birth) "
                            + "VALUES (" + key + ", '" + name + "', '" + email + "', " + mobile + ", '" + zipCode + "', '" + address + "', '" + FORMATTER.format(new Date()) + "', 091";
                    if (dob != null && !dob.isEmpty()) {
                        query = query.concat(", '" + dob + "'");
                    } else {
                        query = query.concat(", NULL ");
                    }
                    query = query.concat(")");
                    statement.executeUpdate(query);

                    query = "INSERT into samcrm_contact_list (mobile, vendor_group_id, name, email, zipcode, address, creation_date, country_code, message, comment, date_of_birth) "
                            + "VALUES (" + mobile + ", " + vendor_group_id + ", '" + name + "', '" + email + "', '" + zipCode + "', '" + address + "', '"
                            + FORMATTER.format(new Date()) + "', 091, '" + message + "', '" + comment + "' ";
                    if (dob != null && !dob.isEmpty()) {
                        query = query.concat(", '" + dob + "'");
                    } else {
                        query = query.concat(", NULL ");
                    }
                    query = query.concat(")");
                    statement = connection.createStatement();
                    statement.executeUpdate(query);
                } else {
                    query = "INSERT into samcrm_contact_list (mobile, vendor_group_id, name, email, zipcode, address, creation_date, country_code, message, comment, date_of_birth) "
                            + "VALUES (" + mobile + ", " + vendor_group_id + ", '" + name + "', '" + email + "', '" + zipCode + "', '" + address + "', '"
                            + FORMATTER.format(new Date()) + "', 091, '" + message + "', '" + comment + "' ";
                    if (dob != null && !dob.isEmpty()) {
                        query = query.concat(", '" + dob + "'");
                    } else {
                        query = query.concat(", NULL ");
                    }
                    query = query.concat(")");
                    statement = connection.createStatement();
                    statement.executeUpdate(query);
                }
            }
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return key;
    }

    @Override
    public ArrayList<VendorDataBean> selectContacts(String vendor_group_id) throws Exception {

        ArrayList<VendorDataBean> contactList = new ArrayList<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectContacts = "SELECT scl.individual_id, si.name, si.email, si.mobile, si.zipcode, si.address, si.country_code, si.date_of_birth, message, comment "
                    + "FROM samcrm_contact_list si join samcrm_individual scl on si.mobile = scl.mobile where si.vendor_group_id = " + vendor_group_id;
            try (ResultSet result = statement.executeQuery(querySelectContacts)) {
                while (result.next()) {
                    VendorDataBean vendorDataBean = new VendorDataBean();
                    vendorDataBean.setIndividual_id(result.getString("individual_id"));
                    vendorDataBean.setName(result.getString("name"));
                    vendorDataBean.setEmail(result.getString("email"));
                    vendorDataBean.setMobile(result.getString("mobile"));
                    vendorDataBean.setZipcode(result.getString("zipcode"));
                    vendorDataBean.setAddress(result.getString("address"));
                    vendorDataBean.setCountry_code(result.getString("country_code"));
                    vendorDataBean.setDob(result.getString("date_of_birth"));
                    vendorDataBean.setMessage(result.getString("message"));
                    vendorDataBean.setComment(result.getString("comment"));
                    contactList.add(vendorDataBean);
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return contactList;
    }

    @Override
    public String addCategory(String categoryName, String categoryDescription, String vendor_group_id, int categorySequence) throws Exception {

        int record;
        String vendorName = null;
        Connection connection;
        try {
            int key = DatabaseConnection.generateNextPrimaryKey("samcrm_productcategory", "category_id");
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String queryInsertCategory = "INSERT into samcrm_productcategory (category_id, name, description, vendor_id, sequence) "
                    + "VALUES (" + key + ", '" + categoryName + "', '" + categoryDescription + "', " + vendor_group_id + ", " + categorySequence + ")";
            record = statement.executeUpdate(queryInsertCategory);

            if (record == 1) {
                vendorName = getVendorName(vendor_group_id);
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return vendorName;
    }

    @Override
    public String editCategory(int categoryId, String categoryName, String categoryDescription, String vendor_group_id, int categorySequence) throws Exception {

        int record;
        String vendorName = null;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String queryInsertCategory = "UPDATE samcrm_productcategory SET name = '" + categoryName + "', description = '" + categoryDescription + "', sequence = " + categorySequence
                    + " WHERE vendor_id = " + vendor_group_id + " AND category_id = " + categoryId;
            record = statement.executeUpdate(queryInsertCategory);

            if (record == 1) {
                vendorName = getVendorName(vendor_group_id);
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return vendorName;
    }

    private String getVendorName(String vendor_group_id) throws Exception {

        String vendorName = null;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectVendorTitle = "SELECT vendor_title from samcrm_vendor_category_subtype WHERE  id = " + vendor_group_id;
            try (ResultSet rs = statement.executeQuery(querySelectVendorTitle)) {
                while (rs.next()) {
                    vendorName = rs.getString("vendor_title");
                    break;
                }
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return vendorName;
    }

    @Override
    public String addProductAndService(String productName, String productDescription, String vendor_group_id, int offer_price, int discount, int productcategory_id, int productSequence) throws Exception {
        int record;
        String vendorName = null;
        Connection connection;
        try {
            int key = DatabaseConnection.generateNextPrimaryKey("samcrm_productservicedetails", "product_id");
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String queryInsertCategory = "INSERT into samcrm_productservicedetails (product_id, name, description, productcategory_id, sequence, offer_price, discount) "
                    + "VALUES (" + key + ", '" + productName + "', '" + productDescription + "', " + productcategory_id + ", " + productSequence + ", " + offer_price + ", " + discount + ")";
            record = statement.executeUpdate(queryInsertCategory);

            if (record == 1) {
                vendorName = getVendorName(vendor_group_id);
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return vendorName;
    }

    @Override
    public String editProductAndService(int productId, String productName, String productDescription, String vendor_group_id, int productSequence, int offer_price, int discount, int productcategory_id) throws Exception {
        int record;
        String vendorName = null;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String queryInsertCategory = "UPDATE samcrm_productservicedetails SET name = '" + productName + "', description = '" + productDescription + "', sequence = " + productSequence
                    + ", offer_price = " + offer_price + ", discount = " + discount
                    + " WHERE productcategory_id = " + productcategory_id + " AND product_id = " + productId;
            record = statement.executeUpdate(queryInsertCategory);

            if (record == 1) {
                vendorName = getVendorName(vendor_group_id);
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return vendorName;
    }

    @Override
    public ArrayList<VendorDataBean> getAreaSpecificVendors(String zipCode, String mobile, int category_id, int subtype_id) throws Exception {

        ArrayList<VendorDataBean> vendorList = new ArrayList<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectVendors = "SELECT distinct si.individual_id, si.name, si.email, si.mobile, si.zipcode, si.address, "
                    + "si.creation_date, si.country_code, si.date_of_birth, "
                    + "svcs.id, svcs.vendor_title, svcs.vendor_registration_no, svcs.logo_name, svcs.logo_url, "
                    + "sv.label as category_name, sv.description as category_description, "
                    + "ss.label as subtype_name, ss.description as subtype_description "
                    + "FROM samcrm_individual si JOIN samcrm_vendor_category_subtype svcs on svcs.mobile = si.mobile "
                    + "JOIN samcrm_vendorcategory sv ON svcs.vendor_category_id = sv.category_id ";
            if (category_id > 0) {
                querySelectVendors = querySelectVendors + " AND svcs.vendor_category_id = " + category_id;
            }

            querySelectVendors = querySelectVendors + " JOIN samcrm_vendorsubtype ss ON svcs.vendor_subtype_id = ss.subtype_id ";
            if (subtype_id > 0) {
                querySelectVendors = querySelectVendors + " AND svcs.vendor_subtype_id = " + subtype_id;
            }

            if (mobile != null && !mobile.isEmpty()) {
                querySelectVendors = querySelectVendors + " LEFT JOIN samcrm_contact_list scl on scl.vendor_group_id = svcs.id "
                        + " and scl.mobile = " + mobile;
            }
            querySelectVendors = querySelectVendors + " WHERE si.zipcode = " + zipCode;

            try (ResultSet result = statement.executeQuery(querySelectVendors)) {
                while (result.next()) {
                    VendorDataBean vendorDataBean = new VendorDataBean();
                    vendorDataBean.setIndividual_id(result.getString("individual_id"));
                    vendorDataBean.setName(result.getString("name"));
                    vendorDataBean.setEmail(result.getString("email"));
                    vendorDataBean.setMobile(result.getString("mobile"));
                    vendorDataBean.setZipcode(result.getString("zipcode"));
                    vendorDataBean.setAddress(result.getString("address"));
                    vendorDataBean.setCreation_date(result.getString("creation_date"));
                    vendorDataBean.setCountry_code(result.getString("country_code"));
                    vendorDataBean.setDob(result.getString("date_of_birth"));
                    vendorDataBean.setVendorTitle(result.getString("vendor_title"));
                    vendorDataBean.setVendorCategoryName(result.getString("category_name"));
                    vendorDataBean.setVendorCategoryDescription(result.getString("category_description"));
                    vendorDataBean.setVendorSubtypeName(result.getString("subtype_name"));
                    vendorDataBean.setVendorSubtypeDescription(result.getString("subtype_description"));
                    vendorDataBean.setVendorGroupId(result.getString("id"));
                    vendorDataBean.setVendorRegistrationNo(result.getString("vendor_registration_no"));
                    vendorDataBean.setVendorLogoName(result.getString("logo_name"));
                    vendorDataBean.setVendorLogoUrl(result.getString("logo_url"));

                    vendorList.add(vendorDataBean);
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return vendorList;
    }

    @Override
    public ArrayList<VendorDataBean> getVendorwiseContactList() throws Exception {
        ArrayList<VendorDataBean> contactList = new ArrayList<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectContacts = "SELECT si.individual_id, si.name, si.email, si.mobile, si.zipcode, si.address, "
                    + "si.creation_date, si.country_code, si.date_of_birth, scl.vendor_group_id, su.is_vendor "
                    + "FROM samcrm_individual si join samcrm_users su on si.mobile = su.mobile left join samcrm_contact_list scl on si.mobile = scl.mobile order by scl.vendor_group_id";
            try (ResultSet result = statement.executeQuery(querySelectContacts)) {
                while (result.next()) {
                    VendorDataBean vendorDataBean = new VendorDataBean();
                    vendorDataBean.setIndividual_id(result.getString("individual_id"));
                    vendorDataBean.setName(result.getString("name"));
                    vendorDataBean.setEmail(result.getString("email"));
                    vendorDataBean.setMobile(result.getString("mobile"));
                    vendorDataBean.setZipcode(result.getString("zipcode"));
                    vendorDataBean.setAddress(result.getString("address"));
                    vendorDataBean.setCreation_date(result.getString("creation_date"));
                    vendorDataBean.setCountry_code(result.getString("country_code"));
                    vendorDataBean.setDob(result.getString("date_of_birth"));
                    if (result.getString("vendor_group_id") == null) {
                        vendorDataBean.setVendorGroupId(result.getString("is_vendor"));
                    } else {
                        vendorDataBean.setVendorGroupId(result.getString("vendor_group_id"));
                    }
                    contactList.add(vendorDataBean);
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return contactList;
    }

    @Override
    public ArrayList<ProductCategoryBean> selectVendorCategoryList() throws Exception {

        ArrayList<ProductCategoryBean> vendorCategoryList = new ArrayList<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectCategory = "SELECT category_id, label FROM samcrm_vendorcategory order by category_id";
            try (ResultSet result = statement.executeQuery(querySelectCategory)) {
                while (result.next()) {
                    ProductCategoryBean vendorCategory = new ProductCategoryBean();
                    vendorCategory.setProductCategoryId(result.getString("category_id"));
                    vendorCategory.setProductCategoryName(result.getString("label"));
                    vendorCategoryList.add(vendorCategory);
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return vendorCategoryList;
    }

    @Override
    public ArrayList<ProductCategoryBean> selectVendorSubTypeList() throws Exception {
        ArrayList<ProductCategoryBean> vendorSubTypeList = new ArrayList<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String querySelectSubtype = "SELECT subtype_id, label, vendorcategory_id FROM samcrm_vendorsubtype";
            try (ResultSet result = statement.executeQuery(querySelectSubtype)) {
                while (result.next()) {
                    ProductCategoryBean vendorSubType = new ProductCategoryBean();
                    vendorSubType.setProductCategoryId(result.getString("subtype_id"));
                    vendorSubType.setProductCategoryName(result.getString("label"));
                    vendorSubType.setVendorId(result.getString("vendorcategory_id"));
                    vendorSubTypeList.add(vendorSubType);
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return vendorSubTypeList;
    }

    @Override
    public String updateProductServiceInventory(int productId, int quantity, String unit) throws Exception {
        int record = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();

            record = statement.executeUpdate("UPDATE samcrm_productserviceinventory SET quantity = " + quantity + ", unit = '" + unit
                    + "' WHERE product_id = " + productId);
            if (record != 1) {
                record = statement.executeUpdate("INSERT into samcrm_productserviceinventory (product_id, quantity, unit) "
                        + " VALUES (" + productId + ", " + quantity + ", '" + unit + "')");
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return record == 1 ? "Inventory Added" : "Inventory updated";
    }

    @Override
    public int checkInventory(int productId) throws Exception {
        int quantity = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("select quantity from samcrm_productserviceinventory WHERE product_id = " + productId);
            if (result.first()) {
                quantity = result.getInt("quantity");
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return quantity;
    }

    @Override
    public int getDiscount(int vendor_id, int customer_id, double total_price) throws Exception {

        int discount = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            try (Statement statement = connection.createStatement()) {
                String selectQuery = "SELECT SUM(credit_reward_points) as reward_points FROM samcrm_creditpoint WHERE unit= 'INR' AND vendor_id = " + vendor_id + " AND customer_id = " + customer_id;
                try (ResultSet result = statement.executeQuery(selectQuery)) {
                    if (result.next()) {
                        int rewardPoints = result.getInt("reward_points");
                        if (total_price >= rewardPoints) {
                            discount = rewardPoints;
                        }
                    }
                }
            }
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }

        return discount;
    }

    @Override
    public String generateCouponCode(int order_id, int vendor_id, int customer_id, int discount) throws Exception {

        String couponCode = "V" + vendor_id + "C" + customer_id + "O" + order_id + "INR" + discount;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            try (Statement statement = connection.createStatement()) {
                String insertQuery = "insert into samcrm_creditpoint(vendor_id, customer_id, order_id, credit_date, credit_reward_points, coupon_code, unit) "
                        + "values(" + vendor_id + ", " + customer_id + ", " + order_id + ", '" + FORMATTER.format(new Date()) + "', " + -discount + ", '" + couponCode + "', 'INR')";
                int record = statement.executeUpdate(insertQuery);
                if (record != 1) {
                    couponCode = "";
                }
            }
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }

        return couponCode;
    }

    @Override
    public int updateInventoryByOrderId(int order_id) throws Exception {

        int records = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String selectQuery = "SELECT soi.product_id, soi.quantity as ordered_qty, spi.quantity as inventory_qty FROM samcrm_obm_items soi join samcrm_productserviceinventory spi on soi.product_id = spi.product_id WHERE order_id =" + order_id;
            try (ResultSet result = statement.executeQuery(selectQuery)) {
                while (result.next()) {
                    int product_id = result.getInt("product_id");
                    int ordered_qty = result.getInt("ordered_qty");
                    int inventory_qty = result.getInt("inventory_qty");
                    updateInventory(connection, product_id, (inventory_qty - ordered_qty));
                }
            }
            connection.close();
        } catch (Exception e) {

        }
        return records;
    }

    private int updateInventory(Connection connection, int product_id, int quantity) {
        int record = 0;
        try (Statement statement = connection.createStatement()) {
            record = statement.executeUpdate("UPDATE samcrm_productserviceinventory SET quantity = " + quantity + " WHERE product_id = " + product_id);
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return record;
    }

    @Override
    public String getUrgentServiceContactNo(int vendor_id) throws Exception {
        String contactNo = "";
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String selectQuery = "SELECT urgent_contact_no1, urgent_contact_no2 FROM samcrm_vendor_category_subtype svcs "
                    + "join samcrm_individual si ON svcs.mobile = si.mobile AND si.individual_id = " + vendor_id;
            ResultSet rs = statement.executeQuery(selectQuery);
            if (rs.next()) {
                long urgentContact = rs.getLong("urgent_contact_no1");
                if (urgentContact > 0) {
                    contactNo = String.valueOf(urgentContact);
                }
                urgentContact = rs.getLong("urgent_contact_no2");
                if (urgentContact > 0) {
                    contactNo = contactNo + "," + String.valueOf(urgentContact);
                }
            }
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return contactNo;
    }

    @Override
    public long checkUserExistance(long mobile) throws Exception {

        long customerId = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String selectQuery = "SELECT individual_id FROM samcrm_individual WHERE mobile = " + mobile;
            ResultSet result = statement.executeQuery(selectQuery);
            if (result.next()) {
                customerId = result.getInt("individual_id");
            }

            connection.close();
        } catch (SQLException sqle) {
        }
        return customerId;
    }

}
