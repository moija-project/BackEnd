package com.example.moija_project.service;

import com.example.moija_project.dto.PostReq;
import com.example.moija_project.dto.PostRes;
import com.example.moija_project.dto.QnADTO;
import com.example.moija_project.dto.UserCheckReq;
import com.example.moija_project.entities.Recruit;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.repository.RecruitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.moija_project.global.BaseResponseStatus.BAD_ACCESS;
import static com.example.moija_project.global.BaseResponseStatus.NOT_EXISTS;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    private final ConditionService conditionService;
    @Autowired
    private final RecruitRepository recruitRepository;
    public void writePost(PostReq.PostWriteReq postWriteReq, Long postId) throws BaseException {
        Recruit recruit;

        //수정일때는 어떤 것을 할지
        if(!postWriteReq.isChanged()) {
            //초기에만 가능한 것들.
            recruit = Recruit.builder()
                    .leaderId(postWriteReq.getLeaderId())
                    .category(postWriteReq.getCategory())
                    .timeFirstWrite(new Timestamp(System.currentTimeMillis()))
                    .likes(0L)
                    .views(0L)
                    .reliabilityRecruit(0)
                    //여기부터 초기 기본값 정하기
                    .latestWrite(new Timestamp(System.currentTimeMillis()))
                    .isAvailable(true)
                    .stateRecruit(true)
                    .build();
            recruit = setDupColumn(recruit,postWriteReq);
        } else {
            if(recruitRepository.findByRecruitIdAndIsAvailableTrue(postId).isPresent()){
                recruit = recruitRepository.findByRecruitIdAndIsAvailableTrue(postId).get();
            } else {
                throw new BaseException(BAD_ACCESS);
            }
            recruit = setDupColumn(recruit,postWriteReq);
            recruit.setRecruitId(null);
            recruit.setTimeLastWrite(new Timestamp(System.currentTimeMillis()));
            recruit.setChanged(true);
            recruitRepository.notAvailable(postId);
        }

        //최종 저장 -> sql 모집에 하나, nosql 조건에 하나 저장
        recruitRepository.save(recruit);
        //조건이 있다면
        if(postWriteReq.getNumCondition() != 0) {
            //recruit의 id를 가져와서 같이 서비스로 넘김
            conditionService.writeQuestions(
                    postWriteReq.getConditions(),
                    recruit.getRecruitId()
            );
        }
    }

    public void remove(UserCheckReq userCheckReq, Long postId) throws BaseException {
        recruitRepository.notAvailable(postId);
    }
    private Recruit setDupColumn(Recruit recruit,PostReq.PostWriteReq postWriteReq) {
        recruit.setTitle(postWriteReq.getTitle());
        recruit.setContents(postWriteReq.getContents());
        recruit.setPenalty(postWriteReq.getPenalty());
        recruit.setNumCondition(postWriteReq.getNumCondition());
        return recruit;
    }

    public PostRes.ListPostRes list(String category, String view_type) throws BaseException {
        ArrayList<Recruit> recruitList = new ArrayList<>();

        //데이터베이스에서 빼놓은 entity
        switch(category){
            case "all":
                recruitList.addAll(recruitRepository.findAllByIsAvailableTrueOrderByStateRecruitDescLatestWriteDesc());
                break;
            case "hobby":
            case "language":
            case "study" :
            case "employ":
            case "etc" :
                recruitList.addAll(recruitRepository.findAllByCategoryAndIsAvailableTrueOrderByStateRecruit(category));
                break;
            default:
                throw new BaseException(BAD_ACCESS);
        }

        PostRes.ListPostRes response = new PostRes.ListPostRes();
        //entity의 List에서 response로 전달
        response.setPost_list(recruitList.stream().map( recruit ->
            new PostRes.ListPostRes.Post(recruit.getRecruitId(),
                    recruit.isStateRecruit(),
                    recruit.getTitle(),
                    recruit.getContents().substring(0,20)+"...",
                    recruit.getLeader().getNickname(),
                    recruit.getLatestWrite(),
                    recruit.getLikes(),
                    recruit.getViews()
                )
            ).collect(Collectors.toList()));
        return response;
    }

    public PostRes.ReadPostRes view(Long postId) throws BaseException {
        Optional<Recruit> selected = recruitRepository.findByRecruitIdAndIsAvailableTrue(postId);
        if(!selected.isEmpty()) {
            Recruit recruit = selected.get();
            updateView(recruit.getRecruitId());
            return PostRes.ReadPostRes.builder()
                    .title(recruit.getTitle())
                    .contents(recruit.getContents())
                    .stateRecruit(recruit.isStateRecruit())
                    .leaderNickname(recruit.getLeader().getNickname())
                    .isChanged(recruit.isChanged())
                    .penalty(recruit.getPenalty())
                    .reliabilityRecruit(recruit.getReliabilityRecruit())
                    .latestWrite(recruit.getLatestWrite())
                    .likes(recruit.getLikes())
                    .views(recruit.getViews())
                    .build();
        } else {
            //보려는 포스트가 없을 경우
            throw new BaseException(NOT_EXISTS);
        }
    }
    @Transactional
    public int updateView(Long id) {
        return recruitRepository.updateView(id);
    }

    public List<QnADTO> viewQuest(UserCheckReq userCheckReq, Long postId) throws BaseException {
        List<QnADTO> questions = conditionService.viewCondition(postId);
        return questions;
    }

}
