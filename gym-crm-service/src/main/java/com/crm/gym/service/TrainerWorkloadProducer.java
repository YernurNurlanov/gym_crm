package com.crm.gym.service;

import com.crm.gym.dto.TrainerWorkloadRequest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class TrainerWorkloadProducer {

    private final JmsTemplate jmsTemplate;

    public TrainerWorkloadProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void send(TrainerWorkloadRequest request) {
        jmsTemplate.convertAndSend(
                "trainer-workload-queue",
                request);
    }
}