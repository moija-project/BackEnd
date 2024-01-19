package com.example.moija_project.service;

import com.example.moija_project.dto.PostReq;
import com.example.moija_project.dto.PostRes;
import com.example.moija_project.dto.UserCheckReq;
import com.example.moija_project.entities.Recruit;
import com.example.moija_project.global.BaseException;
import com.example.moija_project.global.BaseResponse;
import com.example.moija_project.repository.RecruitRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.example.moija_project.global.BaseResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {
    @Autowired
    private final RecruitRepository recruitRepository;
    private final ConditionService conditionService;
    private final WaitingService waitingService;
    private final MemberService memberService;
    public void writePost(PostReq.PostWriteReq postWriteReq, Long postId) throws BaseException {
        Recruit recruit;
        //수정일때는 어떤 것을 할지
        if(!postWriteReq.isChanged()) {
            //해당 id의 포스트에 접근 권한이 없을경우?? - 토큰 구현 이후
            //초기에만 가능한 것들.
            recruit = Recruit.builder()
                    .leaderId(postWriteReq.getLeaderId())
                    .category(postWriteReq.getCategory())
                    .timeFirstWrite(new Timestamp(System.currentTimeMillis()))
                    .likes(0L)
                    .views(0L)
                    .reliabilityRecruit(0)
                    //여기부터 초기 기본값 정하기
                    .latestWrite(new Timestamp(System.currentTimeMillis()))
                    .isAvailable(true)
                    .stateRecruit(true)
                    .build();
            recruit = setDupColumn(recruit,postWriteReq);
        } else {
            //수정이 아닌데 체크되어있음?
            if(postId == 0L) {
                throw new BaseException(BAD_ACCESS);
            }
            //존재하는지 검증
            if(recruitRepository.findByRecruitIdAndIsAvailableTrue(postId).isPresent()){
                recruit = recruitRepository.findByRecruitIdAndIsAvailableTrue(postId).get();
            } else {
                throw new BaseException(BAD_ACCESS);
            }
            recruit = setDupColumn(recruit,postWriteReq);
            recruit.setRecruitId(null);
            recruit.setTimeLastWrite(new Timestamp(System.currentTimeMillis()));
            //이게 '최신순'의 기준이 되고, 따로 이용자에게 보여지는 시간은 아님
            recruit.setLatestWrite(new Timestamp(System.currentTimeMillis()));
            recruit.setChanged(true);
            recruitRepository.notAvailable(postId);
        }

        //최종 저장 -> sql 모집에 하나, nosql 조건에 하나 저장
        recruitRepository.saveAndFlush(recruit);

        //모임이 생겼으니 팀원이 들어갈 member id도 연결해 줘야함.
        memberService.save(recruit.getRecruitId(),recruit.getLeaderId());

        //조건이 있다면
        if(postWriteReq.getNumCondition() != 0
                && postWriteReq.getNumCondition() == postWriteReq.getConditions().size()) {
            //recruit의 id를 가져와서 같이 서비스로 넘김
            conditionService.writeQuestions(
                    postWriteReq.getConditions(),
                    recruit.getRecruitId()
            );
        } else {
            throw new BaseException(NEED_MORE_WRITE);
        }
    }

    public void remove(UserCheckReq userCheckReq, Long postId) throws BaseException {
        Optional<Recruit> recruit = recruitRepository.findByRecruitIdAndIsAvailableTrue(postId);
        if(recruit.isEmpty()) {
            throw new BaseException(NOT_EXISTS);
        }
        //마지막으로 수정한 날짜를 변경 (이 날로 부터 3개월 뒤에 자동 삭제)
        recruit.get().setTimeLastWrite(new Timestamp(System.currentTimeMillis()));
        recruitRepository.saveAndFlush(recruit.get());

        recruitRepository.notAvailable(postId);
    }
    private Recruit setDupColumn(Recruit recruit,PostReq.PostWriteReq postWriteReq) {
        recruit.setTitle(postWriteReq.getTitle());
        recruit.setContents(postWriteReq.getContents());
        recruit.setPenalty(postWriteReq.getPenalty());
        recruit.setNumCondition(postWriteReq.getNumCondition());
        return recruit;
    }

    public List<PostRes.ListPostRes> list(String category, String view_type, Optional<String> userId) throws BaseException {
        ArrayList<Recruit> recruitList = new ArrayList<>();
        if(userId.isPresent()) {
            recruitList.addAll(recruitRepository.findAllByLeaderIdAndIsAvailableTrue(userId.get()));

        } else {

            //데이터베이스에서 빼놓은 entity
            switch (category) {
                case "all":
                    recruitList.addAll(recruitRepository.findAllByIsAvailableTrueOrderByStateRecruitDescLatestWriteDesc());
                    break;
                case "hobby":
                case "language":
                case "study":
                case "employ":
                case "etc":
                    recruitList.addAll(recruitRepository.findAllByCategoryAndIsAvailableTrueOrderByStateRecruit(category));
                    break;
                default:
                    throw new BaseException(BAD_ACCESS);
            }

            switch (view_type) {
                case "latest":
                    break;
                case "most_view":
                    recruitList.sort(Comparator.comparing(Recruit::getViews));
                    break;
                case "most_like":
                    recruitList.sort(Comparator.comparing(Recruit::getLikes));
                    break;
                default:
                    throw new BaseException(BAD_ACCESS);
            }
        }

        if(recruitList.isEmpty()) {
            throw new BaseException(NOT_EXISTS);
        }

        return makeList(recruitList);
    }

    public PostRes.ReadPostRes view(Long postId) throws BaseException {
        Optional<Recruit> selected = recruitRepository.findByRecruitIdAndIsAvailableTrue(postId);
        if(!selected.isEmpty()) {
            Recruit recruit = selected.get();
            updateView(recruit.getRecruitId());
            return PostRes.ReadPostRes.builder()
                    .title(recruit.getTitle())
                    .contents(recruit.getContents())
                    .stateRecruit(recruit.isStateRecruit())
                    .leaderNickname(recruit.getLeader().getNickname())
                    .isChanged(recruit.isChanged())
                    .penalty(recruit.getPenalty())
                    .reliabilityRecruit(recruit.getReliabilityRecruit())
                    .latestWrite(recruit.getLatestWrite())
                    .likes(recruit.getLikes())
                    .views(recruit.getViews())
                    .build();
        } else {
            //보려는 포스트가 없을 경우
            throw new BaseException(NOT_EXISTS);
        }
    }
    @Transactional
    public int updateView(Long id) {
        return recruitRepository.updateView(id);
    }

    //답변을 등록하고, 대기를 걸어놓고 모임 초대를 기다리는 것 , waiting에 접근하고, waiting은 answer에 접근해서 등록

    public void inWaitingQueue(PostReq.PostWaitingReq postWaitingReq, Long postId, String userId) throws BaseException {
        //이미 있는거면 안되게 해야지
        if(memberService.existTeamUser(postId,userId))
            throw new BaseException(TEAM_ALREADY_JOINED);
        if(waitingService.existTeamUser(postId,userId))
            throw new BaseException(WAITING_ALREADY_EXISTS);
        //question개수와 answer개수가 다르다면 답변을 덜 쓴거니 다 채우라고 해야지 -> 이거 프론트에서 처리
        waitingService.saveWaiting(postWaitingReq,postId,userId);
    }

    public void renew(Long postId) throws BaseException{
        //시간 차이 처리
        long current = System.currentTimeMillis();
        Optional<Recruit> recruitOptional =  recruitRepository.findByRecruitIdAndIsAvailableTrue(postId);
        long latest;
        if(recruitOptional.isPresent()) {
            latest = recruitOptional.get().getLatestWrite().getTime();
        } else {
            throw new BaseException(NOT_EXISTS);
        }
        short gap = (short) ((current - latest) / (60 * 60 * 1000));
        //조건에 만족 한다면 서비스를 부름
        if(gap >= 30) {
            recruitRepository.updateTimeLatest(new Timestamp(current),postId);
        } else {
            throw new BaseException(CURRENT_UNAVAILABLE);
        }


    }

    public boolean existPost(Long recruitId) throws BaseException{
        return recruitRepository.existsByRecruitIdAndIsAvailableTrue(recruitId);
    }

    public List<PostRes.ListPostRes> makeList(List<Recruit> recruits) {
        return recruits.stream().map(r ->
                PostRes.ListPostRes.builder()
                        .post_id(r.getRecruitId())
                        .title(r.getTitle())
                        .state_recruit(r.isStateRecruit())
                        .contents(r.getContents().substring(0,20)+"...")
                        .leader_nickname(r.getLeader().getNickname())
                        .latest_write(r.getLatestWrite())
                        .likes(r.getLikes())
                        .views(r.getViews())
                        .build()
        ).toList();
    }

    //true이면 재개 false이면 종료
    public void stateRecruit(Long postId, boolean stateRecruit) throws BaseException {
        if(!recruitRepository.existsByRecruitIdAndIsAvailableTrue(postId))
            throw new BaseException(NOT_EXISTS);
        if(stateRecruit == recruitRepository.isRecruiting(postId)) {
            throw new BaseException(ALrEADY_RECRUIT);
        }
        recruitRepository.updateStateRecruit(postId,stateRecruit);
    }
}
