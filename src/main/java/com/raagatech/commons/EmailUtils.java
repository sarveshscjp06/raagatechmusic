/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.commons;

import com.raagatech.bean.VendorDataBean;
import com.raagatech.data.source.SamcrmDataFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author Sarvesh
 */
public class EmailUtils {

    private static final String SUBJECT = "";
    private static String FOLLOWUPDETAILS = "";
    private static final String ATTACHMENT = "templates/raagatech/docs/AccessmentFormVer2.pdf";
    private static final String ATTACHMENT_SAMCRM = "templates/raagatech/docs/SalesPurchaseModelInLocalMarket.pdf";

    private static final String SIGNATURE = "\n\n\n\n\n\nRaksha Tripathi\n"
            + "'PRAVEEN' Indian Classical Vocal & Instrumental\n"
            + "Principal / Superintendent\n"
            + "raagatech 'The World of Music Education & Performance' \n"
            + "(An authorized exam & study centre for PRAYAG SANGIT SAMITI, ALLAHABAD)\n"
            + "Land-Line: +91 120 4276874 Mobile: +919891029284\n"
            + "http://www.raagatech.com\n";
    private static final String SIGNATURE_SAMCRM = "\n\n\n\n\n\nRaksha Tripathi\n"
            + "Co-Founder\n"
            + "Software Develoment Wing under raagatech organization.\n"
            + "Landline: +91 120 4276874 Mobile: +91 9891029284\n"
            + "http://www.raagatech.com\n";

    private static final String DISCLAIMER_SAMCRM = "Disclaimer: Posting of any advertisement shall not be considered an endorsement of the advertiser, or of the product or service involved.";
    
    public static void sendEmail(Session session, String toEmail, String subject,
            String body) {
        try {
            MimeMessage message = new MimeMessage(session);
            //set message headers
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");
            message.setFrom(new InternetAddress("sarvesh.new@gmail.com", "raagatech -The World Of Music Education & Performance"));
            message.setReplyTo(InternetAddress.parse("sarvesh.new@gmail.com", false));

            message.setSubject(subject, "UTF-8");
            message.setText(body, "UTF-8");
            message.setSentDate(new Date());
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            Transport.send(message);

        } catch (UnsupportedEncodingException | MessagingException e) {
        }
    }

    public static void sendGoogleMail(String email, String inquiryname, String followupDetails) throws IOException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("raksha@raagatech.com",
                    "raagatech"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(email, inquiryname));
            msg.addRecipient(Message.RecipientType.BCC,
                    new InternetAddress("raksha.alld@gmail.com", "Raksha"));
            msg.setSubject("Inquiry: Music Activities");

            String bodyText = "The World Of Music Education & Performance! \n\n"
                    + "Below are the inquiry details.\n\n" + followupDetails
                    + "\n\n Fee Details: \n\n1. For beginner to junior level - Rs. 4500/- quarter. (Option: Rs. 2000/- Month)"
                    + "\n2. For senior upto 6th year level - Rs. 7500/- quarter. (Option: Rs. 3000/- Month)"
                    + "\n\n Please find the registration form attached with this email.";

            Multipart mp = new MimeMultipart();

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setText(bodyText + SIGNATURE);
            mp.addBodyPart(htmlPart);

            MimeBodyPart attachment = new MimeBodyPart();
            String filename = "templates/raagatech/docs/AccessmentFormVer2.pdf";
            FileDataSource source = new FileDataSource(filename);
            attachment.setDataHandler(new DataHandler(source));
            attachment.setFileName(source.getName());
            mp.addBodyPart(attachment);

            msg.setContent(mp);

            Transport.send(msg);
        } catch (AddressException e) {
            // ...
        } catch (MessagingException | UnsupportedEncodingException e) {
            // ...
        }
    }

    public static void sendSimpleMail() {
        final String username = "sarvesh.new@gmail.com";
        final String password = "Arjun@1442";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sarvesh.new@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("raksha.alld@gmail.com"));
            message.setSubject("Testing Subject");
            message.setText("Dear Mail Crawler,"
                    + "\n\n No spam to my email, please!");

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendSamcrmUserAuthenticationEMail(String email, String inquiryname, String followupDetails) throws IOException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("raksha@raagatech.com",
                    "SAM-CRM"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(email, inquiryname));
            msg.addRecipient(Message.RecipientType.BCC,
                    new InternetAddress("raksha.alld@gmail.com", "Raksha"));
            msg.setSubject("samcrm: User Authentication");

            String bodyText = "Sales & Marketing, Customer Relationship Management! \n\n"
                    + "Below are the authentication details.\n\n" + followupDetails
                    + "\n\n INFO: samcrm yearly subscription costs $ 10/- to vendors only."//INR 20,195/- to vendors only
                    + "\n\n Thanks for choosing samcrm! \n\n ";

            Multipart mp = new MimeMultipart();

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setText(bodyText + SIGNATURE_SAMCRM);
            mp.addBodyPart(htmlPart);

