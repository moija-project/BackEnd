package com.example.moija_project.service;

import com.example.moija_project.dto.UserCheckReq;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.repository.RecruitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.moija_project.global.BaseResponseStatus.BAD_ACCESS;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserCheckService {
    private final RecruitRepository recruitRepository;
    public boolean check(UserCheckReq.UserIdReq userIdReq) throws BaseException {
        Optional<String> leader_id = recruitRepository.findLeaderIdByRecruitId(userIdReq.getRecruitId());
        if(leader_id.isPresent() && leader_id.get().equals(userIdReq.getUserId())) {
            return true;
        } else {
            //글쓴이랑 수정하려는 이가 다름 (글의 글쓴이 정보가 비어있는 경우도 포함)
            return false;
        }
    }
}
