package com.jdbc.swingApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ViewById extends JFrame {
    private JTextField empIdTextField;
    private JTextArea displayArea;
    private JButton viewButton, backButton;
    
    public ViewById() {
        // Initialize JFrame settings
        setTitle("View Employee By ID");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize components
        empIdTextField = new JTextField(20);
        displayArea = new JTextArea(10, 30);
        viewButton = new JButton("View Employee");
        backButton = new JButton("Back");
        
        // Panel setup with layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 10, 10));
        panel.add(new JLabel("Enter Employee ID:"));
        panel.add(empIdTextField);
        panel.add(viewButton);
        panel.add(backButton);

        JScrollPane scrollPane = new JScrollPane(displayArea);
        displayArea.setEditable(false);

        // Add components to JFrame
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // ActionListener for the view button
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empId;
                while (true) {
                    empId = JOptionPane.showInputDialog(ViewById.this, "Enter Employee ID:", "Input", JOptionPane.QUESTION_MESSAGE);
                    
                    if (empId == null) {
                        // User pressed Cancel
                        return;
                    } else if (!empId.trim().isEmpty()) {
                        // Valid input
                        empIdTextField.setText(empId.trim());
                        viewEmployeeById();
                        break;
                    } else {
                        // Invalid input, show error message
                        JOptionPane.showMessageDialog(ViewById.this, "Please enter a valid Employee ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        
        // ActionListener for the back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // Hide the ViewById window
                new Index().setVisible(true); // Open the Index window
            }
        });
    }

    public void viewEmployeeById() {
        String empId = empIdTextField.getText().trim();

        if (empId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Employee ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Database connection setup
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "root");
            String query = "SELECT * FROM e1 WHERE emp_id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, empId);

            resultSet = preparedStatement.executeQuery();

            displayArea.setText(""); // Clear previous content

            if (resultSet.next()) {
                displayArea.append("ID: " + resultSet.getString("emp_id") + "\n");
                displayArea.append("Name: " + resultSet.getString("emp_name") + "\n");
                displayArea.append("Salary: " + resultSet.getDouble("salary") + "\n");
                displayArea.append("Email: " + resultSet.getString("email") + "\n");
                displayArea.append("Phone: " + resultSet.getString("phone") + "\n");
                displayArea.append("Address: " + resultSet.getString("address") + "\n");
            } else {
                JOptionPane.showMessageDialog(this, "Employee with ID " + empId + " not found.", "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving employee data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ViewById().setVisible(true);
            }
        });
    }
}
