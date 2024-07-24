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
    private JButton btnThem, btnSua, btnXoa, btnClear;
    private JTextField txtHoTen, txtNamSinh, txtDiaChi, txtSDT, txtChucVu;
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

        add(panel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        btnThem = new JButton("Thêm");
        buttonPanel.add(btnThem);

        btnSua = new JButton("Sửa");
        buttonPanel.add(btnSua);

        btnXoa = new JButton("Xóa");
        buttonPanel.add(btnXoa);

        btnClear = new JButton("Xóa thông tin");
        buttonPanel.add(btnClear);

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

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void setButtonVisibility(boolean isVisible) {
        btnSua.setVisible(isVisible);
        btnXoa.setVisible(isVisible);
    }
}
