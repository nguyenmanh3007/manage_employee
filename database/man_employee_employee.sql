-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: localhost    Database: man_employee
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `EmployeeId` int NOT NULL AUTO_INCREMENT,
  `Code` int NOT NULL,
  `Created` varchar(255) DEFAULT NULL,
  `Email` varchar(255) NOT NULL,
  `EmployeeStatus` bit(1) DEFAULT NULL,
  `Password` varchar(255) NOT NULL,
  `Phone` varchar(255) DEFAULT NULL,
  `TimeCheckin` varchar(255) DEFAULT NULL,
  `TimeCheckout` varchar(255) DEFAULT NULL,
  `Username` varchar(255) NOT NULL,
  PRIMARY KEY (`EmployeeId`),
  UNIQUE KEY `UK_bwf45s73t73cdqh6nn02tm1n5` (`Code`)
) ENGINE=MyISAM AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (30,2341,'23/07/2023','manh.2011@gmail.com',_binary '','$2a$10$x8aEYQqcScF3psohLIyOwe1I1I5d/VD7XXISLUQiEIoom7mlvEOhW','0943743322','08:00:00','17:00:00','user10'),(29,5503,'23/07/2023','manh.2010@gmail.com',_binary '','$2a$10$j0/NdgclDQQy1KQMGNxIVOtRiFDre0s4rn443cHY/jTiy4XrC6dbW','0943743322','08:00:00','17:00:00','user9'),(26,9496,'23/07/2023','manh.2007@gmail.com',_binary '','$2a$10$XLdn2nGMUBkfVKLajN/6TudobeOu9je4eip53vsySnpOqyvxOcWee','0943743322','08:00:00','17:00:00','user6'),(60,8008,'02/08/2023','manh.tran.3007@gmail.com',_binary '','$2a$10$FuYJ7d5KTdiQW.Wfb/Z2rOWmn7NDnAgQqZc6EbEH5WoOrXX8zpmSO','0943743322','08:00:00','17:00:00','tranmanh'),(28,4345,'23/07/2023','manh.2009@gmail.com',_binary '','$2a$10$DVL.3qXZagJBeYXU9LpXietWgEhNlsjY9eb2Zd0hZID2yIiJ/6l3.','0943743322','08:00:00','17:00:00','user8'),(63,2099,'02/08/2023','manh.nguyen.3007@gmail.com',_binary '','$2a$10$JZvFS.rR6dFgjBEhk0W9fu4EnOqOdyYMOJ7.tBwQLGswJCIqUbPEm','0943743322','08:00:00','17:00:00','manhtran'),(38,4112,'27/07/2023','manh.2016@gmail.com',_binary '','$2a$10$fgsaD.Nw2rigxyknbObtYOSLkPpwGfGnj4h89ivUnpnL5GOmlJlc.','0943743322','08:00:00','17:00:00','user17'),(39,1726,'27/07/2023','manh.2019@gmail.com',_binary '','$2a$10$EmQC.X7o1qu0nETX3OZ1fuQgI2wI9sxHq4sASMJh2i35pOBnQ0R0u','0943743322','08:00:00','17:00:00','user18'),(37,7158,'26/07/2023','manh.2017@gmail.com',_binary '','$2a$10$Y0uFWyQp7JjMpTFQkTnKv.JBYyHhS6k.BLWbVbgkBFNCIewzdIemq','0943743322','08:00:00','17:00:00','user16'),(58,5245,'31/07/2023','manh.2020@gmail.com',_binary '','$2a$10$m1feNj0SzXEgSut5YYy3xesv66aX08dmbfNdY5A5Sl3PVamm45pcK','0943743322','08:00:00','17:00:00','user19'),(64,6421,'02/08/2023','nmanh.3007@gmail.com',_binary '','$2a$10$R8zYV9cy.zcoxMgMdmVNpOFQfJXxCpwZwRbQu6X./6vuxIxuFU3Xq','0943743322','08:00:00','17:00:00','nmanh3007');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-04 23:22:16
