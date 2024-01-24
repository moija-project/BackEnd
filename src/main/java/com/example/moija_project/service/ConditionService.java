package com.example.moija_project.service;

import com.example.moija_project.dto.QnADTO;
import com.example.moija_project.mongo_entity.Condition;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.mongo.ConditionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.moija_project.global.BaseResponseStatus.BAD_ACCESS;
import static com.example.moija_project.global.BaseResponseStatus.NOT_EXISTS;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConditionService {
    @Autowired
    ConditionRepository conditionRepository;
    public void writeQuestions(ArrayList<QnADTO> questionReq, Long recruitId) throws BaseException {
        ArrayList<Condition> conditions = new ArrayList<>();

        if(!questionReq.isEmpty()) {
            questionReq.stream().forEach(qnA -> {
                Condition condition = Condition.builder()
                        .recruitId(recruitId)
                        .question(qnA.getQuestion())
                        .answer(qnA.getAnswer())
                        .build();
                conditions.add(condition);
            });
        } else {
            //컨디션 개수는 있다고 왔는데 컨디션 값이 없는 이상한 경우
            throw new BaseException(BAD_ACCESS);
        }
        conditionRepository.saveAll(conditions);
    }
    public List<QnADTO> viewCondition(Long recruitId) throws BaseException{
        List<Condition> conditions = conditionRepository.findByRecruitId(recruitId);
        if(conditions.isEmpty()) {
            throw new BaseException(NOT_EXISTS);
        }
        return conditions.stream()
                .map(c ->
                    new QnADTO(c.getQuestion(),c.getAnswer()))
                .collect(Collectors.toList());
    }
}
