package jdbc_employee_management_system.main;

import javax.swing.SwingUtilities;

import WelcomePage_JDBC.WelcomePage;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WelcomePage());
    }
}

