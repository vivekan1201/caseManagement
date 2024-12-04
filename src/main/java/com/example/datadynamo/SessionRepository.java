package com.example.datadynamo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<Session, String> {
    // MongoRepository already provides basic CRUD operations
}
