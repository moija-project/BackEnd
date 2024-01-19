package com.example.moija_project.service;

import com.example.moija_project.entities.Member;
import com.example.moija_project.entities.TeamId;
import com.example.moija_project.entities.Waiting;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    @Autowired
    MemberRepository memberRepository;
    public void save(Long recruitId,String userId) throws BaseException {
        memberRepository.saveAndFlush(
                Member.builder()
                        .userId(userId)
                        .recruitId(recruitId)
                        .build()
        );
    }

    public boolean existTeamUser(Long postId, String userId) {
            return memberRepository.existsByRecruitIdAndUserId(postId,userId);
    }

}
