package com.callcenter.crm.service;

import com.callcenter.crm.model.Customer;
import com.callcenter.crm.model.ServiceRequest;
import com.callcenter.crm.model.ServiceRequestStatus;
import com.callcenter.crm.model.Technician;
import com.callcenter.crm.repository.SrvRequestRepository;
import com.callcenter.crm.util.ServiceLayerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static com.callcenter.crm.model.ServiceRequestStatus.CLOSED;
import static com.callcenter.crm.model.ServiceRequestStatus.OPEN;

@Service
@Transactional
@AllArgsConstructor
public class SrvRequestServiceImpl implements SrvRequestService {

    private SrvRequestRepository repository;

    private WebClient webClient;

    @Override
    public ServiceRequest getSrvRequest(String srvRequestId) {
        return repository.findById(srvRequestId)
                .orElseThrow(() -> new ServiceLayerException("Service Request Not Found"));
    }

    @Override
    public ServiceRequest newSrvRequest(Customer customer, String description) {
        ServiceRequest srvRequest = new ServiceRequest(customer, description);
        srvRequest.setStatus(OPEN);
        repository.insert(srvRequest);
        webClient.post()
                .uri("/allocate")
                .body(Mono.just(srvRequest), ServiceRequest.class)
                .retrieve().bodyToMono(String.class)
                .onErrorResume(e -> Mono.just("Error"))
                .filter(m -> !m.equals("Error"))
                .map(l -> {
                    ObjectMapper mapper = new ObjectMapper();
                    Technician technician = null;
                    try {
                        TreeNode node = mapper.readTree(l);
                        technician = mapper.treeToValue(node, Technician.class);
                    } catch (JsonProcessingException e) {
                        throw new ServiceLayerException("Json Processing Exception", e);
                    }
                    return technician;
                }).subscribe(l -> {
                    srvRequest.setTechnician(l);
                    repository.save(srvRequest);
                });
        return srvRequest;
    }

    @Override
    public void deleteSrvRequest(String srvRequestId) {
        ServiceRequest srvRequest = getSrvRequest(srvRequestId);
        if(srvRequest.getStatus().equals(CLOSED))
            throw new ServiceLayerException("Cannot delete closed service call");
        String technicianId = srvRequest.getTechnician().getTechnicianId();
        String json = "{'technicianId':'%s','requestId':'%s'}";
        repository.deleteById(srvRequestId);
//        webClient.post()
//                .uri("/technician")
//                .body(Mono.just(String.format(json,technicianId,srvRequestId)),String.class)
//                .retrieve()
//                .onStatus(p -> !p.is2xxSuccessful() ? true : false, r -> Mono.error(new ServiceLayerException("Remote Server did not respond")))
//                .bodyToMono(Void.class)
//                .subscribe();
    }

    @Override
    public void closeSrvRequest(String srvRequestId) {
        ServiceRequest srvRequest = getSrvRequest(srvRequestId);
        srvRequest.setStatus(CLOSED);
        repository.save(srvRequest);
    }
}
