package com.example.moija_project.service;

import com.example.moija_project.dto.PostRes;
import com.example.moija_project.entities.Member;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.moija_project.global.BaseResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyService {
    MemberRepository memberRepository;
    @Autowired
    PostService postService;
    public List<PostRes.ListPostRes> loadRecruitList(String userId) throws BaseException {
        //all latest는 적용되지 않음...
        return postService.list("all","latest", Optional.of("testman1"));
    }

    public List<String> loadMemberList(Long postId, String userId) throws BaseException {
        if(!postService.existPost(postId)) {
            throw new BaseException(BAD_ACCESS);
        }
        List<Member> members = memberRepository.findAllByRecruitId(postId);
        if(members.isEmpty()) {
            throw new BaseException(BAD_ACCESS);
        }
        return members.stream().map(m -> m.getUser().getNickname()).collect(Collectors.toList());
    }

    public void kickMember(Long postId, String userNickname) throws BaseException {
        List<Member> members = memberRepository.findAllByRecruitId(postId);
        //팀 멤버에 킥하려는 애가 없는 경우
        if(members.stream().noneMatch(m -> userNickname.equals(m.getUser().getNickname()))){
            throw new BaseException(USER_NOT_EXISTS);
        } else {
            members.stream().filter(m-> userNickname.equals(m.getUser().getNickname())).forEach(m-> {
                memberRepository.deleteByUserId(m.getUserId());
            });
        }

    }
}
