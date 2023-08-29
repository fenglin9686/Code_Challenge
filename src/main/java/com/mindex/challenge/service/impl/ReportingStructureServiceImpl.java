package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.ReportingStructureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);
    @Autowired
    private EmployeeService employeeService;

    /**
     * Returns a ReportingStructure object
     * Receiving employee id
     * Return new ReportingStructure  if record found
     * Otherwise throw error message
     * @param  employeeId: employee id
     * @return      compensation Object
     */
    @Override
    public ReportingStructure read(String employeeId) {
        LOG.debug("Reading reportingStructure with employeeId: [{}]", employeeId);
        Employee employee = employeeService.read(employeeId);
        int totalReports = this.calculateNumberOfReports(employee);
        return new ReportingStructure(employee, totalReports);
    }
    /**
     * Returns number of report
     * Receiving employee
     * Recursion method calculate number of report
     * And fill out employee information in variable of report, instead just id.
     * @param  employee: employee object
     * @return      number of report
     */
    public int calculateNumberOfReports(Employee employee) {
        int count = 0;
        List<Employee> employeeList = new ArrayList<>();
        List<Employee> reports = employee.getDirectReports();
        if (reports == null || reports.isEmpty()){
            return 0;
        }
        for (Employee report : reports) {
            Employee emp = employeeService.read(report.getEmployeeId());
            count++;
            employeeList.add(emp);
        }
        employee.setDirectReports(employeeList);
        for(Employee e:  employeeList){
            count+=calculateNumberOfReports(e);
        }
        return count;
    }

}
