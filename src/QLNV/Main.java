package QLNV;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    DangNhap loginForm = new DangNhap();
                    loginForm.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
