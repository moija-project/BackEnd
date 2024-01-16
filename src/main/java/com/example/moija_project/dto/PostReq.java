package com.example.moija_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
        @JsonProperty("leader_id")
        String leaderId;
        int penalty =0;
        @JsonProperty("num_condition")
        int numCondition=0;
        @JsonProperty("is_changed")
        boolean isChanged;
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
    @Setter
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
}
