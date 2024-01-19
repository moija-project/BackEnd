package com.example.moija_project.mongo;

import com.example.moija_project.entities.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends MongoRepository<Answer,Long> {
    List<Answer> findAllByWaitingId(Long waitingId);

    boolean existsByWaitingId();

    void deleteAllByWaitingId(Long waitingId);
}
