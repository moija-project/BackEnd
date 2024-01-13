package com.example.moija_project.dto;

import com.example.moija_project.entities.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostRes {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ListPostRes{
        List<Post> post_list;

        @Setter
        @Getter
        @AllArgsConstructor
        public static class Post {
            private Long post_id;
            private boolean state_recruit;
            private String title;
            private String contents;
            private String leader_nickname;
            private Timestamp latest_write;
            private long likes;
            private long views;
        }
    }

    @Builder
    @Getter
    public static class ReadPostRes{
        @JsonProperty("state_recruit")
        private boolean stateRecruit;
        private String title;
        private String contents;
        @JsonProperty("leader_nickname")
        private String leaderNickname;
        @JsonProperty("latest_write")
        private Timestamp latestWrite;
        @JsonProperty("is_changed")
        private boolean isChanged;
        private int penalty;
        @JsonProperty("reliability_recruit")
        private float reliabilityRecruit;
        private long likes;
        private long views;

    }
    public static class AnswerPostRes{}
}
