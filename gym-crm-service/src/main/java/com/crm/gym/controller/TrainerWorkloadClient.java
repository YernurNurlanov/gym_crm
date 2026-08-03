package com.crm.gym.controller;

import com.crm.gym.config.FeignSecurityConfig;
import com.crm.gym.dto.TrainerWorkloadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "trainer-workload-service",
        configuration = FeignSecurityConfig.class
)
public interface TrainerWorkloadClient {

    @PostMapping("/workloads")
    void updateTrainerWorkload(@RequestBody TrainerWorkloadRequest request);
}
