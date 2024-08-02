package QLNV;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

public class NhanVienView extends JFrame {

    private JTable table;
    private TableRowSorter<NhanVienTableModel> rowSorter;
    private JButton btnThem, btnSua, btnXoa, btnClear, btnSearch, btnInsertByFile;
    private JTextField txtID, txtHoTen, txtNamSinh, txtDiaChi, txtSDT, txtChucVu, txtSearch;
    private NhanVienTableModel model;

    public NhanVienView() {
        setTitle("Quản lý nhân viên");
        setBounds(100, 100, 600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2)); // Sửa thành 7 hàng
        
        rowSorter = new TableRowSorter<>();
        table.setRowSorter(rowSorter);

        panel.add(new JLabel("ID:"));
        txtID = new JTextField();
        panel.add(txtID);

        panel.add(new JLabel("Họ tên:"));
        txtHoTen = new JTextField();
        panel.add(txtHoTen);

        panel.add(new JLabel("Năm sinh:"));
        txtNamSinh = new JTextField();
        panel.add(txtNamSinh);

        panel.add(new JLabel("Địa chỉ:"));
        txtDiaChi = new JTextField();
        panel.add(txtDiaChi);

        panel.add(new JLabel("SĐT:"));
        txtSDT = new JTextField();
        panel.add(txtSDT);

        panel.add(new JLabel("Chức vụ:"));
        txtChucVu = new JTextField();
        panel.add(txtChucVu);

        panel.add(new JLabel("Tìm kiếm:")); // Thêm label cho ô tìm kiếm
        txtSearch = new JTextField();
        panel.add(txtSearch);

        add(panel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        btnThem = new JButton("Thêm");
        buttonPanel.add(btnThem);

        btnInsertByFile = new JButton("Chèn File");
        buttonPanel.add(btnInsertByFile);

        btnSua = new JButton("Sửa");
        buttonPanel.add(btnSua);

        btnXoa = new JButton("Xóa");
        buttonPanel.add(btnXoa);

        btnClear = new JButton("Clear");
        buttonPanel.add(btnClear);

        btnSearch = new JButton("Tìm kiếm");
        buttonPanel.add(btnSearch);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showListNhanVien(NhanVienTableModel model) {
        this.model = model;
        table.setModel(model);
        rowSorter.setModel(model); // Gán model cho TableRowSorter
    }

    public NhanVien getNhanVienInfo() {
        try {
            int id = Integer.parseInt(txtID.getText());
            String hoTen = txtHoTen.getText();
            String namSinh = txtNamSinh.getText();
            String diaChi = txtDiaChi.getText();
            String sdt = txtSDT.getText();
            String chucVu = txtChucVu.getText();

            if (hoTen.isEmpty() || namSinh.isEmpty() || diaChi.isEmpty() || sdt.isEmpty() || chucVu.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.");
                return null;
            }
            return new NhanVien(id, hoTen, namSinh, diaChi, sdt, chucVu);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID phải là một số nguyên.");
            return null;
        }
    }

    public void fillNhanVienFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtID.setText(String.valueOf(model.getValueAt(row, 0)));
            txtHoTen.setText((String) model.getValueAt(row, 1));
            txtNamSinh.setText((String) model.getValueAt(row, 2));
            txtDiaChi.setText((String) model.getValueAt(row, 3));
            txtSDT.setText((String) model.getValueAt(row, 4));
            txtChucVu.setText((String) model.getValueAt(row, 5));
        }
    }

    public void clearNhanVienInfo() {
        txtID.setText("");
        txtHoTen.setText("");
        txtNamSinh.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtChucVu.setText("");
        txtSearch.setText("");
    }

    public void addListNhanVienSelectionListener(ListSelectionListener listener) {
        table.getSelectionModel().addListSelectionListener(listener);
    }

    public void addInsertNhanVienListener(ActionListener listener) {
        btnThem.addActionListener(listener);
    }

    public void addInsertFileNhanVienListener(ActionListener listener) {
        btnInsertByFile.addActionListener(listener);
    }

    public void addUpdateNhanVienListener(ActionListener listener) {
        btnSua.addActionListener(listener);
    }

    public void addDeleteNhanVienListener(ActionListener listener) {
        btnXoa.addActionListener(listener);
    }

    public void addClearNhanVienListener(ActionListener listener) {
        btnClear.addActionListener(listener);
    }

    public void addSearchNhanVienListener(ActionListener listener) {
        btnSearch.addActionListener(listener);
    }
    
    public void showSearchResults(ArrayList<NhanVien> results) {
        NhanVienTableModel searchModel = new NhanVienTableModel(results);
        showListNhanVien(searchModel);
    }

    public String getSearchKeyword() {
        return txtSearch.getText().trim();
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void setButtonVisibility(boolean isVisible) {
        btnSua.setEnabled(isVisible);
        btnXoa.setEnabled(isVisible);
        btnInsertByFile.setEnabled(isVisible);
    }
}
