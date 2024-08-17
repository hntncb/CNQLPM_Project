package QLNV;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.table.AbstractTableModel;

public class NhanVienTableModel extends AbstractTableModel {
    private ArrayList<NhanVien> dsNhanVien;
    private ArrayList<Boolean> selectionState;
    private final String[] columnNames = {"ID", "HoTen", "NamSinh", "DiaChi", "SDT", "ChucVu", "Chọn"};

    public NhanVienTableModel(ArrayList<NhanVien> dsNhanVien) {
        this.dsNhanVien = new ArrayList<>(dsNhanVien);
        this.selectionState = new ArrayList<>(Collections.nCopies(dsNhanVien.size(), false));
    }

    @Override
    public int getRowCount() {
        return dsNhanVien.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        NhanVien nv = dsNhanVien.get(rowIndex);
        if (columnIndex == 6) {
            return selectionState.get(rowIndex);
        }
        return getValueForColumn(nv, columnIndex);
    }

    private Object getValueForColumn(NhanVien nv, int columnIndex) {
        if (columnIndex == 0) return nv.getId();
        if (columnIndex == 1) return nv.getHoTen();
        if (columnIndex == 2) return nv.getNamSinh();
        if (columnIndex == 3) return nv.getDiaChi();
        if (columnIndex == 4) return nv.getSdt();
        if (columnIndex == 5) return nv.getChucVu();
        return null;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        NhanVien nv = dsNhanVien.get(rowIndex);
        if (columnIndex == 6) {
            selectionState.set(rowIndex, (Boolean) value);
        } else {
            setValueForColumn(nv, columnIndex, value);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    private void setValueForColumn(NhanVien nv, int columnIndex, Object value) {
        if (columnIndex == 0) nv.setId((Integer) value);
        if (columnIndex == 1) nv.setHoTen((String) value);
        if (columnIndex == 2) nv.setNamSinh((String) value);
        if (columnIndex == 3) nv.setDiaChi((String) value);
        if (columnIndex == 4) nv.setSdt((String) value);
        if (columnIndex == 5) nv.setChucVu((String) value);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 6;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 6) {
            return Boolean.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void removeNhanVien(NhanVien nv) {
        int index = dsNhanVien.indexOf(nv);
        if (index != -1) {
            dsNhanVien.remove(index);
            selectionState.remove(index);
            fireTableDataChanged();
        }
    }

    public ArrayList<NhanVien> getSelectedNhanViens() {
        ArrayList<NhanVien> selectedNhanViens = new ArrayList<>();
        for (int i = 0; i < dsNhanVien.size(); i++) {
            if (selectionState.get(i)) {
                selectedNhanViens.add(dsNhanVien.get(i));
            }
        }
        return selectedNhanViens;
    }

    public void setNhanViens(ArrayList<NhanVien> newDsNhanVien) {
        this.dsNhanVien = new ArrayList<>(newDsNhanVien);
        this.selectionState = new ArrayList<>(Collections.nCopies(newDsNhanVien.size(), false));
        fireTableDataChanged();
    }

    public void clearSelectedNhanViens() {
        Collections.fill(selectionState, false);
        fireTableDataChanged();
    }
}