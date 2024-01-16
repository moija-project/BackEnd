package com.example.moija_project.repository;

import com.example.moija_project.entities.Member;
import com.example.moija_project.entities.Waiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByRecruitIdAndUserId(Long recruitId, String userId);
}
