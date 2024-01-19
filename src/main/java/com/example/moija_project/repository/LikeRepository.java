package com.example.moija_project.repository;

import com.example.moija_project.entities.Like;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
     //@Query(value = "SELECT exists(SELECT 1 FROM Like l1_0 WHERE l1_0.recruitId= :recruitId and l1_0.userId like :userId )")
     boolean existsByRecruitIdAndUserId(Long recruitId, String userId);

     @Transactional
     void deleteByRecruitIdAndUserId (Long recruitId, String userId);
}
