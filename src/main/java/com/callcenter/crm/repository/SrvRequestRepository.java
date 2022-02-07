package com.callcenter.crm.repository;

import com.callcenter.crm.model.ServiceRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SrvRequestRepository extends MongoRepository<ServiceRequest, String> {
}
