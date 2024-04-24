package com.example.moija_project.repository;

import com.example.moija_project.entities.Spore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRepository extends JpaRepository<Spore,Long> {

}
