package dev.kalmh.producer.service;

import dev.kalmh.producer.model.JobProcess;
import dev.kalmh.producer.repository.RedisRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class RedisService {
    private final RedisRepository redisRepository;

    public RedisService(
            RedisRepository redisRepository
    ) {
        this.redisRepository = redisRepository;
    }

    public JobProcess retrieveJob(String jobId) {
        Optional<JobProcess> jobProcess = this.redisRepository.findById(jobId);
        if (jobProcess.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return jobProcess.get();
    }
}
