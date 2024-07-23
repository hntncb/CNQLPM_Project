package QLNV;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionListener;

public class NhanVienView extends JFrame {

    private JButton btnThem, btnSua, btnXoa, btnClear;
    private JTextField txtID, txtHoten, txtNamSinh, txtDiachi, txtSDT, txtChucVu;
    private JScrollPane tblPane;
    private Panel southPane, textPane, buttonPane;
    private JTable table;

    public NhanVienView() {
        initComponents();
        configureLayout();
        configureFrame();
    }

    private void initComponents() {
        table = new JTable();
        buttonPane = new Panel(new FlowLayout());
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnClear = new JButton("Clear");
        buttonPane.add(btnThem);
        buttonPane.add(btnSua);
        buttonPane.add(btnXoa);
        buttonPane.add(btnClear);
        
        textPane = new Panel(new GridLayout(6, 2));
        txtID = new JTextField(15);
        txtHoten = new JTextField(15);
        txtNamSinh = new JTextField(15);
        txtDiachi = new JTextField(15);
        txtSDT = new JTextField(10);
        txtChucVu = new JTextField(15);
        textPane.add(new JLabel("ID:"));
        textPane.add(txtID);
        textPane.add(new JLabel("Họ Tên:"));
        textPane.add(txtHoten);
        textPane.add(new JLabel("Năm Sinh:"));
        textPane.add(txtNamSinh);
        textPane.add(new JLabel("Địa Chỉ:"));
        textPane.add(txtDiachi);
        textPane.add(new JLabel("SDT:"));
        textPane.add(txtSDT);
        textPane.add(new JLabel("Chức Vụ"));
        textPane.add(txtChucVu);
    }

    private void configureLayout() {
        southPane = new Panel(new BorderLayout());
        southPane.add(buttonPane, BorderLayout.NORTH);
        southPane.add(textPane, BorderLayout.CENTER);
        
        tblPane = new JScrollPane(table);
        this.getContentPane().add(tblPane, BorderLayout.CENTER);
        this.getContentPane().add(southPane, BorderLayout.NORTH);
    }

    private void configureFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(700, 300, 800, 600);
    }

    public void showListNhanVien(NhanVienTableModel nhanVienModel) {
        table.setModel(nhanVienModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(350);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
    }

    public void showNhanVien(NhanVien nv) {
        txtID.setText(String.valueOf(nv.getId()));
        txtHoten.setText(nv.getHoTen());
        txtNamSinh.setText(nv.getNamSinh());
        txtDiachi.setText(nv.getDiaChi());
        txtSDT.setText(String.valueOf(nv.getSdt()));
        txtChucVu.setText(nv.getChucVu());
        btnSua.setEnabled(true);
        btnXoa.setEnabled(true);
        btnThem.setEnabled(false);
    }

    public void fillNhanVienFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            txtID.setText(table.getModel().getValueAt(row, 0).toString());
            txtHoten.setText(table.getModel().getValueAt(row, 1).toString());
            txtNamSinh.setText(table.getModel().getValueAt(row, 2).toString());
            txtDiachi.setText(table.getModel().getValueAt(row, 3).toString());
            txtSDT.setText(table.getModel().getValueAt(row, 4).toString());
            txtChucVu.setText(table.getModel().getValueAt(row, 5).toString());
            btnThem.setEnabled(false);
            btnXoa.setEnabled(true);
            btnSua.setEnabled(true);
            btnClear.setEnabled(true);
        }
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    public void clearNhanVienInfo() {
        txtID.setText("");
        txtHoten.setText("");
        txtNamSinh.setText("");
        txtDiachi.setText("");
        txtSDT.setText("");
        txtChucVu.setText("");
        btnXoa.setEnabled(false);
        btnSua.setEnabled(false);
        btnThem.setEnabled(true);
    }

    public NhanVien getNhanVienInfo() {
        try {
            NhanVien nv = new NhanVien();
            if (!txtID.getText().isEmpty()) {
                nv.setId(Integer.parseInt(txtID.getText()));
            }
            nv.setHoTen(txtHoten.getText().trim());
            nv.setNamSinh(txtNamSinh.getText().trim());
            nv.setDiaChi(txtDiachi.getText().trim());
            nv.setDiaChi(txtDiachi.getText().trim());
            nv.setChucVu(txtChucVu.getText().trim());
            return nv;
        } catch (NumberFormatException e) {
            showMessage("Vui lòng nhập đúng định dạng số.");
        } catch (Exception e) {
            showMessage(e.getMessage());
        }
        return null;
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

    public void addListNhanVienSelectionListener(ListSelectionListener listener) {
        table.getSelectionModel().addListSelectionListener(listener);
    }
}
