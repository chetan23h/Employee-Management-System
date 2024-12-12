package com.jdbc.swingApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Index extends JFrame {

    public Index() {
        setTitle("Employee Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window

        JPanel mainPanel = new JPanel(new GridLayout(4, 2, 10, 10)); // 4 rows, 2 columns, 10 pixel gaps

        // Create buttons
        JButton viewEmployeesButton = new JButton("VIEW EMPLOYEES");
        JButton viewEmpByIdButton = new JButton("VIEW EMP BY ID");
        JButton addEmployeeButton = new JButton("ADD EMPLOYEE");
        JButton removeEmployeeButton = new JButton("REMOVE EMPLOYEE");
        JButton updateEmployeeButton = new JButton("UPDATE EMPLOYEE");
        JButton exitButton = new JButton("EXIT");

        // Add buttons to the panel
        mainPanel.add(viewEmployeesButton);
        mainPanel.add(viewEmpByIdButton);
        mainPanel.add(addEmployeeButton);
        mainPanel.add(removeEmployeeButton);
        mainPanel.add(updateEmployeeButton);
        mainPanel.add(exitButton);

        // Add the panel to the frame
        add(mainPanel);

        // Add action listeners to buttons (replace with your actual logic)
        viewEmployeesButton.addActionListener(e -> {
            try {
            	// Create an instance of ViewEmployee
                ViewEmployee viewEmployeeFrame = new ViewEmployee();
                
                // Display the employee data
                viewEmployeeFrame.displayEmployees();
                
                // Make the ViewEmployee window visible
                viewEmployeeFrame.setVisible(true);
            } catch (SQLException e1) {
                // Show error message in a dialog box
                JOptionPane.showMessageDialog(Index.this, "Error fetching employee data: " + e1.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                
            }
        });

        viewEmpByIdButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Create and show the ViewById window
                ViewById viewByIdFrame = new ViewById();
                viewByIdFrame.setVisible(true);
            }
        });



        addEmployeeButton.addActionListener(e -> {
            setVisible(false); // Hide the Index window
            AddEmployee addEmployee = new AddEmployee();
            addEmployee.setVisible(true); // Show Add Employee window
        });

        removeEmployeeButton.addActionListener(e -> {
            setVisible(false); // Hide the Index window
            new RemoveEmployee().setVisible(true); // Open the Remove Employee window
        });


        updateEmployeeButton.addActionListener(e -> {
            setVisible(false); // Hide the Index window
            UpdateEmployee updateEmployee = new UpdateEmployee(); 
            updateEmployee.setVisible(true); // Open the Update Employee window 
        });

        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0); // Exit the application
            }
        });

        setVisible(true); // Make the main window visible
    }

    public static void main(String[] args) {
        // Invoke the GUI thread for launching the application
        SwingUtilities.invokeLater(() -> new Index());
    }
}
