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
-- Table structure for table `confirm`
--

DROP TABLE IF EXISTS `confirm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `confirm` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `employeeId` int NOT NULL,
  `timeCheckIn` varchar(255) DEFAULT NULL,
  `timeCheckOut` varchar(255) DEFAULT NULL,
  `checkInLate` int DEFAULT NULL,
  `checkOutEarly` int DEFAULT NULL,
  `statusCheckIn` varchar(255) DEFAULT NULL,
  `statusCheckOut` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `FKimjktb2mqv2c378u3k0fu0tqn` (`employeeId`)
) ENGINE=MyISAM AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `confirm`
--

LOCK TABLES `confirm` WRITE;
/*!40000 ALTER TABLE `confirm` DISABLE KEYS */;
INSERT INTO `confirm` VALUES (21,37,'2023-07-25 14:53:33','2023-07-25 14:53:42',-413,-126,'dLate','vEarly'),(22,58,'2023-07-31 21:53:06','2023-07-31 21:53:10',-833,293,'dLate','good'),(23,58,'2023-08-01 22:10:22','2003-08-01 22:10:26',-850,310,'dLate','good'),(24,63,'2023-08-02 11:09:56','2023-08-02 11:09:59',-189,-350,'dLate','vEarly'),(25,64,'2023-08-02 15:48:45','2023-08-02 21:44:32',-468,284,'dLate','good'),(26,38,'2023-08-02 22:38:52','2023-08-02 22:38:55',-878,338,'dLate','good'),(27,64,'2023-08-03 10:05:26','2023-08-03 13:46:11',-125,-193,'dLate','vEarly');
/*!40000 ALTER TABLE `confirm` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-04 23:22:15
