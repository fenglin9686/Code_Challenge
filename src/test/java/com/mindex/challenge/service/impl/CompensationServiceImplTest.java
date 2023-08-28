package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String employeeUrl;
    private String compensationUrl;
    private String compensationIdUrl;


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCompensationCreateRead() {
        Employee testEmployeeWithId = new Employee();
        testEmployeeWithId.setFirstName("John");
        testEmployeeWithId.setLastName("Doe");
        testEmployeeWithId.setDepartment("Engineering");
        testEmployeeWithId.setPosition("Developer");

        Employee testEmployeeWithoutId = new Employee();
        testEmployeeWithoutId.setFirstName("Feng");
        testEmployeeWithoutId.setLastName("Lin");
        testEmployeeWithoutId.setDepartment("Engineering");
        testEmployeeWithoutId.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployeeWithId, Employee.class).getBody();
        assertNotNull(createdEmployee.getEmployeeId());

        Employee testCompensationEmployee = new Employee();
        testCompensationEmployee.setEmployeeId(createdEmployee.getEmployeeId());

        Compensation testCompensationWithEmployeeId = new Compensation();
        testCompensationWithEmployeeId.setEmployee(testCompensationEmployee);
        testCompensationWithEmployeeId.setSalary(100000);
        testCompensationWithEmployeeId.setEffectiveDate("2023-8-28");

        Compensation createcompensation=restTemplate.postForEntity(compensationUrl, testCompensationWithEmployeeId, Compensation.class).getBody();
        assertCompensationEquivalence(testCompensationWithEmployeeId,createcompensation );
       assertEmployeeEquivalence(createdEmployee, createcompensation.getEmployee());


        Compensation testCompensationWithoutEmployeeId = new Compensation();
        testCompensationWithoutEmployeeId.setEmployee(testEmployeeWithoutId);
        testCompensationWithoutEmployeeId.setSalary(200000);
        testCompensationWithoutEmployeeId.setEffectiveDate("2023-9-28");

        Compensation createcompensationWithoutEmployeeId=restTemplate.postForEntity(compensationUrl, testCompensationWithoutEmployeeId, Compensation.class).getBody();
        assertCompensationEquivalence(testCompensationWithoutEmployeeId,createcompensationWithoutEmployeeId );
        assertEmployeeEquivalence(testEmployeeWithoutId, createcompensationWithoutEmployeeId.getEmployee());


        // Read checks
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createcompensation.getEmployee().getEmployeeId()).getBody();
        assertCompensationEquivalence(createcompensation, readCompensation);
        assertEmployeeEquivalence(createcompensation.getEmployee(), readCompensation.getEmployee());



    }
    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getSalary(), actual.getSalary());
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
