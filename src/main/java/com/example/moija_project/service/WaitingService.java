package com.example.moija_project.service;

import com.example.moija_project.dto.MypageRes;
import com.example.moija_project.dto.PostReq;
import com.example.moija_project.dto.PostRes;
import com.example.moija_project.dto.QnADTO;
import com.example.moija_project.entities.Answer;
import com.example.moija_project.entities.User;
import com.example.moija_project.entities.Waiting;
import com.example.moija_project.extractor.Genarator;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.repository.WaitingRepository;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.moija_project.global.BaseResponseStatus.BAD_ACCESS;
import static com.example.moija_project.global.BaseResponseStatus.NOT_EXISTS;

@Service
@NoArgsConstructor
@Slf4j
public class WaitingService {
    @Autowired
    WaitingRepository waitingRepository;
    @Autowired
    AnswerService answerService;
    @Autowired
    ConditionService conditionService;
    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;
    public void saveWaiting(PostReq.PostWaitingReq postWaitingReq, Long postId, String userId) throws BaseException {
        Waiting waiting = Waiting.builder()
                .isAsk(postWaitingReq.isAsk())
                .numAnswer(postWaitingReq.getNumAnswer())
                .recruitId(postId)
                .userId(userId)
                //기본값
                .isPermitted(false)
                .build();
        waitingRepository.saveAndFlush(waiting);
        answerService.saveAll(postWaitingReq.getAnswers(), waiting.getWaitingId(), userId);
    }

    public boolean existTeamUser(Long teamId,String userId) {
        return waitingRepository.existsByRecruitIdAndUserId(teamId,userId);
    }

    public List<MypageRes.WaitingListRes> loadWaitingList(String userId) throws BaseException {
        //리크루트 이름 다 가져옴
        List<MypageRes.WaitingListRes> myposts = postService.list("all","latest",Optional.of(userId)).stream()
                .map(post-> MypageRes.WaitingListRes.builder()
                        .latestWrite(post.getLatest_write())
                        .title(post.getTitle())
                        .postId(post.getPost_id())
                        .build()).toList();
        //리크루트 이름으로부터 웨이팅 정보를 다 가져옴
        for (MypageRes.WaitingListRes post : myposts) {
            List<Waiting> waitings = waitingRepository.findAllByRecruitId(post.getPostId());
            List<MypageRes.MemDto> members = waitings.stream().map(w ->
                    MypageRes.MemDto.builder()
                            .waitingId(w.getWaitingId())
                            .is_ask(w.isAsk())
                            .nickname(w.getUser().getNickname())
                            .build()).toList();
            post.setUsers(members);
        }

        return myposts;
    }

    public MypageRes.WaitingRes viewWaiting(Long waitingId) throws BaseException {
        Optional<Waiting> waiting = waitingRepository.findByWaitingId(waitingId);

        if(waiting.isPresent()) {
            MypageRes.WaitingRes waitingRes = MypageRes.WaitingRes.builder()
                    .is_ask(waiting.get().isAsk())
                    .nickname(waiting.get().getUser().getNickname())
                    .gender(waiting.get().getUser().isGender() ? "여":"남")
                    .genaration(new Genarator().changeToGenaration(waiting.get().getUser().getBirth()))
                    .build();
            List<QnADTO> qnaList = conditionService.viewCondition(waiting.get().getRecruitId());
            List<Answer> answers = answerService.findAllByWaitingId(waitingId);
            //혹시나 답변개수랑 질문개수랑 다르면 잘못된거.
            if(qnaList.size() != answers.size()) {throw new BaseException(BAD_ACCESS);}
            //일단 qnaList에 덮어쓰기하는 방식인데 만약 단답형말고 질문 고르기로 바꾼다면 객체를 두개 줄듯
            for(int i = 0 ; i < qnaList.size(); i++) {
                qnaList.get(i).setAnswer(answers.get(i).getAnswer());
            }
            waitingRes.setQnas(qnaList);
            return waitingRes;
        }else {
            throw new BaseException(NOT_EXISTS);
        }

    }

    public void acceptOrDeny(Long waitingId, boolean isAccept) throws BaseException {
        if(!waitingRepository.existsById(waitingId))
            throw new BaseException(BAD_ACCESS);
        Waiting waitingInfo = waitingRepository.findByWaitingId(waitingId).get();
        if(isAccept){
            memberService.save(waitingInfo.getRecruitId(),waitingInfo.getUserId());
        }
        else {
        }
        //일정기간 보관하면서 보이는게...지금까지 보관한거 많으니까 그냥 없앨거야. 만약 안 없앤다면 isPermitted조작으로 수정
        if(waitingInfo.getNumAnswer()!=0) {
            answerService.deleteByWaitingId(waitingId);
        }
        waitingRepository.deleteById(waitingId);

    }
}
