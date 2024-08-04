package QLNV;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class ChamCong extends JFrame {
    private DefaultTableModel tableModel;
    private JComboBox<Integer> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private JTable table;
    private JButton save_btn;

    public ChamCong() {
        save_btn = new JButton("Lưu"); 
        setTitle("Quản lý chấm công");
        monthComboBox = new JComboBox<>();
        yearComboBox = new JComboBox<>();
        for (int i = 1; i <= 12; i++) {
            monthComboBox.addItem(i);
        }
        for (int i = 2020; i <= 2040; i++) {
            yearComboBox.addItem(i);
        }
        setSize(1360, 768);
        setDefaultCloseOperation(ChamCong.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Đặt giá trị mặc định của tháng và năm theo thời gian hiện tại
        Calendar now = Calendar.getInstance();
        int currentMonth = now.get(Calendar.MONTH) + 1; // Calendar.MONTH là 0-based, cần cộng 1
        int currentYear = now.get(Calendar.YEAR);
        monthComboBox.setSelectedItem(currentMonth);
        yearComboBox.setSelectedItem(currentYear);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Tháng:"));
        topPanel.add(monthComboBox);
        topPanel.add(new JLabel("Năm:"));
        topPanel.add(yearComboBox);
        topPanel.add(save_btn);

        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText("- Số: Thời gian làm việc tính lương.\n" +
                                 "- H: Thời gian tham gia hội nghị, đào tạo.\n" +
                                 "- Nb: Nghỉ bù.\n" +
                                 "- NL: Nghỉ lễ.\n" +
                                 "- Co: Nghỉ chăm sóc con ốm.\n" +
                                 "- K: Nghỉ không lương.\n" +
                                 "- Ts: Thai sản.\n" +
                                 "- N: Ngừng việc.\n" +
                                 "- T: Tai nạn.\n" +
                                 "- C: Lao động nghĩa vụ.\n" +
                                 "- P: Nghỉ phép đã duyệt.");
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false); // Đặt nền của JTextArea thành trong suốt
        descriptionArea.setFocusable(false); // Ngăn người dùng chọn hoặc click vào JTextArea

        // Đặt viền thành trong suốt
        descriptionArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setOpaque(false); // Đặt nền của JScrollPane thành trong suốt
        scrollPane.getViewport().setOpaque(false); // Đặt nền của viewport thành trong suốt
        scrollPane.setBorder(BorderFactory.createEmptyBorder()); 
        save_btn.addActionListener(e -> {
            TableCellEditor editor = table.getCellEditor();
            if (editor != null) {
                editor.stopCellEditing();
            }
            updateDatabase();
            updateTable();
        });

        String[] columnNames = {"ID", "Tên nhân viên", "Tổng giờ làm"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);

        JScrollPane scrollPane2 = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane2, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Cập nhật bảng khi chọn tháng hoặc năm
        monthComboBox.addActionListener(e -> {
            setupDatabase();
            updateTable();
        });
        yearComboBox.addActionListener(e -> {
            setupDatabase();
            updateTable();
        });

        // Khởi tạo bảng với tháng và năm hiện tại
        setupDatabase();
        updateTable();
    }
    
    private void setupDatabase() {
        int month = (int) monthComboBox.getSelectedItem();
        int year = (int) yearComboBox.getSelectedItem();
        Connection connection = null;

        try {
            connection = ConnectionFactory.getInstance().getConnection();

            // Câu lệnh kiểm tra sự tồn tại của bản ghi
            String selectSql = "SELECT COUNT(*) FROM chamcong WHERE id_nhanvien = ? AND YEAR(ngay) = ? AND MONTH(ngay) = ? AND DAY(ngay) = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);

            // Câu lệnh chèn bản ghi mới
            String insertSql = "INSERT INTO chamcong (id_nhanvien, ngay, giolam) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);

            // Ngày đầu tiên của tháng
            java.sql.Date firstDayOfMonth = java.sql.Date.valueOf(String.format("%d-%02d-01", year, month));

            // Lấy danh sách nhân viên
            String employeeSql = "SELECT ID FROM thong_tin_nhan_vien";
            PreparedStatement employeeStatement = connection.prepareStatement(employeeSql);
            ResultSet employeeResultSet = employeeStatement.executeQuery();

            while (employeeResultSet.next()) {
                int employeeId = employeeResultSet.getInt("ID");

                // Kiểm tra sự tồn tại của bản ghi
                selectStatement.setInt(1, employeeId);
                selectStatement.setInt(2, year);
                selectStatement.setInt(3, month);
                selectStatement.setInt(4, 1); // Ngày 1 của tháng

                ResultSet resultSet = selectStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count == 0) {
                    // Nếu bản ghi không tồn tại, chèn bản ghi mới với giá trị giolam = 0
                    insertStatement.setInt(1, employeeId);
                    insertStatement.setDate(2, firstDayOfMonth);
                    insertStatement.setInt(3, 0); // Giá trị giolam mặc định là 0
                    insertStatement.addBatch();
                }
            }

            // Thực hiện tất cả các lệnh chèn
            insertStatement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng kết nối để tránh rò rỉ tài nguyên
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

            // Câu lệnh kiểm tra sự tồn tại của bản ghi
            String selectSql = "SELECT COUNT(*) FROM chamcong WHERE id_nhanvien = ? AND YEAR(ngay) = ? AND MONTH(ngay) = ? AND DAY(ngay) = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectSql);

            // Câu lệnh chèn bản ghi mới
            String insertSql = "INSERT INTO chamcong (id_nhanvien, ngay, giolam) VALUES (?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertSql);

            // Câu lệnh cập nhật giá trị
            String updateSql = "UPDATE chamcong SET giolam = ? WHERE id_nhanvien = ? AND YEAR(ngay) = ? AND MONTH(ngay) = ? AND DAY(ngay) = ?";
            PreparedStatement updateStatement = connection.prepareStatement(updateSql);

            // Duyệt qua tất cả các hàng và cột trong bảng
            for (int row = 0; row < tableModel.getRowCount(); row++) {
                int employeeId = (int) tableModel.getValueAt(row, 0);

                for (int col = 3; col < tableModel.getColumnCount(); col++) {
                    int day = col - 2; // Cột ngày bắt đầu từ cột 3
                    Object value = tableModel.getValueAt(row, col);

                    String hoursWorked = value != null ? value.toString() : "";

                    // Kiểm tra sự tồn tại của bản ghi
                    selectStatement.setInt(1, employeeId);
                    selectStatement.setInt(2, year);
                    selectStatement.setInt(3, month);
                    selectStatement.setInt(4, day);

                    ResultSet resultSet = selectStatement.executeQuery();
                    resultSet.next();
                    int count = resultSet.getInt(1);

                    if (count == 0) {
                        // Nếu bản ghi không tồn tại, chèn bản ghi mới với giá trị giolam = ''
                        insertStatement.setInt(1, employeeId);
                        insertStatement.setDate(2, java.sql.Date.valueOf(String.format("%d-%02d-%02d", year, month, day)));
                        insertStatement.setString(3, ""); // Giá trị giolam mặc định là chuỗi rỗng
                        insertStatement.addBatch();
                    }

                    // Cập nhật giá trị vào cơ sở dữ liệu
                    updateStatement.setString(1, hoursWorked);
                    updateStatement.setInt(2, employeeId);
                    updateStatement.setInt(3, year);
                    updateStatement.setInt(4, month);
                    updateStatement.setInt(5, day);

                    updateStatement.addBatch(); // Thực hiện theo lô để cải thiện hiệu suất
                }
            }

            // Thực hiện tất cả các lệnh chèn
            insertStatement.executeBatch();
            // Thực hiện tất cả các lệnh cập nhật
            updateStatement.executeBatch();

            // Hiển thị thông báo thành công
            JOptionPane.showMessageDialog(this, "Dữ liệu đã được cập nhật thành công!");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đảm bảo đóng kết nối để tránh rò rỉ tài nguyên
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateTable() {
        int month = (int) monthComboBox.getSelectedItem();
        int year = (int) yearComboBox.getSelectedItem();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Xóa tất cả các hàng cũ
        tableModel.setRowCount(0);

        // Đảm bảo số cột không thay đổi nếu không có thêm ngày
        while (tableModel.getColumnCount() > 3) {
            tableModel.setColumnCount(3); // Giữ lại 3 cột cố định
        }

        // Thêm cột ngày nếu chưa có
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
            String sql = "SELECT t.ID, t.HoTen, SUM(c.giolam) AS TongGioLam " +
                         "FROM thong_tin_nhan_vien t " +
                         "LEFT JOIN chamcong c ON t.ID = c.id_nhanvien " +
                         "WHERE YEAR(c.ngay) = ? AND MONTH(c.ngay) = ? " +
                         "GROUP BY t.ID, t.HoTen";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, year);
            statement.setInt(2, month);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Object[] rowData = new Object[tableModel.getColumnCount()];
                rowData[0] = resultSet.getInt("ID");
                rowData[1] = resultSet.getString("HoTen");
                rowData[2] = resultSet.getString("TongGioLam");

                // Khởi tạo tất cả các giá trị ngày là chuỗi rỗng
                for (int i = 3; i < rowData.length; i++) {
                    rowData[i] = ""; // Giá trị giờ làm cho các ngày
                }

                // Thêm hàng vào bảng
                tableModel.addRow(rowData);
            }

            // Lấy dữ liệu chi tiết giờ làm cho từng ngày
            String dayDetailsSql = "SELECT id_nhanvien, DAY(ngay) AS Ngay, giolam " +
                                   "FROM chamcong " +
                                   "WHERE YEAR(ngay) = ? AND MONTH(ngay) = ?";
            PreparedStatement dayDetailsStatement = connection.prepareStatement(dayDetailsSql);
            dayDetailsStatement.setInt(1, year);
            dayDetailsStatement.setInt(2, month);

            ResultSet dayDetailsResultSet = dayDetailsStatement.executeQuery();
            while (dayDetailsResultSet.next()) {
                int employeeId = dayDetailsResultSet.getInt("id_nhanvien");
                int day = dayDetailsResultSet.getInt("Ngay");
                String hoursWorked = dayDetailsResultSet.getString("giolam");

                // Tìm hàng tương ứng với nhân viên và cập nhật giá trị giờ làm
                for (int row = 0; row < tableModel.getRowCount(); row++) {
                    if (tableModel.getValueAt(row, 0).equals(employeeId)) {
                        if (day + 2 < tableModel.getColumnCount()) {
                            tableModel.setValueAt(hoursWorked, row, 3 + day - 1);
                        }
                        break;
                    }
                }
            }

            // Đặt kích thước cột
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
            // Đảm bảo đóng kết nối để tránh rò rỉ tài nguyên
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChamCong().setVisible(true));
    }
}
