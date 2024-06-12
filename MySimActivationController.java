package com.telstra.simactivation.controller;

import com.telstra.simactivation.entity.SimCardActivation;
import com.telstra.simactivation.repository.SimCardActivationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class SimActivationController {

    private final RestTemplate restTemplate;
    private final SimCardActivationRepository repository;

    @Autowired
    public SimActivationController(RestTemplate restTemplate, SimCardActivationRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateSim(@RequestBody Map<String, String> request) {
        String iccid = request.get("iccid");
        String customerEmail = request.get("customerEmail");

        Map<String, String> activationRequest = new HashMap<>();
        activationRequest.put("iccid", iccid);

        ResponseEntity<Map> response = restTemplate.postForEntity(
            "http://localhost:8444/actuate",
            activationRequest,
            Map.class
        );

        boolean success = (boolean) response.getBody().get("success");

        SimCardActivation activation = new SimCardActivation();
        activation.setIccid(iccid);
        activation.setCustomerEmail(customerEmail);
        activation.setActive(success);

        repository.save(activation);

        return ResponseEntity.ok(success ? "Activation successful" : "Activation failed");
    }

    @GetMapping("/activation")
    public ResponseEntity<?> getActivation(@RequestParam Long simCardId) {
        Optional<SimCardActivation> activation = repository.findById(simCardId);

        if (activation.isPresent()) {
            return ResponseEntity.ok(activation.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
