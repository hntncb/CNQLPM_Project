package QLNV;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    private final Color evenRowColor;
    private final Color oddRowColor;
    private final Color headerBackgroundColor;
    private final Color headerForegroundColor;
    private final Font headerFont;

    public CustomTableCellRenderer(Color evenRowColor, Color oddRowColor, Color headerBackgroundColor, Color headerForegroundColor, Font headerFont) {
        this.evenRowColor = evenRowColor;
        this.oddRowColor = oddRowColor;
        this.headerBackgroundColor = headerBackgroundColor;
        this.headerForegroundColor = headerForegroundColor;
        this.headerFont = headerFont;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (row == -1) { // Tiêu đề cột
            setBackground(headerBackgroundColor);
            setForeground(headerForegroundColor);
            setFont(headerFont);
        } else { // Hàng dữ liệu
            if (row % 2 == 0) {
                cell.setBackground(evenRowColor);
            } else {
                cell.setBackground(oddRowColor);
            }
            
            setForeground(Color.BLACK);
            
            if (isSelected) {
                cell.setBackground(cell.getBackground().darker());
            }
        }

        // Thêm viền đậm cho tất cả các ô
        setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        return cell;
    }
}