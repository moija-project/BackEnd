package com.example.moija_project.mongo;


import com.example.moija_project.mongo_entity.Nickname;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NicknameRepository extends MongoRepository<Nickname,String> {
}
