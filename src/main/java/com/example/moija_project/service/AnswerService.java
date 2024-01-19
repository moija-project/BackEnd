package com.example.moija_project.service;

import com.example.moija_project.entities.Answer;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.mongo.AnswerRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.moija_project.global.BaseResponseStatus.BAD_ACCESS;

@Service
@Slf4j
@NoArgsConstructor
public class AnswerService {
    @Autowired
    AnswerRepository answerRepository;
    public void saveAll(ArrayList<String> answers,Long waitingId, String userId) {
        List<Answer> answerCollection =
                answers.stream().map(s -> Answer.builder()
                        .waitingId(waitingId)
                        .userId(userId)
                        .answer(s)
                        .build()
                ).collect(Collectors.toList());
        answerRepository.saveAll(answerCollection);
    }
    List<Answer> findAllByWaitingId(Long waitingId) {
        return answerRepository.findAllByWaitingId(waitingId);
    }

    public void deleteByWaitingId(Long waitingId) throws BaseException {
        if(!answerRepository.existsByWaitingId())
            throw new BaseException(BAD_ACCESS);
        answerRepository.deleteAllByWaitingId(waitingId);
    }
}
