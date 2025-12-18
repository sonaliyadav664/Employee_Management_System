package jdbc_employee_management_system.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import jdbc_employee_management_system.entity.Employee;
import jdbc_employee_management_system.util.DBConnection;

public class EmployeeDAO {

    public void save(Employee e) {
        String sql = "INSERT INTO employee(name, role, email, phone, salary) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.dbConnect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getRole());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getPhone());
            ps.setDouble(5, e.getSalary());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee";
        try (Connection con = DBConnection.dbConnect();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Employee e = new Employee();
                e.setId(rs.getInt("id"));
                e.setName(rs.getString("name"));
                e.setRole(rs.getString("role"));
                e.setEmail(rs.getString("email"));
                e.setPhone(rs.getString("phone"));
                e.setSalary(rs.getDouble("salary"));
                list.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        try (Connection con = DBConnection.dbConnect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(Employee e) {
        String sql = "UPDATE employee SET name = ?, role = ?, email = ?, phone = ?, salary = ? WHERE id = ?";
        try (Connection con = DBConnection.dbConnect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getRole());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getPhone());
            ps.setDouble(5, e.getSalary());
            ps.setInt(6, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Employee> searchByName(String name) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employee WHERE name LIKE ?";
        try (Connection con = DBConnection.dbConnect(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + name + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee e = new Employee();
                    e.setId(rs.getInt("id"));
                    e.setName(rs.getString("name"));
                    e.setRole(rs.getString("role"));
                    e.setEmail(rs.getString("email"));
                    e.setPhone(rs.getString("phone"));
                    e.setSalary(rs.getDouble("salary"));
                    list.add(e);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
}
