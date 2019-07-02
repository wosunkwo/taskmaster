package com.osunkwo.taskmaster;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

@EnableScan
public interface TaskMasterRepository extends CrudRepository<TaskMaster, UUID> {
    Optional<TaskMaster> findById(UUID id);
}
