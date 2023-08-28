package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureImplTest {

    private String employeeUrl;
    private String getReportingStructureUrl;


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        getReportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testCreateReadUpdate() {
        List<Employee>  empList=new ArrayList<>();
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());

        empList.add(createdEmployee);

        Employee testReportingStructureEmployee = new Employee();
        testReportingStructureEmployee.setFirstName("Feng");
        testReportingStructureEmployee.setLastName("Lin");
        testReportingStructureEmployee.setDepartment("Engineering II");
        testReportingStructureEmployee.setPosition("Developer");
        testReportingStructureEmployee.setDirectReports(empList);
        Employee createdReportingStructureEmployee = restTemplate.postForEntity(employeeUrl, testReportingStructureEmployee, Employee.class).getBody();

        ReportingStructure readReportingStructure = restTemplate.getForEntity(getReportingStructureUrl, ReportingStructure.class, createdReportingStructureEmployee.getEmployeeId()).getBody();
        assertEquals(createdReportingStructureEmployee.getDirectReports().size(), readReportingStructure.getNumberOfReports());
        assertEmployeeEquivalence(createdReportingStructureEmployee.getDirectReports().get(0), createdEmployee);

    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
