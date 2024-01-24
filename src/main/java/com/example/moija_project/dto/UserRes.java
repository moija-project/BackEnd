package com.example.moija_project.dto;

import lombok.*;

public class UserRes {
    @Setter
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProfileRes {
        String nickname;
        String bornIn;
        Float reliabilityUser;
        String profilePhotoUrl;

    }
}
