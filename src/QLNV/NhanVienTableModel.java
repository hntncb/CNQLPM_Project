package QLNV;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class NhanVienTableModel extends AbstractTableModel {

    private final ArrayList<NhanVien> dsNhanVien;
    private Object[][] data;
    private final String[] columnNames = {"ID", "HoTen", "NamSinh", "DiaChi", "SDT", "ChucVu", "Chọn"};
    private boolean showSelectColumn = false; // Biến điều khiển cột "Chọn"

    public NhanVienTableModel(ArrayList<NhanVien> dsNhanVien) {
        this.dsNhanVien = dsNhanVien;
        this.data = new Object[dsNhanVien.size()][columnNames.length];

        for (int i = 0; i < dsNhanVien.size(); i++) {
            NhanVien sv = dsNhanVien.get(i);
            data[i] = new Object[]{sv.getId(), sv.getHoTen(), sv.getNamSinh(), sv.getDiaChi(), sv.getSdt(), sv.getChucVu(), false};
        }
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return showSelectColumn ? columnNames.length : columnNames.length - 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= data.length || columnIndex >= getColumnCount()) {
            throw new IndexOutOfBoundsException("Invalid row or column index");
        }
        if (!showSelectColumn && columnIndex >= 6) {
            columnIndex++; // Điều chỉnh chỉ số cột nếu cột chọn không hiển thị
        }
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex >= data.length || columnIndex >= getColumnCount()) {
            throw new IndexOutOfBoundsException("Invalid row or column index");
        }
        if (!showSelectColumn && columnIndex >= 6) {
            columnIndex++; // Điều chỉnh chỉ số cột nếu cột chọn không hiển thị
        }
        data[rowIndex][columnIndex] = aValue;
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return column < 6 || showSelectColumn ? columnNames[column] : columnNames[column + 1];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (showSelectColumn && columnIndex == 6) { // Cột "Chọn" ở chỉ số 6
            return Boolean.class;
        }
        return Object.class;
    }

    public boolean isShowSelectColumn() {
        return showSelectColumn;
    }

    public void toggleSelectColumn() {
        showSelectColumn = !showSelectColumn;
        fireTableStructureChanged(); // Cập nhật cấu trúc bảng
    }
    public void removeSelectedRows() {
        // Tạo danh sách các chỉ số hàng cần xóa
        ArrayList<Integer> rowsToRemove = new ArrayList<>();

        // Tìm các hàng có giá trị true ở cột "Chọn"
        for (int i = 0; i < data.length; i++) {
            if (showSelectColumn && (Boolean) data[i][6]) {
                rowsToRemove.add(i);
            }
        }

        // Xóa các hàng từ cuối đến đầu để không làm xáo trộn chỉ số
        for (int i = rowsToRemove.size() - 1; i >= 0; i--) {
            int rowIndex = rowsToRemove.get(i);
            dsNhanVien.remove(rowIndex); // Xóa khỏi danh sách nhân viên
            // Cập nhật dữ liệu của bảng
            Object[][] newData = new Object[dsNhanVien.size()][columnNames.length];
            for (int j = 0; j < dsNhanVien.size(); j++) {
                NhanVien sv = dsNhanVien.get(j);
                newData[j] = new Object[]{sv.getId(), sv.getHoTen(), sv.getNamSinh(), sv.getDiaChi(), sv.getSdt(), sv.getChucVu(), false};
            }
            data = newData;
        }
        
        fireTableDataChanged(); // Cập nhật bảng
    }

    public void removeRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < dsNhanVien.size()) {
            dsNhanVien.remove(rowIndex); // Xóa khỏi danh sách nhân viên

            // Cập nhật dữ liệu của bảng
            Object[][] newData = new Object[dsNhanVien.size()][columnNames.length];
            for (int i = 0; i < dsNhanVien.size(); i++) {
                NhanVien sv = dsNhanVien.get(i);
                newData[i] = new Object[]{sv.getId(), sv.getHoTen(), sv.getNamSinh(), sv.getDiaChi(), sv.getSdt(), sv.getChucVu(), false};
            }
            data = newData;
            fireTableDataChanged(); // Cập nhật bảng
        }
    }

}
