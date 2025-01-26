package com.mindex.challenge.dao;

import com.mindex.challenge.data.Compensation;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

/*
 * I think I'll write my thoughts here
 * I pretty much copied over all the employees code for Compensation
 * This is because we are doing the same computations as Employee but they should be separate
 */
@Repository
public interface CompensationRepository extends MongoRepository<Compensation, String> {
    Compensation findByEmployeeId(String employeeId);
}