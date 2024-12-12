package com.jdbc.swingApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateEmployee extends JFrame {
    private JTextField empIdTextField, nameTextField, salaryTextField, emailTextField, phoneTextField, addressTextField;
    private JCheckBox nameCheckBox, salaryCheckBox, emailCheckBox, phoneCheckBox, addressCheckBox;
    private JButton updateButton, backButton;

    public UpdateEmployee() {
        setTitle("Update Employee");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create and add components
        JLabel empIdLabel = new JLabel("Employee ID:");
        empIdTextField = new JTextField(10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(empIdLabel, gbc);
        gbc.gridx = 1;
        add(empIdTextField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        nameTextField = new JTextField(20);
        nameCheckBox = new JCheckBox();
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(nameLabel, gbc);
        gbc.gridx = 1;
        add(nameTextField, gbc);
        gbc.gridx = 2;
        add(nameCheckBox, gbc);

        JLabel salaryLabel = new JLabel("Salary:");
        salaryTextField = new JTextField(10);
        salaryCheckBox = new JCheckBox();
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(salaryLabel, gbc);
        gbc.gridx = 1;
        add(salaryTextField, gbc);
        gbc.gridx = 2;
        add(salaryCheckBox, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailTextField = new JTextField(20);
        emailCheckBox = new JCheckBox();
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(emailLabel, gbc);
        gbc.gridx = 1;
        add(emailTextField, gbc);
        gbc.gridx = 2;
        add(emailCheckBox, gbc);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneTextField = new JTextField(15);
        phoneCheckBox = new JCheckBox();
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(phoneLabel, gbc);
        gbc.gridx = 1;
        add(phoneTextField, gbc);
        gbc.gridx = 2;
        add(phoneCheckBox, gbc);

        JLabel addressLabel = new JLabel("Address:");
        addressTextField = new JTextField(30);
        addressCheckBox = new JCheckBox();
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(addressLabel, gbc);
        gbc.gridx = 1;
        add(addressTextField, gbc);
        gbc.gridx = 2;
        add(addressCheckBox, gbc);

        updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployee();
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 6;
        add(updateButton, gbc);

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Closes the current frame
                // Optionally, you can navigate to the main menu or previous screen if applicable
                 new Index(); // Uncomment this if you have a MainMenu class
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 6;
        add(backButton, gbc);

        pack();
        setVisible(true);
    }

    public void updateEmployee() {
        String empId = empIdTextField.getText();

        // Database connection (replace with your actual credentials)
        String url = "jdbc:mysql://localhost:3306/employee";
        String user = "root";
        String password = "chetanab23";

        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            PreparedStatement preparedStatement = createPreparedStatement(connection);

            if (preparedStatement != null) { // Check if any fields were selected for update
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearInputFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating employee. No changes made.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select at least one field to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary format. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
        StringBuilder queryBuilder = new StringBuilder("UPDATE e1 SET ");
        int index = 1;
        boolean hasUpdates = false;

        if (nameCheckBox.isSelected()) {
            queryBuilder.append("emp_name = ?, ");
            hasUpdates = true;
        }
        if (salaryCheckBox.isSelected()) {
            queryBuilder.append("salary = ?, ");
            hasUpdates = true;
        }
        if (emailCheckBox.isSelected()) {
            queryBuilder.append("email = ?, ");
            hasUpdates = true;
        }
        if (phoneCheckBox.isSelected()) {
            queryBuilder.append("phone = ?, ");
            hasUpdates = true;
        }
        if (addressCheckBox.isSelected()) {
            queryBuilder.append("address = ? ");
            hasUpdates = true;
        }

        if (hasUpdates) {
            queryBuilder.deleteCharAt(queryBuilder.length() - 2);
        } else {
            return null;
        }

        queryBuilder.append(" WHERE emp_id = ?");

        PreparedStatement preparedStatement = connection.prepareStatement(queryBuilder.toString());

        if (nameCheckBox.isSelected()) {
            preparedStatement.setString(index++, nameTextField.getText());
        }
        if (salaryCheckBox.isSelected()) {
            preparedStatement.setDouble(index++, Double.parseDouble(salaryTextField.getText()));
        }
        if (emailCheckBox.isSelected()) {
            preparedStatement.setString(index++, emailTextField.getText());
        }
        if (phoneCheckBox.isSelected()) {
            preparedStatement.setString(index++, phoneTextField.getText());
        }
        if (addressCheckBox.isSelected()) {
            preparedStatement.setString(index++, addressTextField.getText());
        }

        preparedStatement.setString(index, empIdTextField.getText());

        return preparedStatement;
    }

    private void clearInputFields() {
        nameTextField.setText("");
        salaryTextField.setText("");
        emailTextField.setText("");
        phoneTextField.setText("");
        addressTextField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UpdateEmployee());
    }
}