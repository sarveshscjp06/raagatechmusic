/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.data.source;

import com.raagatech.bean.CustomerAddressBean;
import com.raagatech.bean.OrderDataBean;
import com.raagatech.bean.OrderTrackerBean;
import com.raagatech.bean.OrderedItemBean;
import com.raagatech.bean.PaymentBean;
import static com.raagatech.data.source.DatabaseConnection.FORMATTER;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sarvesh
 */
public class OrderBookingDAO implements OrderBookingInterface {

    @Override
    public void createOrder(OrderDataBean orderDataBean) throws Exception {

        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            int key = checkForOpenOrder(orderDataBean.getVendor_id(), orderDataBean.getCustomer_id(), connection);
            if (key == 0) {
                int addressId = getExistingPrimaryAddress(orderDataBean.getCustomer_id(), connection);//
                if (addressId == 0) {
                    addressId = createAddress(orderDataBean.getCustomer_id(), connection);
                }
                key = DatabaseConnection.generateNextPrimaryKey("samcrm_obm_orders", "order_id");
                String order_date = FORMATTER.format(new Date());

                Statement statement = connection.createStatement();
                String insertQuery = "INSERT into samcrm_obm_orders (order_id, customer_id, order_date, status, comments, "
                        + "vendor_id, delivery_date, delivery_person, address_id, couponcode) "
                        + "VALUES (" + key + ", " + orderDataBean.getCustomer_id() + ", '" + order_date + "', 'open', '" + orderDataBean.getComments() + "', "
                        + orderDataBean.getVendor_id() + ", '" + FORMATTER.format(new Date()) + "', " + orderDataBean.getDelivery_address() + ", " + addressId + ", '" + orderDataBean.getCouponCode() + "')";
                int record = statement.executeUpdate(insertQuery);

                if (record == 1) {
                    orderDataBean.setOrder_id(key);
                    orderDataBean.setOrder_date(FORMATTER.parse(order_date));
                    orderDataBean.setStatus("open");
                } else {
                    connection.rollback();
                }
            } else {
                orderDataBean.setOrder_id(key);
                orderDataBean.setStatus("open");
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
    }

    @Override
    public int createItems(ArrayList<OrderedItemBean> items, int order_id) throws Exception {
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();

            for (OrderedItemBean itemBean : items) {
                itemBean.setOrder_id(order_id);
                if (itemBean.getItem_id() == 0) {
                    String selectQuery = "SELECT item_id FROM samcrm_obm_items WHERE order_id = " + order_id + " AND product_id = " + itemBean.getProduct_id();
                    ResultSet rs = statement.executeQuery(selectQuery);
                    if (rs.next()) {
                        itemBean.setItem_id(rs.getInt("item_id"));
                    }
                }
                if (itemBean.getItem_id() > 0) {
                    String updateQuery = "UPDATE samcrm_obm_items SET quantity = " + itemBean.getQuantity() + " WHERE order_id = " + order_id + " AND item_id = " + itemBean.getItem_id();
                    statement = connection.createStatement();
                    statement.executeUpdate(updateQuery);
                } else {
                    String querySelectProductDetails = "SELECT name, description, offer_price, discount "
                            + " FROM samcrm_productservicedetails where product_id = " + itemBean.getProduct_id();
                    statement = connection.createStatement();
                    ResultSet result = statement.executeQuery(querySelectProductDetails);
                    if (result.next()) {
                        int key = DatabaseConnection.generateNextPrimaryKey("samcrm_obm_items", "item_id");
                        itemBean.setItem_id(key);
                        itemBean.setProduct_name(result.getString("name"));
                        String price = result.getString("offer_price");
                        if (price.contains("-")) {
                            price = price.split("-")[0];
                        }
                        itemBean.setPrice(Float.valueOf(price));
                        itemBean.setPart_number(result.getString("discount"));
                        statement = connection.createStatement();
                        String insertQuery = "INSERT into samcrm_obm_items (item_id, product_id, quantity, order_id) "
                                + "VALUES (" + itemBean.getItem_id() + ", " + itemBean.getProduct_id() + ", " + itemBean.getQuantity() + ", " + order_id + ")";
                        statement.executeUpdate(insertQuery);
                    }
                }
            }
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return items.size();
    }

//    @Override
//    public int createCustomer(String customerDetails) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    private int createAddress(long customerId, Connection connection) throws Exception {

        int key = 0;
        try {
            CustomerAddressBean address = new CustomerAddressBean();
            Statement statement = connection.createStatement();
            String querycustomerAddress = "SELECT si.address, si.name, si.mobile, si.email, si.zipcode, si.country_code "
                    + "from samcrm_individual si inner join samcrm_users su on su.mobile = si.mobile AND "
                    + "su.is_loggedin = 1 AND si.individual_id = " + customerId;
            try (ResultSet result = statement.executeQuery(querycustomerAddress)) {
                if (result.first()) {
                    key = DatabaseConnection.generateNextPrimaryKey("samcrm_obm_address", "address_id");
                    String name = result.getString("name");
                    name = name.replaceAll("\\s+", "_");
                    address.setRecepient(name);
                    address.setStreet(result.getString("address"));
                    address.setCountry(result.getString("country_code"));
                    address.setZipcode(result.getString("zipcode"));
                    address.setMobile(result.getLong("mobile"));
                }
            }
            if (key > 0) {
                statement = connection.createStatement();
                String insertQuery = "INSERT into samcrm_obm_address (address_id, street, state, zipcode, country, addresstype, city) "//, recepient, mobile
                        + "VALUES (" + key + ", '" + address.getStreet() + "', '', '" + address.getZipcode() + "', '" + address.getCountry() + "', 'primary', '')";//" + address.getMobile()+"
                statement.executeUpdate(insertQuery);
            }
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return key;
    }

    private int checkForOpenOrder(int vendorId, long customerId, Connection connection) {

        int orderId = 0;
        try {
            String selectQuery = "SELECT order_id FROM samcrm_obm_orders WHERE status = 'open' "
                    + " AND customer_id = " + customerId + " AND vendor_id = " + vendorId;
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(selectQuery);
            if (result.next()) {
                orderId = result.getInt("order_id");
            }
        } catch (Exception e) {

        }
        return orderId;
    }

    private int getExistingPrimaryAddress(long customerId, Connection connection) {

        int addressId = 0;
        try {
            String selectQuery = "SELECT soa.address_id FROM samcrm_obm_address soa join samcrm_obm_orders soo on soa.address_id = soo.address_id "
                    + " AND soa.addresstype = 'primary' AND soo.status = 'closed' AND soo.customer_id = " + customerId;
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(selectQuery);
            if (result.next()) {
                addressId = result.getInt("address_id");
            }
        } catch (Exception e) {

        }
        return addressId;
    }

    @Override
    public String completeTransaction(PaymentBean paymentBean) throws Exception {

        String transactionNo = "";
        Connection connection;
        try {
            String transaction_date = FORMATTER.format(new Date());
            int key = DatabaseConnection.generateNextPrimaryKey("samcrm_obm_payment", "transaction_id");
            connection = DatabaseConnection.createConnection();
            transactionNo = transaction_date + "_" + key;
            Statement statement = connection.createStatement();
            String insertQuery = "INSERT into samcrm_obm_payment (transaction_id, order_id, transaction_date, "
                    + "grossTotalPayment, gst, sgst, serviceCharges, transactionNo, couponCode, paymentMode) "
                    + "VALUES (" + key + ", " + paymentBean.getOrderId() + ", " + transaction_date
                    + ", " + paymentBean.getGrossTotalPayment() + ", " + paymentBean.getGst() + ", " + paymentBean.getSgst()
                    + ", " + paymentBean.getServiceCharges() + ", " + transactionNo + ", " + paymentBean.getCouponCode() + ", " + paymentBean.getPaymentMode() + ")";
            int record = statement.executeUpdate(insertQuery);
            if (record > 0) {
                paymentBean.setTransactionDate(transaction_date);
                paymentBean.setTransactionNo(transactionNo);
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return transactionNo;
    }

    @Override
    public String generateInvoice(String transactionNo) {
        String invoiceNo = "invoicing-work-in-progress";

        return invoiceNo;
    }

    @Override
    public ArrayList<OrderDataBean> getOrdersList(String mobile, int customerId, String status) throws Exception {

        ArrayList<OrderDataBean> ordersList = new ArrayList<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            try (Statement statement = connection.createStatement()) {
                String queryOrders = "select soo.order_id, vendor_id, customer_id, order_date, total_price, status, comments, "
                        + "delivery_date, delivery_person, soo.address_id, soa.street, soa.state, soa.zipcode, "
                        + "soa.country, soa.addresstype, soa.city from samcrm_obm_orders soo join samcrm_obm_address soa "
                        + "on soo.address_id = soa.address_id where status = '" + status + "' AND (customer_id = " + customerId + " "
                        + "OR customer_id IN (select customer_id from samcrm_obm_orders where status = 'open' AND vendor_id = " + customerId + ") "
                        + "OR customer_id IN (SELECT individual_id FROM samcrm_individual si JOIN samcrm_contact_list scl ON si.mobile = scl.mobile AND scl.vendor_group_id = " + customerId + "))";

                if ((mobile == null || mobile.isEmpty()) && customerId == 0 && status.equals("closed")) {
                    queryOrders = "select soo.order_id, soo.vendor_id, soo.customer_id, soo.order_date, soo.total_price, soo.status, "
                            + "soo.comments, soo.delivery_date, soo.delivery_person, soo.address_id, soa.street, soa.state, soa.zipcode, "
                            + "soa.country, soa.addresstype, soa.city from samcrm_obm_orders soo join samcrm_obm_address soa on "
                            + "soo.address_id = soa.address_id where soo.status = 'closed' "
                            + "AND soo.delivery_date <= '"+FORMATTER.format(new Date())+"' + interval 7 day";
                }else if ((mobile == null || mobile.isEmpty()) && status.equals("cancelled")) {
                    queryOrders = "select soo.order_id, soo.vendor_id, soo.customer_id, soo.order_date, soo.total_price, soo.status, "
                            + "soo.comments, soo.delivery_date, soo.delivery_person, soo.address_id, soa.street, soa.state, soa.zipcode, "
                            + "soa.country, soa.addresstype, soa.city from samcrm_obm_orders soo join samcrm_obm_address soa on "
                            + "soo.address_id = soa.address_id where soo.status = 'cancelled' ";
                    if(customerId > 0){
                            queryOrders += " AND soo.order_id = "+customerId;
                    }
                }
                try (ResultSet result = statement.executeQuery(queryOrders)) {
                    while (result.next()) {
                        OrderDataBean orderDataBean = new OrderDataBean();
                        orderDataBean.setOrder_id(result.getInt("order_id"));
                        orderDataBean.setVendor_id(result.getInt("vendor_id"));
                        orderDataBean.setCustomer_id(result.getInt("customer_id"));
                        orderDataBean.setOrder_date(result.getDate("order_date"));
                        orderDataBean.setPrice(result.getDouble("total_price"));
                        orderDataBean.setStatus(result.getString("status"));
                        orderDataBean.setComments(result.getString("comments"));
                        orderDataBean.setDelivery_date(result.getDate("delivery_date"));
                        orderDataBean.setDelivery_person(result.getString("delivery_person"));
                        CustomerAddressBean addressBean = new CustomerAddressBean();
                        addressBean.setAddresstype(result.getString("addresstype"));
                        //addressBean.setRecepient(result.getString("recepient"));
                        addressBean.setStreet(result.getString("street"));
                        addressBean.setCountry(result.getString("country"));
                        addressBean.setZipcode(result.getString("zipcode"));
                        //addressBean.setMobile(result.getLong("mobile"));//9312181442
                        orderDataBean.setCustomerAddress(addressBean);

                        ArrayList<OrderedItemBean> items = getOrderedItems(orderDataBean.getOrder_id());
                        orderDataBean.setItems(items);

                        ordersList.add(orderDataBean);
                    }
                }
            }
            connection.close();
        } catch (SQLException sqle) {
        }
        return ordersList;
    }

    private ArrayList<OrderedItemBean> getOrderedItems(int order_id) throws Exception {

        ArrayList<OrderedItemBean> items = new ArrayList<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            try (Statement statement = connection.createStatement()) {
                String selectQuery = "SELECT soi.item_id, soi.product_id, soi.quantity, soi.order_id, spd.name, spd.description, spd.offer_price, spd.discount "
                        + "FROM samcrm_obm_items soi join samcrm_productservicedetails spd ON soi.product_id = spd.product_id "
                        + "WHERE order_id = " + order_id;
                try (ResultSet result = statement.executeQuery(selectQuery)) {
                    while (result.next()) {
                        OrderedItemBean itemBean = new OrderedItemBean();
                        itemBean.setItem_id(result.getInt("item_id"));
                        itemBean.setProduct_id(result.getInt("product_id"));
                        itemBean.setQuantity(result.getInt("quantity"));
                        itemBean.setOrder_id(result.getInt("order_id"));
                        itemBean.setProduct_name(result.getString("name"));
                        itemBean.setDescription(result.getString("description"));
                        String price = result.getString("offer_price");
                        if (price.contains("-")) {
                            price = price.split("-")[0];
                        }
                        itemBean.setPrice(Float.valueOf(price));
                        itemBean.setDiscount(result.getInt("discount"));

                        items.add(itemBean);
                    }
                }
            }
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return items;
    }

    @Override
    public int updateCartItemQuantity(int itemId, int quantity, int orderId) throws Exception {
        int record = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            try (Statement statement = connection.createStatement()) {
                String updateQuery = "UPDATE samcrm_obm_items SET quantity = " + quantity + " WHERE order_id = " + orderId + " AND item_id = " + itemId;
                record = statement.executeUpdate(updateQuery);
            }
            connection.close();
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return record;

    }

    @Override
    public boolean confirmOrder(int vendor_id, int customer_id, int order_id, String couponCode, double total_price) throws Exception {

        int record = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            try (Statement statement = connection.createStatement()) {
                String insertQuery = "UPDATE samcrm_obm_orders set status = 'confirmed', couponcode = '" + couponCode + "', total_price = " + total_price + ", "
                        + "confirmation_date = '" + FORMATTER.format(new Date()) + "' WHERE status = 'open' AND order_id = " + order_id + " AND customer_id = " + customer_id + " AND vendor_id = " + vendor_id;

                if (couponCode.length() == 4) {
                    insertQuery = insertQuery + " AND couponcode = " + Integer.parseInt(couponCode);
                }
                record = statement.executeUpdate(insertQuery);
            }
            if (record == 1) {
                record = insertDeliveryStatus(connection, order_id);
            }
            if (record == 0) {
                connection.rollback();
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return record == 1;
    }

    @Override
    public int updateOrderDelivery(int order_id, int delivery_person_id, String recepient, long contact_no) throws Exception {
        int record = 0;
        int discountFromVendor = 1;
        int vendor_id = 0;
        int customer_id = 0;
        int address_id = 0;
        double total_price = 0.00;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            String selectQuery = "SELECT vendor_id, customer_id, address_id, total_price FROM samcrm_obm_orders WHERE status = 'confirmed' AND order_id = " + order_id;
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(selectQuery);
            if (result.next()) {
                vendor_id = result.getInt("vendor_id");
                customer_id = result.getInt("customer_id");
                address_id = result.getInt("address_id");
                total_price = result.getDouble("total_price");
            }
            if (vendor_id != 0 && customer_id != 0 && total_price != 0) {
                statement = connection.createStatement();
                selectQuery = "select credit_reward_points as discountFromVendor from samcrm_creditpoint WHERE unit = '%' AND customer_id is null AND vendor_id = " + vendor_id;
                ResultSet set = statement.executeQuery(selectQuery);
                while (set.next()) {
                    discountFromVendor = set.getInt("discountFromVendor");
                }

                statement = connection.createStatement();
                String updateQuery = "UPDATE samcrm_obm_orders set status = 'closed', delivery_person = '" + delivery_person_id + "', delivery_date = '" + FORMATTER.format(new Date()) + "' "
                        + "WHERE status = 'confirmed' AND order_id = " + order_id;
                record = statement.executeUpdate(updateQuery);
                if (record == 1 && address_id != 0) {
                    record = updateAddressForRecepient(connection, recepient, contact_no, address_id);
                    if (record == 1) {
                        int credit_reward_points = ((int) (total_price * discountFromVendor) / 100) / 2;
                        record = allocateRewardPointsToCustomer(connection, vendor_id, customer_id, order_id, credit_reward_points);
                        if (record == 1) {
                            record = allocateCreditPointsToSamCrm(connection, vendor_id, order_id, credit_reward_points);
                        }
                    }
                }

                if (record == 0) {
                    connection.rollback();
                }
            }
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return record == 1 ? customer_id : 0;
    }

    private int updateAddressForRecepient(Connection connection, String recepient, long contact_no, int address_id) {
        int record = 0;
        try (Statement statement = connection.createStatement()) {
            String updateQuery = "UPDATE samcrm_obm_address set recepient = '" + recepient + "', contact_no = " + contact_no + " WHERE recepient IS NULL AND address_id = " + address_id;
            record = statement.executeUpdate(updateQuery);
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return record;
    }

    private int allocateRewardPointsToCustomer(Connection connection, int vendor_id, int customer_id, int order_id, int rewardPoints) throws Exception {

        int record = 0;
        try (Statement statement = connection.createStatement()) {
            String insertQuery = "insert into samcrm_creditpoint(vendor_id, customer_id, order_id, credit_date, credit_reward_points, unit) "
                    + "values(" + vendor_id + ", " + customer_id + ", " + order_id + ", '" + FORMATTER.format(new Date()) + "', " + rewardPoints + ", 'INR')";
            record = statement.executeUpdate(insertQuery);
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return record;
    }

    private int allocateCreditPointsToSamCrm(Connection connection, int vendor_id, int order_id, int rewardPoints) throws Exception {

        int record = 0;
        try (Statement statement = connection.createStatement()) {
            String insertQuery = "insert into samcrm_creditpoint(vendor_id, customer_id, order_id, credit_date, credit_reward_points, unit) "
                    + "values(3, " + vendor_id + ", " + order_id + ", '" + FORMATTER.format(new Date()) + "', " + rewardPoints + ", 'INR')";
            record = statement.executeUpdate(insertQuery);
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return record;
    }

    @Override
    public int createNewAddress(int vendor_id, int customer_id, int order_id, CustomerAddressBean address) throws Exception {

        int key = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement;
            key = DatabaseConnection.generateNextPrimaryKey("samcrm_obm_address", "address_id");
            statement = connection.createStatement();
            String insertQuery = "INSERT into samcrm_obm_address (address_id, street, state, zipcode, country, addresstype, city) "//, recepient, mobile
                    + "VALUES (" + key + ", '" + address.getStreet() + "', '', '" + address.getZipcode() + "', '" + address.getCountry() + "', 'secondary', '')";//" + address.getMobile()+"
            statement.executeUpdate(insertQuery);
            statement.close();
            address.setAddress_id(key);

            statement = connection.createStatement();
            String updateQuery = "UPDATE samcrm_obm_orders set address_id = " + key + " WHERE status = 'open' AND order_id = " + order_id + " AND customer_id = " + customer_id + " AND vendor_id = " + vendor_id;
            statement.executeUpdate(updateQuery);
            statement.close();
            connection.close();
        } catch (SQLException sqle) {
            Logger.getAnonymousLogger().log(Level.SEVERE, this.getClass().getName(), sqle);
        }
        return key;
    }

    @Override
    public int removeItem(int order_id, int vendor_id, int customer_id, int item_id) throws Exception {
        int record = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            int orderId = checkForOpenOrder(vendor_id, customer_id, connection);
            if (orderId == order_id) {
                Statement statement = connection.createStatement();
                String deleteQuery = "delete from samcrm_obm_items where item_id = " + item_id + " AND order_id = " + order_id;
                record = statement.executeUpdate(deleteQuery);
                connection.close();
            }
        } catch (Exception e) {

        }
        return record;
    }

    @Override
    public int cancelOrder(int order_id, String comment, int delivery_person_id) throws Exception {
        int record = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            String couponcode = getCouponCode(connection, order_id);
            Statement statement = connection.createStatement();
            String updateQuery = "UPDATE samcrm_obm_orders SET status = 'cancelled', comment = '" + comment + "', delivery_person = '" + delivery_person_id + "' "
                    + "WHERE order_id = " + order_id + " AND status = 'confirmed'";
            record = statement.executeUpdate(updateQuery);
            if (record == 1 && couponcode != null && !couponcode.isEmpty()) {
                updateQuery = "DELETE FROM samcrm_creditpoint WHERE order_id = " + order_id + " AND coupon_code = '" + couponcode + "'";
                statement = connection.createStatement();
                record = statement.executeUpdate(updateQuery);
            }
            if (record == 1) {
                connection.close();
            } else {
                connection.rollback();
            }
        } catch (Exception e) {

        }
        return record;
    }

    private String getCouponCode(Connection connection, int order_id) throws Exception {

        String couponcode = null;
        try (Statement statement = connection.createStatement()) {
            String selectQuery = "SELECT couponcode FROM samcrm_obm_orders WHERE status = 'confirmed' AND order_id = " + order_id;
            ResultSet rs = statement.executeQuery(selectQuery);
            if (rs.next()) {
                couponcode = rs.getString("couponcode");
            }
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return couponcode;
    }

    private int insertDeliveryStatus(Connection connection, int order_id) throws Exception {

        int record = 0;
        try (Statement statement = connection.createStatement()) {
            String insertQuery = "insert into samcrm_delivery_status(order_id) VALUES (" + order_id + ")";
            record = statement.executeUpdate(insertQuery);
        } catch (SQLException sqle) {
            System.out.println(sqle);
        }
        return record;
    }

    @Override
    public boolean updateDeliveryStatus(int order_id, int delivery_person_id, int step) throws Exception {

        int record = 0;
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            Statement statement = connection.createStatement();
            String set = " delivery_person_id = " + delivery_person_id;
            if (step == 1) {
                set = set + " , ready_time = '" + Calendar.HOUR + ":" + Calendar.MINUTE + ":" + Calendar.SECOND + "' ";
            }
            if (step == 2) {
                set = set + " , out_to_time = '" + Calendar.HOUR + ":" + Calendar.MINUTE + ":" + Calendar.SECOND + "' ";
            }
            if (step == 3) {
                set = set + " , delivery_time = '" + Calendar.HOUR + ":" + Calendar.MINUTE + ":" + Calendar.SECOND + "' ";
            }
            if (step >= 4 && step <= 8) {
                set = set + " , comment_id = " + step;
            }
            if (step >= 9 && step <= 13) {
                set = set + " , feedback_id = " + step;
            }
            String updateQuery = "UPDATE samcrm_delivery_status SET " + set + " WHERE order_id = " + order_id;
            record = statement.executeUpdate(updateQuery);
            connection.close();

        } catch (Exception e) {

        }
        return record == 1;
    }

    @Override
    public ArrayList<OrderTrackerBean> getOrderDeliveryStatus(int order_id, int customer_id) throws Exception {

        ArrayList<OrderTrackerBean> list = new ArrayList<>();
        Connection connection;
        try {
            connection = DatabaseConnection.createConnection();
            String selectQuery = "SELECT sds.id, sds.order_id, delivery_person_id, ready_time, out_to_time, delivery_time, scm.description as comment, sfm.description as feedback "
                    + "FROM samcrm_delivery_status sds "
                    + "join samcrm_obm_orders soo on sds.order_id = soo.order_id AND soo.status = 'confirmed' "
                    + "left join samcrm_comment_master scm on scm.sequence = sds.comment_id "
                    + "left join samcrm_feedback_master sfm on sfm.sequence = sds.feedback_id WHERE soo.order_id = " + order_id + " AND soo.customer_id = " + customer_id;
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(selectQuery);
            if (result.next()) {
                OrderTrackerBean trackerBean = new OrderTrackerBean();
                trackerBean.setTrackerId(result.getInt("id"));
                trackerBean.setOrderId(result.getInt("order_id"));
                trackerBean.setDeliveryPersonId(result.getInt("delivery_person_id"));
                trackerBean.setReadyTime(result.getString("ready_time"));
                trackerBean.setOutToTime(result.getString("out_to_time"));
                trackerBean.setDeliveryTime(result.getString("delivery_time"));
                trackerBean.setComment(result.getString("comment"));
                trackerBean.setFeedback(result.getString("feedback"));
                list.add(trackerBean);
            }
            connection.close();
        } catch (Exception e) {

        }
        return list;
    }

}
