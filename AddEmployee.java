package com.jdbc.swingApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*; 

public class AddEmployee extends JFrame {
    private JTextField empIdTextField, empNameTextField, empSalaryTextField, empEmailTextField, empPhoneTextField, empAddressTextField;
    private JButton addButton, clearButton, backButton;

    public AddEmployee() {
        setTitle("Add Employee");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(9, 2, 10, 10)); // Updated grid to fit the new Back button

        // Create labels and text fields
        add(new JLabel("Employee ID:"));
        empIdTextField = new JTextField();
        add(empIdTextField);

        add(new JLabel("Employee Name:"));
        empNameTextField = new JTextField();
        add(empNameTextField);

        add(new JLabel("Salary:"));
        empSalaryTextField = new JTextField();
        add(empSalaryTextField);

        add(new JLabel("Email:"));
        empEmailTextField = new JTextField();
        add(empEmailTextField);

        add(new JLabel("Phone:"));
        empPhoneTextField = new JTextField();
        add(empPhoneTextField);

        add(new JLabel("Address:"));
        empAddressTextField = new JTextField();
        add(empAddressTextField);

        // Add button to submit form
        addButton = new JButton("Add Employee");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addEmployee();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(AddEmployee.this, "Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        add(addButton);

        // Clear button to clear all fields
        clearButton = new JButton("Clear");
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearFields();
            }
        });
        add(clearButton);
        
        // Back button to go back to the previous screen
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); // Hide the current window
                new Index().setVisible(true); // Open the Index window
            }
        });
        add(backButton);
    }

    public void addEmployee() throws SQLException {
        String empId = empIdTextField.getText();
        String empName = empNameTextField.getText();
        String empSalaryStr = empSalaryTextField.getText();
        String empEmail = empEmailTextField.getText();
        String empPhone = empPhoneTextField.getText();
        String empAddress = empAddressTextField.getText();

        // Validate input (e.g., check for empty fields, correct data types)
        if (empId.isEmpty() || empName.isEmpty() || empSalaryStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double empSalary = Double.parseDouble(empSalaryStr);

            // 1. Establish database connection
            Connection connection = null;
            PreparedStatement preparedStatement = null;

            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "root");

                // SQL query to insert employee data
                String query = "INSERT INTO e1 (emp_id, emp_name, salary, email, phone, address) VALUES (?, ?, ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, empId);
                preparedStatement.setString(2, empName);
                preparedStatement.setDouble(3, empSalary);
                preparedStatement.setString(4, empEmail);
                preparedStatement.setString(5, empPhone);
                preparedStatement.setString(6, empAddress);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Error adding employee.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } finally {
                // Close database resources
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        empIdTextField.setText("");
        empNameTextField.setText("");
        empSalaryTextField.setText("");
        empEmailTextField.setText("");
        empPhoneTextField.setText("");
        empAddressTextField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AddEmployee().setVisible(true);
            }
        });
    }
}
