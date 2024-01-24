package com.example.moija_project.service;

import com.example.moija_project.dto.UserRes;
import com.example.moija_project.entities.User;
import com.example.moija_project.extractor.Genarator;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.mongo.NicknameRepository;
import com.example.moija_project.mongo_entity.Nickname;
import com.example.moija_project.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static com.example.moija_project.global.BaseResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    GCSService gcsService;
    @Autowired
    NicknameRepository nicknameRepository;
    public UserRes.ProfileRes loadProfile(String userId) throws BaseException, IOException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()) {
            return UserRes.ProfileRes.builder()
                    .nickname(userOptional.get().getNickname())
                    .bornIn(Genarator.changeToBornIn(userOptional.get().getBirth()))
                    .profilePhotoUrl(gcsService.readGcsFile(userOptional.get().getProfile()==null? Optional.empty():Optional.of(userOptional.get().getProfile())))
                    .reliabilityUser(userOptional.get().getReliabilityUser())
                    .build();
        }else {
            throw new BaseException(BAD_ACCESS);
        }
    }

    @Transactional
    public void saveProfile(String fileName,String data, String userId) throws BaseException, IOException {
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()) {
            fileName = String.format("%s_profile",userOptional.get().getNickname())+fileName;
            String gcsName = gcsService.writeGcsFile(fileName,data);
            userRepository.updateProfile(gcsName,userId);
        } else {
            throw new BaseException(LOGIN_FIRST);
        }
    }

    public void editNickname(String newNickname, String userId) throws BaseException {
        Optional<User> userOptional = userRepository.findById(userId);
        //중복 닉이 있다면
        checkNicknameDup(newNickname);
        if(userOptional.isPresent()) {
            if(userOptional.get().getNickname().equals(newNickname)) {
                throw new BaseException(NOT_EDIT);
            }else {
                userRepository.updateNickname(newNickname,userId);
                //에딧은 밑에꺼가 필요하고, 이닛은 필요없다.
                //닉네임 변경시간이 너무 잦다면
                if(nicknameRepository.findById(userId).isPresent()) {
                    Nickname nickname = nicknameRepository.findById(userId).get();
                    //90일 이상 되었다면
                    // 현재 날짜와 시간
                    LocalDateTime currentDate = LocalDateTime.now();

                    // 90일 이전의 날짜와 시간 계산
                    LocalDateTime ninetyDaysAgo = currentDate.minus(90, ChronoUnit.DAYS);

                    // 현재로부터 90일 이전인지 검증
                    if (currentDate.isBefore(ninetyDaysAgo)) {
                        nicknameRepository.save(Nickname.builder()
                                .userId(userId)
                                .lastModifiedDate( currentDate )
                                .build());
                    }
                    throw new BaseException(NICKNAME_CHANGE_AVAILABLE);
                }
            }
        } else {
            throw new BaseException(LOGIN_FIRST);
        }
    }

    private void checkNicknameDup(String newNickname) throws BaseException{
        if(userRepository.existsByNickname(newNickname) ) {
            throw new BaseException(DUPLICATE_NICKNAME);
        }
    }
}
