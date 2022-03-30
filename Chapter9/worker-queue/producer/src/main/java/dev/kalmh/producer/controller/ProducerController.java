package dev.kalmh.producer.controller;

import dev.kalmh.producer.service.ProducerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
    private final ProducerService producerService;

    public ProducerController(
            ProducerService producerService
    ) {
        this.producerService = producerService;
    }

    @GetMapping("/")
    public void sendMessage() {producerService.send();}
}
