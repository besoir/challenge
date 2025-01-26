package com.mindex.challenge.data;

public class Compensation {

    /*
     * I just used the same id as the employees table
     * I did this because you cant have an instance of Compensation without an existing employee
     * If you were to tie compensation to another table I would add a newPk
     * And then send the newPk to another table as a fk for that table
     */
    private String employeeId;
    private String salary;
    private String effectiveDate;

    public Compensation() {
    }

    //create getters
    public String getEmployeeId() {
        return this.employeeId;
    }

    public String getSalary() {
        return this.salary;
    }

    public String getEffectiveDate() {
        return this.effectiveDate;
    }

    //Create setters below
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