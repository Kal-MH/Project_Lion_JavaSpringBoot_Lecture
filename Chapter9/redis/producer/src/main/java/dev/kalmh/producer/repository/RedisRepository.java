package dev.kalmh.producer.repository;

import dev.kalmh.producer.model.JobProcess;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<JobProcess, String> {
}
