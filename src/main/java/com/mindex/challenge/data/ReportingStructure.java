package com.mindex.challenge.data;


public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;


    public ReportingStructure(Employee employee, int numberOfReports) {
        setEmployee(employee);
        setNumberOfReports(numberOfReports);
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getNumberOfReports() {
        return this.numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }

}
