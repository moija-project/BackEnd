package com.example.moija_project.repository;

import com.example.moija_project.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like,Long> {
     boolean existsByRecruitIdAAndUserId(@Param(value = "recruitId")Long recruitId, @Param(value = "userId")String userId);


}
