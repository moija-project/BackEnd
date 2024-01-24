package com.example.moija_project.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class UserReq {
    static public class UserJoinReq{
        /*
        *
        * {
            "user_id": "qwer123",
            "id_check": true,
            "password_encode": "wjgbaKdwheb234Adjgn==",
            "name": "김수정",
            "nickname": "테스트맨",
            "double_nick": false,
            "gender": 1,
            "birth": 2001-01-01,
            "phone_number": "010-1111-1111",
            "email": "mytest@test.com",
        * }
        *
        * */

        @JsonProperty(value = "user_id")
        String userId;
        @JsonProperty(value = "password_encode")
        String password;
        String name;
        String nickname;
        boolean gender;
        LocalDate birth;
        @JsonProperty(value = "phone_number")
        String phoneNumber;
        String email;

    }
}
