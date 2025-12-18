package gui;

import jdbc_employee_management_system.dao.EmployeeDAO;
import jdbc_employee_management_system.entity.Employee;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class EmployeeManagementGUI extends JFrame {

    private JTextField nameField, roleField, emailField, phoneField, salaryField, deleteIdField;
    private JTable table;
    private DefaultTableModel tableModel;
    private final EmployeeDAO dao;

    public EmployeeManagementGUI() {
        dao = new EmployeeDAO();
        setTitle("Employee Management System");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        initComponents();
        loadEmployees(); // ✅ THIS LINE IS IMPORTANT
        setVisible(true);
    }


    private void initComponents() {
        Color bgColor = new Color(235, 245, 255);
        Color labelColor = new Color(0, 51, 102);
        Color addColor = new Color(70, 130, 180);
        Color loadColor = new Color(60, 179, 113);
        Color deleteColor = new Color(220, 20, 60);
        Color updateColor = new Color(100, 149, 237);
        Color searchColor = new Color(255, 165, 0);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(bgColor);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Add / Update Employee",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                labelColor
        ));

        nameField = new JTextField(); roleField = new JTextField();
        emailField = new JTextField(); phoneField = new JTextField();
        salaryField = new JTextField();

        formPanel.add(createLabel("Name:", labelColor)); formPanel.add(nameField);
        formPanel.add(createLabel("Role:", labelColor)); formPanel.add(roleField);
        formPanel.add(createLabel("Email:", labelColor)); formPanel.add(emailField);
        formPanel.add(createLabel("Phone:", labelColor)); formPanel.add(phoneField);
        formPanel.add(createLabel("Salary:", labelColor)); formPanel.add(salaryField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setBackground(bgColor);

        JButton addBtn = new JButton("➕ Add");
        JButton updateBtn = new JButton("✏️ Update");
        JButton loadBtn = new JButton("🔄 Load All");
        JButton searchBtn = new JButton("🔍 Search");
        JButton deleteBtn = new JButton("🗑️ Delete");

        deleteIdField = new JTextField(5);

        addBtn.setBackground(addColor);      addBtn.setForeground(Color.WHITE);
        updateBtn.setBackground(updateColor); updateBtn.setForeground(Color.WHITE);
        loadBtn.setBackground(loadColor);    loadBtn.setForeground(Color.WHITE);
        searchBtn.setBackground(searchColor);searchBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(deleteColor);deleteBtn.setForeground(Color.WHITE);

        addBtn.addActionListener(e -> addEmployee());
        updateBtn.addActionListener(e -> updateEmployee());
        loadBtn.addActionListener(e -> loadEmployees());
        searchBtn.addActionListener(e -> searchEmployee());
        deleteBtn.addActionListener(e -> deleteEmployee());

        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(loadBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(new JLabel("ID:"));
        buttonPanel.add(deleteIdField);
        buttonPanel.add(deleteBtn);

        String[] columns = {"ID", "Name", "Role", "Email", "Phone", "Salary"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("SansSerif", Font.PLAIN, 13));
        table.setRowHeight(22);

        table.getTableHeader().setBackground(new Color(64, 64, 64));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                roleField.setText(tableModel.getValueAt(row, 2).toString());
                emailField.setText(tableModel.getValueAt(row, 3).toString());
                phoneField.setText(tableModel.getValueAt(row, 4).toString());
                salaryField.setText(tableModel.getValueAt(row, 5).toString());
                deleteIdField.setText(tableModel.getValueAt(row, 0).toString());
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Employee Records"));

        setLayout(new BorderLayout(10, 10));
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER); 
    }

    private JLabel createLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        return label;
    }

    private void addEmployee() {
        try {
            Employee e = getEmployeeFromForm();
            dao.save(e);
            JOptionPane.showMessageDialog(this, "✅ Employee added!");
            clearForm(); loadEmployees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❗ Error: " + ex.getMessage());
        }
    }

    private void updateEmployee() {
        try {
            int id = Integer.parseInt(deleteIdField.getText());
            Employee e = getEmployeeFromForm();
            e.setId(id);
            dao.update(e);
            JOptionPane.showMessageDialog(this, "✏️ Employee updated!");
            clearForm(); loadEmployees();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Update Error: " + ex.getMessage());
        }
    }

    private void deleteEmployee() {
        try {
            int id = Integer.parseInt(deleteIdField.getText());
            dao.deleteById(id);
            JOptionPane.showMessageDialog(this, "🗑️ Deleted!");
            clearForm(); loadEmployees();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Delete Error: " + e.getMessage());
        }
    }

    private void searchEmployee() {
        String searchName = JOptionPane.showInputDialog(this, "Enter name to search:");
        if (searchName == null || searchName.trim().isEmpty()) return;

        List<Employee> list = dao.searchByName(searchName.trim());
        tableModel.setRowCount(0);
        for (Employee e : list) {
            tableModel.addRow(new Object[]{
                    e.getId(), e.getName(), e.getRole(), e.getEmail(), e.getPhone(), e.getSalary()
            });
        }
    }

    private void loadEmployees() {
        List<Employee> list = dao.findAll();
        tableModel.setRowCount(0);
        for (Employee e : list) {
            tableModel.addRow(new Object[]{
                    e.getId(), e.getName(), e.getRole(), e.getEmail(), e.getPhone(), e.getSalary()
            });
        }
    }

    private Employee getEmployeeFromForm() {
        Employee e = new Employee();
        e.setName(nameField.getText());
        e.setRole(roleField.getText());
        e.setEmail(emailField.getText());
        e.setPhone(phoneField.getText());
        e.setSalary(Double.parseDouble(salaryField.getText()));
        return e;
    }

    private void clearForm() {
        nameField.setText(""); roleField.setText(""); emailField.setText("");
        phoneField.setText(""); salaryField.setText(""); deleteIdField.setText("");
    }
    
}
