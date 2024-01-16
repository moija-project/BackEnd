package com.example.moija_project.service;

import com.example.moija_project.entities.Answer;
import com.example.moija_project.repository.AnswerRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@NoArgsConstructor
public class AnswerService {
    @Autowired
    AnswerRepository answerRepository;
    public void saveAll(ArrayList<String> answers,Long postId, String userId) {
        List<Answer> answerCollection =
                answers.stream().map(s -> Answer.builder()
                        .recruitId(postId)
                        .userId(userId)
                        .answer(s)
                        .build()
                ).collect(Collectors.toList());
        answerRepository.saveAll(answerCollection);
    }
}
