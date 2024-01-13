package com.example.moija_project.repository;

import com.example.moija_project.entities.Condition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConditionRepository extends MongoRepository<Condition,Long> {
    @Override
    <S extends Condition> List<S> saveAll(Iterable<S> entities);
    List<Condition> findByRecruitId(Long recruitId);
}
