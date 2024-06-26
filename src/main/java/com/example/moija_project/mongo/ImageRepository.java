package com.example.moija_project.mongo;

import com.example.moija_project.mongo_entity.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends MongoRepository<Image,Long> {
    List<Image> findAllByUrlContainsIgnoreCase(String recruitId);

    void deleteByRecruitIdAndNumber(Long postId, int index);

    boolean existsByRecruitIdAndNumber(Long recruitId, int number);

    Optional<Image> findByRecruitIdAndNumber(Long recruitId,int number);

    @Query("{'recruitId' : ?0}")
    @Update("{'$set': {'recruitId': ?1}}")
    void updateRecruitId(Long prev, Long next);
}
