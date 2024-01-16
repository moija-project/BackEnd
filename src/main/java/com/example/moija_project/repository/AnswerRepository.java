package com.example.moija_project.repository;

import com.example.moija_project.entities.Answer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends MongoRepository<Answer,Long> {


}
