package com.example.moija_project.controller;

import com.example.moija_project.dto.PostReq;
import com.example.moija_project.dto.PostRes;
import com.example.moija_project.dto.QnADTO;
import com.example.moija_project.dto.UserCheckReq;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.global.BaseResponse;
import com.example.moija_project.service.ConditionService;
import com.example.moija_project.service.LikeService;
import com.example.moija_project.service.PostService;
import com.example.moija_project.service.UserCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static com.example.moija_project.global.BaseResponseStatus.*;

//scheduled remover 필요!! 현재 지워진 글은 비트만 바꿔서 안보이게 하고 있으므로, 1달에 한번씩 데이터베이스 삭제, 3달 이상 지난 사용 불가능 포스트 삭제!!!


@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final UserCheckService userCheckService;
    private final ConditionService conditionService;

    private final LikeService likeService;
    @PostMapping("/write")
    public BaseResponse<Void> writePost(
            @RequestBody PostReq.PostWriteReq postWriteReq
    ) throws BaseException, IOException {
        postService.writePost(postWriteReq,0L);
        return new BaseResponse<Void>(SUCCESS);
    }

    @PatchMapping("/write/{postId}")
    public BaseResponse<Void> editPost(
            @PathVariable(name="postId") Long postId,
            @RequestBody PostReq.PostWriteReq postWriteReq
    ) throws BaseException, IOException {
        postService.writePost(postWriteReq,postId);
        return new BaseResponse<Void>(SUCCESS);
    }

    @DeleteMapping("/delete/{postId}")
    public BaseResponse<Void> deletePost(
            @PathVariable(name="postId") Long postId,
            @RequestBody UserCheckReq userCheckReq
    ) throws BaseException, IOException {
        postService.remove(userCheckReq,postId);
        return  new BaseResponse<Void>(SUCCESS);
    }

    @GetMapping("/list")
    public BaseResponse<List> loadPostList(
            @RequestParam(value="category") String category,
            @RequestParam(value = "view_type") String viewType
    ) throws BaseException, IOException {
        List<PostRes.ListPostRes> response = postService.list(category,viewType,Optional.empty());
        return new BaseResponse<List>(response);
    }

    @GetMapping("/page")
    public BaseResponse<PostRes.ReadPostRes> viewPost(
            @RequestParam(value = "post_id") Long postId
    ) throws BaseException, IOException {
        PostRes.ReadPostRes response = postService.view(postId);
        return new BaseResponse<>(response);
    }

    @PostMapping("/like")
    public BaseResponse<Void> likePost(
            @RequestBody PostReq.PostLikeReq postLikeReq
    ) throws BaseException, IOException {
        if(!postService.existPost(postLikeReq.getRecruitId())) {
            throw new BaseException(NOT_EXISTS);
        }
        likeService.userPostLike(postLikeReq,"testman2");
        return new BaseResponse<>(SUCCESS);
    }

    @PostMapping("/clip")
    public BaseResponse<Void> clipPost(
            @RequestBody PostReq.PostLikeReq postLikeReq
    ) throws BaseException, IOException {
        likeService.userPostLike(postLikeReq,"testman2");
        return new BaseResponse<>(SUCCESS);
    }

    @PostMapping("/question/{postId}")
    public BaseResponse<List> viewQuestion(
            @PathVariable(name="postId") Long postId,
            @RequestBody UserCheckReq.UserIdReq userIdReq
    ) throws BaseException, IOException {
        if(!postService.existPost(postId))
            throw new BaseException(NOT_EXISTS);
        if(userCheckService.check(userIdReq)){//이게 필요할까 과연?
            List<QnADTO> conditions = conditionService.viewCondition(userIdReq.getRecruitId());
            return new BaseResponse<List>(conditions);
        } else {
            throw new BaseException(BAD_ACCESS);
        }
    }

    //waiting 스키마 / answer 컬렉션에 접근해야함.
    @PostMapping("/waiting/{postId}")
    public BaseResponse<Void> writeAnswer(
            @PathVariable(name="postId") Long postId,
            @RequestBody PostReq.PostWaitingReq postWaitingReq
    ) throws BaseException, IOException {
        postService.inWaitingQueue(postWaitingReq,postId,"testman2");
        return new BaseResponse<>(SUCCESS);
    }

    @PostMapping("/renew/{postId}")
    public BaseResponse<Void> renew(
            @PathVariable(name = "postId") Long postId
    ) throws BaseException, IOException {
        postService.renew(postId);
        return new BaseResponse<Void>(SUCCESS);
    }
    @PostMapping("/stop/{postId}")
    public BaseResponse<Void> stopRecruit(
            @PathVariable Long postId
    ) throws BaseException {
        postService.stateRecruit(postId,false);
        return new BaseResponse<>(SUCCESS);
    }
    @PostMapping("/start/{postId}")
    public BaseResponse<Void> startRecruit(
            @PathVariable Long postId
    ) throws BaseException {
        postService.stateRecruit(postId,false);
        return new BaseResponse<>(SUCCESS);
    }

}
