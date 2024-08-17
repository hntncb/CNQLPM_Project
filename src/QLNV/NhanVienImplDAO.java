/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QLNV;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class NhanVienImplDAO implements DAO {

    private final String selectAll = "SELECT * FROM thong_tin_nhan_vien";
    private final String sqlInsert = "INSERT INTO thong_tin_nhan_vien (ID, HoTen, NamSinh, DiaChi, SDT, ChucVu) VALUES (?, ?, ?, ?, ?, ?)";
    private final String sqlUpdate = "UPDATE thong_tin_nhan_vien SET HoTen = ?, NamSinh = ?, DiaChi = ?, SDT = ?, ChucVu = ? WHERE ID = ?";
    private final String sqlDelete = "DELETE FROM thong_tin_nhan_vien WHERE ID = ?";
    private final String sqlSearch = "SELECT * FROM thong_tin_nhan_vien WHERE ID LIKE ? OR HoTen LIKE ?";
    private static final SimpleDateFormat INPUT_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
    private static final SimpleDateFormat OUTPUT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private Connection getConnection() throws SQLException {
        return ConnectionFactory.getInstance().getConnection();
    }

    public ArrayList<NhanVien> getAll() throws SQLException {
        Connection con = getConnection();
        Statement st = null;
        ResultSet rs = null;
        ArrayList<NhanVien> listAll = new ArrayList<>();
        try {
            st = con.createStatement();
            rs = st.executeQuery(selectAll);
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getInt("ID"), 
                        rs.getString("HoTen"), 
                        rs.getString("NamSinh"), 
                        rs.getString("DiaChi"), 
                        rs.getString("SDT"), 
                        rs.getString("ChucVu")
                );
                listAll.add(nv);
            }
        } finally {
            closeResultSet(rs);
            closeStatement(st);
            closeConnection(con);
        }
        return listAll;
    }

    public void insert(NhanVien nv) throws SQLException {
        Connection con = getConnection();
        PreparedStatement pr = null;
        try {
            pr = con.prepareStatement(sqlInsert);
            pr.setInt(1, nv.getId());
            pr.setString(2, nv.getHoTen());
            pr.setString(3, nv.getNamSinh());
            pr.setString(4, nv.getDiaChi());
            pr.setString(5, nv.getSdt());
            pr.setString(6, nv.getChucVu());
            pr.executeUpdate();
        } finally {
            closeStatement(pr);
            closeConnection(con);
        }
    }

    public void insertByFile(String filePath) throws SQLException, IOException {
        Connection con = null;
        BufferedReader br = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);
            br = new BufferedReader(new FileReader(filePath));
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    if (line.startsWith("\ufeff")) {
                        line = line.substring(1);
                    }
                    isFirstLine = false;
                }
                String[] values = line.split(",");
                if (values.length == 6) {
                    try {
                        int id = Integer.parseInt(values[0].trim());
                        String hoTen = values[1].trim();
                        String namSinhStr = values[2].trim();
                        String diaChi = values[3].trim();
                        String sdt = values[4].trim();
                        String chucVu = values[5].trim();
                        java.util.Date namSinhDate = INPUT_DATE_FORMAT.parse(namSinhStr);
                        String namSinh = OUTPUT_DATE_FORMAT.format(namSinhDate);
                        NhanVien nv = new NhanVien(id, hoTen, namSinh, diaChi, sdt, chucVu);
                        insert(nv);
                    } catch (NumberFormatException | ParseException e) {
                        System.err.println("Lỗi dữ liệu: " + e.getMessage());
                    }
                }
            }
            con.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            closeConnection(con);
        }
    }
    public void update(NhanVien nv) throws SQLException {
        Connection con = getConnection();
        PreparedStatement pr = null;
        try {
            pr = con.prepareStatement(sqlUpdate);
            pr.setString(1, nv.getHoTen());
            pr.setString(2, nv.getNamSinh());
            pr.setString(3, nv.getDiaChi());
            pr.setString(4, nv.getSdt());
            pr.setString(5, nv.getChucVu());
            pr.setInt(6, nv.getId());
            pr.executeUpdate();
        } finally {
            closeStatement(pr);
            closeConnection(con);
        }
    }

    public boolean delete(NhanVien nv) throws SQLException {
        Connection con = getConnection();
        PreparedStatement pr = null;
        int rowsAffected = 0;
        try {
            pr = con.prepareStatement(sqlDelete);
            pr.setInt(1, nv.getId());
            rowsAffected = pr.executeUpdate();
        } finally {
            closeStatement(pr);
            closeConnection(con);
        }
        return rowsAffected > 0;
    }
    
    public ArrayList<NhanVien> search(String keyword) throws SQLException {
        Connection con = getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<NhanVien> resultList = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(sqlSearch);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getInt("ID"),
                        rs.getString("HoTen"),
                        rs.getString("NamSinh"),
                        rs.getString("DiaChi"),
                        rs.getString("SDT"),
                        rs.getString("ChucVu")
                );
                resultList.add(nv);
            }
        } finally {
            closeResultSet(rs);
            closeStatement(pstmt);
            closeConnection(con);
        }
        return resultList;
    }

    public void showData() throws SQLException {
        ArrayList<NhanVien> listNhanVien = getAll();
        for (NhanVien nv : listNhanVien) {
            System.out.println(nv.toString());
        }
    }

}
