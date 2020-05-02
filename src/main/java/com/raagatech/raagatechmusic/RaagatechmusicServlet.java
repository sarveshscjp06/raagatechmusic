/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.raagatechmusic;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.raagatech.commons.EmailUtils;
import com.raagatech.commons.QRCodeGenerator;
import com.raagatech.commons.SMSUtils;
import com.raagatech.commons.Utility;
import com.raagatech.data.source.Constants;
import com.raagatech.data.source.DatabaseConnection;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sarvesh
 */
@SuppressWarnings("serial")
public class RaagatechmusicServlet extends HttpServlet {

    private static final Logger _logger = Logger.getLogger(RaagatechmusicServlet.class.getName());
    //expressed in milliseconds
//    private final static long ONCE_PER_DAY = 1000 * 60 * 60 * 24;
//
//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        System.out.println("Hello Sarvesh, What are you doing these days!");
//
//        TimerTask fetchMail = new RaagatechScheduler();
//        //perform the task once a day at 4 a.m., starting tomorrow morning
//        //(other styles are possible as well)
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(fetchMail, RaagatechScheduler.getTomorrowMorning4am(), ONCE_PER_DAY);
//
//        super.init(config); //To change body of generated methods, choose Tools | Templates.
//    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        resp.setContentType("text/plain");
        //resp.getWriter().println("Sarvesh Hello, world");

        if (req.getParameter("requestIdentifier") != null && !req.getParameter("requestIdentifier").isEmpty() && !req.getParameter("requestIdentifier").equals("GetQrCode")) {
            resp.setContentType("text/html");
            PrintWriter out = resp.getWriter();
            out.println("<input type=\"hidden\" id=\"hitCounter\" name=\"hitCounter\" value=\"3487\"");
            req.getRequestDispatcher("index.html").include(req, resp);
            out.close();
            //resp.sendRedirect("index.html");
        } else {

//            resp.setContentType("text/html");
//            PrintWriter out = resp.getWriter();
//            out.println("<input type=\"hidden\" id=\"hitCounter\" name=\"hitCounter\" value=\"3487\"");
//            req.getRequestDispatcher("index.html").include(req, resp);
//            out.close();
            //resp.sendRedirect("index.html");
            try {
                _logger.info("Cron Job has been executed");
                //Put your logic here
                //BEGIN
                String subject = "SAM-CRM: android app for near-by shops";
                String followupDetails = "CUSTOMER Benefited for:\n\n"
                        + "1. Get delivery within 30 minutes to 2 hours.\n"
                        + "2. Discount offer from vendors on basis of shopping capacity.\n"
                        + "3. Reward points and coupon facility and reference benefits!"
                        + "\n\nVENDOR Benefited for:\n\n"
                        + "1. Valued and long-lasting customer-base.\n"
                        + "2. Inventory based smart SALES & MARKETING.\n"
                        + "3. Sufficient and Secured time for add-Ons and for family!\n";

                EmailUtils.executeCronJob(subject, followupDetails);

                //END
            } catch (IOException ex) {
                //Log any exceptions in your Cron Job
            }
        }

        //local mysql check raagatechdb connection
//        try {
//            Class.forName(Constants.dbDriverClass);
//            Connection conn = DriverManager.getConnection(Constants.dbUrl, Constants.dbUsername, Constants.dbPassword);
//            PreparedStatement stmt = conn.prepareStatement("select * from level");
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                System.out.println(rs.getInt(1) + "  " + rs.getString(2));
//            }
//            conn.close();
//
//        } catch (ClassNotFoundException | SQLException e) {
//            e.printStackTrace();
//        }
//        resp.sendRedirect("index.html");
//        String url;
//        url = System.getProperty("ae-cloudsql.cloudsql-database-url");
//        try {
//            // Load the class that provides the new "jdbc:google:mysql://" prefix.
//            Class.forName("com.mysql.jdbc.GoogleDriver");
//        } catch (ClassNotFoundException e) {
//            throw new ServletException("Error loading Google JDBC Driver", e);
//        }
//        resp.getWriter().println(" connecting to: " + url);
//        Connection conn;
//        try {
//            conn = DriverManager.getConnection("jdbc:google:mysql://raagatechmusic:raagatechsqlinstance/raagatechdb", "sarvesh", "arjunarya");
//            resp.getWriter().println(" connecting string: " + conn);
//            PreparedStatement stmt = conn.prepareStatement("select * from level");
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()) {
//                resp.getWriter().println(rs.getInt(1) + "  " + rs.getString(2));
//            }
//            conn.close();
//        } catch (SQLException e) {
//            throw new ServletException("Sarvesh: SQL error", e);
//        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        //local mysql check raagatechdb connection

        String inquiryname = req.getParameter("text-680");
        String email = req.getParameter("email-680");
        long mobileNo = Long.parseLong(req.getParameter("tel-630"));
        String followupDetails = req.getParameter("textarea-398");

        if (req.getParameter("requestIdentifier") != null && !req.getParameter("requestIdentifier").isEmpty() && !req.getParameter("requestIdentifier").equals("qrform")) {
            try {
                QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
                BitMatrix matrix = qrCodeGenerator.getQRCode(inquiryname, mobileNo, email, followupDetails);
                String filePath = "D:\\QRCODE\\" + inquiryname + ".png";
                MatrixToImageWriter.writeToFile(matrix, filePath.substring(filePath
                        .lastIndexOf('.') + 1), new File(filePath));
                System.out.println("QR Code image created successfully!");
            } catch (Exception e) {

            }
        } else {

            boolean inquiryStatus = Boolean.FALSE;
            if (Utility.isNotNull(inquiryname) && Utility.isNotNull(email)) {
                try {
                    int inquiry_id = DatabaseConnection.selectInquiry(email, mobileNo);
                    if (inquiry_id > 0) {
                        inquiryStatus = DatabaseConnection.updateInquiry(inquiry_id, inquiryname, 1, email, mobileNo, 1, "NA", followupDetails);
                    } else {
                        inquiryStatus = DatabaseConnection.insertInquiry(inquiryname, 1, email, mobileNo,
                                1, "NA", followupDetails);
                    }
                    if (inquiryStatus && !email.equalsIgnoreCase("raksha@raagatech.com")) {
                        EmailUtils.sendGoogleMail(email, inquiryname, followupDetails);
                        SMSUtils.sendSMS(String.valueOf(mobileNo), followupDetails);

                    }

                } catch (Exception e) {
                }
                if (inquiryStatus) {
                    resp.getWriter().println("Hi " + inquiryname + ",");
                    resp.getWriter().println("The appointment details have been sent via SMS and e-mail service.");
                    resp.getWriter().println("Thanks!");
                } else {
                    resp.getWriter().println("Appointment could not succeeed. Maybe duplicate email and mobile no.");
                    resp.getWriter().println("Please try again after sometime!");
                }
            }
        }
        doGet(req, resp);
    }

}
