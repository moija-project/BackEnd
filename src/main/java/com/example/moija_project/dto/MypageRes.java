package com.example.moija_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MypageRes {
    @Getter@Setter@Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WaitingListRes {
        List<MemDto> users;
        String title;
        @JsonProperty("post_id")
        Long postId;
        @JsonProperty("latest_write")
        Timestamp latestWrite;

    }
    @Setter@Builder@Getter@AllArgsConstructor@NoArgsConstructor
    public static class MemDto {
        String nickname;
        Long waitingId;
        boolean is_ask;
    }

    @Setter@Builder@Getter@AllArgsConstructor@NoArgsConstructor
    public static class WaitingRes {
        String nickname;
        String gender;
        //프로필사진 추가 나중에...
        String genaration;
        List<QnADTO> qnas;
        boolean is_ask;

    }
}
