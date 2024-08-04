-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th8 04, 2024 lúc 12:32 PM
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
-- Cấu trúc bảng cho bảng `chamcong`
--

CREATE TABLE `chamcong` (
  `id` int(50) NOT NULL,
  `id_nhanvien` int(11) NOT NULL,
  `ngay` date NOT NULL,
  `giolam` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

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

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tk_dangnhap`
--

CREATE TABLE `tk_dangnhap` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `user_type` enum('admin','user') NOT NULL DEFAULT 'user'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `tk_dangnhap`
--

INSERT INTO `tk_dangnhap` (`id`, `username`, `password`, `user_type`) VALUES
(1, 'admin', 'a', 'admin'),
(2, 'user', 'a', 'user'),
(3, 'a', 'a', 'user');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `chamcong`
--
ALTER TABLE `chamcong`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_nhanvien` (`id_nhanvien`);

--
-- Chỉ mục cho bảng `thong_tin_nhan_vien`
--
ALTER TABLE `thong_tin_nhan_vien`
  ADD PRIMARY KEY (`ID`);

--
-- Chỉ mục cho bảng `tk_dangnhap`
--
ALTER TABLE `tk_dangnhap`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `chamcong`
--
ALTER TABLE `chamcong`
  MODIFY `id` int(50) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `tk_dangnhap`
--
ALTER TABLE `tk_dangnhap`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `chamcong`
--
ALTER TABLE `chamcong`
  ADD CONSTRAINT `fk_nhanvien` FOREIGN KEY (`id_nhanvien`) REFERENCES `thong_tin_nhan_vien` (`ID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
