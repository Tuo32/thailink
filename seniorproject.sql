-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- 主机： 127.0.0.1
-- 生成日期： 2023-05-02 16:46:50
-- 服务器版本： 10.4.28-MariaDB
-- PHP 版本： 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 数据库： `seniorproject`
--

-- --------------------------------------------------------

--
-- 表的结构 `sys_customer`
--

CREATE TABLE `sys_customer` (
  `customerID` int(11) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `birthDate` date DEFAULT NULL,
  `birthPlace` varchar(255) DEFAULT NULL,
  `passport` varchar(60) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `postcode` varchar(10) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL COMMENT 'If the customer is not adult, must specify the name and contact of the customer''s parents',
  `modifyTime` timestamp NULL DEFAULT current_timestamp(),
  `createTime` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `sys_customer`
--

INSERT INTO `sys_customer` (`customerID`, `name`, `gender`, `birthDate`, `birthPlace`, `passport`, `address`, `postcode`, `phone`, `email`, `comment`, `modifyTime`, `createTime`) VALUES
(0, NULL, NULL, NULL, NULL, '', NULL, NULL, NULL, NULL, NULL, '2023-04-21 13:55:51', '2023-02-11 16:00:00'),
(1, 'liu', 'Female', '2013-03-01', '沈阳', 'ED5321', 'muaklek', '1180', '+66 1234567890', 'liutianen22@gmail.com', 'hello', '2023-04-22 15:36:47', '2023-02-11 16:00:00');

-- --------------------------------------------------------

--
-- 表的结构 `sys_file`
--

CREATE TABLE `sys_file` (
  `fileID` int(11) NOT NULL,
  `serviceID` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `size` int(11) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `sys_file`
--

INSERT INTO `sys_file` (`fileID`, `serviceID`, `name`, `type`, `size`, `url`) VALUES
(166, 59, '屏幕截图 2023-04-22 002730.jpg', 'jpg', 1, 'http://localhost:9090/file/屏幕截图 2023-04-22 002730.jpg'),
(168, 59, 'ip记录.txt', 'txt', 6, 'http://localhost:9090/file/ip记录.txt'),
(169, 60, 'download.jpg', 'jpg', 5, 'http://localhost:9090/file/download.jpg'),
(170, 60, '新建 文本文档.txt', 'txt', 0, 'http://localhost:9090/file/新建 文本文档.txt');

-- --------------------------------------------------------

--
-- 表的结构 `sys_finance`
--

CREATE TABLE `sys_finance` (
  `financeID` int(11) NOT NULL,
  `serviceID` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `amount` double(10,1) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `sys_finance`
--

INSERT INTO `sys_finance` (`financeID`, `serviceID`, `description`, `amount`, `createTime`) VALUES
(97, 64, 'ceshi', 100.0, '2023-05-01 14:15:00'),
(98, 64, 'ceshi', 100.0, '2023-05-01 14:27:11'),
(99, 64, 'ceshi', 100.0, '2023-05-01 14:30:19'),
(100, 64, 'ceshi', 100.0, '2023-05-01 14:30:52'),
(106, 64, '500', 500.0, '2023-05-01 14:38:36'),
(107, 65, '500', 500.0, '2023-05-01 14:44:32'),
(109, 65, '500', 500.0, '2023-05-01 14:45:29');

-- --------------------------------------------------------

--
-- 表的结构 `sys_finance_history`
--

CREATE TABLE `sys_finance_history` (
  `financeID` int(11) NOT NULL,
  `serviceID` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `sys_finance_history`
--

INSERT INTO `sys_finance_history` (`financeID`, `serviceID`, `description`, `amount`, `createTime`) VALUES
(88, 58, 'test1', 100, '2023-04-22 14:18:49'),
(89, 58, 'trest2', 200, '2023-04-22 14:19:06'),
(90, 58, 'test3', 300, '2023-04-22 14:19:15');

-- --------------------------------------------------------

--
-- 表的结构 `sys_menu`
--

CREATE TABLE `sys_menu` (
  `menuID` int(11) NOT NULL,
  `menuName` varchar(255) NOT NULL,
  `path` varchar(255) NOT NULL,
  `DocName` varchar(255) NOT NULL,
  `description` varchar(255) NOT NULL,
  `pid` int(11) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `sys_menu`
--

INSERT INTO `sys_menu` (`menuID`, `menuName`, `path`, `DocName`, `description`, `pid`, `icon`, `position`) VALUES
(1, 'User Management', '/user', 'User', 'User', NULL, 'el-icon-menu', 'aside'),
(2, 'Role Management', '/role', 'Role', 'Role', NULL, 'el-icon-user', 'aside'),
(3, 'File Management', '/file', 'File', 'The authority to browse file management page', 9, 'el-icon-folder-opened', 'aside'),
(4, 'Customer Management', '/customer', 'Customer', 'The authority to browse customer management page', 7, 'el-icon-s-custom', 'aside'),
(5, 'Finance Management', '/finance', 'Finance', 'The authority to browse finance management page', 10, 'el-icon-coin', 'aside'),
(6, 'Customer Edit', '', '', 'The authority to edit customer data', 7, NULL, NULL),
(7, 'Customer ', '', '', 'This is only for the front-end tree component and categorizaton', NULL, NULL, NULL),
(8, 'Customer Delete', '', '', 'The authority to delete customer data', 7, NULL, NULL),
(9, 'File', '', '', 'This is only for the front-end tree component and categorizaton', NULL, NULL, NULL),
(10, 'Finance', '', '', 'This is only for the front-end tree component and categorizaton', NULL, NULL, NULL),
(13, 'File Upload', '', '', 'The authority to upload file', 9, NULL, NULL),
(14, 'File Delete', '', '', 'The authority to delete file', 9, NULL, NULL),
(15, 'Study', '', '', 'This is only for the front-end tree component and categorizaton', NULL, NULL, ''),
(16, 'Study Management', '/study', 'Study', 'The authority to browse study management page', 15, 'el-icon-school', 'aside'),
(17, 'Visa Management', '/visa', 'Visa', 'The authority to browse visa management page', 18, 'el-icon-s-check', 'aside'),
(18, 'Visa', '', '', 'This is only for the front-end tree component and categorizaton', NULL, NULL, ''),
(20, 'Finace Edit', '', '', 'The authority to edit finance data', 10, NULL, NULL),
(21, 'Visa Status Update', '', '', 'The authority to confirm the payment and update service status', 18, '', ''),
(22, 'Visa Delete', '', '', 'The authority to delete visa data', 18, NULL, NULL),
(23, 'Visa Edit', '', '', 'The authority to edit visa data', 18, NULL, NULL),
(24, 'Study Edit', '', '', 'The authority to edit study data', 15, NULL, NULL),
(25, 'Study Delete', '', '', 'The authority to delete study data', 15, NULL, NULL),
(26, 'Study Status Update', '', '', 'The authority to confirm the payment and update service status', 15, '', ''),
(28, 'Visa View All', '', '', 'The authority to view all visa records', 18, NULL, NULL),
(29, 'Service', '', '', 'This is only for the front-end tree component and categorizaton', NULL, NULL, NULL),
(30, 'Service Management', '/service', 'Service', 'The authority to browse service management page', 29, 'el-icon-s-order', 'aside'),
(31, 'Service Edit', '', '', 'The authority to edit service data', 29, NULL, NULL),
(32, 'Study View All', '', '', 'The authority to view all study records', 15, NULL, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `sys_receipt`
--

CREATE TABLE `sys_receipt` (
  `receiptID` int(11) NOT NULL,
  `userID` int(11) NOT NULL,
  `createTime` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `sys_receipt`
--

INSERT INTO `sys_receipt` (`receiptID`, `userID`, `createTime`) VALUES
(1083, 1, '2023-04-22 15:14:04'),
(1084, 1, '2023-04-22 15:14:14'),
(1086, 1, '2023-04-22 15:15:34'),
(1087, 1, '2023-04-22 15:37:16'),
(1089, 1, '2023-04-22 15:37:23');

-- --------------------------------------------------------

--
-- 表的结构 `sys_role`
--

CREATE TABLE `sys_role` (
  `roleID` int(11) NOT NULL,
  `roleName` varchar(50) NOT NULL,
  `description` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `sys_role`
--

INSERT INTO `sys_role` (`roleID`, `roleName`, `description`) VALUES
(1, 'Admin', 'administrator of this system, has authority to create,read,update and delete users'),
(14, 'Finance Officer', '12');

-- --------------------------------------------------------

--
-- 表的结构 `sys_role_menu`
--

CREATE TABLE `sys_role_menu` (
  `roleID` int(11) NOT NULL,
  `menuID` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `sys_role_menu`
--

INSERT INTO `sys_role_menu` (`roleID`, `menuID`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 13),
(1, 14),
(1, 15),
(1, 16),
(1, 17),
(1, 18),
(1, 21),
(1, 23),
(1, 24),
(1, 26),
(1, 29),
(1, 30),
(1, 31),
(14, 1),
(14, 2),
(14, 3),
(14, 4),
(14, 5),
(14, 13),
(14, 16),
(14, 17),
(14, 21),
(14, 26);

-- --------------------------------------------------------

--
-- 表的结构 `sys_service`
--

CREATE TABLE `sys_service` (
  `serviceID` int(11) NOT NULL,
  `customerID` int(11) NOT NULL,
  `infoID` int(11) DEFAULT NULL,
  `serviceType` char(20) NOT NULL,
  `price` double NOT NULL,
  `paid` double NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `sys_service`
--

INSERT INTO `sys_service` (`serviceID`, `customerID`, `infoID`, `serviceType`, `price`, `paid`) VALUES
(59, 1, 29, 'Visa', 1500, 4112.45),
(60, 1, 19, 'Study', 700, 0),
(61, 1, 32, 'Visa', 1500, 0),
(62, 1, 20, 'Study', 2000, 0),
(63, 1, 33, 'Visa', 56787, 0),
(64, 1, 34, 'Visa', 200, 800),
(65, 1, 35, 'Visa', 500, 1000);

-- --------------------------------------------------------

--
-- 表的结构 `sys_service_history`
--

CREATE TABLE `sys_service_history` (
  `serviceID` int(11) NOT NULL,
  `customerID` int(11) DEFAULT NULL,
  `infoID` int(11) DEFAULT NULL,
  `serviceType` char(20) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `paid` double DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- 转存表中的数据 `sys_service_history`
--

INSERT INTO `sys_service_history` (`serviceID`, `customerID`, `infoID`, `serviceType`, `price`, `paid`) VALUES
(58, 1, NULL, 'Visa', NULL, 600);

-- --------------------------------------------------------

--
-- 表的结构 `sys_study`
--

CREATE TABLE `sys_study` (
  `studyID` int(11) NOT NULL,
  `bloodType` varchar(5) DEFAULT NULL,
  `religion` varchar(40) DEFAULT NULL,
  `formerSchool` varchar(100) DEFAULT NULL,
  `fatherName` varchar(30) DEFAULT NULL,
  `motherName` varchar(30) DEFAULT NULL,
  `applySchool` varchar(100) DEFAULT NULL,
  `status` varchar(30) DEFAULT 'Phase1 unpaid',
  `userID` int(11) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `updated` timestamp NULL DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `sys_study`
--

INSERT INTO `sys_study` (`studyID`, `bloodType`, `religion`, `formerSchool`, `fatherName`, `motherName`, `applySchool`, `status`, `userID`, `comment`, `updated`, `createTime`) VALUES
(19, 'AB', 'Christianity', 'hh', 'hhhhhhhhhh', 'hhhhhhhhhhhhhhhhhhhhhh', 'hhhhhhhhhhhhhh', 'Phase2 unpaid', 1, NULL, '2023-05-01 13:51:14', '2023-04-22 15:37:44'),
(20, 'AB', 'Buddhism', 'q2we3', '123', '123123', '213', 'Phase2 unpaid', 55, NULL, '2023-05-01 13:51:19', '2023-04-22 16:17:42');

-- --------------------------------------------------------

--
-- 表的结构 `sys_user`
--

CREATE TABLE `sys_user` (
  `userID` int(11) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `createTime` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `modifyTime` timestamp NULL DEFAULT current_timestamp(),
  `roleID` int(11) DEFAULT NULL,
  `authentication` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `sys_user`
--

INSERT INTO `sys_user` (`userID`, `username`, `password`, `name`, `phone`, `address`, `email`, `createTime`, `modifyTime`, `roleID`, `authentication`) VALUES
(1, 'admin123', '0192023a7bbd73250516f069df18b500', 'liu', '+66 0980490571', 'Bangkok2', 'liutianen22@gmail.com', '2023-04-13 07:01:55', '2023-04-22 15:35:59', 1, NULL),
(54, 'dd', '1aabac6d068eef6a7bad3fdf50a05cc8', 'sss', '+87 123456', '12344', 'sss@gmail.com', '2023-04-22 16:11:16', '2023-04-22 16:11:16', 14, NULL);

-- --------------------------------------------------------

--
-- 表的结构 `sys_visa`
--

CREATE TABLE `sys_visa` (
  `visaID` int(11) NOT NULL,
  `visaType` varchar(50) NOT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `userID` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT 'Phase1 unpaid',
  `updated` timestamp NULL DEFAULT NULL,
  `createTime` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- 转存表中的数据 `sys_visa`
--

INSERT INTO `sys_visa` (`visaID`, `visaType`, `comment`, `userID`, `status`, `updated`, `createTime`) VALUES
(29, 'Tourist visa', NULL, 1, 'Completed', '2023-05-01 13:34:20', '2023-04-22 14:29:15'),
(32, 'Business visa', NULL, 54, 'Phase1 unpaid', '2023-05-01 13:47:34', '2023-04-22 16:11:42'),
(33, 'Education visa', NULL, 1, 'Phase1 unpaid', '2023-05-01 13:43:28', '2023-04-22 16:38:24'),
(34, 'Tourist visa', NULL, 1, 'Phase2 unpaid', '2023-05-01 14:50:07', '2023-05-01 14:10:12'),
(35, 'Tourist visa', NULL, 1, 'Completed', '2023-05-01 14:46:40', '2023-05-01 14:44:13');

--
-- 转储表的索引
--

--
-- 表的索引 `sys_customer`
--
ALTER TABLE `sys_customer`
  ADD PRIMARY KEY (`customerID`);

--
-- 表的索引 `sys_file`
--
ALTER TABLE `sys_file`
  ADD PRIMARY KEY (`fileID`),
  ADD UNIQUE KEY `name` (`name`),
  ADD KEY `customerID` (`serviceID`);

--
-- 表的索引 `sys_finance`
--
ALTER TABLE `sys_finance`
  ADD PRIMARY KEY (`financeID`,`serviceID`),
  ADD KEY `serviceID` (`serviceID`);

--
-- 表的索引 `sys_finance_history`
--
ALTER TABLE `sys_finance_history`
  ADD PRIMARY KEY (`financeID`);

--
-- 表的索引 `sys_menu`
--
ALTER TABLE `sys_menu`
  ADD PRIMARY KEY (`menuID`);

--
-- 表的索引 `sys_receipt`
--
ALTER TABLE `sys_receipt`
  ADD PRIMARY KEY (`receiptID`);

--
-- 表的索引 `sys_role`
--
ALTER TABLE `sys_role`
  ADD PRIMARY KEY (`roleID`);

--
-- 表的索引 `sys_role_menu`
--
ALTER TABLE `sys_role_menu`
  ADD PRIMARY KEY (`roleID`,`menuID`),
  ADD KEY `1` (`menuID`);

--
-- 表的索引 `sys_service`
--
ALTER TABLE `sys_service`
  ADD PRIMARY KEY (`serviceID`),
  ADD KEY `infoID` (`infoID`),
  ADD KEY `customerID` (`customerID`);

--
-- 表的索引 `sys_service_history`
--
ALTER TABLE `sys_service_history`
  ADD PRIMARY KEY (`serviceID`);

--
-- 表的索引 `sys_study`
--
ALTER TABLE `sys_study`
  ADD PRIMARY KEY (`studyID`);

--
-- 表的索引 `sys_user`
--
ALTER TABLE `sys_user`
  ADD PRIMARY KEY (`userID`),
  ADD KEY `role` (`roleID`);

--
-- 表的索引 `sys_visa`
--
ALTER TABLE `sys_visa`
  ADD PRIMARY KEY (`visaID`),
  ADD KEY `userID` (`userID`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `sys_customer`
--
ALTER TABLE `sys_customer`
  MODIFY `customerID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- 使用表AUTO_INCREMENT `sys_file`
--
ALTER TABLE `sys_file`
  MODIFY `fileID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=172;

--
-- 使用表AUTO_INCREMENT `sys_finance`
--
ALTER TABLE `sys_finance`
  MODIFY `financeID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=114;

--
-- 使用表AUTO_INCREMENT `sys_menu`
--
ALTER TABLE `sys_menu`
  MODIFY `menuID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- 使用表AUTO_INCREMENT `sys_receipt`
--
ALTER TABLE `sys_receipt`
  MODIFY `receiptID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=1091;

--
-- 使用表AUTO_INCREMENT `sys_role`
--
ALTER TABLE `sys_role`
  MODIFY `roleID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- 使用表AUTO_INCREMENT `sys_service`
--
ALTER TABLE `sys_service`
  MODIFY `serviceID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=66;

--
-- 使用表AUTO_INCREMENT `sys_study`
--
ALTER TABLE `sys_study`
  MODIFY `studyID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- 使用表AUTO_INCREMENT `sys_user`
--
ALTER TABLE `sys_user`
  MODIFY `userID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;

--
-- 使用表AUTO_INCREMENT `sys_visa`
--
ALTER TABLE `sys_visa`
  MODIFY `visaID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- 限制导出的表
--

--
-- 限制表 `sys_file`
--
ALTER TABLE `sys_file`
  ADD CONSTRAINT `sys_file_ibfk_1` FOREIGN KEY (`serviceID`) REFERENCES `sys_service` (`serviceID`);

--
-- 限制表 `sys_finance`
--
ALTER TABLE `sys_finance`
  ADD CONSTRAINT `sys_finance_ibfk_1` FOREIGN KEY (`serviceID`) REFERENCES `sys_service` (`serviceID`);

--
-- 限制表 `sys_role_menu`
--
ALTER TABLE `sys_role_menu`
  ADD CONSTRAINT `1` FOREIGN KEY (`menuID`) REFERENCES `sys_menu` (`menuID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `2` FOREIGN KEY (`roleID`) REFERENCES `sys_role` (`roleID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- 限制表 `sys_service`
--
ALTER TABLE `sys_service`
  ADD CONSTRAINT `sys_service_ibfk_1` FOREIGN KEY (`customerID`) REFERENCES `sys_customer` (`customerID`);

--
-- 限制表 `sys_user`
--
ALTER TABLE `sys_user`
  ADD CONSTRAINT `role` FOREIGN KEY (`roleID`) REFERENCES `sys_role` (`roleID`);

--
-- 限制表 `sys_visa`
--
ALTER TABLE `sys_visa`
  ADD CONSTRAINT `sys_visa_ibfk_1` FOREIGN KEY (`userID`) REFERENCES `sys_user` (`userID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
