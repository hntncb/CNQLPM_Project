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
        nhanVienView.addSelectListListener(new SelectListListener());
        nhanVienView.addNextPageListener(new NextPageListener());
        nhanVienView.addPreviousPageListener(new PreviousPageListener());
        nhanVienView.setButtonVisibility(userType == UserType.ADMIN);
        nhanVienView.setVisible(true);
        nhanVienView.setEnabled(true);
        updatePageInfo();
    }
    
    private void updatePageInfo() {
        nhanVienView.updatePageInfo(nhanVienModel.getCurrentPage(), nhanVienModel.getTotalPages());
    }

    class NextPageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            nhanVienModel.nextPage();
            updatePageInfo();
        }
    }

    class PreviousPageListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            nhanVienModel.previousPage();
            updatePageInfo();
        }
    }

    private void refreshTableData() throws SQLException {
        ArrayList<NhanVien> allNhanViens = dao.getAll();
        nhanVienModel.setNhanViens(allNhanViens);
        nhanVienView.showListNhanVien(nhanVienModel);
        updatePageInfo();
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
            try {
                nhanVienView.clearNhanVienInfo();
                refreshTableData();
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
                    refreshTableData();
                    nhanVienView.clearNhanVienInfo();
                    nhanVienView.showMessage("Thêm thành công!");
                } catch (SQLException e1) {
                    nhanVienView.showMessage("Trùng ID");
                }
            }
        }
    }

    class DeleteNhanVienListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<NhanVien> selectedNhanViens = nhanVienModel.getSelectedNhanViens();
            if (!selectedNhanViens.isEmpty()) {
                try {
                    for (NhanVien nv : selectedNhanViens) {
                        dao.delete(nv);
                        nhanVienModel.removeNhanVien(nv);
                    }
                    nhanVienView.clearNhanVienInfo();
                    nhanVienView.showMessage("Xóa thành công!");
                    refreshTableData();
                } catch (SQLException ex) {
                    nhanVienView.showMessage("Lỗi: " + ex.toString());
                }
            } else {
                nhanVienView.showMessage("Vui lòng chọn ít nhất một nhân viên để xóa.");
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
                    nhanVienView.showMessage("Thêm dữ liệu từ file thành công!");
                } catch (SQLException | IOException ex) {
                    nhanVienView.showMessage("Lỗi: " + ex.getMessage());
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
                    refreshTableData();
                } catch (SQLException e1) {
                    nhanVienView.showMessage(e1.toString());
                }
            }
        }
    }
    
    class SearchNhanVienListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String keyword = nhanVienView.getSearchKeyword();
            try {
                ArrayList<NhanVien> searchResults = dao.search(keyword);
                if (searchResults.isEmpty()) {
                    nhanVienView.showMessage("Không tìm thấy kết quả.");
                } else {
                    nhanVienView.showListNhanVien(new NhanVienTableModel(searchResults));
                }
            } catch (SQLException ex) {
                nhanVienView.showMessage("Lỗi: " + ex.getMessage());
            }
        }
    }
    
    class SelectListListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<NhanVien> selected = nhanVienModel.getSelectedNhanViens();
            System.out.println("Nhân viên được chọn:"+selected);
            if (selected.isEmpty()) {
                nhanVienView.showMessage("Không có nhân viên nào được chọn.");
            } else {
                System.out.println(selected);
            }
        }
    }
}
