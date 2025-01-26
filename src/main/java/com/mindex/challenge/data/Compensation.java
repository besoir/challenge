package com.mindex.challenge.data;

public class Compensation {
    private String employeeId;
    private String salary;
    private String effectiveDate;

    public Compensation() {
    }

    public String getEmployeeId() {
        return this.employeeId;
    }

    public String getSalary() {
        return this.salary;
    }

    public String getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEmployeeId(String id) {
        this.employeeId = id;
    }

    public void setSalary(String s) {
        this.salary = s;
    }

    public void setEffectiveDate(String date) {
        this.effectiveDate = date;
    }
}