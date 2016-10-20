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

import com.techelevator.projects.model.Department;
import com.techelevator.projects.model.DepartmentDAO;



public class JDBCDepartmentDAOIntegrationTest {
	
	/* Using this particular implementation of DataSource so that
	 * every database interaction is part of the same database
	 * session and hence the same database transaction */
	private static SingleConnectionDataSource dataSource;
	private DepartmentDAO dao;
	
	/* Before any tests are run, this method initializes the datasource for testing. */
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/projects");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		/* The following line disables autocommit for connections 
		 * returned by this DataSource. This allows us to rollback
		 * any changes after each test */
		dataSource.setAutoCommit(false);
	}
	
	/* After all tests have finished running, this method will close the DataSource */
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		dao = new JDBCDepartmentDAO(dataSource);
	}

	/* After each test, we rollback any changes that were made to the database so that
	 * everything is clean for the next test */
	@After
	public void rollback() throws SQLException {
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void created_department_has_correct_name() {
		String departmentName = "Department of Happiness";
			
		Department savedDepartment = dao.createDepartment(departmentName);
		
		assertNotNull(savedDepartment);
		assertEquals(departmentName, savedDepartment.getName());
	}

	@Test
	public void department_can_be_found_by_id_after_being_created() {
		String departmentName = "Department of Happiness";
		
		Department savedDepartment = dao.createDepartment(departmentName);
		Department foundDepartment = dao.getDepartmentById(savedDepartment.getId());
	
		assertNotNull(foundDepartment);
		assertEquals(savedDepartment.getId(), foundDepartment.getId());
		assertEquals(savedDepartment.getName(), foundDepartment.getName());
	}
	
	@Test
	public void get_all_departments_gets_all_departments() {
		int beforeCount = (dao.getAllDepartments()).size();
		dao.createDepartment("Test Department");
		int afterCount = (dao.getAllDepartments()).size(); 
		
		Assert.assertEquals(afterCount, beforeCount + 1);
	}
	
	@Test 
	public void update_name_updates_correctly() {
		dao.updateDepartmentName((long) 1, "Test");
		List<Department> testDeptList = dao.searchDepartmentsByName("Test");
		String testDeptName = (testDeptList.get(0)).getName();
		Assert.assertEquals(testDeptName, "Test");
	} 
	
	@Test
	public void search_for_non_exists_id_returns_null_dept() {
		Department nullDept = dao.getDepartmentById((long) -1);
		String name = nullDept.getName();
		Assert.assertNull(name);
	}
}