//            MimeBodyPart attachment = new MimeBodyPart();
//            String filename = "templates/raagatech/docs/AccessmentFormVer2.pdf";
//            FileDataSource source = new FileDataSource(filename);
//            attachment.setDataHandler(new DataHandler(source));
//            attachment.setFileName(source.getName());
//            mp.addBodyPart(attachment);
            msg.setContent(mp);

            Transport.send(msg);
        } catch (AddressException e) {
            // ...
        } catch (MessagingException | UnsupportedEncodingException e) {
            // ...
        }
    }

    private static void sendSamcrmDailyEMailAd(String subject, InternetAddress[] addresses) throws IOException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("raksha@raagatech.com",
                    "raagatech"));
            msg.addRecipients(Message.RecipientType.TO, addresses);
//            msg.addRecipient(Message.RecipientType.BCC,
//                    new InternetAddress("raksha.alld@gmail.com", "Raksha"));
            msg.setSubject(SUBJECT + subject);// + subject

            String bodyText = "<h2>Sales And Marketing - Customer Relationship Management</h2>\n\n"
                    + "\n<h4>" + FOLLOWUPDETAILS + "</h4>"
                    + "\n\n <h3>NOTE:</h3> <h6>SAM-CRM is developed under raagatech organization and costs to vendors only at $ 10/- yearly. \n\n"
                    + "Enclsure (for understanding about how the app works):</h6>\n"
                    + "SalesPurchaseModelInLocalMarket.pdf\n";

            Multipart mp = new MimeMultipart();

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setText(bodyText + SIGNATURE_SAMCRM);
            mp.addBodyPart(htmlPart);

//            MimeBodyPart attachment = new MimeBodyPart();
//            FileDataSource source = new FileDataSource(ATTACHMENT);
//            attachment.setDataHandler(new DataHandler(source));
//            attachment.setFileName(source.getName());
//            mp.addBodyPart(attachment);
            MimeBodyPart attachmentSamcrm = new MimeBodyPart();
            FileDataSource sourceSamcrm = new FileDataSource(ATTACHMENT_SAMCRM);
            attachmentSamcrm.setDataHandler(new DataHandler(sourceSamcrm));
            attachmentSamcrm.setFileName(sourceSamcrm.getName());
            mp.addBodyPart(attachmentSamcrm);

            msg.setContent(mp);

            Transport.send(msg);
        } catch (AddressException e) {
        } catch (MessagingException | UnsupportedEncodingException e) {
        }
    }

    public static void executeCronJob(String subject, String followupDetails) throws UnsupportedEncodingException, IOException {

        if (followupDetails != null) {
            FOLLOWUPDETAILS = followupDetails;
        }
        ArrayList<VendorDataBean> vendorwiseContactList = new ArrayList<>();
        try {
            vendorwiseContactList = SamcrmDataFactory.getVendorDataInstance().getVendorwiseContactList();
        } catch (Exception e) {
        }
        int vendorGroupId = 0;
        List<String> emailIds = new ArrayList();
        List<String> namesList = new ArrayList();

        InternetAddress[] addresses;
        for (VendorDataBean contact : vendorwiseContactList) {
            if (vendorGroupId == 0) {
                vendorGroupId = Integer.parseInt(contact.getVendorGroupId());
            }

            if (vendorGroupId == Integer.parseInt(contact.getVendorGroupId())) {
                emailIds.add(contact.getEmail());
                namesList.add(contact.getName());
            } else {
                addresses = new InternetAddress[emailIds.size()];
                for (int index = 0; index < emailIds.size(); index++) {
                    addresses[index] = new InternetAddress(emailIds.get(index), namesList.get(index));
                }
                if (addresses.length != 0) {
                    sendSamcrmDailyEMailAd(subject, addresses);
                }
                vendorGroupId = Integer.parseInt(contact.getVendorGroupId());
                emailIds.clear();
                namesList.clear();

                emailIds.add(contact.getEmail());
                namesList.add(contact.getName());
            }
        }
        if (!emailIds.isEmpty()) {
            addresses = new InternetAddress[emailIds.size()];
            for (int index = 0; index < emailIds.size(); index++) {
                addresses[index] = new InternetAddress(emailIds.get(index), namesList.get(index));
            }
            sendSamcrmDailyEMailAd(subject, addresses);
        }
    }

    public static void pushSalesAndServicesViaEmail(String vendorBrandName, String fromEmail, String email, String subject, String offer, String inquiryname) throws IOException {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);            
            msg.setFrom(new InternetAddress("raksha@raagatech.com",
                    "FOR "+vendorBrandName));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(fromEmail, vendorBrandName));
            msg.addRecipient(Message.RecipientType.CC,
                    new InternetAddress(email, inquiryname));
            msg.addRecipient(Message.RecipientType.BCC,
                    new InternetAddress("info@raagatech.com", "for-samcrm"));
            msg.setSubject("Promotional: " + subject);

            String bodyText = "Sales & Marketing - Customer Relationship Management \n"+offer+"\n\n";

            Multipart mp = new MimeMultipart();

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setText(bodyText + DISCLAIMER_SAMCRM);
            mp.addBodyPart(htmlPart);
            msg.setContent(mp);

            Transport.send(msg);
        } catch (AddressException e) {
        } catch (MessagingException | UnsupportedEncodingException e) {
        }
    }
}
