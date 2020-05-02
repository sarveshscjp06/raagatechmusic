/**
 * Copyright (C) 2017, raagatech.
 * 
 * All rights reserved under the Terms and Conditions of 
 * raagatech by Jagriti Jan Kalyan Samiti, Allahabad.
 * 
 * $Id: Constants.java, v 1.1 Exp $
 *
 * Date Author Changes
 * Jun 18, 2017, 1:35:02 PM, Sarvesh created.
 */
package com.raagatech.data.source;

/**
 * TODO do make available database constraints here 
 * @author <a href=mailto:sarveshtripathi@raagatech.com>Sarvesh</a>
 * @version $Revision: 1.1 Jun 18, 2017 1:35:02 PM
 * @see com.raagatech.data.source.Constants
 */
public class Constants {
	
	public static String dbDriverClass = "com.mysql.jdbc.Driver";
	public static String dbName = "raagatechdb";
	public static String hostName = "localhost:3306";
	public static String dbUsername = "sarvesh";
	public static String dbPassword = "arjunarya";
	public static String dbUrl = "jdbc:mysql://"+hostName+"/"+dbName;
        
        public static String dbDriverClass_production = "com.mysql.jdbc.GoogleDriver";
	public static String dbName_production = "raagatechdb";
	public static String hostName_production = "raagatechmusic:raagatechsqlinstance";
	public static String dbUsername_production = "sarvesh";
	public static String dbPassword_production = "arjunarya";
	public static String dbUrl_production = "jdbc:google:mysql://"+hostName_production+"/"+dbName_production;	
}
