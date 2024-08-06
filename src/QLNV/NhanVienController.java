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

    class ClearNhanVienListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
        	try {
                nhanVienView.clearNhanVienInfo();
                nhanVienView.showListNhanVien(new NhanVienTableModel(dao.getAll()));
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
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
            try {
                if (nhanVienModel.isShowSelectColumn()) {
                    // Nếu cột "Chọn" được hiển thị, xóa các hàng đã chọn
                    nhanVienModel.removeSelectedRows();
                    nhanVienView.showMessage("Xóa các hàng đã chọn thành công!");
                } else {
                    // Nếu không có cột "Chọn", xóa hàng hiện tại
                    NhanVien nv = nhanVienView.getNhanVienInfo();
                    if (nv != null) {
                        dao.delete(nv);
                        nhanVienView.clearNhanVienInfo();
                        ArrayList<NhanVien> ds = dao.getAll();
                        if (ds != null) {
                            nhanVienView.showListNhanVien(new NhanVienTableModel(ds));
                        } else {
                            nhanVienView.showMessage("Dữ liệu rỗng");
                        }
                        nhanVienView.showMessage("Xóa thành công!");
                    }
                }
                nhanVienView.clearNhanVienInfo();
                nhanVienView.showListNhanVien(new NhanVienTableModel(dao.getAll()));
            } catch (SQLException e1) {
                nhanVienView.showMessage("Đã xảy ra lỗi khi xóa: " + e1.getMessage());
                e1.printStackTrace();
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
            String keyword = nhanVienView.getSearchKeyword(); // Lấy từ khóa tìm kiếm từ View
            try {
                ArrayList<NhanVien> searchResults = dao.search(keyword); // Tìm kiếm nhiều nhân viên
                if (searchResults.isEmpty()) {
                    nhanVienView.showMessage("Không tìm thấy kết quả.");
                } else {
                    nhanVienView.showListNhanVien(new NhanVienTableModel(searchResults)); // Hiển thị kết quả tìm kiếm
                }
            } catch (SQLException ex) {
                nhanVienView.showMessage("Lỗi: " + ex.getMessage());
            }
        }
    }
}
