package QLNV;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DangNhap loginForm = new DangNhap();
                loginForm.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}