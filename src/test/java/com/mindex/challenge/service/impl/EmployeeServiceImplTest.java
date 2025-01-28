package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeesUrl;
    private String employeeIdUrl;
    private String reportingStructureIdUrl;

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeesUrl = "http://localhost:" + port + "/employees";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureIdUrl = "http://localhost:" + port + "/reportingstructure/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Lord Stark");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }

    @Test
    public void testCreateReportingStructure() {
        //this should be the numberOfReports after all is done
        final int ACTUALNUMBEROFREPORTS = 1;

        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        Employee testEmployee2 = new Employee();
        testEmployee2.setFirstName("Tommy");
        testEmployee2.setLastName("Hanley");
        testEmployee2.setDepartment("Engineering");
        testEmployee2.setPosition("Developer II");

        //create first employee
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        //make sure added
        assertNotNull(createdEmployee.getEmployeeId());
        //set test id to created
        testEmployee.setEmployeeId(createdEmployee.getEmployeeId());

        //make a new list and put the first employee in to make them a direct report
        List<Employee> toPut = new ArrayList<Employee>();
        toPut.add(testEmployee);
        testEmployee2.setDirectReports(toPut);

        //post the second employee that contains direct reports
        Employee createdEmployee2 = restTemplate.postForEntity(employeeUrl, testEmployee2, Employee.class).getBody();
        //make sure it worked
        assertNotNull(createdEmployee2.getEmployeeId());
        
        ReportingStructure fromCall = restTemplate.getForEntity(reportingStructureIdUrl, ReportingStructure.class, createdEmployee2.getEmployeeId()).getBody();

        assertReportingStructureEquivalence(fromCall, ACTUALNUMBEROFREPORTS);
    }

    @Test
    public void testEmployees() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("Lord Stark");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        //call the list of employees endpoint
        ResponseEntity<List<Employee>> response = restTemplate.exchange(
                employeesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Employee>>() {}
        );

        //this is the count of stuff in our db before a add a new employee
        int countBeforeAdd = response.getBody().size();


        //add new employee
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        //make sure added
        assertNotNull(createdEmployee.getEmployeeId());

        //call the list of employees endpoint
        ResponseEntity<List<Employee>> responseUpdate = restTemplate.exchange(
                employeesUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Employee>>() {}
        );


        //create new list from response
        List<Employee> employeesUpdate = responseUpdate.getBody();

        assertCountNotEquivalence(countBeforeAdd, employeesUpdate.size());
        
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    //for the endpoint of the count of employees
    //check if num added equal to the constant
    private static void assertCountNotEquivalence(int unexpected, int actual) {
      assertNotEquals(unexpected, actual);
    }

    //we want an assert function like the one above for ReportingStructure
    //we just will test for numberOfReports because we can call the existing test for the employees in the obj
    private static void assertReportingStructureEquivalence(ReportingStructure actual, int expected) {
      assertEquals(expected, actual.getNumberOfReports());
  }
}
