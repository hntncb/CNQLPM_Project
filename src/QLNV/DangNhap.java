package QLNV;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            UserType userType = authenticateUser(username, password);
            if (userType != null) {
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
                handleSuccessfulLogin(userType);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Đăng nhập thất bại. Vui lòng kiểm tra tên đăng nhập và mật khẩu.");
                usernameField.setText("");
                passwordField.setText("");
            }
        }
    }
    
    private UserType authenticateUser(String username, String password) {
        try (Connection conn = ConnectionFactory.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement("SELECT user_type FROM tk_dangnhap WHERE username=? AND password=?")) {
             
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String userTypeString = resultSet.getString("user_type");
                    return UserType.valueOf(userTypeString.toUpperCase());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void handleSuccessfulLogin(UserType userType) {
        // Mở SubFrame và truyền quyền người dùng vào
        SwingUtilities.invokeLater(() -> {
            SubFrame subFrame = new SubFrame(userType);
            subFrame.setVisible(true);
        });
    }

}
