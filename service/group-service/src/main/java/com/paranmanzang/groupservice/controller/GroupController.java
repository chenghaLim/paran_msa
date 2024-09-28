package com.paranmanzang.groupservice.controller;

import com.paranmanzang.groupservice.model.domain.GroupModel;
import com.paranmanzang.groupservice.model.domain.JoiningModel;
import com.paranmanzang.groupservice.model.domain.PointModel;
import com.paranmanzang.groupservice.service.impl.GroupServiceImpl;
import com.paranmanzang.groupservice.service.impl.JoiningServiceImpl;
import com.paranmanzang.groupservice.service.impl.PointServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupServiceImpl groupService;
    private final JoiningServiceImpl joiningService;
    private final PointServiceImpl pointService;

    //(#61 Old Ver.) 전체 그룹 가져오기
    @GetMapping("/grouplist")
    public ResponseEntity<?> getAllGrouplist(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(groupService.groupList(PageRequest.of(page, size)));
    }

    // 그룹에 해당하는 유저리스트 가져오기
    @GetMapping("/userlist/{groupId}")
    public ResponseEntity<?> getUserListById(@PathVariable Long groupId) {
        return ResponseEntity.ok(joiningService.getUserListById(groupId));
    }

    // 소모임 만든 후 채팅방 개설 후 실행
    @PutMapping("/chatroomupdate/{groupId}")
    public ResponseEntity<?> updateChatRoomId(@RequestBody String roomId, @PathVariable Long groupId) {
        return ResponseEntity.ok(groupService.updateChatRoomId(roomId, groupId));
    }

    //#61. 참여중인 소모임 조회
    @GetMapping("/mygrouplist")
    public ResponseEntity<?> getGrouplistByUserId(@RequestParam("nickname") String nickname, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(groupService.groupsByUserNickname(nickname, PageRequest.of(page, size)));
    }

    //#63.소모임 등록
    @PostMapping("/plusgroup") //request: groupname, groupconcept
    public ResponseEntity<?> addGroup(@Valid @RequestBody GroupModel groupModel,
                                      BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok(groupService.addGroup(groupModel));
    }

    //#64.소모임 등록 승인
    @Transactional
    @PutMapping("/adminanswer")
    public ResponseEntity<?> enableGroup(@RequestParam Long groupId) {
        return ResponseEntity.ok(groupService.enableGroup(groupId));
    }

    //#66.소모임 멤버 추가
    @PostMapping("/plusmember")//request: userNickname, groupId
    public ResponseEntity<?> addmember(@Valid @RequestBody JoiningModel joiningModel,
                                       BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok(joiningService.addMember(joiningModel));
    }

    //#66-1.소모임 멤버 승인
    //처리 후 바로 채팅방 입장

    @Transactional
    @PutMapping("/adminplusMember")
    public ResponseEntity<?> ebableGroupMember(@RequestParam("groupId") Long groupId,
                                               @RequestParam("nickname") String userNickname) {
        return ResponseEntity.ok(joiningService.enableMember(groupId, userNickname));
    }
    //#66-2.소모임 멤버승인 취소

    @Transactional
    @PutMapping("/adminoutMember")
    public ResponseEntity<?> disableGroupMember(@RequestParam("groupId") Long groupId,
                                                @RequestParam("nickname") String userNickname) {
        return ResponseEntity.ok(joiningService.disableMember(groupId, userNickname));
    }
    //#64-1.소모임 삭제

    @DeleteMapping("/deleteGroup")
    public ResponseEntity<?> deleteGroup(@RequestParam("groupId") Long groupId) {
        return ResponseEntity.ok(groupService.deleteGroup(groupId));
    }
    //#75.소모임 포인트 적립

    @PostMapping("/pointup")//request : groupId point
    public ResponseEntity<?> addPoint(@Valid @RequestBody PointModel pointModel,
                                      BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok(pointService.addPoint(pointModel));
    }
    // 소모임 포인트 조회

    @GetMapping("/mygrouppoint")//request : groupId
    public ResponseEntity<?> mygrouppoint(@RequestParam Long groupId, @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(pointService.searchPoint(groupId, PageRequest.of(page, size)));
    }

    // 소모임 포인트 사용
    @PostMapping("/usepoint")//request :  groupId point
    public ResponseEntity<?> usePoint(@RequestBody PointModel pointModel) {
        return ResponseEntity.ok(pointService.usePoint(pointModel));
    }

    //#76.소모임 포인트 취소 (PR-90.결제상태 변경 시)
    @DeleteMapping("/paymentcancel")
    public ResponseEntity<?> cancelPoint(@RequestParam Long pointId) {
        return ResponseEntity.ok(pointService.deletePoint(pointId));
    }

    //#64.소모임 승인 취소
    @Transactional
    @PutMapping("/adminoutGroup")
    public ResponseEntity<?> enableCancelGroup(@RequestParam Long groupId) {
        return ResponseEntity.ok(groupService.enableCancelGroup(groupId));
    }

    // 승인해야 하는 소모임 리스트
    @GetMapping("/updateenablelist")
    public ResponseEntity<?> enableGroupList(@RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(groupService.enableGroupList(PageRequest.of(page, size)));
    }
}
