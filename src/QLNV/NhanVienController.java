package QLNV;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NhanVienController {

    private final NhanVienImplDAO dao;
    private final NhanVienTableModel nhanVienModel;
    private final NhanVienView nhanVienView;
    private UserType userType;

    public NhanVienController(NhanVienView nhanVienView, NhanVienTableModel nhanVienModel) {
        this.nhanVienView = nhanVienView;
        this.nhanVienModel = nhanVienModel;
        this.dao = new NhanVienImplDAO();
        nhanVienView.addInsertFileNhanVienListener(new InsertFileNhanVienListener());
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
    
    class InsertFileNhanVienListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    dao.insertByFile(selectedFile.getAbsolutePath());
                    nhanVienView.showListNhanVien(new NhanVienTableModel(dao.getAll()));
                    nhanVienView.showMessage("Chèn dữ liệu từ file thành công!");
                } catch (SQLException | IOException ex) {
                    nhanVienView.showMessage("Lỗi: " + ex.getMessage());
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
            NhanVien foundNhanVien = nhanVienView.getSearchInfo();
            if (foundNhanVien != null) {
                // Hiển thị thông báo với thông tin nhân viên
                String searchResult = String.format("ID: %d, Họ tên: %s, Năm sinh: %s, Địa chỉ: %s, SĐT: %s, Chức vụ: %s",
                        foundNhanVien.getId(), foundNhanVien.getHoTen(), foundNhanVien.getNamSinh(),
                        foundNhanVien.getDiaChi(), foundNhanVien.getSdt(), foundNhanVien.getChucVu());
                nhanVienView.showMessage(searchResult);
                
                // Cập nhật bảng hiển thị với thông tin nhân viên đã tìm thấy
                ArrayList<NhanVien> searchResults = new ArrayList<>();
                searchResults.add(foundNhanVien);
                nhanVienView.showListNhanVien(new NhanVienTableModel(searchResults));
            } else {
                // Nhân viên không được tìm thấy hoặc có lỗi
                // Thông báo đã được hiển thị trong phương thức getSearchInfo
            }
        }
    }

}
