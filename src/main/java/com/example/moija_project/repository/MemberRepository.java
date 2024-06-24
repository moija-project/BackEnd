package com.example.moija_project.repository;

import com.example.moija_project.dto.PostRes;
import com.example.moija_project.entities.Member;
import com.example.moija_project.entities.Recruit;
import com.example.moija_project.entities.TeamId;
import com.example.moija_project.entities.Waiting;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    List<Member> findAllByUserId(String userId);

    @Query("SELECT m.recruit FROM Member AS m \n" +
            "INNER JOIN m.recruit AS r ON m.recruitId = r.recruitId\n" +
            "WHERE m.userId = :userId AND m.userId != r.leaderId AND r.isAvailable = true")
    Page<Recruit> findRecruitMyJoinedTeam(@Param("userId") String userId, Pageable pageable);
}
