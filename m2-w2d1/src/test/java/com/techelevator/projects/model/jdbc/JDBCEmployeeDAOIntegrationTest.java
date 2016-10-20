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


public class JDBCEmployeeDAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private EmployeeDAO dao;
	
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
		dao = new JDBCEmployeeDAO(dataSource);
	}

	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}

	@Test
	public void get_all_employees_returns_expected_amount() {
		int beforeCount = (dao.getAllEmployees()).size();
		String query = "INSERT INTO employee (first_name, last_name, birth_date, gender, hire_date)" +
					   "VALUES('nate', 'anderson', to_date('19831220', 'YYYYMMDD'), 'M', to_date('20161017', 'YYYYMMDD'))";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(query);
		int afterCount = (dao.getAllEmployees()).size();
		Assert.assertEquals(beforeCount + 1, afterCount);
	}
	
	@Test
	public void search_employee_by_name() {
		List<Employee> testList = dao.searchEmployeesByName("Flo", "Henderson");
		Employee flo = testList.get(0);
		String formattedName = flo.toString();
		Assert.assertEquals("Henderson, Flo", formattedName);
	}
	
	@Test
	public void first_or_last_name_wrong_will_still_match_an_employee() {
		List<Employee> testList = dao.searchEmployeesByName("Floooooow", "Henderson");
		Employee flo = testList.get(0);
		String formattedName = flo.toString();
		Assert.assertEquals("Henderson, Flo", formattedName);
	}

	@Test
	public void test_get_employee_by_dept() {
		int beforeCount = (dao.getEmployeesByDepartmentId(1)).size();
		String query = "INSERT INTO employee (department_id, first_name, last_name, birth_date, gender, hire_date)" +
				   	   "VALUES(1, 'nate', 'anderson', to_date('19831220', 'YYYYMMDD'), 'M', to_date('20161017', 'YYYYMMDD'))";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(query);
		int afterCount = (dao.getEmployeesByDepartmentId(1)).size();
		Assert.assertEquals(beforeCount + 1, afterCount);
	}
	
	@Test
	public void find_employees_without_project() {
		int beforeCount = (dao.getEmployeesWithoutProjects()).size();
		String query = "INSERT INTO employee (department_id, first_name, last_name, birth_date, gender, hire_date)" +
				   	   "VALUES(1, 'nate', 'anderson', to_date('19831220', 'YYYYMMDD'), 'M', to_date('20161017', 'YYYYMMDD'))";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(query);
		int afterCount = (dao.getEmployeesWithoutProjects()).size();
		Assert.assertEquals(beforeCount + 1, afterCount);
	}
	
	@Test
	public void change_employee_department() {
		String query = "INSERT INTO employee (department_id, first_name, last_name, birth_date, gender, hire_date)" +
					   "VALUES(1, 'nate', 'anderson', to_date('19831220', 'YYYYMMDD'), 'M', to_date('20161017', 'YYYYMMDD'))";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(query);
		List<Employee> testList = dao.searchEmployeesByName("nate", "anderson");
		Employee nate = testList.get(0);
		dao.changeEmployeeDepartment(new Long(nate.getId()), new Long(10));
		testList = dao.searchEmployeesByName("nate", "anderson");
		nate = testList.get(0);
		Long newDept = nate.getDepartmentId(); 
		Assert.assertEquals(newDept, new Long(10));
	}
	
	@Test
	public void get_employee_by_project_id() {
		int beforeCount = (dao.getEmployeesByProjectId((long) 1).size());
		String query = "INSERT INTO employee (department_id, first_name, last_name, birth_date, gender, hire_date)" +
				   "VALUES(1, 'nate', 'anderson', to_date('19831220', 'YYYYMMDD'), 'M', to_date('20161017', 'YYYYMMDD'))";
		JdbcTemplate template = new JdbcTemplate(dataSource);
		template.update(query);
		List<Employee> testList = dao.searchEmployeesByName("nate", "anderson");
		Employee nate = testList.get(0);
		JDBCProjectDAO testDao = new JDBCProjectDAO(dataSource);
		testDao.addEmployeeToProject(new Long(1), nate.getId());
		int afterCount = (dao.getEmployeesByProjectId((long) 1).size());
		
		Assert.assertEquals(afterCount, beforeCount + 1);
	}
}

 












