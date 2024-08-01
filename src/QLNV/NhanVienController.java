package QLNV;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class NhanVienController {

    private final NhanVienImplDAO dao;
    private final NhanVienTableModel nhanVienModel;
    private final NhanVienView nhanVienView;
    private UserType userType;

    public NhanVienController(NhanVienView nhanVienView, NhanVienTableModel nhanVienModel) {
        this.nhanVienView = nhanVienView;
        this.nhanVienModel = nhanVienModel;
        this.dao = new NhanVienImplDAO();
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public void showNhanVienView() {
        nhanVienView.showListNhanVien(nhanVienModel);
        nhanVienView.addListNhanVienSelectionListener(new ListNhanVienSelectionListener());
        nhanVienView.addDeleteNhanVienListener(new DeleteNhanVienListener());
        nhanVienView.addUpdateNhanVienListener(new UpdateNhanVienListener());
        nhanVienView.addInsertNhanVienListener(new InsertNhanVienListener());
        nhanVienView.addClearNhanVienListener(new ClearNhanVienListener());
        nhanVienView.addSearchNhanVienListener(new SearchNhanVienListener());
        nhanVienView.setButtonVisibility(userType == UserType.ADMIN);
        nhanVienView.setVisible(true);
        nhanVienView.setEnabled(true);
    }

    class ListNhanVienSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            nhanVienView.fillNhanVienFromSelectedRow();
        }
    }

    class ClearNhanVienListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            nhanVienView.clearNhanVienInfo();
        }
    }

    class InsertNhanVienListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            NhanVien nv = nhanVienView.getNhanVienInfo();
            if (nv != null) {
                try {
                    dao.insert(nv);
                    nhanVienView.clearNhanVienInfo();
                    nhanVienView.showListNhanVien(new NhanVienTableModel(dao.getAll()));
                    nhanVienView.showMessage("Thêm thành công!");
                } catch (SQLException e1) {
                    nhanVienView.showMessage(e1.toString());
                }
            }
        }
    }

    class DeleteNhanVienListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            NhanVien nv = nhanVienView.getNhanVienInfo();
            if (nv != null) {
                try {
                    dao.delete(nv);
                    nhanVienView.clearNhanVienInfo();
                    ArrayList<NhanVien> ds = dao.getAll();
                    if (ds != null) {
                        nhanVienView.showListNhanVien(new NhanVienTableModel(ds));
                    } else {
                        nhanVienView.showMessage("Dữ liệu rỗng");
                    }
                    nhanVienView.showMessage("Xóa thành công!");
                } catch (SQLException e1) {
                    nhanVienView.showMessage(e1.toString());
                }
            }
        }
    }

    class UpdateNhanVienListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            NhanVien nv = nhanVienView.getNhanVienInfo();
            if (nv != null) {
                try {
                    dao.update(nv);
                    nhanVienView.showListNhanVien(new NhanVienTableModel(dao.getAll()));
                    nhanVienView.showMessage("Cập nhật thành công!");
                } catch (SQLException e1) {
                    nhanVienView.showMessage(e1.toString());
                }
            }
        }
    }
    
    class SearchNhanVienListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String searchResult = nhanVienView.getSearchInfo();
            if (searchResult.startsWith("ID:")) {
                // A employee was found
                nhanVienView.showMessage(searchResult);
                
                // Extract the employee information from the search result
                String[] parts = searchResult.split(", ");
                int id = Integer.parseInt(parts[0].split(": ")[1]);
                String hoTen = parts[1].split(": ")[1];
                String namSinh = parts[2].split(": ")[1];
                String diaChi = parts[3].split(": ")[1];
                String sdt = parts[4].split(": ")[1];
                String chucVu = parts[5].split(": ")[1];
                
                // Create a NhanVien object with the found information
                NhanVien foundNhanVien = new NhanVien(id, hoTen, namSinh, diaChi, sdt, chucVu);
                
                // Create a new ArrayList with the found NhanVien and update the table
                ArrayList<NhanVien> searchResults = new ArrayList<>();
                searchResults.add(foundNhanVien);
                nhanVienView.showListNhanVien(new NhanVienTableModel(searchResults));
            } else {
                // No employee was found or there was an error
                nhanVienView.showMessage(searchResult);
            }
        }
    }
}
