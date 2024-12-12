package com.jdbc.swingApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewEmployee extends JFrame {
    private JTable employeeTable;
    private JScrollPane scrollPane;

    public ViewEmployee() {
        setTitle("Employee Details");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize JTable and JScrollPane
        employeeTable = new JTable();
        scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, BorderLayout.CENTER);

        // Add button panel at the bottom
        JPanel buttonPanel = new JPanel();
        
        // Add back button to return to Index page
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            setVisible(false); // Hide the ViewEmployee window
            new Index().setVisible(true); // Open the Index window
        });
        buttonPanel.add(backButton);
        
        // Add button to fetch employee data
        JButton fetchButton = new JButton("Fetch Employees");
        fetchButton.addActionListener(e -> {
            try {
                displayEmployees();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(ViewEmployee.this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(fetchButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void displayEmployees() throws SQLException {
        // 1. Establish database connection
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            // Replace with your actual database credentials
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "root");
            statement = connection.createStatement();

            // SQL query to retrieve employee data
            String query = "SELECT emp_id, emp_name, salary, email, phone, address FROM e1";
            resultSet = statement.executeQuery(query);

            // Create a table model to populate the JTable
            DefaultTableModel tableModel = new DefaultTableModel();
            tableModel.addColumn("Emp ID");
            tableModel.addColumn("Emp Name");
            tableModel.addColumn("Salary");
            tableModel.addColumn("Email");
            tableModel.addColumn("Phone");
            tableModel.addColumn("Address");

            // Populate the table model with data from the result set
            while (resultSet.next()) {
                Object[] rowData = {
                        resultSet.getString("emp_id"),
                        resultSet.getString("emp_name"),
                        resultSet.getDouble("salary"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("address")
                };
                tableModel.addRow(rowData);
            }

            // Set the table model to the JTable
            employeeTable.setModel(tableModel);

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
            JOptionPane.showMessageDialog(this, "Error retrieving employee data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close database resources
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ViewEmployee().setVisible(true));
    }
}
