-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 04, 2014 at 08:52 PM
-- Server version: 5.5.40-0ubuntu0.14.04.1
-- PHP Version: 5.5.9-1ubuntu4.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `redcrawler_`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`redcrawler`@`localhost` PROCEDURE `dorepeat`(p1 INT)
BEGIN
SET @x =0;
REPEAT SET @x = @x + 1; UNTIL @x > p1 END REPEAT;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `PostComments`
--

CREATE TABLE IF NOT EXISTS `PostComments` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `link` varchar(8192) CHARACTER SET utf8 COLLATE utf8_general_mysql500_ci NOT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_mysql500_ci NOT NULL,
  `redditID` varchar(10) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  `parentID` varchar(10) CHARACTER SET ascii COLLATE ascii_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2478 ;

-- --------------------------------------------------------

--
-- Table structure for table `PostTitles`
--

CREATE TABLE IF NOT EXISTS `PostTitles` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `link` varchar(8192) DEFAULT NULL,
  `content` text CHARACTER SET utf8 COLLATE utf8_general_mysql500_ci,
  `redditID` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `redditID` (`redditID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=110 ;

-- --------------------------------------------------------

--
-- Table structure for table `rawlinks`
--

CREATE TABLE IF NOT EXISTS `rawlinks` (
  `ID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `LINK` varchar(8192) CHARACTER SET utf8 COLLATE utf8_general_mysql500_ci DEFAULT NULL,
  `lastAccess` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2758 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
