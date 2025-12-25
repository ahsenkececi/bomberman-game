package com.bomberman.views;

import com.bomberman.repository.LeaderboardRepository;
import com.bomberman.repository.LeaderboardRepository.LeaderboardEntry;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class LeaderboardView extends JPanel {

    private LeaderboardRepository leaderboardRepo;
    private JTable leaderboardTable;
    private DefaultTableModel tableModel;
    private JButton backButton;

    public LeaderboardView() {
        this.leaderboardRepo = new LeaderboardRepository();
        setLayout(new BorderLayout());
        setBackground(new Color(34, 40, 49));

        // Title panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(34, 40, 49));
        JLabel titleLabel = new JLabel("🏆 LEADERBOARD 🏆");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(253, 203, 110));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Rank", "Username", "Score", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Read-only
            }
        };

        leaderboardTable = new JTable(tableModel);
        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 16));
        leaderboardTable.setRowHeight(30);
        leaderboardTable.setBackground(new Color(57, 62, 70));
        leaderboardTable.setForeground(Color.WHITE);
        leaderboardTable.setSelectionBackground(new Color(0, 173, 181));
        leaderboardTable.setSelectionForeground(Color.WHITE);
        leaderboardTable.setGridColor(new Color(34, 40, 49));

        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < leaderboardTable.getColumnCount(); i++) {
            leaderboardTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Header styling
        leaderboardTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        leaderboardTable.getTableHeader().setBackground(new Color(0, 173, 181));
        leaderboardTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        scrollPane.getViewport().setBackground(new Color(34, 40, 49));
        add(scrollPane, BorderLayout.CENTER);

        // Back button
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(34, 40, 49));
        backButton = new JButton("← Back to Menu");
        backButton.setFont(new Font("Arial", Font.BOLD, 16));
        backButton.setBackground(new Color(238, 82, 83));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setPreferredSize(new Dimension(200, 40));
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load data
        refresh();
    }

    public void refresh() {
        // Clear table
        tableModel.setRowCount(0);

        // Load top 10 scores
        List<LeaderboardEntry> entries = leaderboardRepo.getTopScores(10);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        for (LeaderboardEntry entry : entries) {
            Object[] row = {
                    "#" + entry.getRank(),
                    entry.getUsername(),
                    entry.getScore(),
                    dateFormat.format(entry.getDate())
            };
            tableModel.addRow(row);
        }

        // If empty
        if (entries.isEmpty()) {
            Object[] row = {"", "No scores yet!", "", ""};
            tableModel.addRow(row);
        }
    }

    public JButton getBackButton() {
        return backButton;
    }
}