/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.commons;

import com.google.appengine.repackaged.org.apache.commons.codec.binary.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Sarvesh
 */
public class JavaTest {

    public static void generateJSON(ArrayList<ContactBean> listOfContacts) {

        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        try {
            object.put("CONTACTS", listOfContacts);
            array.put(object);
        } catch (JSONException e) {
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        ArrayList<ContactBean> listOfContacts = new ArrayList<>();

        listOfContacts.add(new ContactBean("Shailesh", "shai123", "shailesh.transport@gmail.com"));
        listOfContacts.add(new ContactBean("Sarvesh", "sarv123", "sarvesh.new@gmail.com"));
        listOfContacts.add(new ContactBean("Brijesh", "brij123", "brijesh.tm@gmail.com"));

        //uploadImageIntoBaucket();
        //downloadImageFromBaucket();
        //generateJSON(listOfContacts);
        //sendSimpleMail();
        //sendGoogleMail();
        //SMSUtils.sendSMS("9312181442,9953735792", "sms test");

        /*       //Your authentication key
        String authkey = "185479AhnoJHbbMwN5a1a53b5";
        //Multiple mobiles numbers separated by comma
        String mobiles = "9312181442";
        //Sender ID,While using route4 sender id should be 6 characters long.
        String senderId = "Raksha";
        //Your message to send, Add URL encoding here.
        String message = "Sender id check";
        //define route
        String route = "4";

        //Prepare Url
        URLConnection myURLConnection = null;
        URL myURL = null;
        BufferedReader reader = null;

        //encoding message
        String encoded_message = URLEncoder.encode(message);

        //Send SMS API
        String mainUrl = "http://api.msg91.com/api/sendhttp.php?";

        //Prepare parameter string
        StringBuilder sbPostData = new StringBuilder(mainUrl);
        sbPostData.append("authkey=" + authkey);
        sbPostData.append("&mobiles=" + mobiles);
        sbPostData.append("&message=" + encoded_message);
        sbPostData.append("&route=" + route);
        sbPostData.append("&sender=" + senderId);

        //final string
        mainUrl = sbPostData.toString();
        try {
            //prepare connection
            myURL = new URL(mainUrl);
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
            e.printStackTrace();
        }*/
        System.out.println(LocalDate.now().getYear());
        try {
            CloudStorage.downloadFile("raagatechbucket", "whatsappcontacts.csv", "src/main/resources");
        } catch (Exception e) {

        }
    }

    private static void sendSimpleMail() {
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

    private static void sendGoogleMail() {

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("sarvesh.new@gmail.com", "Example.com Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("raksha.alld@gmail.com", "Mr. User"));
            msg.setSubject("Your Example.com account has been activated");
            msg.setText("This is a test");
            Transport.send(msg);
        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        } catch (UnsupportedEncodingException e) {
            // ...
        }
    }

    public static void uploadImageIntoBaucket() {
        String filePath = "src/main/resources/sarvesh.jpg";//"/images/stories/sarvesh.jpg";
        try {
            //CloudStorage.uploadFile("raagatechmusic.appspot.com", filePath);
            FileInputStream imageInFile = new FileInputStream(filePath);
            byte[] imageData = new byte[(int) filePath.length()];
            imageInFile.read(imageData);
            String imageBitmapString = Base64.encodeBase64URLSafeString(imageData);
            byte[] imageByteArray = Base64.decodeBase64(imageBitmapString);
            InputStream stream = new ByteArrayInputStream(imageByteArray);
            CloudStorage.uploadFileFromInputStream("raagatechmusic.appspot.com", "sarvesh2.png", stream, "image/png");
        } catch (Exception e) {
        }
    }

    public static void downloadImageFromBaucket() {

        String directory = "images/stories";
        try {
            InputStream stream = CloudStorage.downloadFileFromBucket("raagatechmusic.appspot.com", "sarvesh5.png", directory);
            byte[] imageData = toByteArray(stream);
            String imageBitmapString = com.google.common.io.BaseEncoding.base64().encode(imageData);
            stream.close();
            System.out.println(imageBitmapString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        byte[] buffer = new byte[10240];
        int len;

        // read bytes from the input stream and store them in buffer
        while ((len = in.read(buffer)) != -1) {
            // write bytes from the buffer into output stream
            os.write(buffer, 0, len);
        }

        return os.toByteArray();
    }
}
