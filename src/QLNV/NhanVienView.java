package QLNV;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

public class NhanVienView extends JFrame {

    private JTable table;
    private TableRowSorter<NhanVienTableModel> rowSorter;
    private JButton btnThem, btnSua, btnXoa, btnClear, btnSearch, btnInsertByFile,btnSelectList,btnNext, btnPrevious;
    private JTextField txtID, txtHoTen, txtNamSinh, txtDiaChi, txtSDT, txtChucVu, txtSearch;
    private NhanVienTableModel model;
    private JLabel lblPageInfo;

    public NhanVienView() {
        setTitle("Quản lý nhân viên");
        setSize(1300, 730);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));
        
        rowSorter = new TableRowSorter<>();
        table.setRowSorter(rowSorter);

        //panel.add(new JLabel("ID:"));
        txtID = new JTextField();
        //panel.add(txtID);

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

        panel.add(new JLabel("Tìm kiếm:"));
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
        
        btnSelectList = new JButton("Lấy danh sách chọn");
        //buttonPanel.add(btnSelectList);
        
        JPanel paginationPanel = new JPanel();
        btnPrevious = new JButton("Trước");
        btnNext = new JButton("Sau");
        lblPageInfo = new JLabel("Page 1 of 1");
        paginationPanel.add(btnPrevious);
        paginationPanel.add(lblPageInfo);
        paginationPanel.add(btnNext);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2)); // Tạo một panel với 1 hàng, 2 cột
        mainPanel.add(paginationPanel);
        mainPanel.add(buttonPanel);
        add(mainPanel, BorderLayout.SOUTH);
        setupTextFieldValidation();
    }
    private void setupTextFieldValidation() {
        addTextFieldValidator(txtHoTen, TextFieldValidator::isChar);
        addTextFieldValidator(txtChucVu, TextFieldValidator::isChar);
        addTextFieldValidator(txtDiaChi, TextFieldValidator::isChar);
        addTextFieldValidator(txtNamSinh, input -> TextFieldValidator.isDate(input) || input.isEmpty());
        addTextFieldValidator(txtSDT, input -> TextFieldValidator.isPhone(input) && TextFieldValidator.charLimit(input, 11));
        addTextFieldValidator(txtSearch, TextFieldValidator::isAlphanumeric);
    }
    private void addTextFieldValidator(JTextField textField, java.util.function.Predicate<String> validator) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                validate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                validate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                validate();
            }

//            private void validate() {
//                SwingUtilities.invokeLater(() -> {
//                    String text = textField.getText();
//                    if (!validator.test(text)) {
//                        textField.setForeground(Color.RED);
//                    } else {
//                        textField.setForeground(Color.BLACK);
//                    }
//                });
//            }
        });
    }

    public void showListNhanVien(NhanVienTableModel model) {
        this.model = model;
        table.setModel(model);
        rowSorter.setModel(model);
        table.getColumnModel().getColumn(6).setCellRenderer(table.getDefaultRenderer(Boolean.class));
        table.getColumnModel().getColumn(6).setCellEditor(table.getDefaultEditor(Boolean.class));
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

            if (!TextFieldValidator.isChar(hoTen) || !TextFieldValidator.isChar(chucVu)) {
                JOptionPane.showMessageDialog(this, "Họ tên và Chức vụ chỉ được chứa ký tự chữ cái.");
                return null;
            }

            if (!TextFieldValidator.isAlphanumeric(diaChi)) {
                JOptionPane.showMessageDialog(this, "Địa chỉ chỉ được chứa ký tự chữ cái và số.");
                return null;
            }

            if (!TextFieldValidator.isDate(namSinh)) {
                JOptionPane.showMessageDialog(this, "Năm sinh phải có định dạng YYYY-MM-DD.");
                return null;
            }

            if (!TextFieldValidator.isPhone(sdt) || !TextFieldValidator.charLimit(sdt, 11)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại phải là số và không quá 11 ký tự.");
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
    
    public void updatePageInfo(int currentPage, int totalPages) {
        lblPageInfo.setText("Page " + (currentPage + 1) + " of " + totalPages);
    }

    public void addNextPageListener(ActionListener listener) {
        btnNext.addActionListener(listener);
    }

    public void addPreviousPageListener(ActionListener listener) {
        btnPrevious.addActionListener(listener);
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
    
    public void addSelectListListener(ActionListener listener) {
        btnSelectList.addActionListener(listener);
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
