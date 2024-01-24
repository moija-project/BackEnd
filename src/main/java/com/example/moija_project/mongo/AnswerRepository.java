package com.example.moija_project.mongo;

import com.example.moija_project.mongo_entity.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends MongoRepository<Answer,Long> {
    List<Answer> findAllByWaitingId(Long waitingId);

    boolean existsByWaitingId(Long waitingId);

    void deleteAllByWaitingId(Long waitingId);
}
