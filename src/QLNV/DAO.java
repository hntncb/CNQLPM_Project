package QLNV;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DAO {
    void insert(NhanVien nv) throws SQLException;
    void update(NhanVien nv) throws SQLException; 
    boolean delete(NhanVien nv) throws SQLException;
    ArrayList<NhanVien> getAll() throws SQLException;
    void showData() throws SQLException;                  
}