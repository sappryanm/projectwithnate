package com.techelevator.projects.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		List<Employee> employeeList = new ArrayList<Employee>();
		
		String query = "SELECT * " +
					   "FROM employee";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		
		while(results.next()) {  
			Employee employee = new Employee();
			
			Long employeeId = results.getLong("employee_id");
			Long departmentId = results.getLong("department_id");
			String firstName = results.getString("first_name");
			String lastName = results.getString("last_name");
			LocalDate birthDate = LocalDate.parse(results.getString("birth_date"));
			char gender = (results.getString("gender")).charAt(0);
			LocalDate hireDate = LocalDate.parse(results.getString("hire_date"));
			
			employee.setId(employeeId);
			employee.setDepartmentId(departmentId);
			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			employee.setBirthDay(birthDate);
			employee.setGender(gender);
			employee.setHireDate(hireDate);
			employeeList.add(employee);
		}
		
		return employeeList;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		List<Employee> employeeList = new ArrayList<Employee>();
		
		String query = "SELECT * " +
					   "FROM employee ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		
		while(results.next()) {
			String employeeFirstName = results.getString("first_name");
			String employeeLastName = results.getString("last_name");
			if (employeeFirstName.equals(firstNameSearch) || employeeLastName.equals(lastNameSearch)) {
				Employee employee = new Employee();
				Long employeeId = (long)(results.getInt("employee_id"));
				Long departmentId = (long)(results.getInt("department_id"));
				String firstName = results.getString("first_name");
				String lastName = results.getString("last_name");
				LocalDate birthDate = LocalDate.parse(results.getString("birth_date"));
				char gender = (results.getString("gender")).charAt(0);
				LocalDate hireDate = LocalDate.parse(results.getString("hire_date"));
				
				employee.setId(employeeId);
				employee.setDepartmentId(departmentId);
				employee.setFirstName(firstName);
				employee.setLastName(lastName);
				employee.setBirthDay(birthDate);
				employee.setGender(gender);
				employee.setHireDate(hireDate);
				
				employeeList.add(employee);
			}
		}
		return employeeList;
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		List<Employee> deptList = new ArrayList<Employee>();
		
		String query = "SELECT * " +
					   "FROM employee " +
					   "WHERE department_id = " + id;
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		
		while(results.next()) {
			Employee employee = new Employee();
			
			Long employeeId = (long)(results.getInt("employee_id"));
			Long departmentId = (long)(results.getInt("department_id"));
			String firstName = results.getString("first_name");
			String lastName = results.getString("last_name");
			LocalDate birthDate = LocalDate.parse(results.getString("birth_date"));
			char gender = (results.getString("gender")).charAt(0);
			LocalDate hireDate = LocalDate.parse(results.getString("hire_date"));
			
			employee.setId(employeeId);
			employee.setDepartmentId(departmentId);
			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			employee.setBirthDay(birthDate);
			employee.setGender(gender);
			employee.setHireDate(hireDate);
			
			deptList.add(employee);
		}
		return deptList;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		List<Employee> noProjectList = new ArrayList<Employee>();
		
		String query = "SELECT * " +
					   "FROM employee e " +
					   "LEFT JOIN project_employee pe ON e.employee_id = pe.employee_id " +
					   "WHERE pe.employee_id is NULL";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		
		while(results.next()) {
			Employee employee = new Employee();
			
			Long employeeId = (long)(results.getInt("employee_id"));
			Long departmentId = (long)(results.getInt("department_id"));
			String firstName = results.getString("first_name");
			String lastName = results.getString("last_name");
			LocalDate birthDate = LocalDate.parse(results.getString("birth_date"));
			char gender = (results.getString("gender")).charAt(0);
			LocalDate hireDate = LocalDate.parse(results.getString("hire_date"));
			
			employee.setId(employeeId);
			employee.setDepartmentId(departmentId);
			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			employee.setBirthDay(birthDate);
			employee.setGender(gender);
			employee.setHireDate(hireDate);
			
			noProjectList.add(employee);
		}
		return noProjectList;
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		List<Employee> empList = new ArrayList<Employee>();
		
		String query = "SELECT first_name, last_name " +
					   "FROM employee e " +
					   "INNER JOIN project_employee pe ON pe.employee_id = e.employee_id " +
					   "WHERE pe.project_id = " + projectId;
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		
		while(results.next()) {
			Employee employee = new Employee();
			
			String firstName = results.getString("first_name");
			String lastName = results.getString("last_name");
	
			employee.setFirstName(firstName);
			employee.setLastName(lastName);
			
			empList.add(employee);
		}
		return empList;
	}

	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		String changeDept = "UPDATE employee " +
							"SET department_id = " + departmentId + " " +
							"WHERE employee_id = ?";
		jdbcTemplate.update(changeDept, employeeId);
	}
}
