package jdbc_employee_management_system.service;

import java.util.List;

import jdbc_employee_management_system.dao.EmployeeDAO;
import jdbc_employee_management_system.entity.Employee;

public class EmployeeService {

	  private final EmployeeDAO dao = new EmployeeDAO();

	    public void addEmployee(Employee e) {
	        dao.save(e);
	    }

	    public List<Employee> getAllEmployees() {
	        return dao.findAll();
	    }

	    public void removeEmployeeById(int id) {
	        dao.deleteById(id);
	    }

}
