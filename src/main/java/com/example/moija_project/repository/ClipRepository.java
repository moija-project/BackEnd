package com.example.moija_project.repository;

import com.example.moija_project.entities.Clip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClipRepository extends JpaRepository<Clip,Long> {
    boolean existsByRecruitIdAndUserId(Long recruitId, String userId);

    void deleteByRecruitIdAndUserId(Long recruitId, String userId);
}
