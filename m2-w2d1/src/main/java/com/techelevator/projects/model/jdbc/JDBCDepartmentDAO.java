package com.techelevator.projects.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;

public class JDBCDepartmentDAO implements DepartmentDAO {
	
	private JdbcTemplate jdbcTemplate;

	public JDBCDepartmentDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Department> getAllDepartments() {
		List<Department> departmentList = new ArrayList<Department>();
		
		String query = "SELECT * " +
					   "FROM department";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		
		while(results.next()) {  
			String departmentName = results.getString("name"); 
			long departmentId = results.getInt("department_id");
			Department department = new Department();
			department.setName(departmentName);
			department.setId(departmentId);
			departmentList.add(department);
		}
		return departmentList;
	}

	@Override
	public List<Department> searchDepartmentsByName(String nameSearch) {
		List<Department> departmentList = new ArrayList<Department>();
		
		String query = "SELECT name, department_id " +
					   "FROM department";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		
		while(results.next()) {
			String departmentName = results.getString("name");
			Long departmentId = results.getLong("department_id");
			if (departmentName.equals(nameSearch)) {
				Department department = new Department();
				department.setName(departmentName);
				department.setId(departmentId);
				departmentList.add(department);
			}
		}
		return departmentList;
	}

	@Override
	public void updateDepartmentName(Long departmentId, String departmentName) {
		String sqlUpdateDepartment = "UPDATE department " +
									 "SET name =" + " '" + departmentName + "' " +
									 "WHERE department_id = ?";
		
		jdbcTemplate.update(sqlUpdateDepartment, departmentId);
	}
	
	@Override
	public Department createDepartment(String departmentName) {
		String query  = "INSERT INTO department (name) " +
						"VALUES ('" + departmentName + "')"; 
		jdbcTemplate.update(query);
		
		return searchDepartmentsByName(departmentName).get(0);
	}

	@Override
	public Department getDepartmentById(Long id) {
		Department department = new Department();
		
		String query = "SELECT department_id, name " +
					   "FROM department";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		
		while(results.next()) {
			long departmentId = results.getInt("department_id");
			String departmentName = results.getString("name");
			if (departmentId == id) {
				department.setName(departmentName);
				department.setId(departmentId);
				return department;
			}
		}
		return department;
	}

}



