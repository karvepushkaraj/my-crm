package com.callcenter.crm.service;

import com.callcenter.crm.model.Customer;
import com.callcenter.crm.model.ServiceRequest;

public interface SrvRequestService {

    ServiceRequest getSrvRequest(String srvRequestId);

    ServiceRequest newSrvRequest(Customer customer, String description);

    void deleteSrvRequest(String srvRequestId);

    void closeSrvRequest(String srvRequestId);

}
