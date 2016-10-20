package com.techelevator.projects.model.jdbc;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;
import com.techelevator.projects.model.ProjectDAO;

public class JDBCProjectDAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private ProjectDAO dao;
	private EmployeeDAO empDao;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		dao = new JDBCProjectDAO(dataSource);
		empDao = new JDBCEmployeeDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void remove_employee_from_project() {
		int beforeCount = (empDao.getEmployeesByProjectId((long) 1).size());
		String query = "INSERT INTO employee (department_id, first_name, last_name, birth_date, gender, hire_date)" +
				   	   "VALUES(1, 'nate', 'anderson', to_date('19831220', 'YYYYMMDD'), 'M', to_date('20161017', 'YYYYMMDD'))";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(query);
		List<Employee> testList = empDao.searchEmployeesByName("nate", "anderson");
		String query2 = "INSERT INTO employee (department_id, first_name, last_name, birth_date, gender, hire_date)" +
			   	   "VALUES(1, 'ryan', 'sapp', to_date('19811220', 'YYYYMMDD'), 'M', to_date('20151017', 'YYYYMMDD'))";
		template.update(query2);
		List<Employee> testList2 = empDao.searchEmployeesByName("ryan", "sapp");
		Employee nate = testList.get(0);
		Employee ryan = testList2.get(0);
		dao.addEmployeeToProject(new Long(1), nate.getId());
		dao.addEmployeeToProject(new Long(1), ryan.getId());
		dao.removeEmployeeFromProject(new Long(1), ryan.getId());
		int afterCount = (empDao.getEmployeesByProjectId((long) 1).size());
		
		Assert.assertEquals(afterCount, beforeCount + 1);
	}
	
	@Test
	public void get_all_active_projects() {
		int beforeCount = dao.getAllActiveProjects().size();
		String query = "INSERT INTO project (name, from_date)" +
					   "VALUES ('test project', '2014-10-01')";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(query);
		int afterCount = dao.getAllActiveProjects().size();
		Assert.assertEquals(afterCount, beforeCount + 1);
	}
	
	@Test
	public void get_all_active_projects_with_null_from_date() {
		int beforeCount = dao.getAllActiveProjects().size();
		String query = "INSERT INTO project (name)" +
					   "VALUES ('test project')";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(query);
		int afterCount = dao.getAllActiveProjects().size();
		Assert.assertEquals(afterCount, beforeCount);
	}
	
	@Test
	public void get_project_with_to_date_in_the_future() {
		int beforeCount = dao.getAllActiveProjects().size();
		String query = "INSERT INTO project (name, from_date, to_date)" +
					   "VALUES ('test project', '2014-10-01', '9999-12-30')";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(query);
		int afterCount = dao.getAllActiveProjects().size();
		Assert.assertEquals(afterCount, beforeCount + 1);
	}
}





















