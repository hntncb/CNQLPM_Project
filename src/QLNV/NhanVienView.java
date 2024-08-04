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
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public class NhanVienView extends JFrame {

	private JTable table;
	private TableRowSorter<NhanVienTableModel> rowSorter;
	private JButton btnThem, btnSua, btnXoa, btnClear, btnSearch, btnInsertByFile,btnSelectColumn;
	private JTextField txtID, txtHoTen, txtNamSinh, txtDiaChi, txtSDT, txtChucVu, txtSearch;
	private NhanVienTableModel model;
	
	public NhanVienView() {
		setTitle("Quản lý nhân viên");
		setSize(1300, 750);
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
		
		btnSelectColumn = new JButton("Chọn nhiều");
        buttonPanel.add(btnSelectColumn);

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
		
		btnSelectColumn.addActionListener(e -> toggleSelectColumn());
		
		ListSelectionModel select = table.getSelectionModel();
		select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		select.addListSelectionListener(new ListSelectionListener() {
		    public void valueChanged(ListSelectionEvent e) {
		        handleTableSelectionChanged(e);
		    }
		});

		add(buttonPanel, BorderLayout.SOUTH);
	}
	private void toggleSelectColumn() {
	    if (model != null) {
	        model.toggleSelectColumn(); // Đảo trạng thái cột chọn trong model
	        setTableColumnWidths(); // Cập nhật kích thước cột
	    }
	}

	public void showListNhanVien(NhanVienTableModel model) {
	    this.model = model;
	    table.setModel(model);
	    rowSorter.setModel(model); // Gán model cho TableRowSorter
	    setTableColumnWidths(); // Cài đặt kích thước cột
	}
	private void handleTableSelectionChanged(ListSelectionEvent e) {
	    if (!e.getValueIsAdjusting()) {
	        int row = table.getSelectedRow();
	        int column = table.getSelectedColumn();
	        if (row != -1 && column == 6) {
	            Object currentData = table.getValueAt(row, column);
	            if (currentData instanceof Boolean) {
	                boolean isTrue = (Boolean) currentData;
	                table.setValueAt(!isTrue, row, 6);
	            } else if (currentData instanceof String) {
	                boolean isTrue = Boolean.parseBoolean((String) currentData);
	                table.setValueAt(!isTrue, row, 6);
	            }
	        }
	    }
	}
	public void setTableColumnWidths() {
	    TableColumnModel columnModel = table.getColumnModel();
	    int columnCount = columnModel.getColumnCount();

	    // Cài đặt kích thước cho các cột khác
	    columnModel.getColumn(0).setPreferredWidth(50); // Cột ID
	    columnModel.getColumn(1).setPreferredWidth(150); // Cột Họ tên
	    columnModel.getColumn(2).setPreferredWidth(100); // Cột Năm sinh
	    columnModel.getColumn(3).setPreferredWidth(300); // Cột Địa chỉ
	    columnModel.getColumn(4).setPreferredWidth(100); // Cột SĐT
	    columnModel.getColumn(5).setPreferredWidth(150); // Cột Chức vụ

	    // Xử lý kích thước cột "Chọn"
	    if (model != null && model.isShowSelectColumn()) {
	        if (columnCount > 6) {
	            columnModel.getColumn(6).setPreferredWidth(50); // Kích thước cột "Chọn" khi hiện
	            columnModel.getColumn(6).setMinWidth(50); // Đặt kích thước tối thiểu
	            columnModel.getColumn(6).setMaxWidth(50); // Đặt kích thước tối đa
	        }
	    } else if (columnCount > 6) {
	        columnModel.getColumn(6).setMinWidth(0);
	        columnModel.getColumn(6).setMaxWidth(0); // Ẩn cột "Chọn"
	    }
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
	    btnXoa.addActionListener(e -> {
	        if (model.isShowSelectColumn()) {
	            model.removeSelectedRows(); // Xóa các hàng được chọn
	        } else {
	            // Sử dụng phương thức xóa hiện tại (có thể là xóa hàng đã chọn)
	            int selectedRow = table.getSelectedRow();
	            if (selectedRow >= 0) {
	                model.removeRow(selectedRow); // Xóa hàng được chọn
	            }
	        }
	    });
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
	private void deleteSelectedRows() {
	    if (model != null && model.isShowSelectColumn()) {
	        // Xóa các hàng có giá trị true ở cột 6
	        int rowCount = table.getRowCount();
	        for (int i = rowCount - 1; i >= 0; i--) {
	            Boolean isSelected = (Boolean) table.getValueAt(i, 6);
	            if (isSelected != null && isSelected) {
	                model.removeRow(i);
	            }
	        }
	    } else {
	        // Thực hiện xóa hàng đã chọn như trước
	        int selectedRow = table.getSelectedRow();
	        if (selectedRow != -1) {
	            model.removeRow(selectedRow);
	        }
	    }
	}

}
