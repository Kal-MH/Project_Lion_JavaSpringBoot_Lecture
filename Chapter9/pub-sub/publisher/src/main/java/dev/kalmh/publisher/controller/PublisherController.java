package dev.kalmh.publisher.controller;

import dev.kalmh.publisher.service.PublisherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(
            PublisherService publisherService
    ) {
        this.publisherService = publisherService;
    }

    @GetMapping("/")
    public void sendMessage() {publisherService.publishMessage();}
}
