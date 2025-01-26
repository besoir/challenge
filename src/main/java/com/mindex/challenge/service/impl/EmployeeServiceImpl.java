package com.mindex.challenge.service.impl;

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

    @Override
    public ReportingStructure readReportingStructure(String id) {
        LOG.debug("Creating employee with id [{}]", id);

        ReportingStructure report = new ReportingStructure(employeeRepository.findByEmployeeId(id));

        if (report == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        report.setNumberOfReports(getNumberOfReports(id));

        return report;
    }

    /*
     * This function grabs the numeber of reports based on the employee passed to it
     * Because I am used to functional languages I put this function here
     * Otherwise I would have made a function outside of this file in order to feel more organized
     */
    private int getNumberOfReports(String id) {
        Employee e = employeeRepository.findByEmployeeId(id);
        
        //Check if direct reports under current employee
        if(e.getDirectReports() == null) {
            return 0;
        }
        
        //no matter what now we will have at least this amount to add to the total
        int currCount = e.getDirectReports().size();

        //run the same thing across the current employees direct reports
        for(Employee f : e.getDirectReports()) {
            //increment recursively
            currCount += getNumberOfReports(f.getEmployeeId());
        }

        return currCount;
    } 

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }
}
