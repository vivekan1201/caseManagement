package com.example.datadynamo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomServiceRepository extends MongoRepository<CustomService, String> {
    // Custom queries can be added here if needed

CustomService findByServiceName(String serviceName);




}
