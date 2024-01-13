package com.example.moija_project.controller;

import com.example.moija_project.dto.PostReq;
import com.example.moija_project.dto.PostRes;
import com.example.moija_project.dto.QnADTO;
import com.example.moija_project.dto.UserCheckReq;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.global.BaseResponse;
import com.example.moija_project.service.ConditionService;
import com.example.moija_project.service.PostService;
import com.example.moija_project.service.UserCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.example.moija_project.global.BaseResponseStatus.BAD_ACCESS;
import static com.example.moija_project.global.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;
    private final UserCheckService userCheckService;
    private final ConditionService conditionService;
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

    @PostMapping("/question/{postId}")
    public BaseResponse<List> viewQuestion(
            @PathVariable(name="postId") Long postId,
            @RequestBody UserCheckReq.UserIdReq userIdReq
    ) throws BaseException, IOException {
        if(userCheckService.check(userIdReq)){
            List<QnADTO> conditions = conditionService.viewCondition(userIdReq.getRecruitId());
            return new BaseResponse<List>(conditions);
        } else {
            throw new BaseException(BAD_ACCESS);
        }
    }
    @GetMapping("/list")
    public BaseResponse<PostRes.ListPostRes> loadPostList(
            @RequestParam(value="category") String category,
            @RequestParam(value = "view_type") String viewType
    ) throws BaseException, IOException {
        PostRes.ListPostRes response = postService.list(category,viewType);
        return new BaseResponse<>(response);
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
            @RequestBody UserCheckReq userCheckReq
    ) throws BaseException, IOException {

        return new BaseResponse<>(SUCCESS);
    }

    //waiting 스키마 / answer 컬렉션에 접근해야함.
//    @PostMapping("/answer/{postId}")
//    public BaseResponse<List> writeAnswer(
//            @PathVariable(name="postId") Long postId,
//            @RequestBody PostReq.PostAnswerReq postAnswerReq
//    ) throws BaseException, IOException {
//        return new BaseResponse<List>();
//    }

}
