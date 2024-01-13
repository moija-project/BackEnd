package com.example.moija_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class UserCheckReq {
    @Getter
    public static class UserIdReq {
        @JsonProperty("user_id")
        String userId;
        @JsonProperty("post_id")
        Long recruitId;
    }
}
