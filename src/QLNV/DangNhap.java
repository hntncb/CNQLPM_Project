package QLNV;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DangNhap extends JFrame implements ActionListener {
    private JLabel titleLabel, usernameLabel, passwordLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public DangNhap() {
        setTitle("Đăng nhập");
        setBounds(850, 400, 300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        titleLabel = new JLabel("Đăng nhập");
        titleLabel.setBounds(110, 10, 80, 20);
        add(titleLabel);

        usernameLabel = new JLabel("Tên đăng nhập:");
        usernameLabel.setBounds(30, 50, 100, 20);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(130, 50, 120, 20);
        add(usernameField);

        passwordLabel = new JLabel("Mật khẩu:");
        passwordLabel.setBounds(30, 80, 100, 20);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(130, 80, 120, 20);
        add(passwordField);

        loginButton = new JButton("Đăng nhập");
        loginButton.setBounds(100, 120, 100, 30);
        loginButton.addActionListener(this);
        add(loginButton);

        passwordField.addActionListener(e -> loginButton.doClick());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                handleSuccessfulLogin();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Đăng nhập thất bại. Vui lòng kiểm tra tên đăng nhập và mật khẩu.");
                usernameField.setText("");
                passwordField.setText("");
            }
        }
    }
    
    private boolean authenticateUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/qlnv", "root", "");
             PreparedStatement statement = conn.prepareStatement("SELECT * FROM tk_dangnhap WHERE username=? AND password=?")) {
             
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void handleSuccessfulLogin() {
        try {
            NhanVienImplDAO sinhVienDAO = new NhanVienImplDAO();
            NhanVienTableModel sinhvienModel = new NhanVienTableModel(sinhVienDAO.getAll());
            NhanVienView sinhvienView = new NhanVienView();
            NhanVienController controller = new NhanVienController(sinhvienView, sinhvienModel);
            controller.showNhanVienView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}