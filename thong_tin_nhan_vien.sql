-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th7 23, 2024 lúc 07:05 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `qlnv`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `thong_tin_nhan_vien`
--

CREATE TABLE `thong_tin_nhan_vien` (
  `ID` int(11) NOT NULL,
  `HoTen` varchar(50) NOT NULL,
  `DiaChi` varchar(50) NOT NULL,
  `SDT` int(10) NOT NULL,
  `ChucVu` varchar(20) NOT NULL,
  `namSinh` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `thong_tin_nhan_vien`
--

INSERT INTO `thong_tin_nhan_vien` (`ID`, `HoTen`, `DiaChi`, `SDT`, `ChucVu`, `namSinh`) VALUES
(1039454493, 'Đỗ Tuấn Phong', 'Số 33, Đường Đống Đa, Phường Tân Thịnh', 473928342, 'Nhân viên', '1990-05-15'),
(1238495668, 'Lê Tuấn Kiệt', 'Số 48, Đường Phúc Lộc, Phường Quang Trung', 443586493, 'Nhân viên', '1985-08-22'),
(1243323445, 'Lều Ngọc An', 'Số 5, Đường Trần Phú, Phường Quang Trung', 987463748, 'Nhân viên', '1992-11-12'),
(1253849302, 'Dương Tất Thành', 'Số 3, Đường Quang Trung, Phường Phan Đình Phùng', 958473823, 'Nhân viên', '1988-07-19'),
(1263274382, 'Phạm Việt Anh', 'Số 8, Đường Nguyễn Lương Bằng, Phường Tân Thịnh', 234338452, 'Nhân viên', '1991-03-08'),
(1332938456, 'Đặng Tiến Thắng', 'Số 25, Đường Hùng Vương, Phường Phan Đình Phùng', 968574854, 'Nhân viên', '1986-12-03'),
(1345950123, 'Vũ Đức Anh', 'Số 72, Đường Yết Kiêu, Phường Tân Thành', 668342192, 'Nhân viên', '1987-01-25'),
(1443948506, 'Nguyễn Tuấn Hải', 'Số 12, Đường Lương Ngọc Quyến, Phường Tân Lập', 238594856, 'Nhân viên', '1990-04-18'),
(1483744952, 'Dương Minh Trung', 'Số 27, Đường Bắc Hà, Phường Quang Vinh', 987443282, 'Nhân viên', '1993-09-30'),
(1485948695, 'Trần Tuấn Minh', 'Số 14, Đường Trần Phú, Phường Phan Đình Phùng', 759345231, 'Nhân viên', '1989-06-14'),
(1748324943, 'Lê Minh Tuấn', 'Số 56, Đường Trần Đại Nghĩa, Phường Tân Lập', 584392354, 'Nhân viên', '1984-02-22'),
(1859453203, 'Lê Tiến Tâm', 'Số 39, Đường Hùng Vương, Phường Quang Trung', 211238359, 'Nhân viên', '1994-08-10'),
(1920231931, 'Nguyễn Minh Quyền', 'Số 41, Đường Trần Đại Nghĩa, Phường Tân Thành', 584734824, 'Nhân viên', '1985-11-25'),
(1933594834, 'Trần Phương Tuấn', 'Số 13, Đường Bắc Hà, Phường Tân Lập', 757483745, 'Nhân viên', '1991-05-06'),
(1937485732, 'Ngô Quang Vinh', 'Số 62, Đường Võ Thị Sáu, Phường Tân Lập', 958473821, 'Nhân viên', '1987-03-27'),
(1938475932, 'Lê Minh Quân', 'Số 9, Đường Hà Huy Tập, Phường Quang Trung', 584937234, 'Nhân viên', '1990-07-17'),
(1947583933, 'Lê Cao Thắng', 'Số 21, Đường Lý Thái Tổ, Phường Tân Thành', 658594323, 'Nhân viên', '1988-10-09'),
(1948374823, 'Nguyễn Minh Nghĩa', 'Số 7, Đường Trịnh Văn Cấn, Phường Quang Vinh', 796837575, 'Nhân viên', '1992-01-11'),
(1948573029, 'Trần Xuân Toản', 'Số 17, Đường Hồ Chí Minh, Phường Quang Vinh', 859843921, 'Nhân viên', '1986-04-02');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `thong_tin_nhan_vien`
--
ALTER TABLE `thong_tin_nhan_vien`
  ADD PRIMARY KEY (`ID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
