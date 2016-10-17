package com.techelevator.projects.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Project;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAO implements ProjectDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCProjectDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Project> getAllActiveProjects() {
		List<Project> projList = new ArrayList<>();
		
		String query = "SELECT name, project_id " +
					   "FROM project " +
					   "WHERE from_date IS NOT NULL AND to_date IS NULL";
		SqlRowSet results = jdbcTemplate.queryForRowSet(query);
		
		while (results.next()) {
			Project project = new Project();
			String name = results.getString("name");
			Long id = results.getLong("project_id");
			project.setName(name);
			project.setId(id);
			projList.add(project);
		}
		
		return projList;
	}

	@Override
	public void removeEmployeeFromProject(Long projectId, Long employeeId) {
		String query = "DELETE FROM project_employee " + 
					   "WHERE project_id = ? AND employee_id = ?";
		jdbcTemplate.update(query, projectId, employeeId);
	}

	@Override
	public void addEmployeeToProject(Long projectId, Long employeeId) {
		String query = "INSERT INTO project_employee(project_id, employee_id) " +
					   "VALUES (?, ?)";
		jdbcTemplate.update(query, projectId, employeeId);
	}

}