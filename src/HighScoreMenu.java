import db.Highscore;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class HighScoreMenu extends JPanel {
    private final Game parent;

    public HighScoreMenu(List<Highscore> highscores, Game parent) {
        this.parent = parent;
        setLayout(new BorderLayout());

        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Name", "Score"}, 0);
        JTable table = new JTable(tableModel);

        for (Highscore score : highscores) {
            Object[] rowData = {score.name(), score.score()};
            tableModel.addRow(rowData);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> closeMenu());
        add(closeButton, BorderLayout.SOUTH);
    }

    private void closeMenu() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
        parent.openRestartDialog();
    }
}