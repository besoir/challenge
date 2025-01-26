package com.mindex.challenge.data;
import com.mindex.challenge.data.Employee;

public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;

    public ReportingStructure() {
    }

    public ReportingStructure(Employee e) {
        this.employee = e;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public int getNumberOfReports() {
        return this.numberOfReports;
    }

    public void setEmployee(Employee e) {
        this.employee = e;
    }
    
    public void setNumberOfReports(int i) {
        this.numberOfReports = i;
    } 
}
