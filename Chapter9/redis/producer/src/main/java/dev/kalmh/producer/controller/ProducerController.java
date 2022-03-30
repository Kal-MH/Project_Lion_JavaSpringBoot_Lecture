package dev.kalmh.producer.controller;

import dev.kalmh.producer.model.JobProcess;
import dev.kalmh.producer.service.ProducerService;
import dev.kalmh.producer.service.RedisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
    private final ProducerService producerService;
    private final RedisService redisService;

    public ProducerController(
            ProducerService producerService,
            RedisService redisService
    ) {
        this.producerService = producerService;
        this.redisService = redisService;
    }

    @GetMapping("/")
    public void sendMessage() {producerService.send();}

    @GetMapping("/{jobId}")
    public JobProcess getResult(@PathVariable("jobId") String jobId) {
        return redisService.retrieveJob(jobId);
    }
}
