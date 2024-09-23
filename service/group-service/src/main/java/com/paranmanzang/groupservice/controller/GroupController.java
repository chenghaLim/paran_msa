package com.paranmanzang.groupservice.controller;

import com.paranmanzang.groupservice.model.domain.GroupModel;
import com.paranmanzang.groupservice.model.domain.JoiningModel;
import com.paranmanzang.groupservice.model.domain.PointModel;
import com.paranmanzang.groupservice.service.impl.GroupServiceImpl;
import com.paranmanzang.groupservice.service.impl.JoiningServiceImpl;
import com.paranmanzang.groupservice.service.impl.PointServiceImpl;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups/groups")
@CrossOrigin(origins = "http://localhost:3000")
public class GroupController {
    private GroupServiceImpl groupService;
    private JoiningServiceImpl joiningService;
    private PointServiceImpl pointService;

    public GroupController(GroupServiceImpl groupService, JoiningServiceImpl joiningService, PointServiceImpl pointService) {
        this.groupService = groupService;
        this.joiningService = joiningService;
        this.pointService = pointService;
    }

    //(#61 Old Ver.)
    @GetMapping("/grouplist")
    public ResponseEntity<?> getAllGrouplist() {
        return ResponseEntity.ok(groupService.groupList());
    }

    //#61. 참여중인 소모임 조회
    @GetMapping("/mygrouplist")
    public ResponseEntity<?> getGrouplistByUserId(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok(groupService.groupsByUserNickname(nickname));
    }

    //#63.소모임 등록 (O)
    @PostMapping("/plusgroup") //request: groupname, groupconcept
    public ResponseEntity<?> addGroup(@Valid @RequestBody GroupModel groupModel,
                                      BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        return ResponseEntity.ok(groupService.addGroup(groupModel));
    }

    //#64.소모임 등록 승인 요청(O)
    @Transactional
    @PutMapping("/adminanswer")
    public ResponseEntity<?> enableGroup(@RequestParam Long groupId) {
        return ResponseEntity.ok(groupService.enableGroup(groupId));
    }

    //#64.소모임 승인 취소
    @Transactional
    @PutMapping("/adminoutGroup")
    public ResponseEntity<?> enableCancelGroup(@RequestParam Long groupId) {
        return ResponseEntity.ok(groupService.enableCancelGroup(groupId));
    }

    //#65.소모임장으로 등록
    @Transactional
    @PutMapping("/addleader")
    public ResponseEntity<?> addLeader(@RequestParam("groupId") Long groupId,
                                       @RequestParam("nickname") String userNickname) {
        return ResponseEntity.ok(groupService.addLeader(groupId, userNickname));
    }

    //#66.소모임 멤버 추가
    @PostMapping("/plusmember")//request: userNickname, groupId
    public ResponseEntity<?> addmember(@Valid @RequestBody JoiningModel joiningModel,
                                       BindingResult bindingResult) throws BindException {
        System.out.println(joiningModel.toString());
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

    @GetMapping("/mygrouppoint")//request : groupId
    public ResponseEntity<?> mygrouppoint(@RequestParam Long groupId) {
        return ResponseEntity.ok(pointService.searchPoint(groupId));
    }

    @PostMapping("/usepoint")//request :  groupId point
    public ResponseEntity<?> usePoint(@RequestBody PointModel pointModel) {
        return ResponseEntity.ok(pointService.usePoint(pointModel));
    }

    //#76.소모임 포인트 취소 (PR-90.결제상태 변경 시)
    @DeleteMapping("/paymentcancel")
    public ResponseEntity<?> cancelPoint(@RequestParam Long pointId) {
        return ResponseEntity.ok(pointService.deletePoint(pointId));
    }
    //#76-1.소모임 포인트 만료 - date 기준 : Service단에 Scheduled 사용

}
