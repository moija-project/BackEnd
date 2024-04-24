package com.example.moija_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;

public class PostReq {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PostWriteReq{
        String title;
        String contents;
        String category;
        int penalty =0;
        @JsonProperty("num_condition")
        int numCondition=0;
        @JsonProperty("is_changed")
        boolean isChanged;
        @JsonProperty("changed_pictures")
        int[] changedPictures;
        ArrayList<QnADTO> conditions;
    }
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Getter
//    @Setter
//    public static class PostDelReq{}
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter@Builder
    public static class PostWaitingReq {
        ArrayList<String> answers;
        @JsonProperty("num_answer")
        short numAnswer;
        @JsonProperty("is_ask")
        boolean isAsk;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PostLikeReq{
        @JsonProperty("post_id")
        Long recruitId;
        int vote;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class PostClipReq{
        @JsonProperty("post_id")
        Long recruitId;
        int clip;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public class PostDelReq {
        String userId;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public class PostGrantReq {
        String postId;
        float score;
    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter@Builder
    public static class ScoreDto {
        String userId;
        String score;
    }
}
