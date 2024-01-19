package com.example.moija_project.repository;

import com.example.moija_project.entities.Member;
import com.example.moija_project.entities.TeamId;
import com.example.moija_project.entities.Waiting;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, TeamId> {
    Optional<Member> findByRecruitIdAndUserId(Long recruitId, String userId);

    boolean existsByRecruitIdAndUserId(Long postId, String userId);

    List<Member> findAllByRecruitId(Long recruitId);

    @Transactional
    void deleteByUserId(String userId);
}
