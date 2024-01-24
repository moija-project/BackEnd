package com.example.moija_project.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter@Getter@Builder@NoArgsConstructor@AllArgsConstructor
public class FileDTO {
    //파일 이름 포맷 {닉네임}_profile.jpeg  ex) testman1_profile.jpeg => profile은 무조건 jpg로 저장함(용량)
    //만약 게시글 파일이라면 [2023-01-23 13:07:23][{닉네임}]테스트모집이미지.jpeg
    String fileName;

    String data;

}
