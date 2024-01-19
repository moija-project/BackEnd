package com.example.moija_project.controller;

import com.example.moija_project.dto.MypageReq;
import com.example.moija_project.dto.MypageRes;
import com.example.moija_project.dto.PostRes;
import com.example.moija_project.dto.QnADTO;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.global.BaseResponse;
import com.example.moija_project.repository.UserRepository;
import com.example.moija_project.service.ClipService;
import com.example.moija_project.service.MyService;
import com.example.moija_project.service.WaitingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.moija_project.global.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MyService myService;
    @Autowired
    ClipService clipService;
    @Autowired
    WaitingService waitingService;
    @PostMapping("/team/list")
    public BaseResponse<List> loadTeamList() throws BaseException {
        List<PostRes.ListPostRes> response = myService.loadRecruitList("testman1");
        return new BaseResponse<List>(response);
    }

    @PostMapping("/member/{postId}")
    public BaseResponse<List> loadMemberList(
            @PathVariable Long postId
    ) throws BaseException {
        List<String> response = myService.loadMemberList(postId,"testman1");
        return new BaseResponse<List>(response);
    }
    @PostMapping("/member/kick/{postId}")
    public BaseResponse<Void> kickMember(
            @PathVariable Long postId,
            @RequestBody MypageReq.MyKickReq myKickReq
    ) throws BaseException {
        myService.kickMember(postId,myKickReq.getUserNickname());
        return new BaseResponse<Void>(SUCCESS);
    }
    @PostMapping("/waiting/list")
    public BaseResponse<List> loadWaitingList() throws BaseException{
        List<MypageRes.WaitingListRes> response = waitingService.loadWaitingList("testman1");
        return new BaseResponse<List>(response);
    }

    @PostMapping("/waiting/{waitingId}")
    public BaseResponse<MypageRes.WaitingRes> viewWaiting(
            @PathVariable Long waitingId
    )throws BaseException {
        MypageRes.WaitingRes response = waitingService.viewWaiting(waitingId);
        return new BaseResponse<>(response);
    }

    @PostMapping("/accept/{waitingId}")
    public BaseResponse<Void> acceptWaiting(
            @PathVariable Long waitingId
    )throws BaseException {
        waitingService.acceptOrDeny(waitingId,true);
        return new BaseResponse<>(SUCCESS);
    }
    @PostMapping("/deny/{waitingId}")
    public BaseResponse<Void> denyWaiting(
        @PathVariable Long waitingId
    )throws BaseException {
        waitingService.acceptOrDeny(waitingId,false);
        return new BaseResponse<>(SUCCESS);
    }

    @PostMapping("/clip")
    public BaseResponse<List> viewMyClip() throws BaseException{
        List<PostRes.ListPostRes> response = clipService.viewUsersClip("testman1");
        return new BaseResponse<List>(response);
    }

//    @PostMapping("/profile")
//    public BaseResponse<MypageRes.ProfileRes> viewMyProfile() throws BaseException{
//        MypageRes.ProfileRes response = userRepository
//        return new BaseResponse<MypageRes.ProfileRes>(response);
//    }
//    @PatchMapping("/profile/edit/pho")
//    public BaseResponse<Void> editPhoto(){}
//    @PatchMapping("/profile/edit/nick")
//    public BaseResponse<Void> editNick(){}



}
