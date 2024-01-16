package com.example.moija_project.service;

import com.example.moija_project.dto.PostReq;
import com.example.moija_project.entities.Waiting;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.repository.WaitingRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@NoArgsConstructor
@Slf4j
public class WaitingService {
    @Autowired
    WaitingRepository waitingRepository;
    @Autowired
    AnswerService answerService;
    public void saveWaiting(PostReq.PostWaitingReq postWaitingReq, Long postId, String userId) throws BaseException {
        Waiting waiting = Waiting.builder()
                .isAsk(postWaitingReq.isAsk())
                .numAnswer(postWaitingReq.getNumAnswer())
                .recruitId(postId)
                .userId(userId)
                //기본값
                .isPermitted(false)
                .build();

        answerService.saveAll(postWaitingReq.getAnswers(),postId,userId);
    }

    public boolean existTeamUser(Long teamId,String userId) {
        Optional<Waiting> waitingOptional = waitingRepository.findByRecruitIdAndUserId(teamId,userId);
        if(waitingOptional.isPresent())
            return true;
        else
            return false;
    }
}
