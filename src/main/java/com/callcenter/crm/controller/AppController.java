package com.callcenter.crm.controller;

import com.callcenter.crm.model.Customer;
import com.callcenter.crm.model.ServiceRequest;
import com.callcenter.crm.service.SrvRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/service-request")
@AllArgsConstructor
public class AppController {

    private SrvRequestService srvRequestService;

    @GetMapping
    public ServiceRequest getSrvRequest(@RequestParam(value = "id", required = true) String srvRequestId) {
        return srvRequestService.getSrvRequest(srvRequestId);
    }

    @PostMapping
    public ServiceRequest createNewRequest(@RequestBody String input) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(input);
        Customer customer = mapper.treeToValue(node.get("customer"), Customer.class);
        String description = node.get("description") == null ? "" : node.get("description").asText();
        return srvRequestService.newSrvRequest(customer, description);
    }

    @DeleteMapping("/{id}")
    public void deleteSrvRequest(@PathVariable(value = "id", required = true) String srvRequestId) {
        srvRequestService.deleteSrvRequest(srvRequestId);
    }

    @PutMapping("/{id}")
    public void closeSrvRequest(@PathVariable(value = "id", required = true) String srvRequestId) {
        srvRequestService.closeSrvRequest(srvRequestId);
    }
}
