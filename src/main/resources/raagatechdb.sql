-- MySQL dump 10.13  Distrib 5.6.37, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: raagatechdb
-- ------------------------------------------------------
-- Server version	5.6.37

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `aspirant`
--

DROP TABLE IF EXISTS `aspirant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `aspirant` (
  `aspirant_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `date_of_birth` date NOT NULL,
  `qualification_id` int(11) DEFAULT NULL,
  `occupation` varchar(100) DEFAULT NULL,
  `school_and_class` varchar(100) DEFAULT NULL,
  `inspiration_id` int(11) NOT NULL,
  `inspirator_id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `country_code` int(11) DEFAULT NULL,
  `mobile` bigint(20) NOT NULL,
  `level_id` int(11) NOT NULL,
  `duration` varchar(10) DEFAULT NULL,
  `address_line1` varchar(50) NOT NULL,
  `address_line2` varchar(50) DEFAULT NULL,
  `address_line3` varchar(50) DEFAULT NULL,
  `address_line4` varchar(50) DEFAULT NULL,
  `fee_preference_id` int(11) NOT NULL,
  PRIMARY KEY (`aspirant_id`),
  KEY `inspiration_id_fkey` (`inspiration_id`),
  KEY `inspirator_id_fkey` (`inspirator_id`),
  KEY `aspirant_level_id_fkey` (`level_id`),
  KEY `fee_preference_id_fkey` (`fee_preference_id`),
  CONSTRAINT `aspirant_level_id_fkey` FOREIGN KEY (`level_id`) REFERENCES `level` (`level_id`),
  CONSTRAINT `fee_preference_id_fkey` FOREIGN KEY (`fee_preference_id`) REFERENCES `fee_structure` (`fee_id`),
  CONSTRAINT `inspiration_id_fkey` FOREIGN KEY (`inspiration_id`) REFERENCES `inspiration` (`inspiration_id`),
  CONSTRAINT `inspirator_id_fkey` FOREIGN KEY (`inspirator_id`) REFERENCES `inspirator` (`inspirator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `aspirant`
--

