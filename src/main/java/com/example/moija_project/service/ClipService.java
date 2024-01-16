package com.example.moija_project.service;

import com.example.moija_project.dto.PostReq;
import com.example.moija_project.entities.Clip;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.repository.ClipRepository;
import com.example.moija_project.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.moija_project.global.BaseResponseStatus.*;
import static com.example.moija_project.global.BaseResponseStatus.BAD_ACCESS;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClipService {
    ClipRepository clipRepository;
    RecruitRepository recruitRepository;
    public void userPostClip(PostReq.PostClipReq clipReq, String userId) throws BaseException {
        Long recruitId = clipReq.getRecruitId();
        //내 게시물에 스크랩시
        if(recruitRepository.findLeaderIdByRecruitId(recruitId).isPresent() &&
                recruitRepository.findLeaderIdByRecruitId(recruitId).get().equals(userId)) {
            throw new BaseException(CANNOT_CLIP_MINE);
        }
        //클립 누를때
        if(clipReq.getClip() == 1) {
            if(clipRepository.existsByRecruitIdAndUserId(recruitId,userId)) {
                throw new BaseException(LIKE_ALREADY_EXISTS);
            } else {
                //클립 수는 일단 구현은 안함. 굳이 싶어서? = 프론ㄴ트엔드에 숫자가 반영되는지??
                //클립한 사람과 모집의 관계 처리.
                Clip clip = Clip.builder()
                        .recruitId(recruitId)
                        .userId(userId)
                        .build();
                clipRepository.saveAndFlush(clip);
            }
            //좋아요를 취소할 때
        } else if (clipReq.getClip() == 0) {
            if(clipRepository.existsByRecruitIdAndUserId(recruitId,userId)) {
                clipRepository.deleteByRecruitIdAndUserId(recruitId,userId);
            } else {
                throw new BaseException(LIKE_NOT_EXISTS);
            }
        } else {
            throw new BaseException(BAD_ACCESS);
        }
    }
}
