package com.mindex.challenge.service.impl;

import java.util.List;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.data.ReportingStructure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    //I wanted to add this endpoint for my own testing and often I use lists of all employees in my current projects
    @Override
    public List<Employee> readEmployees() {
        LOG.debug("Grabbing all Employees");

        //create a list from the findAll method of MongoRepo
        List<Employee> employees = employeeRepository.findAll();
        
        return employees;
    }

    @Override
    public ReportingStructure readReportingStructure(String id) {
        LOG.debug("Creating Reporting Structure for eployee with id: [{}]" + id);

        //get the employee because we want to check if its valid to throw the error earlier
        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        ReportingStructure report = new ReportingStructure(employee);

        report.setNumberOfReports(getNumberOfReports(id));

        return report;
    }

    /*
     * This function grabs the number of reports based on the employee passed to it
     * If you wanted to speed this up you could cache the results
     * This function is located here because then theres limited passing of the employee list from employeeRepository
     * Private because we dont use it outside of the method
     */
    private int getNumberOfReports(String id) {
        //grab the employee we want to start the current count from
        Employee e = employeeRepository.findByEmployeeId(id);

        //if you wanted speed you could create a hashmap by the id here for a cache
        
        //Check if direct reports under current employee
        //null means there arent any, originally I didnt look at the data correctly
        //and thought that I would be able to check size but that is not true
        if(e.getDirectReports() == null) {
            return 0;
        }
        
        //no matter what now we will have at least this amount to add to the total
        int currCount = e.getDirectReports().size();

        //now we want to grab the number of reports under the current employee
        //run the same thing across the current employees direct reports
        //If we ran through the employeeRepository list one by one the function would take longer and longer
        //So lets only hit the employees that we need to
        for(Employee f : e.getDirectReports()) {
            //increment count with recursive use of function
            currCount += getNumberOfReports(f.getEmployeeId());
        }

        //boom heres your number
        return currCount;
    } 

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }
}
