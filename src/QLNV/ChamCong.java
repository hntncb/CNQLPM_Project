package QLNV;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class ChamCong extends JFrame {
	private DefaultTableModel tableModel;
	private JComboBox<Integer> monthComboBox, yearComboBox;
	private JTable table;
	private JButton save_btn, prevBtn, nextBtn, quydinhBtn;
	private JLabel pageInfo;
	private int currentPage = 1, rowsPerPage = 40, totalPages;
	private UserType userType;

	public ChamCong(UserType userType) {
		this.userType = userType;
		save_btn = new JButton("Lưu");
		prevBtn = new JButton("Trước");
		nextBtn = new JButton("Sau");
		pageInfo = new JLabel();
		quydinhBtn = new JButton("Quy định chấm công");
		quydinhBtn.addActionListener(e -> showQuyDinh());
		setTitle("Quản lý chấm công");
		monthComboBox = new JComboBox<>();
		yearComboBox = new JComboBox<>();
		for (int i = 1; i <= 12; i++) {
			monthComboBox.addItem(i);
		}
		for (int i = 2020; i <= 2040; i++) {
			yearComboBox.addItem(i);
		}
		setSize(1360, 730);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());

		Calendar now = Calendar.getInstance();
		int currentMonth = now.get(Calendar.MONTH) + 1;
		int currentYear = now.get(Calendar.YEAR);
		monthComboBox.setSelectedItem(currentMonth);
		yearComboBox.setSelectedItem(currentYear);
		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel("Tháng:"));
		topPanel.add(monthComboBox);
		topPanel.add(new JLabel("Năm:"));
		topPanel.add(yearComboBox);
		topPanel.add(save_btn);
		topPanel.add(quydinhBtn);

		String[] columnNames = { "ID", "Tên nhân viên", "Tổng giờ làm" };
		tableModel = new DefaultTableModel(columnNames, 0);
		table = new JTable(tableModel);

		table.setDefaultRenderer(Object.class, new CustomCellRenderer());

		JScrollPane tableScrollPane = new JScrollPane(table);
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(prevBtn);
		bottomPanel.add(pageInfo);
		bottomPanel.add(nextBtn);

		add(topPanel, BorderLayout.NORTH);
		add(tableScrollPane, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		save_btn.addActionListener(e -> {
			if (table.getCellEditor() != null) {
				table.getCellEditor().stopCellEditing();
			}
			updateDatabase();
			updateTable();
		});

		prevBtn.addActionListener(e -> {
			if (currentPage > 1) {
				currentPage--;
				updateTable();
			}
		});

		nextBtn.addActionListener(e -> {
			if (currentPage < totalPages) {
				currentPage++;
				updateTable();
			}
		});

		monthComboBox.addActionListener(e -> {
			setupDatabase();
			updateTable();
		});
		yearComboBox.addActionListener(e -> {
			setupDatabase();
			updateTable();
		});

		setupDatabase();
		updateTable();

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				table.repaint();
			}
		});
	}

	private class CustomCellRenderer extends DefaultTableCellRenderer {
		private final Calendar today = Calendar.getInstance();

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			int todayDay = today.get(Calendar.DAY_OF_MONTH);
			int todayMonth = today.get(Calendar.MONTH) + 1;
			int todayYear = today.get(Calendar.YEAR);

			int selectedRow = table.getSelectedRow();
			int month = (int) monthComboBox.getSelectedItem();
			int year = (int) yearComboBox.getSelectedItem();

			cell.setBackground(Color.LIGHT_GRAY);
			cell.setForeground(Color.BLACK);

			if (column == todayDay + 2 && month == todayMonth && year == todayYear) {
				cell.setBackground(Color.WHITE);
			}

			if (selectedRow == row) {
				cell.setBackground(Color.WHITE);
				cell.setForeground(Color.BLACK);
			}

			return cell;
		}
	}

	private void showQuyDinh() {
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader("src/resources/quydinhchamcong.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line).append("\n");
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Không thể đọc file quy định chấm công!", "Lỗi",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		JOptionPane.showMessageDialog(this, content.toString(), "Quy định chấm công", JOptionPane.INFORMATION_MESSAGE);
	}

	private void setupDatabase() {
		int month = (int) monthComboBox.getSelectedItem();
		int year = (int) yearComboBox.getSelectedItem();
		Connection connection = null;

		try {
			connection = ConnectionFactory.getInstance().getConnection();
			String selectSql = "SELECT COUNT(*) FROM chamcong WHERE id_nhanvien = ? AND YEAR(ngay) = ? AND MONTH(ngay) = ? AND DAY(ngay) = ?";
			PreparedStatement selectStatement = connection.prepareStatement(selectSql);
			String insertSql = "INSERT INTO chamcong (id_nhanvien, ngay, giolam) VALUES (?, ?, ?)";
			PreparedStatement insertStatement = connection.prepareStatement(insertSql);

			java.sql.Date firstDayOfMonth = java.sql.Date.valueOf(String.format("%d-%02d-01", year, month));
			String employeeSql = "SELECT ID FROM thong_tin_nhan_vien";
			PreparedStatement employeeStatement = connection.prepareStatement(employeeSql);
			ResultSet employeeResultSet = employeeStatement.executeQuery();

			while (employeeResultSet.next()) {
				int employeeId = employeeResultSet.getInt("ID");
				selectStatement.setInt(1, employeeId);
				selectStatement.setInt(2, year);
				selectStatement.setInt(3, month);
				selectStatement.setInt(4, 1);

				ResultSet resultSet = selectStatement.executeQuery();
				resultSet.next();
				int count = resultSet.getInt(1);

				if (count == 0) {
					insertStatement.setInt(1, employeeId);
					insertStatement.setDate(2, firstDayOfMonth);
					insertStatement.setInt(3, 0);
					insertStatement.addBatch();
				}
			}
			insertStatement.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void updateDatabase() {
	    int month = (int) monthComboBox.getSelectedItem();
	    int year = (int) yearComboBox.getSelectedItem();
	    Connection connection = null;

	    try {
	        connection = ConnectionFactory.getInstance().getConnection();
	        connection.setAutoCommit(false);

	        boolean success = updateEmployeeData(connection, year, month);

	        if (success) {
	            connection.commit();
	            showMessage("đã Cập nhật dữ liệu", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	        } else {
	            connection.rollback();
	            showMessage("Không có dữ liệu để cập nhật.", "Thông báo", JOptionPane.WARNING_MESSAGE);
	        }

	    } catch (SQLException e) {
	        handleSQLException(e, connection);
	    } finally {
	        closeConnection(connection);
	    }
	}

	private boolean updateEmployeeData(Connection connection, int year, int month) throws SQLException {
	    Calendar now = Calendar.getInstance();
	    int currentDay = now.get(Calendar.DAY_OF_MONTH);
	    int currentMonth = now.get(Calendar.MONTH) + 1;
	    int currentYear = now.get(Calendar.YEAR);

	    String checkSql = "SELECT giolam FROM chamcong WHERE id_nhanvien = ? AND YEAR(ngay) = ? AND MONTH(ngay) = ? AND DAY(ngay) = ?";
	    String updateSql = "UPDATE chamcong SET giolam = ? WHERE id_nhanvien = ? AND YEAR(ngay) = ? AND MONTH(ngay) = ? AND DAY(ngay) = ?";
	    String insertSql = "INSERT INTO chamcong (id_nhanvien, ngay, giolam) VALUES (?, ?, ?)";

	    boolean invalidEditAttempt = false;
	    boolean invalidInputAttempt = false;

	    try (PreparedStatement checkStatement = connection.prepareStatement(checkSql);
	         PreparedStatement updateStatement = connection.prepareStatement(updateSql);
	         PreparedStatement insertStatement = connection.prepareStatement(insertSql)) {

	        for (int row = 0; row < tableModel.getRowCount(); row++) {
	            int employeeId = (int) tableModel.getValueAt(row, 0);

	            for (int col = 3; col < tableModel.getColumnCount(); col++) {
	                int day = col - 2;
	                Object value = tableModel.getValueAt(row, col);
	                String hoursWorked = value != null ? value.toString() : "";

	                boolean isEditable = (userType == UserType.ADMIN) || 
	                    (userType == UserType.USER && (day == currentDay && month == currentMonth && year == currentYear));

	                if (isEditable) {
	                    if (!hoursWorked.isEmpty()) {
	                        checkStatement.setInt(1, employeeId);
	                        checkStatement.setInt(2, year);
	                        checkStatement.setInt(3, month);
	                        checkStatement.setInt(4, day);
	                        ResultSet checkResult = checkStatement.executeQuery();

	                        if (checkResult.next()) {
	                            updateStatement.setString(1, hoursWorked);
	                            updateStatement.setInt(2, employeeId);
	                            updateStatement.setInt(3, year);
	                            updateStatement.setInt(4, month);
	                            updateStatement.setInt(5, day);
	                            updateStatement.addBatch();
	                        } else {
	                            java.sql.Date date = java.sql.Date.valueOf(String.format("%d-%02d-%02d", year, month, day));
	                            insertStatement.setInt(1, employeeId);
	                            insertStatement.setDate(2, date);
	                            insertStatement.setString(3, hoursWorked);
	                            insertStatement.addBatch();
	                        }
	                    }
	                } else if (userType == UserType.USER) {
	                    invalidEditAttempt = true;
	                }

	                if (!isValidInput(hoursWorked)) {
	                    invalidInputAttempt = true;
	                }
	            }
	        }

	        updateStatement.executeBatch();
	        insertStatement.executeBatch();

	        return !invalidInputAttempt;

	    } catch (SQLException e) {
	        throw new SQLException("Lỗi khi cập nhật dữ liệu", e);
	    }
	}

	private void handleSQLException(SQLException e, Connection connection) {
	    e.printStackTrace();
	    showMessage("Có lỗi xảy ra khi cập nhật dữ liệu.", "Lỗi", JOptionPane.ERROR_MESSAGE);
	    try {
	        if (connection != null) {
	            connection.rollback();
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	private void closeConnection(Connection connection) {
	    if (connection != null) {
	        try {
	            connection.setAutoCommit(true);
	            connection.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	private void showMessage(String message, String title, int messageType) {
	    JOptionPane.showMessageDialog(this, message, title, messageType);
	}

	private boolean isValidInput(String input) {
	    if (input == null || input.trim().isEmpty()) {
	        return true;
	    }

	    try {
	        int hours = Integer.parseInt(input);
	        return hours >= 0 && hours <= 24;
	    } catch (NumberFormatException e) {
	        return input.matches("H|Nb|NL|Co|K|Ts|N|T|C|P");
	    }
	}

	private void updateTable() {
		int month = (int) monthComboBox.getSelectedItem();
		int year = (int) yearComboBox.getSelectedItem();
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		tableModel.setRowCount(0);

		// Cập nhật số cột ngày
		while (tableModel.getColumnCount() > 3) {
			tableModel.setColumnCount(3);
		}

		if (tableModel.getColumnCount() <= 3) {
			for (int i = 1; i <= daysInMonth; i++) {
				if (tableModel.getColumnCount() <= 3 + i - 1) {
					tableModel.addColumn(i);
				}
			}
		}

		Connection connection = null;
		try {
			connection = ConnectionFactory.getInstance().getConnection();

			// Truy vấn số lượng hàng
			String countSql = "SELECT COUNT(DISTINCT t.ID) FROM thong_tin_nhan_vien t "
					+ "LEFT JOIN chamcong c ON t.ID = c.id_nhanvien " + "WHERE YEAR(c.ngay) = ? AND MONTH(c.ngay) = ?";
			PreparedStatement countStatement = connection.prepareStatement(countSql);
			countStatement.setInt(1, year);
			countStatement.setInt(2, month);
			ResultSet countResultSet = countStatement.executeQuery();
			countResultSet.next();
			int totalRows = countResultSet.getInt(1);
			totalPages = (int) Math.ceil(totalRows / (double) rowsPerPage);

			// Truy vấn dữ liệu nhân viên và tổng số giờ làm
			String sql = "SELECT t.ID, t.HoTen, SUM(c.giolam) AS TongGioLam FROM thong_tin_nhan_vien t "
					+ "LEFT JOIN chamcong c ON t.ID = c.id_nhanvien " + "WHERE YEAR(c.ngay) = ? AND MONTH(c.ngay) = ? "
					+ "GROUP BY t.ID, t.HoTen LIMIT ? OFFSET ?";
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setInt(1, year);
			statement.setInt(2, month);
			statement.setInt(3, rowsPerPage);
			statement.setInt(4, (currentPage - 1) * rowsPerPage);
			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Object[] rowData = new Object[tableModel.getColumnCount()];
				rowData[0] = resultSet.getInt("ID");
				rowData[1] = resultSet.getString("HoTen");
				rowData[2] = resultSet.getString("TongGioLam");

				for (int i = 3; i < rowData.length; i++) {
					rowData[i] = "";
				}

				tableModel.addRow(rowData);
			}

			// Truy vấn chi tiết ngày
			String dayDetailsSql = "SELECT id_nhanvien, DAY(ngay) AS Ngay, giolam FROM chamcong "
					+ "WHERE YEAR(ngay) = ? AND MONTH(ngay) = ?";
			PreparedStatement dayDetailsStatement = connection.prepareStatement(dayDetailsSql);
			dayDetailsStatement.setInt(1, year);
			dayDetailsStatement.setInt(2, month);
			ResultSet dayDetailsResultSet = dayDetailsStatement.executeQuery();

			while (dayDetailsResultSet.next()) {
				int employeeId = dayDetailsResultSet.getInt("id_nhanvien");
				int day = dayDetailsResultSet.getInt("Ngay");
				String hoursWorked = dayDetailsResultSet.getString("giolam");

				for (int row = 0; row < tableModel.getRowCount(); row++) {
					if (tableModel.getValueAt(row, 0).equals(employeeId)) {
						if (day >= 1 && day <= daysInMonth) {
							tableModel.setValueAt(hoursWorked, row, 3 + day - 1);
						}
						break;
					}
				}
			}

			pageInfo.setText("Trang " + currentPage + " / " + totalPages);

			TableColumn idColumn = table.getColumnModel().getColumn(0);
			TableColumn nameColumn = table.getColumnModel().getColumn(1);
			TableColumn totalHoursColumn = table.getColumnModel().getColumn(2);

			idColumn.setPreferredWidth(50);
			nameColumn.setPreferredWidth(150);
			totalHoursColumn.setPreferredWidth(100);

			for (int i = 3; i < table.getColumnCount(); i++) {
				table.getColumnModel().getColumn(i).setPreferredWidth(25);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