LOCK TABLES `aspirant` WRITE;
/*!40000 ALTER TABLE `aspirant` DISABLE KEYS */;
/*!40000 ALTER TABLE `aspirant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fee_structure`
--

DROP TABLE IF EXISTS `fee_structure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fee_structure` (
  `fee_id` int(11) NOT NULL AUTO_INCREMENT,
  `fee_preference` varchar(100) NOT NULL,
  `amount` int(11) NOT NULL,
  `material_id` int(11) NOT NULL,
  PRIMARY KEY (`fee_id`),
  KEY `syllabus_material_id_fkey` (`material_id`),
  CONSTRAINT `syllabus_material_id_fkey` FOREIGN KEY (`material_id`) REFERENCES `syllabus_material` (`material_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fee_structure`
--

LOCK TABLES `fee_structure` WRITE;
/*!40000 ALTER TABLE `fee_structure` DISABLE KEYS */;
/*!40000 ALTER TABLE `fee_structure` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `followupdetails`
--

DROP TABLE IF EXISTS `followupdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `followupdetails` (
  `followup_id` int(11) NOT NULL AUTO_INCREMENT,
  `inquiry_id` int(11) NOT NULL,
  `inquirystatus_id` int(11) NOT NULL,
  `followup_date` date NOT NULL,
  `followup_details` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`followup_id`),
  KEY `inquiry_id_fkey` (`inquiry_id`),
  CONSTRAINT `inquiry_id_fkey` FOREIGN KEY (`inquiry_id`) REFERENCES `inquiry` (`inquiry_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `followupdetails`
--

LOCK TABLES `followupdetails` WRITE;
/*!40000 ALTER TABLE `followupdetails` DISABLE KEYS */;
INSERT INTO `followupdetails` VALUES (1,1,1,'2017-08-16','to test joining 2 tables.'),(2,1,2,'2017-08-21','next1'),(3,1,3,'2017-08-21','The candidate will register in 2 - 3 days'),(4,7,1,'2017-08-23','fsfsdf'),(5,8,1,'2017-08-23','Test'),(6,7,2,'2017-08-23','not interested'),(7,1,4,'2017-08-23','postponed for 2 - 3 weeks '),(8,8,5,'2017-08-23','registered with raagatech'),(9,10,1,'2017-09-28','attachment'),(10,18,1,'2018-09-16','raagatech (The World of Music Education & Performance) inviting you and your family member to join our music inspiration center. We are offering Indian / Western Vocals, Instrumental, Dance and personality development classes.Address: Gaurs Stadium, Gaur City 1, Greater Noida West.'),(11,28,1,'2018-12-15','test appointment request');
/*!40000 ALTER TABLE `followupdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inquiry`
--

DROP TABLE IF EXISTS `inquiry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inquiry` (
  `inquiry_id` int(11) NOT NULL AUTO_INCREMENT,
  `firstname` varchar(100) NOT NULL,
  `inquiry_date` date NOT NULL,
  `inspiration_id` int(11) NOT NULL,
  `email` varchar(100) NOT NULL,
  `mobile` decimal(10,0) NOT NULL,
  `level_id` int(11) NOT NULL,
  `address_line1` varchar(200) NOT NULL,
  PRIMARY KEY (`inquiry_id`),
  UNIQUE KEY `inquiry_ukey` (`mobile`,`email`),
  KEY `inquiry_inspiration_id_fkey` (`inspiration_id`),
  KEY `inquiry_level_id_fkey` (`level_id`),
  CONSTRAINT `inquiry_inspiration_id_fkey` FOREIGN KEY (`inspiration_id`) REFERENCES `inspiration` (`inspiration_id`),
  CONSTRAINT `inquiry_level_id_fkey` FOREIGN KEY (`level_id`) REFERENCES `level` (`level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inquiry`
--

LOCK TABLES `inquiry` WRITE;
/*!40000 ALTER TABLE `inquiry` DISABLE KEYS */;
INSERT INTO `inquiry` VALUES (1,'Rudranch Tripathi','2017-08-07',7,'rudransh@raagatech.com',9312181442,2,'Galaxy North Avenue II'),(3,'Raksha Pandey','2017-08-23',4,'raksha.alld@gmail.com',9891029284,9,'Noida'),(4,'Raksha Pandey','2017-08-23',3,'raksha@raagatech.com',9891029284,8,'Noida'),(6,'raksha3','2017-08-23',3,'r@r',432423423,2,'eqweqwf'),(7,'raksha4','2017-08-23',6,'w@w',5345345345,5,'fsfsf'),(8,'Samriddhi Tripathi','2017-08-23',2,'samriddhi@raagatech.com',9953735792,2,'GNA2'),(10,'sarvesh tripathi','2017-09-28',7,'sarveshscjp06@yahoo.co.in',9312181442,5,'F530'),(18,'Unknown','2018-09-16',1,'info@raagatech.com',9891029284,1,'Unknown'),(28,'Sarvesh Tripathi','2018-12-15',1,'sarvesh.new@gmail.com',9312181442,1,'NA');
/*!40000 ALTER TABLE `inquiry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inquirystatusmaster`
--

DROP TABLE IF EXISTS `inquirystatusmaster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inquirystatusmaster` (
  `inquirystatus_id` int(11) NOT NULL AUTO_INCREMENT,
  `label_text` varchar(10) NOT NULL,
  `label_color` varchar(10) NOT NULL,
  `description` varchar(200) NOT NULL,
  PRIMARY KEY (`inquirystatus_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inquirystatusmaster`
--

LOCK TABLES `inquirystatusmaster` WRITE;
/*!40000 ALTER TABLE `inquirystatusmaster` DISABLE KEYS */;
INSERT INTO `inquirystatusmaster` VALUES (1,'OPEN','#FFFF00','The lead has been generated.'),(2,'COLD','#FF0000','The lead has denied for uncertain duration.'),(3,'CONFIRM','#FFA500','The lead is interested for music training in raagatech.'),(4,'HOLD','#A9A9A9','The lead is interested for music training but have some issues.'),(5,'CLOSE','#00FF00','The lead has started music training at raagatech.');
/*!40000 ALTER TABLE `inquirystatusmaster` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspiration`
--

DROP TABLE IF EXISTS `inspiration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspiration` (
  `inspiration_id` int(11) NOT NULL AUTO_INCREMENT,
  `inspirationname` varchar(100) NOT NULL,
  `material_id` int(11) NOT NULL,
  PRIMARY KEY (`inspiration_id`),
  KEY `material_id_fkey` (`material_id`),
  CONSTRAINT `material_id_fkey` FOREIGN KEY (`material_id`) REFERENCES `syllabus_material` (`material_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspiration`
--

LOCK TABLES `inspiration` WRITE;
/*!40000 ALTER TABLE `inspiration` DISABLE KEYS */;
INSERT INTO `inspiration` VALUES (1,'Indian Classical Vocals',1),(2,'Guitar',2),(3,'FineArts',3),(4,'Keyboards',4),(5,'Kathak',5),(6,'Bharatnatyam',6),(7,'Bollywood Dance',7),(8,'Drums',8),(9,'Tabla',9),(10,'Harmonium',10),(11,'Western Vocals',11),(12,'Drama & Speech',12),(13,'Saxophone',13);
/*!40000 ALTER TABLE `inspiration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inspirator`
--

DROP TABLE IF EXISTS `inspirator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inspirator` (
  `inspirator_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `middle_name` varchar(50) DEFAULT NULL,
  `last_name` varchar(50) DEFAULT NULL,
  `date_of_birth` date NOT NULL,
  `qualification_id` int(11) DEFAULT NULL,
  `occupation` varchar(100) DEFAULT NULL,
  `specialization` int(11) NOT NULL,
  `jack1` varchar(100) DEFAULT NULL,
  `jack2` varchar(100) DEFAULT NULL,
  `jack3` varchar(100) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `country_code` int(11) NOT NULL,
  `mobile` bigint(20) NOT NULL,
  `address_line1` varchar(50) NOT NULL,
  `address_line2` varchar(50) DEFAULT NULL,
  `address_line3` varchar(50) DEFAULT NULL,
  `address_line4` varchar(50) DEFAULT NULL,
  `date_of_joining` date NOT NULL,
  `date_of_resignation` date NOT NULL,
  PRIMARY KEY (`inspirator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inspirator`
--

LOCK TABLES `inspirator` WRITE;
/*!40000 ALTER TABLE `inspirator` DISABLE KEYS */;
/*!40000 ALTER TABLE `inspirator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `level`
--

DROP TABLE IF EXISTS `level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `level` (
  `level_id` int(11) NOT NULL AUTO_INCREMENT,
  `levelname` varchar(50) NOT NULL,
  PRIMARY KEY (`level_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `level`
--

LOCK TABLES `level` WRITE;
/*!40000 ALTER TABLE `level` DISABLE KEYS */;
INSERT INTO `level` VALUES (1,'Praveshika'),(2,'First Year'),(3,'Junior Diploma'),(4,'Third Year'),(5,'Senior Diploma'),(6,'Fifth Year'),(7,'Prabhakar'),(8,'Seventh Year'),(9,'Praveen');
/*!40000 ALTER TABLE `level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_contact_list`
--

DROP TABLE IF EXISTS `samcrm_contact_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_contact_list` (
  `mobile` bigint(20) NOT NULL,
  `vendor_group_id` int(11) NOT NULL,
  UNIQUE KEY `samcrm_contact_list_ukey` (`mobile`,`vendor_group_id`),
  KEY `samcrm_contact_list_fkey` (`vendor_group_id`),
  CONSTRAINT `samcrm_contact_list_fkey` FOREIGN KEY (`vendor_group_id`) REFERENCES `samcrm_vendor_category_subtype` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_contact_list`
--

LOCK TABLES `samcrm_contact_list` WRITE;
/*!40000 ALTER TABLE `samcrm_contact_list` DISABLE KEYS */;
INSERT INTO `samcrm_contact_list` VALUES (9415382922,1),(9415878481,1),(9953735792,1),(9999275533,1),(9350868586,2),(9873533628,2);
/*!40000 ALTER TABLE `samcrm_contact_list` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_individual`
--

DROP TABLE IF EXISTS `samcrm_individual`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_individual` (
  `individual_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `mobile` bigint(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `zipcode` varchar(10) NOT NULL,
  `country_code` int(11) NOT NULL,
  `address` varchar(200) DEFAULT NULL,
  `creation_date` date NOT NULL,
  `password` varchar(10) DEFAULT NULL,
  `profile_pic` varchar(100) DEFAULT NULL,
  `profile_color` varchar(10) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `marriage_aniversary` date DEFAULT NULL,
  `occupation` varchar(100) DEFAULT NULL,
  `spouse` varchar(100) DEFAULT NULL,
  `child1` varchar(100) DEFAULT NULL,
  `child2` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`individual_id`),
  UNIQUE KEY `mobile` (`mobile`),
  UNIQUE KEY `individual_ukey` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_individual`
--

LOCK TABLES `samcrm_individual` WRITE;
/*!40000 ALTER TABLE `samcrm_individual` DISABLE KEYS */;
INSERT INTO `samcrm_individual` VALUES (1,'',9312181442,'','201306',91,NULL,'2018-01-24','ddrdddrd',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,'Samriddhi tripathi',9953735792,'sarveshscjp06@yahoo.co.in','201306',91,'GNA 2','2018-03-10','samriddhi',NULL,NULL,'2009-09-17',NULL,NULL,NULL,NULL,NULL),(3,'Raksha Tripathi',9891029284,'raksha.alld@gmail.com','201306',91,NULL,'2018-03-28','raagatech',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,'brijesh tripathi',9999275533,'br@g.com','201301',91,'ddcc. xxxx ','2018-04-07',NULL,NULL,NULL,'1982-11-15',NULL,NULL,NULL,NULL,NULL),(6,'Arjun Tripathi',9415878481,'arjun@yahoo.com','301201',91,'noida','2018-04-07',NULL,NULL,NULL,'2007-09-25',NULL,NULL,NULL,NULL,NULL),(7,'vineet rajput',9873533628,'vinitshishodia6656@gmail.com','201301',91,'gaurs stadium','2018-05-13',NULL,NULL,NULL,'1980-01-01',NULL,NULL,NULL,NULL,NULL),(8,'Kapil',9350868586,'kapil@gmail.com','201301',91,'gaurs stadium','2018-05-13',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9,'shailesh tripathi',9415382922,'shailesh.transport@gmail.com','228131',91,NULL,'2018-05-24','shailesh12',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `samcrm_individual` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_obm_address`
--

DROP TABLE IF EXISTS `samcrm_obm_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_obm_address` (
  `street` varchar(40) DEFAULT NULL,
  `state` varchar(40) DEFAULT NULL,
  `zipcode` varchar(10) DEFAULT NULL,
  `country` varchar(40) DEFAULT NULL,
  `addresstype` varchar(10) DEFAULT NULL,
  `address_id` varchar(10) NOT NULL,
  `city` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_obm_address`
--

LOCK TABLES `samcrm_obm_address` WRITE;
/*!40000 ALTER TABLE `samcrm_obm_address` DISABLE KEYS */;
INSERT INTO `samcrm_obm_address` VALUES ('null','','228131','91','primary','1','');
/*!40000 ALTER TABLE `samcrm_obm_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_obm_customer`
--

DROP TABLE IF EXISTS `samcrm_obm_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_obm_customer` (
  `customer_id` int(11) NOT NULL,
  `first_name` varchar(40) DEFAULT NULL,
  `last_name` varchar(40) DEFAULT NULL,
  `phone_number` int(10) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `password` varchar(40) DEFAULT NULL,
  `creditcardtype` varchar(40) DEFAULT NULL,
  `creditcardnumber` varchar(100) DEFAULT NULL,
  `status` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_obm_customer`
--

LOCK TABLES `samcrm_obm_customer` WRITE;
/*!40000 ALTER TABLE `samcrm_obm_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `samcrm_obm_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_obm_customer_address`
--

DROP TABLE IF EXISTS `samcrm_obm_customer_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_obm_customer_address` (
  `customer_id` varchar(10) NOT NULL,
  `address_id` varchar(10) NOT NULL,
  PRIMARY KEY (`customer_id`),
  UNIQUE KEY `address_id` (`address_id`),
  CONSTRAINT `fk_addressid` FOREIGN KEY (`address_id`) REFERENCES `samcrm_obm_address` (`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_obm_customer_address`
--

LOCK TABLES `samcrm_obm_customer_address` WRITE;
/*!40000 ALTER TABLE `samcrm_obm_customer_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `samcrm_obm_customer_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_obm_items`
--

DROP TABLE IF EXISTS `samcrm_obm_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_obm_items` (
  `item_id` int(11) NOT NULL,
  `product_id` int(11) DEFAULT NULL,
  `product_name` varchar(100) DEFAULT NULL,
  `part_number` varchar(100) DEFAULT NULL,
  `price` float(8,2) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `order_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_obm_items`
--

LOCK TABLES `samcrm_obm_items` WRITE;
/*!40000 ALTER TABLE `samcrm_obm_items` DISABLE KEYS */;
INSERT INTO `samcrm_obm_items` VALUES (1,30,NULL,NULL,NULL,5,1);
/*!40000 ALTER TABLE `samcrm_obm_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_obm_orders`
--

DROP TABLE IF EXISTS `samcrm_obm_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_obm_orders` (
  `order_id` int(11) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `order_date` date DEFAULT NULL,
  `price` float(8,2) DEFAULT NULL,
  `status` varchar(40) DEFAULT NULL,
  `comments` varchar(400) DEFAULT NULL,
  `vendor_id` int(11) NOT NULL,
  `delivery_date` date NOT NULL,
  `delivery_time` varchar(10) NOT NULL,
  `delivery_person` varchar(100) NOT NULL,
  `address_id` int(11) NOT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_obm_orders`
--

LOCK TABLES `samcrm_obm_orders` WRITE;
/*!40000 ALTER TABLE `samcrm_obm_orders` DISABLE KEYS */;
INSERT INTO `samcrm_obm_orders` VALUES (1,9,'2019-03-02',130.50,'open','test order booking mobule',1,'2019-01-17','12:40:23','Sarvesh',1);
/*!40000 ALTER TABLE `samcrm_obm_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_obm_product`
--

DROP TABLE IF EXISTS `samcrm_obm_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_obm_product` (
  `product_id` int(11) NOT NULL,
  `product_code` varchar(10) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(400) DEFAULT NULL,
  `category` varchar(10) DEFAULT NULL,
  `subtype` varchar(10) DEFAULT NULL,
  `list_price` int(11) DEFAULT NULL,
  `producer` varchar(40) DEFAULT NULL,
  `image` blob,
  `image_url` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_obm_product`
--

LOCK TABLES `samcrm_obm_product` WRITE;
/*!40000 ALTER TABLE `samcrm_obm_product` DISABLE KEYS */;
/*!40000 ALTER TABLE `samcrm_obm_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_obm_shipment`
--

DROP TABLE IF EXISTS `samcrm_obm_shipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_obm_shipment` (
  `order_id` int(11) NOT NULL,
  `first_name` varchar(40) DEFAULT NULL,
  `last_name` varchar(40) DEFAULT NULL,
  `street` varchar(100) DEFAULT NULL,
  `city` varchar(100) DEFAULT NULL,
  `state` varchar(100) DEFAULT NULL,
  `zipcode` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_obm_shipment`
--

LOCK TABLES `samcrm_obm_shipment` WRITE;
/*!40000 ALTER TABLE `samcrm_obm_shipment` DISABLE KEYS */;
/*!40000 ALTER TABLE `samcrm_obm_shipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_productcategory`
--

DROP TABLE IF EXISTS `samcrm_productcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_productcategory` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(200) NOT NULL,
  `vendor_id` int(11) NOT NULL,
  `sequence` int(11) NOT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_productcategory`
--

LOCK TABLES `samcrm_productcategory` WRITE;
/*!40000 ALTER TABLE `samcrm_productcategory` DISABLE KEYS */;
INSERT INTO `samcrm_productcategory` VALUES (1,'BEAUTY','comprises of OFFERS, WAXING, FACIAL, MANI-PEDI, HAIR SPA, DE-TAN / BLEACH, HAIR COLOUR, THREADING',1,1),(2,'HANDSOME','comprises of GROOMING PACKAGES, 599 OFFERS, GROOMING, FACIAL, MANI-PEDIDE-TAN/ BLEACH, HAIR CARE',1,2),(3,'HAIR CARE','data unavailable',1,3),(4,'FACIAL','data unavailable',1,4),(5,'DE-TAN/ BLEACH','data unavailable',1,5),(6,'MANI-PEDI','data unavailable',1,6),(7,'THREADING','data unavailable',1,7),(8,'WAXING','data unavailable',1,8),(9,'MAKEUP','MAKEUP PACKAGES, BASIC / PARTY MAKEUP,ENGAGEMENT MAKEUP, BRIDAL MAKEUP, HAIR DO / SAREE DRAPING',1,9),(10,'MASSAGE','data unavailable',1,10),(11,'Test-TAB','QA work in-progress',1,11),(12,'Create-TAB','QA work in-progress',1,12);
/*!40000 ALTER TABLE `samcrm_productcategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_productservicedetails`
--

DROP TABLE IF EXISTS `samcrm_productservicedetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_productservicedetails` (
  `product_id` int(11) NOT NULL AUTO_INCREMENT,
  `productcategory_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` varchar(400) NOT NULL,
  `offer_price` varchar(100) DEFAULT NULL,
  `discount` int(11) DEFAULT NULL,
  `sequence` int(11) NOT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=124 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_productservicedetails`
--

LOCK TABLES `samcrm_productservicedetails` WRITE;
/*!40000 ALTER TABLE `samcrm_productservicedetails` DISABLE KEYS */;
INSERT INTO `samcrm_productservicedetails` VALUES (18,1,'Seasonal Full Body Nourishment','Step 1 : Application of Creamy Scrub Step 2 : Application of Rejuvenating & Nourishing Body Oil Step 3 : Full Body Steam Step 4 : Body Wrap with Cling Film Plastic Wrap Step 5 : Patanjali Hair Oil Massage','749 - 90',NULL,1),(19,1,'Seasonal Wax Free','FREE NORMAL/CHOCOLATE WAXING - Full Hands, Half legs & Underarms VLCC - Facial (Fruit / Anti Tan / Skin Tightening) L-Oreal - Hair Spa (Upto Shoulder Length) Basic - Manicure Basic - Pedicure','1399 - 165',NULL,1),(20,1,'Seasonal Ban Tan','FREE RAAGA - DE-TAN / OXY - BLEACH - (Face & Neck) VLCC - Facial (Fruit / Anti Tan / Skin Tightening) Basic - Pedicure Massage - Head Massage (15 Min)','799 - 106',NULL,1),(21,1,'999 Beauty','VLCC - Cleanup (Fruit / Anti Tan / Skin Tightening) Raaga - De Tan / Oxy - Bleach (Face & Neck) Basic - Manicure Basic - Pedicure Threading - Eyebrow & Upperlips Massage - Head Massage','999 - 140',NULL,1),(22,1,'Festive 1499 Beauty','Normal / Chocolate Wax - Full Body Normal / Chocolate Wax - Bikini VLCC - Cleanup (Fruit / Anti Tan / Skin Tightening) Basic - Manicure Basic - Pedicure','1499 - 218',NULL,1),(23,1,'Add On 1','Under Eye Treatment (EyeYouth Therapy)\nVedic Line (Argan & Coffee Bean Extract) (Add on) *Kindly select only If you want to add it with the above offers.','350 - 25',NULL,1),(24,1,'Add On 2','De-Tan / Bleach\nRaaga - De Tan / Oxy - Bleach - (Face & Neck) *Kindly select only If you want to add it with the above offers.','190 - 25',NULL,1),(25,1,'Add On 3','Rica Waxing - Bikini*\nAdd On - *Kindly select only If you want to add Rica Waxing with the Beauty Grandeur & Beauty Civitech offers. Bikini Waxing','380 - 30',NULL,1),(26,1,'Add On 4','Head Massage\nAdd On - Massage for 16 min *Kindly select only If you want to add it with the above offers.','99 - 16',NULL,1),(27,1,'Wax It - Rica','Rica Waxing - Full Legs, Full Hands & Underarms Threading - Eyebrows, Upperlips & Forehead','745 - 70',NULL,1),(28,2,'Quick Fix','Hair Cut Shaving Massage - Head Massage Raaga - De-Tan / Oxy - Bleach - (Face & Neck)','499 - 76',NULL,1),(29,2,'De-Stress','Hair Cut Shaving L-Oreal - Hair Spa VLCC - Facial','999 - 113',NULL,1),(30,2,'Party Ready','Hair Cut Shaving VLCC - Pedicure Raaga - De-Tan - Full Hands O3+ - Facial (Light & Bright / Stay Young)','1499 - 153',NULL,1),(31,2,'Complete Makeover','Hair Cut Shaving Lotus - Facial (Radiant Gold) Sara - De-Tan Manicure Sara - De-Tan Pedicure Raaga - De-Tan -Face & Neck L-Oreal Majirel - Hair Color Massage - (Head + Hand + Back) Massage','2019 - 216',NULL,1),(32,2,'Grooming Deals','VLCC - Cleanup (Fruit / Anti Tan / Skin Tightening) Raaga - De Tan / Oxy - Bleach (Face & Neck) Basic - Pedicure','799 - 110',NULL,1),(33,2,'599 Grooming','Hair Cut Shaving Raaga - De Tan / Oxy - Bleach (Face & Neck) Basic - Pedicure','599 - 93',NULL,1),(34,2,'Special Facial','VLCC Fruit','480 - 60',NULL,1),(35,2,'Mani-Pedi Combo','Basic Manicure Basic Pedicure','450 - 75',NULL,1),(36,2,'De Tan - RAAGA','Full Body','570 - 60',NULL,1),(37,2,'L-OREAL Hair Colour','Application (L-Oreal - Only Basic Colour Shades 1, 2, 3, 4 & 5 )','480 - 30',NULL,1),(38,3,'Hair Spa - LOreal','Upto Shoulder','400 - 30',NULL,1),(39,3,'L-Oreal Inoa (Ammonia Free)','L-Oreal - Only Basic Colour Shades 1, 2, 3, 4 & 5','480 - 30',NULL,1),(42,3,'L-Oreal Majirel (With Ammonia)','L-Oreal - Only Basic Colour Shades 1, 3, 4 & 5','480 - 30',NULL,1),(43,3,'L-OREAL (Majirel) Hair Colour','Application (L-Oreal - Only Basic Colour Shades 1, 3, 4 & 5 )','480 - 30',NULL,1),(44,3,'L-OREAL Hair Spa (grooming)','Hair Spa','480 - 30',NULL,1),(45,3,'Ampoules (grooming)','Anti Dandruff Ampoule','100 - 0',NULL,1),(46,3,'Add On (grooming)','Head Wash','180 - 30',NULL,1),(47,3,'GROOMING 1','Head+Hand+Back Massage','180 - 30',NULL,1),(48,3,'GROOMING 2','Hair Cut','150 - 25',NULL,1),(49,3,'GROOMING 3','Shaving','90 - 15',NULL,1),(58,4,'Classic Facial','VLCC Fruit','480 - 60',NULL,1),(59,4,'Luxury Facial','Vedic Line - Moroccan Argan Oil','600 - 60',NULL,1),(60,4,'Premium Facial','Vedic Line - For Oily Skin Bio White with Cryo Mask','750 - 60',NULL,1),(61,4,'Classic Cleanup','Back Scrub (Lotus)','265 - 30',NULL,1),(62,4,'Premium Cleanup','Pore Clean Up Agelock by O3+ (For Oily & Acne prone Skin)','565 - 40',NULL,1),(63,4,'Face Mask','O3+ Silk Radiating','245 - 20',NULL,1),(64,4,'Under Eye Treatment (EyeYouth Therapy)','Vedic Line Argan & Coffee Bean Extract','350 - 25',NULL,1),(65,4,'Raaga Normal to Oily','for beauty and grooming both','495 - 60',NULL,1),(66,4,'Raaga Normal to Dry','for beauty and grooming both','495 - 60',NULL,1),(67,4,'VLCC Brightening','By Specifix','485 - 60',NULL,1),(68,5,'Bleach - 1','Underarms (Oxy)','132 - 15',NULL,1),(69,5,'De Tan - 1','Full Hands Including Underarms','264 - 30',NULL,1),(70,5,'De Tan - 2','Face & Neck','192 - 25',NULL,1),(71,5,'De Tan - 3','Full Legs','324 - 40',NULL,1),(72,5,'De Tan - 4','Full Body','570 - 60',NULL,1),(73,5,'De Tan - 5','Stomach','162 - 20',NULL,1),(74,5,'Bleach - 2','Stomach','230 - 30',NULL,1),(75,5,'Bleach - 3','Full Hands Including Underarms','290 - 40',NULL,1),(76,5,'Bleach - 4','Full Body','560 - 60',NULL,1),(77,5,'Bleach - 5','Full Legs','400 - 50',NULL,1),(78,6,'Mani-Pedi Combo - 1','Basic Mani & Pedi Basic Manicure Basic Pedicure','450 - 75',NULL,1),(79,6,'Mani-Pedi Combo - 2','O3+ Mani & Pedi Luxury Crystal Spa\nO3+ Manicure Luxury Crystal Spa O3+ pedicure Luxury Crystal Spa','999 - 100',NULL,1),(80,6,'Manicure - 1','Basic Manicure','180 - 30',NULL,1),(81,6,'Manicure - 2','De-Tan Manicure','420 - 45',NULL,1),(82,6,'Manicure - 3','O3+ Manicure - Luxury Crystal Spa','470 - 45',NULL,1),(83,6,'Pedicure - 1','Basic Pedicure','270 - 45',NULL,1),(84,6,'Pedicure - 2','De-Tan Pedicure','510 - 60',NULL,1),(85,6,'Pedicure - 3','O3+ Pedicure - Luxury Crystal Spa','560 - 60',NULL,1),(86,6,'Pedicure - 4','Nail Paint Apply','60 - 10',NULL,1),(87,6,'Pedicure - 5','Cut, File & Polish','120 - 20',NULL,1),(88,7,'Full Face','Eyebrows, Upperlips, Forehead, Chin & Side Locks','180 - 30',NULL,1),(89,7,'Eye Brow','Eye Brow','30 - 5',NULL,1),(90,7,'Upper Lips','Upper Lips','30 - 5',NULL,1),(91,7,'Forehead','Forehead','30 - 5',NULL,1),(92,7,'Chin','Chin','30 - 5',NULL,1),(93,7,'Side Lock','Side Lock','60 - 10',NULL,1),(94,8,'Underarms','Underarms','60 - 10',NULL,1),(95,8,'Stomach','Stomach','190 - 20',NULL,1),(96,8,'Half Back','Half Back','190 - 20',NULL,1),(97,8,'Half Legs','Half Legs','190 - 20',NULL,1),(98,8,'Full Hands','Full Hands Without Underarms','220 - 25',NULL,1),(99,8,'Full Back','Full Back','220 - 25',NULL,1),(100,8,'Full Hands With Underarms','Full Hands With Underarms','280 - 35',NULL,1),(101,8,'Bikini','Bikini','250 - 30',NULL,1),(102,8,'Full Body','Full Body Excluding Bikini','730 - 110',NULL,1),(103,8,'Full Legs','Full Legs','340 - 45',NULL,1),(104,9,'Delight','Normal Hair Do Saree Draping','699 - 50',NULL,1),(105,9,'Bliss','Basic Makeup Normal Hair Do','999 - 65',NULL,1),(106,9,'Glamour','Basic Makeup Normal Hair Do Saree Draping','1199 - 85',NULL,1),(107,9,'Bloom','Party Makeup Basic Normal Hair Do','1799 - 70',NULL,1),(108,9,'Charm','Party Makeup Basic Fancy Hair Do','1999 - 90',NULL,1),(109,9,'Goddess','Party Makeup Basic Normal Hair Do Saree Draping','1999 - 90',NULL,1),(110,9,'Allure','Party Makeup Basic Fancy Hair Do Saree Draping','2199 - 110',NULL,1),(111,9,'Pre Bridal Package','Pre Bridal Beauty Package Body Polishing Airbrush Bridal Makeup','13999 - 485',NULL,1),(112,9,'Bliss Advance','Basic Make up Advance Hair Do','1299 - 75',NULL,1),(113,9,'Saree Draping','Saree Draping','300 - 30',NULL,1),(114,10,'Full Body Massage (Olive)','Olive Oil (Figgaro)','599 - 60',NULL,1),(115,10,'Relax Your Senses','Full Body Massage + Head Massage','660 - 80',NULL,1),(116,10,'Body Polishing Deals','Herbal Body Polishing with Massage Step 1 : Apricot Scrub Full Body Step 2 : Lemongrass Aromatherapy Oil Massage Step 3 : Haldi & Kesar Body Pack Step 4 : Rose Moisturizing Lotion','1099 - 94',NULL,1),(117,10,'Head & Foot','Head & Foot','240 - 30',NULL,1),(118,10,'Back Massage','Back Massage','240 - 30',NULL,1),(119,10,'Full Body Massage (Moroccan)','Moroccan Oil','499 - 60',NULL,1),(120,10,'Full Body - Vaadi','Aromatherapy Body Oil','545 - 60',NULL,1),(121,10,'Full Body with Scrub','Lotus','805 - 90',NULL,1),(122,10,'Vaadi (Without Massage)','Herbal Body Polishing without Massage Step 1 : Apricot Scrub Full Body Step 2 : Lemongrass Aromatherapy Oil Massage Step 3 : Haldi & Kesar Body Pack Step 4 : Rose Moisturizing Lotion','950 - 75',NULL,1),(123,10,'Vedic Line Chocolate Spa','Body Polishing with Massage Step 1 : Choco Deli Sugar Scrub Step 2 : Choco Vanilla Face & Body Butter Step 3 : Milk Chocolate Massage Gel Step 4 : Choco Strawberry Lip Cream Step 5 : Choco Cherry Mud Pack Step 6 : Choco Butter Scotch Cream Pack Step 7 : Choco Almond Oil Complex','100',10,1);
/*!40000 ALTER TABLE `samcrm_productservicedetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_productserviceinventory`
--

DROP TABLE IF EXISTS `samcrm_productserviceinventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_productserviceinventory` (
  `product_id` int(11) NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit` varchar(10) NOT NULL,
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_productserviceinventory`
--

LOCK TABLES `samcrm_productserviceinventory` WRITE;
/*!40000 ALTER TABLE `samcrm_productserviceinventory` DISABLE KEYS */;
/*!40000 ALTER TABLE `samcrm_productserviceinventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_profile_images`
--

DROP TABLE IF EXISTS `samcrm_profile_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_profile_images` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `individual_id` int(11) NOT NULL,
  `image_name` varchar(100) NOT NULL,
  `image_url` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `samcrm_individual_id_fkey` (`individual_id`),
  CONSTRAINT `samcrm_individual_id_fkey` FOREIGN KEY (`individual_id`) REFERENCES `samcrm_individual` (`individual_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_profile_images`
--

LOCK TABLES `samcrm_profile_images` WRITE;
/*!40000 ALTER TABLE `samcrm_profile_images` DISABLE KEYS */;
/*!40000 ALTER TABLE `samcrm_profile_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_users`
--

DROP TABLE IF EXISTS `samcrm_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_users` (
  `mobile` bigint(20) NOT NULL,
  `is_active` int(11) NOT NULL,
  `is_vendor` int(11) NOT NULL,
  `is_loggedin` int(11) NOT NULL,
  `is_on_promotion` int(11) NOT NULL,
  `ip_address` varchar(100) NOT NULL,
  UNIQUE KEY `samcrm_users_ukey` (`mobile`,`ip_address`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_users`
--

LOCK TABLES `samcrm_users` WRITE;
/*!40000 ALTER TABLE `samcrm_users` DISABLE KEYS */;
INSERT INTO `samcrm_users` VALUES (9312181442,1,1,1,1,'000000000000000'),(9415382922,1,0,1,1,'000000000000000'),(9891029284,1,2,0,1,'000000000000000'),(9953735792,1,0,0,1,'000000000000000');
/*!40000 ALTER TABLE `samcrm_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_vendor_category_subtype`
--

DROP TABLE IF EXISTS `samcrm_vendor_category_subtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_vendor_category_subtype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` bigint(20) NOT NULL,
  `vendor_category_id` int(11) NOT NULL,
  `vendor_subtype_id` int(11) NOT NULL,
  `vendor_title` varchar(100) NOT NULL,
  `vendor_registration_no` varchar(100) NOT NULL,
  `logo_name` varchar(100) NOT NULL,
  `logo_url` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `samcrm_vendor_category_subtype_ukey` (`mobile`,`vendor_category_id`,`vendor_subtype_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_vendor_category_subtype`
--

LOCK TABLES `samcrm_vendor_category_subtype` WRITE;
/*!40000 ALTER TABLE `samcrm_vendor_category_subtype` DISABLE KEYS */;
INSERT INTO `samcrm_vendor_category_subtype` VALUES (1,9312181442,1,1,'Simply Divine','temp01','',''),(2,9891029284,11,9,'Raagatech','D-648','','');
/*!40000 ALTER TABLE `samcrm_vendor_category_subtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_vendorcategory`
--

DROP TABLE IF EXISTS `samcrm_vendorcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_vendorcategory` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `label` varchar(100) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_vendorcategory`
--

LOCK TABLES `samcrm_vendorcategory` WRITE;
/*!40000 ALTER TABLE `samcrm_vendorcategory` DISABLE KEYS */;
INSERT INTO `samcrm_vendorcategory` VALUES (1,'Beauty salon','a beauty salon and a hair salon and although many small businesses do offer both sets of treatments'),(2,'Grocery Store','Grocery stores often offer non-perishable food, non-food that is packaged in bottles, boxes, and cans'),(3,'Food and beverage','responsible for maintaining high quality of food and service, food costing, managing restaurants, bars, etc.'),(4,'Fruits and vegetables','In everyday usage, vegetables are certain parts of plants that are consumed by humans as food as part of a savory meal'),(5,'Bakery','produces and sells flour-based food baked in an oven such as bread, cookies, cakes, pastries, and pies'),(6,'Dairy product','a type of food produced from or containing the milk of mammals'),(7,'Sweets and Snacks','sweets and snack come in a variety of forms including packaged snack foods and other processed foods, as well as items made from fresh ingredients'),(8,'Auto-mobile services','A motor vehicle service or tune-up is a series of maintenance procedures carried out at a set time interval'),(9,'Auto-mobile services','A motor vehicle service or tune-up is a series of maintenance procedures carried out at a set time interval'),(10,'Broker','arranges transactions between a buyer and a seller for a commission when the deal is executed'),(11,'Education and training','Education is the process of facilitating learning, or the acquisition of knowledge, skills, values, beliefs, and habits'),(12,'Travels','private retailer or public service that provides travel and tourism related services to the public');
/*!40000 ALTER TABLE `samcrm_vendorcategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `samcrm_vendorsubtype`
--

DROP TABLE IF EXISTS `samcrm_vendorsubtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `samcrm_vendorsubtype` (
  `subtype_id` int(11) NOT NULL AUTO_INCREMENT,
  `label` varchar(100) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  `vendorcategory_id` int(11) NOT NULL,
  PRIMARY KEY (`subtype_id`),
  KEY `samcrm_vendorcategory_fkey` (`vendorcategory_id`),
  CONSTRAINT `samcrm_vendorcategory_fkey` FOREIGN KEY (`vendorcategory_id`) REFERENCES `samcrm_vendorcategory` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `samcrm_vendorsubtype`
--

LOCK TABLES `samcrm_vendorsubtype` WRITE;
/*!40000 ALTER TABLE `samcrm_vendorsubtype` DISABLE KEYS */;
INSERT INTO `samcrm_vendorsubtype` VALUES (1,'Beauty treatments','hair treatment, eye extensions, acne treatment, manicure, pedicure and more',1),(2,'Spa','A spa is a location where mineral-rich spring water (and sometimes seawater) is used to give medicinal baths',1),(3,'Meditation','Parlour Meditation may be used to reduce stress, anxiety, depression, and pain',1),(4,'Barber','mainly to cut, dress, groom, style and shave men and boys hair',1),(5,'Coaching Institute','',11),(6,'Tuition Classes','',11),(7,'Computer Institute','',11),(8,'Car Training','',11),(9,'Music Learning','',11),(10,'Dance Training','',11),(11,'Personality Development Centre','',11),(12,'Fashion Design Centre','',11),(13,'Language Learning','',11);
/*!40000 ALTER TABLE `samcrm_vendorsubtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `syllabus_material`
--

DROP TABLE IF EXISTS `syllabus_material`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `syllabus_material` (
  `material_id` int(11) NOT NULL,
  `materialname` varchar(100) NOT NULL,
  `level_id` int(11) NOT NULL,
  PRIMARY KEY (`material_id`),
  KEY `level_id_fkey` (`level_id`),
  CONSTRAINT `level_id_fkey` FOREIGN KEY (`level_id`) REFERENCES `level` (`level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `syllabus_material`
--

LOCK TABLES `syllabus_material` WRITE;
/*!40000 ALTER TABLE `syllabus_material` DISABLE KEYS */;
INSERT INTO `syllabus_material` VALUES (1,'will be updated soon',1),(2,'will be updated soon',1),(3,'will be updated soon',1),(4,'will be updated soon',1),(5,'will be updated soon',1),(6,'will be updated soon',1),(7,'will be updated soon',1),(8,'will be updated soon',1),(9,'will be updated soon',1),(10,'will be updated soon',1),(11,'will be updated soon',1),(12,'will be updated soon',1),(13,'will be updated soon',1);
/*!40000 ALTER TABLE `syllabus_material` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `creation_date` date NOT NULL,
  `email` varchar(100) NOT NULL,
  `country_code` int(11) NOT NULL,
  `mobile` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Sarvesh Tripathi','ddrdddrd','2017-08-07','sarveshtripathi@raagatech.com',91,9312181442),(2,'Sarvesh1 Tripathi','ddrdddrd','2017-08-07','sarveshtripathi1@raagatech.com',91,9312181442),(3,'Sarvesh1 Tripathi','ddrdddrd','2017-08-07','sarveshtripathi1@raagatech.com',91,9312181442),(4,'Sarvesh Tripathi','ddrdddrd','2017-08-07','sarveshtripathi@raagatech.com',91,9312181442),(5,'Sarvesh Tripathi','ddrdddrd','2017-08-07','sarveshtripathi@raagatech.com',91,9312181442),(6,'aa','ddrdddrd','2018-01-01','a@a.com',91,1234123412);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `whatsappcontacts`
--

DROP TABLE IF EXISTS `whatsappcontacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `whatsappcontacts` (
  `mobile` bigint(20) NOT NULL,
  `country_code` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `creation_date` date NOT NULL,
  `email` varchar(100) NOT NULL,
  PRIMARY KEY (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `whatsappcontacts`
--

LOCK TABLES `whatsappcontacts` WRITE;
/*!40000 ALTER TABLE `whatsappcontacts` DISABLE KEYS */;
/*!40000 ALTER TABLE `whatsappcontacts` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-02 19:50:13
