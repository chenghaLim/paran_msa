package com.paranmanzang.groupservice.service.impl;

import com.paranmanzang.groupservice.model.domain.ErrorField;
import com.paranmanzang.groupservice.model.domain.GroupModel;
import com.paranmanzang.groupservice.model.domain.GroupResponseModel;
import com.paranmanzang.groupservice.model.entity.Joining;
import com.paranmanzang.groupservice.model.repository.GroupRepository;
import com.paranmanzang.groupservice.model.repository.JoiningRepository;
import com.paranmanzang.groupservice.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final JoiningRepository joiningRepository;
    private final GroupRepository groupRepository;

    public Page<GroupResponseModel> groupList(Pageable pageable) {
        return groupRepository.findGroup(pageable);
    }

    public Page<GroupResponseModel> groupsByUserNickname(String nickname, Pageable pageable) {
        return groupRepository.findByNickname(nickname, pageable);
    }

    public Boolean duplicatename(String groupname) {
        return groupRepository.existsByName(groupname);
    }

    @Transactional
    public Object addGroup(GroupModel groupModel) {
        return Optional.ofNullable(groupModel)
                .filter(group -> !duplicatename(group.getGroupname()))
                .map(group -> {
                    // 그룹 저장 후 Joining 엔티티 저장
                    Optional.of(groupRepository.save(group.toEntity()))
                            .map(savedGroup -> {
                                joiningRepository.save(Joining.builder()
                                        .enabled(true)
                                        .group(savedGroup)
                                        .nickname(savedGroup.getNickname())
                                        .build());
                                return Boolean.TRUE;
                            });
                    return true;
                })
                .orElse(Boolean.FALSE);
    }

    public Object enableGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .map(groupToEnable -> {
                    if (groupToEnable.isEnabled()) {
                        return (Object) new ErrorField(groupId, "이미 관리자 승인된 group입니다.");
                    } else {
                        groupToEnable.setEnabled(true);
                        groupRepository.save(groupToEnable);
                        return (Object) (groupToEnable.isEnabled() ? Boolean.TRUE : Boolean.FALSE);
                    }
                })
                .orElseGet(() -> new ErrorField(groupId, "group이 존재하지 않습니다."));
    }


    public Object enableCancelGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .map(groupToEnable -> {
                    if (!groupToEnable.isEnabled()) {
                        return (Object) new ErrorField(groupId, "이미 관리자 승인 취소된 group입니다.");
                    }
                    groupToEnable.setEnabled(false);
                    groupRepository.save(groupToEnable);
                    return (Object) (!groupToEnable.isEnabled() ? Boolean.TRUE : Boolean.FALSE);
                })
                .orElseGet(() -> new ErrorField(groupId, "group이 존재하지 않습니다."));
    }

    public Object deleteGroup(Long groupId) {
        return groupRepository.findById(groupId)
                .map(group -> {
                    groupRepository.deleteById(groupId);
                    return (Object) Boolean.TRUE;
                })
                .orElseGet(() -> new ErrorField(groupId, "group이 존재하지 않습니다."));
    }


    @Override
    public Object getGroupById(Long groupId) {
        return GroupResponseModel.fromEntity(groupRepository.findById(groupId).get());
    }

    @Override
    public Object updateChatRoomId(String roomId, Long groupId) {
        return groupRepository.findById(groupId)
                .map(group -> {
                    group.setChatRoomId(roomId);
                    groupRepository.save(group);
                    return (Object) Boolean.TRUE;
                })
                .orElseGet(() -> new ErrorField(groupId, "group이 존재하지 않습니다."));
    }

    @Override
    public Page<GroupResponseModel> enableGroupList(Pageable pageable) {
        return groupRepository.findByEnable(pageable);
    }
}
