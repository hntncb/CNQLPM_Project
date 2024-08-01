
package QLNV;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class NhanVienTableModel extends AbstractTableModel {

    private final ArrayList<NhanVien> dsNhanVien;
    private final Object[][] data;
    private final String[] columnNames = {"ID", "HoTen", "NamSinh", "DiaChi", "SDT", "ChucVu"};

    public NhanVienTableModel(ArrayList<NhanVien> dsNhanVien) {
        this.dsNhanVien = dsNhanVien;
        this.data = new Object[dsNhanVien.size()][columnNames.length];

        for (int i = 0; i < dsNhanVien.size(); i++) {
            NhanVien sv = dsNhanVien.get(i);
            data[i] = new Object[]{sv.getId(), sv.getHoTen(), sv.getNamSinh(), sv.getDiaChi(), sv.getSdt(), sv.getChucVu()};
        }
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= data.length || columnIndex >= columnNames.length) {
            throw new IndexOutOfBoundsException("Invalid row or column index");
        }
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
