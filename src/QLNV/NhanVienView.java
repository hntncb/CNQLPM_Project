package QLNV;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionListener;

public class NhanVienView extends JFrame {

    private JTable table;
    private JButton btnThem, btnSua, btnXoa, btnClear, btnSearch;
    private JTextField txtHoTen, txtNamSinh, txtDiaChi, txtSDT, txtChucVu, txtSearch;
    private NhanVienTableModel model;

    public NhanVienView() {
        setTitle("Quản lý nhân viên");
        setBounds(100, 100, 600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

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
        
        txtSearch = new JTextField();
        panel.add(txtSearch);

        add(panel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        btnThem = new JButton("Thêm");
        buttonPanel.add(btnThem);

        btnSua = new JButton("Sửa");
        buttonPanel.add(btnSua);

        btnXoa = new JButton("Xóa");
        buttonPanel.add(btnXoa);

        btnClear = new JButton("Clear");
        buttonPanel.add(btnClear);
        
        btnSearch = new JButton("Search");
        buttonPanel.add(btnSearch);
        

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void showListNhanVien(NhanVienTableModel model) {
        this.model = model;
        table.setModel(model);
    }

    public NhanVien getNhanVienInfo() {
        String hoTen = txtHoTen.getText();
        String namSinh = txtNamSinh.getText();
        String diaChi = txtDiaChi.getText();
        String sdt = txtSDT.getText();
        String chucVu = txtChucVu.getText();

        if (hoTen.isEmpty() || namSinh.isEmpty() || diaChi.isEmpty() || sdt.isEmpty() || chucVu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin.");
            return null;
        }
        return new NhanVien(hoTen, namSinh, diaChi, sdt, chucVu);
    }
    
    public String getSearchInfo() {
        String hoTen = txtSearch.getText().trim();
        if (hoTen.isEmpty()) {
            return "Vui lòng nhập tên nhân viên cần tìm kiếm.";
        }
        
        for (int i = 0; i < model.getRowCount(); i++) {
            String currentHoTen = (String) model.getValueAt(i, 1); // Assuming hoTen is in the second column (index 1)
            if (currentHoTen.equalsIgnoreCase(hoTen)) {
                // Construct the NhanVien information from the table data
                int id = (int) model.getValueAt(i, 0);
                String namSinh = (String) model.getValueAt(i, 2);
                String diaChi = (String) model.getValueAt(i, 3);
                String sdt = (String) model.getValueAt(i, 4);
                String chucVu = (String) model.getValueAt(i, 5);
                
                return String.format("ID: %d, Họ tên: %s, Năm sinh: %s, Địa chỉ: %s, SĐT: %s, Chức vụ: %s",
                                     id, currentHoTen, namSinh, diaChi, sdt, chucVu);
            }
        }
        return "Không tìm thấy nhân viên với họ tên: " + hoTen;
    }


    public void fillNhanVienFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtHoTen.setText((String) table.getValueAt(row, 1));
            txtNamSinh.setText((String) table.getValueAt(row, 2));
            txtDiaChi.setText((String) table.getValueAt(row, 3));
            txtSDT.setText((String) table.getValueAt(row, 4));
            txtChucVu.setText((String) table.getValueAt(row, 5));
        }
    }

    public void clearNhanVienInfo() {
        txtHoTen.setText("");
        txtNamSinh.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtChucVu.setText("");
    }

    public void addListNhanVienSelectionListener(ListSelectionListener listener) {
        table.getSelectionModel().addListSelectionListener(listener);
    }

    public void addInsertNhanVienListener(ActionListener listener) {
        btnThem.addActionListener(listener);
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

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void setButtonVisibility(boolean isVisible) {
        btnSua.setEnabled(isVisible);
        btnXoa.setEnabled(isVisible);
    }
}
