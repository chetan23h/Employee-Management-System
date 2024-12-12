package com.jdbc.swingApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RemoveEmployee extends JFrame {
    private JTextField empIdTextField;
    private JTextArea displayArea;
    private JButton removeButton;
    private JButton clearButton;
    private JButton backButton;

    // Constructor to set up the UI components
    public RemoveEmployee() {
        setTitle("Remove Employee");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Initialize components
        empIdTextField = new JTextField(20);
        displayArea = new JTextArea(5, 30);
        removeButton = new JButton("Remove Employee");
        clearButton = new JButton("Clear");
        backButton = new JButton("Back");

        // Set up layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10)); // Updated to 5 rows to accommodate back button
        panel.add(new JLabel("Enter Employee ID:"));
        panel.add(empIdTextField);
        panel.add(removeButton);
        panel.add(clearButton);
        panel.add(backButton); // Added back button to the panel

        // Set up display area
        JScrollPane scrollPane = new JScrollPane(displayArea);
        displayArea.setEditable(false); // Make it non-editable

        // Add components to the frame
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Prompt the user to enter the Employee ID
                String empId = JOptionPane.showInputDialog(RemoveEmployee.this, "Enter Employee ID to remove:", "Input", JOptionPane.QUESTION_MESSAGE);

                // Validate input
                if (empId != null && !empId.trim().isEmpty()) {
                    // Proceed to remove the employee
                    removeEmployee(empId.trim());
                } else {
                    JOptionPane.showMessageDialog(RemoveEmployee.this, "Please enter a valid Employee ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                empIdTextField.setText("");
                displayArea.setText("");
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the current window
                // Optionally, navigate to a previous screen or main menu (this part can be customized as needed)
                new Index().setVisible(true); // Assuming MainMenu is another JFrame class
            }
        });
    }

    // Method to remove an employee from the database
    public void removeEmployee(String empId) {
        empId = empIdTextField.getText().trim();

        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Employee ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. Establish database connection (replace with your actual database credentials)
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "root");

            // SQL query to delete the employee by ID
            String query = "DELETE FROM e1 WHERE emp_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, empId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                displayArea.setText("Employee with ID " + empId + " removed successfully.");
                empIdTextField.setText(""); // Clear input field
            } else {
                displayArea.setText("Employee with ID " + empId + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection or query errors
            JOptionPane.showMessageDialog(this, "Error removing employee: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close database resources
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new RemoveEmployee().setVisible(true);
            }
        });
    }
}
