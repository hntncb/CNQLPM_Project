package QLNV;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class NhanVienImplDAO implements DAO {

    private final String selectAll = "SELECT * FROM thong_tin_nhan_vien";
    private final String sqlInsert = "INSERT INTO thong_tin_nhan_vien (ID, HoTen, NamSinh, DiaChi, SDT, ChucVu) VALUES (?, ?, ?, ?, ?, ?)";
    private final String sqlUpdate = "UPDATE thong_tin_nhan_vien SET HoTen = ?, NamSinh = ?, DiaChi = ?, SDT = ?, ChucVu = ? WHERE ID = ?";
    private final String sqlDelete = "DELETE FROM thong_tin_nhan_vien WHERE ID = ?";
    private final String sqlFindByID = "SELECT * FROM thong_tin_nhan_vien WHERE ID = ?";
    private final String sqlFindByName = "SELECT * FROM thong_tin_nhan_vien WHERE HoTen = ?";

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

    @Override
    public ArrayList<NhanVien> getAll() throws SQLException {
        ArrayList<NhanVien> listAll = new ArrayList<>();
        try (Connection con = getConnection(); 
             Statement st = con.createStatement(); 
             ResultSet rs = st.executeQuery(selectAll)) {
            
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
        }
        return listAll;
    }

    @Override
    public void insert(NhanVien nv) throws SQLException {
        try (Connection con = getConnection(); 
             PreparedStatement pr = con.prepareStatement(sqlInsert)) {
            
            pr.setInt(1, nv.getId());
            pr.setString(2, nv.getHoTen());
            pr.setString(3, nv.getNamSinh());
            pr.setString(4, nv.getDiaChi());
            pr.setString(5, nv.getSdt());
            pr.setString(6, nv.getChucVu());
            pr.executeUpdate();
        }
    }

    @Override
    public void update(NhanVien nv) throws SQLException {
        try (Connection con = getConnection(); 
             PreparedStatement pr = con.prepareStatement(sqlUpdate)) {
            
            pr.setString(1, nv.getHoTen());
            pr.setString(2, nv.getNamSinh());
            pr.setString(3, nv.getDiaChi());
            pr.setString(4, nv.getSdt());
            pr.setString(5, nv.getChucVu());
            pr.setInt(6, nv.getId());
            pr.executeUpdate();
        }
    }

    @Override
    public boolean delete(NhanVien nv) throws SQLException {
        int rowsAffected = 0;
        try (Connection con = getConnection(); 
             PreparedStatement pr = con.prepareStatement(sqlDelete)) {
            
            pr.setInt(1, nv.getId());
            rowsAffected = pr.executeUpdate();
        }
        return rowsAffected > 0;
    }

    @Override
    public NhanVien findByID(int id) throws SQLException {
        NhanVien nv = null;
        try (Connection con = getConnection(); 
             PreparedStatement stmt = con.prepareStatement(sqlFindByID)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nv = new NhanVien(
                            rs.getInt("ID"), 
                            rs.getString("HoTen"), 
                            rs.getString("NamSinh"), 
                            rs.getString("DiaChi"), 
                            rs.getString("SDT"), 
                            rs.getString("ChucVu")
                    );
                }
            }
        }
        return nv;
    }

    @Override
    public NhanVien findByName(String name) throws SQLException {
        NhanVien nv = null;
        try (Connection con = getConnection(); 
             PreparedStatement stmt = con.prepareStatement(sqlFindByName)) {
            
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nv = new NhanVien(
                            rs.getInt("ID"), 
                            rs.getString("HoTen"), 
                            rs.getString("NamSinh"), 
                            rs.getString("DiaChi"), 
                            rs.getString("SDT"), 
                            rs.getString("ChucVu")
                    );
                }
            }
        }
        return nv;
    }

    @Override
    public void showData() throws SQLException {
        ArrayList<NhanVien> listNhanVien = getAll();
        for (NhanVien nv : listNhanVien) {
            System.out.println(nv.toString());
        }
    }
}
