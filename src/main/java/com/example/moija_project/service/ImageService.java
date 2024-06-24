package com.example.moija_project.service;

import com.example.moija_project.mongo.ImageRepository;
import com.example.moija_project.mongo_entity.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    @Autowired
    ImageRepository imageRepository;
    public void saveImage(String url, Long recruitId, int num) {
        Image image = Image.builder()
                .number(num)
                .url(url)
                .recruitId(recruitId)
                .build();
        imageRepository.save(image);
    }

    public List<Image> loadImageUrl(Long recruitId) {
        return imageRepository.findAllByUrlContainsIgnoreCase(String.format("[%s](",Long.toString(recruitId)));
    }

    public Image findByRecruitIdAndNumber(Long recruitId) {
        return imageRepository.findByRecruitIdAndNumber(recruitId,0).orElse(new Image("http://resource.mo.ija.kro.kr/image/[14](0)-fb9ca2b0-93a7-4e9b-9a40-79b8f7b444ac.jpg?Expires=9223372036854775&KeyName=imnew-key&Signature=gfehWvRzFtk-CcfRxoQS-V-GvN4="));
    }

    public void deleteByRecruitIdAndNumber(Long postId, int index) {
        imageRepository.deleteByRecruitIdAndNumber(postId,index);
    }

    public void updateRecruitId(Long prev, Long next) {
        imageRepository.updateRecruitId(prev,next);
    }

    public boolean existsByRecruitIdAndNumber(Long recruitId, int number) {
        return imageRepository.existsByRecruitIdAndNumber(recruitId,number);
    }
}
