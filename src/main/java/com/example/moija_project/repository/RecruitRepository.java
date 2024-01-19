package com.example.moija_project.repository;

import com.example.moija_project.entities.Recruit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Repository
public interface RecruitRepository extends JpaRepository<Recruit,Long> {

    //좋아요 API
    @Modifying
    @Transactional
    @Query(value="update Recruit r set r.likes = r.likes + 1 where r.recruitId = :recruitId")
    Integer updateLikeUp(@Param("recruitId") Long recruitId);
    @Modifying
    @Transactional
    @Query(value="update Recruit r set r.likes = r.likes - 1 where r.recruitId = :recruitId")
    Integer updateLikeDown(@Param("recruitId") Long recruitId);

    //조회수 API
    @Modifying
    @Transactional
    @Query(value="update Recruit r set r.views = r.views + 1 where r.recruitId = :recruitId")
    Integer updateView(@Param("recruitId") Long recruitId);
    @Modifying
    @Transactional
    @Query(value="update Recruit r set r.isAvailable=false where r.recruitId = :recruitId")
    Integer notAvailable(@Param("recruitId") Long recruitId);

    Optional<Recruit> findByRecruitIdAndIsAvailableTrue(Long Id);

    List<Recruit> findAllByLeaderIdAndIsAvailableTrue(String leaderId);
    List<Recruit> findAllByIsAvailableTrueOrderByStateRecruitDescLatestWriteDesc();
    List<Recruit> findAllByCategoryAndIsAvailableTrueOrderByStateRecruit(String category);

    //user 체크하기 위함
    @Query("SELECT r.leaderId FROM Recruit r WHERE r.recruitId = :recruitId")
    Optional<String> findLeaderIdByRecruitId(@Param("recruitId") Long recruitId);


    @Modifying
    @Transactional
    @Query("UPDATE Recruit r SET r.latestWrite= :latest where r.recruitId= :recruitId")
    void updateTimeLatest(@Param(value = "latest") Timestamp latest, @Param(value = "recruitId") Long recruitId);

    boolean existsByRecruitIdAndIsAvailableTrue(Long recruitId);

    @Modifying
    @Transactional
    @Query("UPDATE Recruit r SET r.stateRecruit=:stateRecruit where r.recruitId= :postId")
    void updateStateRecruit(@Param(value = "postId")Long postId, @Param(value = "stateRecruit")boolean stateRecruit);

    @Query("SELECT r.stateRecruit FROM Recruit r WHERE r.recruitId= :postId")
    boolean isRecruiting(@Param(value = "postId")Long postId);
}