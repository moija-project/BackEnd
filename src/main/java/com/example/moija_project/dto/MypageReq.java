package com.example.moija_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class MypageReq {
    @Getter@Setter@Builder@AllArgsConstructor
    public static class MyKickReq{
        @JsonProperty(value = "user_nickname")
        String userNickname;
    }

}
