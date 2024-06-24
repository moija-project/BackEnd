package com.example.moija_project.repository;

import com.example.moija_project.entities.Recruit;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    @Query("SELECT r.title FROM Recruit r WHERE r.recruitId = :recruitId AND r.isAvailable = true")
    Optional<String> findTitleByRecruitIdAndIsAvailableTrue(@Param("recruitId") Long Id);
    Optional<Recruit> findByRecruitId(Long Id);

    Page<Recruit> findAllByLeaderIdAndIsAvailableTrueAndStateRecruitTrue(String leaderId, Pageable pageable);
    Page<Recruit> findAllByLeaderIdAndIsAvailableTrueAndStateRecruitFalse(String leaderId, Pageable pageable);
    List<Recruit> findAllByLeaderId(String leaderId);
    List<Recruit> findAllByIsAvailableTrueOrderByStateRecruitDescLatestWriteDesc();
    List<Recruit> findAllByCategoryAndIsAvailableTrueOrderByStateRecruit(String category);
    Page<Recruit> findAllByCategoryContainingAndIsAvailableTrueAndStateRecruitTrue(String category, Pageable pageable);
    Page<Recruit> findAllByCategoryContainingAndIsAvailableTrueAndStateRecruitFalse(String category, Pageable pageable);

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

    Page<Recruit> findAllByTitleContainingAndIsAvailableTrue(String title,Pageable pageable);
    //v2 : 쿼리딴에서 모든걸 해결하는게 제일 성능이 좋을 것이다... 불필요한 데이터 참조가 적어지므로...
    Page<Recruit> findAllByTitleContainingIgnoreCaseAndStateRecruitAndIsAvailableTrue(String title, Boolean stateRecruit, Pageable pageable);
    Page<Recruit> findAllByContentsContainingIgnoreCaseAndStateRecruitAndIsAvailableTrue(String contents, Boolean stateRecruit, Pageable pageable);
    Page<Recruit> findAllByLeader_NicknameContainingIgnoreCaseAndStateRecruitAndIsAvailableTrue(String userId, Boolean stateRecruit, Pageable pageable);
    Page<Recruit> findAllByTitleContainingOrContentsContainingOrLeader_NicknameContainingAndStateRecruitAndIsAvailableTrue(String keyword1,String keyword2,String keyword3, Boolean stateRecruit, Pageable pageable);
    //내부에서만 접근함
    //성능 저하 - 만약 이사람이 쓴 글이 너무 많으면
    List<Recruit> findAllByLeaderIdAndIsAvailableTrue(String userId, Sort sort);
    // -- v2


    @Modifying
    @Transactional
    @Query("UPDATE Recruit r SET r.reliabilityRecruit= :score where r.recruitId= :postId")
    void updateReliabilityRecruit(@Param(value = "postId")Long postId, @Param(value = "score")float score);



}
