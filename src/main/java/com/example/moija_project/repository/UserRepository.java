package com.example.moija_project.repository;

import com.example.moija_project.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE User u SET u.nickname = :newNickname WHERE u.userId= :userId")
    void updateNickname(@Param(value = "newNickname") String newNickname, @Param(value = "userId")String userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE User u SET u.profile = :newProfile WHERE u.userId= :userId")
    void updateProfile(@Param(value = "newProfile") String newProfile, @Param(value = "userId")String userId);

    boolean existsByNickname(String newNickname);
}
