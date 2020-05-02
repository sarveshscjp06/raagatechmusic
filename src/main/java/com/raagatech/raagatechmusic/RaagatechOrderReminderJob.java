/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.raagatechmusic;

import com.raagatech.bean.OrderDataBean;
import com.raagatech.bean.VendorDataBean;
import com.raagatech.commons.EmailUtils;
import com.raagatech.commons.SMSUtils;
import com.raagatech.data.source.SamcrmDataFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Sarvesh
 */
public class RaagatechOrderReminderJob extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RaagatechNewYearJob</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RaagatechNewYearJob at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            ArrayList<OrderDataBean> ordersList = SamcrmDataFactory.getOrderBookingInstance().getOrdersList(null, 0, "closed");
            for (OrderDataBean bean : ordersList) {
                VendorDataBean vendorData = SamcrmDataFactory.getUserDataInstance().selectUserData(bean.getVendor_id());
                VendorDataBean customerData = SamcrmDataFactory.getUserDataInstance().selectUserData(bean.getCustomer_id());

                String subject = ":: Looking for more orders!";
                String followupDetails = "Hello "+customerData.getVendorTitle()+" "+customerData.getName();
                followupDetails += "\n\nThis is being more than 6 days you didn't ordered me! I'll be happy to take your order. Thanks.'\n\n";
                followupDetails += "From: \n"+vendorData.getName()+"\nAddress: "+vendorData.getAddress()+"\nContact No: "+vendorData.getMobile(); 

                EmailUtils.pushSalesAndServicesViaEmail(vendorData.getName(), vendorData.getEmail(), customerData.getEmail(), subject, followupDetails, customerData.getName());
                SMSUtils.sendSamcrmOtp(customerData.getMobile(), followupDetails);
            }
        } catch (Exception e) {
        }
        //processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
