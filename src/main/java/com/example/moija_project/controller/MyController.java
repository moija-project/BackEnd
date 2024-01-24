package com.example.moija_project.controller;

import com.example.moija_project.dto.*;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.global.BaseResponse;
import com.example.moija_project.repository.UserRepository;
import com.example.moija_project.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.moija_project.global.BaseResponseStatus.NOT_EXISTS;
import static com.example.moija_project.global.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/my")
public class MyController {
    @Autowired
    MyService myService;
    @Autowired
    ClipService clipService;
    @Autowired
    UserService userService;
    @Autowired
    WaitingService waitingService;
    @PostMapping("/team/list")
    public BaseResponse<List> loadTeamList() throws BaseException {
        List<PostRes.ListPostRes> response = myService.loadRecruitList("testman1");
        return new BaseResponse<List>(response);
    }

    @PostMapping("/member/{postId}")
    public BaseResponse<List> loadMemberList(
            @PathVariable(value = "postId") Long postId
    ) throws BaseException {
        List<String> response = myService.loadMemberList(postId);
        return new BaseResponse<List>(response);
    }
    @PostMapping("/member/kick/{postId}")
    public BaseResponse<Void> kickMember(
            @PathVariable(value = "postId") Long postId,
            @RequestBody MypageReq.MyKickReq myKickReq
    ) throws BaseException {
        myService.kickMember(postId,myKickReq.getUserNickname());
        return new BaseResponse<Void>(SUCCESS);
    }
    @PostMapping("/waiting/list")
    public BaseResponse<List> loadWaitingList() throws BaseException{
        List<PostRes.ListPostRes> target = myService.loadRecruitList("testman1");
        List<MypageRes.WaitingListRes> response = waitingService.loadWaitingList(target);
        return new BaseResponse<List>(response);
    }

    @PostMapping("/waiting/{waitingId}")
    public BaseResponse<MypageRes.WaitingRes> viewWaiting(
            @PathVariable(value = "waitingId") Long waitingId
    )throws BaseException {
        MypageRes.WaitingRes response = waitingService.viewWaiting(waitingId);
        return new BaseResponse<>(response);
    }

    @PostMapping("/accept/{waitingId}")
    public BaseResponse<Void> acceptWaiting(
            @PathVariable(value = "waitingId") Long waitingId
    )throws BaseException {
        waitingService.acceptOrDeny(waitingId,true);
        return new BaseResponse<>(SUCCESS);
    }
    @PostMapping("/deny/{waitingId}")
    public BaseResponse<Void> denyWaiting(
        @PathVariable(value = "waitingId") Long waitingId
    )throws BaseException {
        waitingService.acceptOrDeny(waitingId,false);
        return new BaseResponse<>(SUCCESS);
    }

    @PostMapping("/clip")
    public BaseResponse<List> viewMyClip() throws BaseException{
        List<PostRes.ListPostRes> response = clipService.viewUsersClip("testman2");
        if(response.isEmpty())
            throw new BaseException(NOT_EXISTS);
        return new BaseResponse<List>(response);
    }

    @PostMapping("/profile")
    public BaseResponse<UserRes.ProfileRes> viewMyProfile() throws BaseException, IOException {
        UserRes.ProfileRes response = userService.loadProfile("testman1");
        return new BaseResponse<>(response);
    }
    @PatchMapping("/profile/edit/photo")
    public BaseResponse<Void> editPhoto(
            @RequestBody FileDTO fileDTO, @RequestParam("filename") Optional<String> filename
    ) throws BaseException, IOException {
        userService.saveProfile(fileDTO.getFileName(),fileDTO.getData(),"testman1");
        return new BaseResponse<Void>(SUCCESS);
    }
    @PatchMapping("/profile/edit/nick")
    public BaseResponse<Void> editNick(
            @RequestBody String newNickname
    ) throws BaseException {
        userService.editNickname(newNickname,"testman1");
        return new BaseResponse<Void>(SUCCESS);
    }



}
