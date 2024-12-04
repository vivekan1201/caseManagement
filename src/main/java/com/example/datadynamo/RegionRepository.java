package com.example.datadynamo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface RegionRepository extends MongoRepository<Region, String> {
    // No additional methods needed; basic CRUD operations are already available


    void deleteByName(String name);
}
