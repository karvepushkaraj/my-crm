package com.callcenter.crm.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("service_requests")
@Data
public class ServiceRequest {

    @Id
    private String id;
    private Customer customer;
    private String description;
    private Technician technician;
    private ServiceRequestStatus status;

    public ServiceRequest(Customer customer, String description) {
        this.customer = customer;
        this.description = description;
    }
}
