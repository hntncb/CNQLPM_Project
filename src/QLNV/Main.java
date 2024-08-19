package QLNV;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
            	UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                DangNhap loginForm = new DangNhap();
                loginForm.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}