package com.example.moija_project.service;

import com.example.moija_project.dto.MypageRes;
import com.example.moija_project.dto.PostRes;
import com.example.moija_project.entities.Member;
import com.example.moija_project.global.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.moija_project.global.BaseResponseStatus.*;
import static com.example.moija_project.service.PostService.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyService {
    @Autowired
    MemberService memberService;
    @Autowired
    PostService postService;
    @Autowired
    WaitingService waitingService;
    public List<PostRes.ListPostRes> loadRecruitList(String userId, int pageNo) throws BaseException {
        return postService.myPageList(userId,pageNo);
    }
    public List<PostRes.ListPostRes> loadRecruitListAll(String userId) throws BaseException {
        return postService.myPageListAll(userId,getLatestSort());
    }
    public List<PostRes.ListPostRes> loadSendList(String userId,int pageNo) throws BaseException {
        Sort sort = Sort.by(Sort.Direction.DESC, "waitingId");
        return waitingService.loadMyWaitingRecruit(userId,getPageable(pageNo,sort));
    }

    public List<MypageRes.MemListRes> loadMemberList(Long postId) throws BaseException {
        if(!postService.existPost(postId)) {
            throw new BaseException(BAD_ACCESS);
        }
        List<Member> members = memberService.findAllByRecruitId(postId);
        if(members.isEmpty()) {
            throw new BaseException(BAD_ACCESS);
        }
        return members.stream().sorted(Member::isLeader).map(m -> new MypageRes.MemListRes(m.getUser().getNickname(), m.getUserId())).collect(Collectors.toList());
    }

    public void kickMember(Long postId, String userNickname) throws BaseException {
        List<Member> members = memberService.findAllByRecruitId(postId);
        //팀 멤버에 킥하려는 애가 없는 경우
        if (members.stream().noneMatch(m -> userNickname.equals(m.getUser().getNickname()))) {
            throw new BaseException(USER_NOT_EXISTS);
        } else {
            members.stream().filter(m -> userNickname.equals(m.getUser().getNickname())).forEach(m -> {
                memberService.deleteByUserId(m.getUserId());
            });
        }

    }
    // 멤버를 찾아서 반환 -> 멤버에 연결된 Recruit이고, 멤버는 페이저블이 아니기 때문에 모두 출력 가능하다.
    // sql 단에서 하는게 더 성능이 좋을 것 같다.
    public List<PostRes.ListPostRes> findBymemberIdTeam(String userId) {
        List<Member> members= memberService.findAllByUserId(userId);
        //완전한 삭제가 되지 않은 게시글 처리
        members.removeIf(m -> !m.getRecruit().isAvailable());
        return makeList(members.stream().map(Member::getRecruit).toList());
    }

    public List<PostRes.ListPostRes> loadMyJoinedTeam(String userId, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC,"teamId");
        return makeList(memberService.loadMyJoinedTeam(userId,getPageable(pageNo,sort)));
    }
}
