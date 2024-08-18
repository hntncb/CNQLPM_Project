package QLNV;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class SubFrame extends JFrame {
    private UserType userType;
    
    public SubFrame(UserType userType) {
        this.userType = userType;

        setTitle("Ứng dụng Quản lý Nhân viên");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton btnThongTinNhanVien = new JButton("Thông tin nhân viên");
        JButton btnChamCong = new JButton("Chấm công");
        JButton btnDangXuat = new JButton("Đăng xuất");

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
            	
				try {
					NhanVienImplDAO nhanVienDAO = new NhanVienImplDAO();
                    NhanVienTableModel nhanVienModel = new NhanVienTableModel(nhanVienDAO.getAll());
                    NhanVienView nhanVienView = new NhanVienView();
                    NhanVienController controller = new NhanVienController(nhanVienView, nhanVienModel);
	            	controller.setUserType(userType);
	                ChamCong chamcong = new ChamCong(userType);
	                chamcong.setVisible(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
                
            }
        });

        // Cài đặt ActionListener cho nút "Đăng xuất"
        btnDangXuat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Đóng cửa sổ hiện tại và mở cửa sổ đăng nhập
                dispose();
                DangNhap dangNhap = new DangNhap();
                dangNhap.setVisible(true);
            }
        });

        JPanel panel = new JPanel();
        panel.add(btnThongTinNhanVien);
        panel.add(btnChamCong);
        panel.add(btnDangXuat);
        add(panel);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        SubFrame.this,
                        "Bạn có chắc chắn muốn thoát không?",
                        "Xác nhận thoát",
                        JOptionPane.YES_NO_OPTION
                );

                if (option == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
    }
}
