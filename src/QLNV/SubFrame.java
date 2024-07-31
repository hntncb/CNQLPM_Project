package QLNV;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SubFrame extends JFrame {
    private UserType userType;

    // Constructor nhận quyền người dùng
    public SubFrame(UserType userType) {
        this.userType = userType;

        // Cài đặt tiêu đề và kích thước cho JFrame
        setTitle("Ứng dụng Quản lý Nhân viên");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo các nút
        JButton btnThongTinNhanVien = new JButton("Thông tin nhân viên");
        JButton btnChamCong = new JButton("Chấm công");

        // Cài đặt ActionListener cho nút "Thông tin nhân viên"
        btnThongTinNhanVien.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hiển thị thông tin nhân viên
            	try {
                    NhanVienImplDAO nhanVienDAO = new NhanVienImplDAO();
                    NhanVienTableModel nhanVienModel = new NhanVienTableModel(nhanVienDAO.getAll());
                    NhanVienView nhanVienView = new NhanVienView();
                    NhanVienController controller = new NhanVienController(nhanVienView, nhanVienModel);
                    controller.setUserType(userType);
                    controller.showNhanVienView();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        // Cài đặt ActionListener cho nút "Chấm công"
        btnChamCong.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChamCong chamcong = new ChamCong();
                chamcong.setVisible(true);
            }
        });

        // Tạo một panel để chứa các nút
        JPanel panel = new JPanel();
        panel.add(btnThongTinNhanVien);
        panel.add(btnChamCong);

        // Điều chỉnh các nút dựa trên quyền người dùng

        // Thêm panel vào JFrame
        add(panel);

        // Hiển thị JFrame
        setVisible(true);
    }
}
