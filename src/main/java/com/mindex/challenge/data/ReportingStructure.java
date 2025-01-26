package com.mindex.challenge.data;
import com.mindex.challenge.data.Employee;

public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;

    public ReportingStructure() {
    }

    //I added this constructor because in getting the reports i dont have to call the setter
    public ReportingStructure(Employee e) {
        this.employee = e;
    }

    //create getters
    public Employee getEmployee() {
        return this.employee;
    }

    public int getNumberOfReports() {
        return this.numberOfReports;
    }

    //create setters
    public void setEmployee(Employee e) {
        this.employee = e;
    }
    
    public void setNumberOfReports(int i) {
        this.numberOfReports = i;
    } 
}
