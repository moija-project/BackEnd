package com.example.moija_project.service;

import com.example.moija_project.dto.PostRes;
import com.example.moija_project.entities.Member;
import com.example.moija_project.entities.Recruit;
import com.example.moija_project.entities.TeamId;
import com.example.moija_project.entities.Waiting;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.moija_project.service.PostService.getPageable;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    @Autowired
    private final MemberRepository memberRepository;
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

    public List<Member> findAllByRecruitId(Long postId) {
        return memberRepository.findAllByRecruitId(postId);
    }

    public void deleteByUserId(String userId) {
        memberRepository.deleteByUserId(userId);
    }

    public List<Member> findAllByUserId(String userId) {
        return  memberRepository.findAllByUserId(userId);
    }

    public List<Recruit> loadMyJoinedTeam(String userId, Pageable pageable) {

        return memberRepository.findRecruitMyJoinedTeam(userId,pageable).getContent();
    }
}
