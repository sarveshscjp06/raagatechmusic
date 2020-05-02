/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raagatech.aspirant.service.rest.impl;

import com.raagatech.commons.CloudStorage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.raagatech.commons.Utility;
import javax.ws.rs.GET;
import org.json.JSONObject;

/**
 *
 * @author Sarvesh
 */
@Path("/upload")
public class FileUploadService {

    /**
     * Returns text response to caller containing uploaded file location
     *
     * @param params
     * @return error response in case of missing parameters an internal
     * exception or success response if file has been stored successfully
     */
    @POST
    @Path("/uploadInputStream")
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadImage(String params) {

        String response;
        JSONObject data = new JSONObject(params);
        try {
            //byte[] imageByteArray = com.google.common.io.BaseEncoding.base64().decode(bitmap);
            InputStream stream = new ByteArrayInputStream(data.getString("bitMap").getBytes());
            String contentType = "image/png";
            CloudStorage.uploadFileFromInputStream("raagatechmusic.appspot.com", "sarvesh6.png", stream, contentType);
            response = Utility.constructJSON("login", true, "Data storage successful!");
        } catch (Exception e) {
            response = Utility.constructJSON("login", false, "Data storage failed");
        }
        return response;
    }

    /**
     * Returns text response to caller containing uploaded file location
     *
     * @param params
     * @return error response in case of missing parameters an internal
     * exception or success response if file has been stored successfully
     */
    @POST
    @Path("/uploadImage")
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadFile(InputStream params) {

        // check if all form parameters are provided
        if (params == null) {
            return "";
        }

        try {
            byte[] imageData = toByteArray(params);
            String imageBitmapString = com.google.common.io.BaseEncoding.base64().encode(imageData);
            byte[] imageByteArray = com.google.common.io.BaseEncoding.base64().decode(imageBitmapString);
            InputStream stream = new ByteArrayInputStream(imageByteArray);
//            String filePath = "src/main/resources/sarvesh.jpg";
//            FileInputStream imageInFile = new FileInputStream(filePath);
//            byte[] imageData = new byte[(int) filePath.length()];
//            imageInFile.read(imageData);
//            String imageBitmapString = Base64.encodeBase64URLSafeString(imageData);
//            byte[] imageByteArray = Base64.decodeBase64(imageBitmapString);

//            String filePath = "images/stories/facebookIcon.png";//"WEB-INF/classes/sarvesh.jpg";//"/images/stories/facebookIcon.png";
            try {
//                CloudStorage.uploadFile("raagatechmusic.appspot.com", filePath);
                String contentType = "image/png";
                CloudStorage.uploadFileFromInputStream("raagatechmusic.appspot.com", "sarvesh3.jpg", stream, contentType);
            } catch (Exception e) {
            }
        } catch (IOException e) {
            return Response.status(500).entity("Can not save file").build().toString();
        }

        return "";
    }

    @POST
    @Path("/downloadInputStream")
    @Produces(MediaType.APPLICATION_JSON)
    public String getUploadedImage(String params) {

        String response;
        //JSONObject data = new JSONObject(params);
        String directory = "images/stories";
        try {
            InputStream stream = CloudStorage.downloadFileFromBucket("raagatechmusic.appspot.com", "sarvesh6.png", directory);
            byte[] imageData = toByteArray(stream);
            String imageBitmapString = com.google.common.io.BaseEncoding.base64().encode(imageData);
            stream.close();
            response = Utility.constructJSON("login", true, imageBitmapString);
        } catch (Exception e) {
            response = Utility.constructJSON("login", false, "Data download failed");
        }
        return response;
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
    
    @GET
    @Path("/downloadFile")
    @Produces(MediaType.APPLICATION_JSON)
    public String downloadFile() {

        String response;
        String bucket = "raagatechbucket";
        String fileName = "whatsappcontacts.csv";
        String directory = "templates/raagatech/docs";
        try {
            CloudStorage.downloadFile(bucket, fileName, directory);
            response = Utility.constructJSON("login", true, "Data download succeed");
        } catch (Exception e) {
            response = Utility.constructJSON("login", false, "Data download failed");
        }
        return response;
    }    
}
