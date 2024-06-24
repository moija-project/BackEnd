package com.example.moija_project.repository;

import com.example.moija_project.entities.Recruit;
import com.example.moija_project.entities.Waiting;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WaitingRepository extends JpaRepository<Waiting, Long> {
    Optional<Waiting> findByRecruitIdAndUserId(Long recruitId, String userId);

    boolean existsByRecruitIdAndUserId(Long teamId, String userId);
    Optional<Waiting> findByWaitingId(Long waitingId);

    //대기 수락하기.
    @Transactional
    @Modifying
    @Query(value = "UPDATE Waiting w SET w.isPermitted=true WHERE w.recruitId = :recruitId AND w.userId= :userId")
    void waitingPermmit(@Param("recruitId")Long recruitId, @Param("userId")String userId);

    List<Waiting> findAllByRecruitId(Long recruitId, Sort sort);

    List<Waiting> findAllByUserId(String userId);

    //내가 보낸 요청 목록.
    @Query(value = "SELECT r FROM Waiting AS w INNER JOIN w.recruit AS r ON w.recruitId = r.recruitId WHERE w.userId = :userId AND r.isAvailable = true")
    List<Recruit> loadRecruitByUserId(@Param("userId") String userId, Pageable pageable);

    //내 팀에 온 요청 목록
//    @Query(value = "SELECT w.user,r FROM Waiting AS w INNER JOIN w.recruit AS r ON w.recruitId = r.recruitId WHERE r.leaderId = :userId AND w.recruitId = r.recruitId AND r.isAvailable = true")
//    List<UserRecruit> findMyRecruitWaiting(String userId, Pageable pageable);
}
