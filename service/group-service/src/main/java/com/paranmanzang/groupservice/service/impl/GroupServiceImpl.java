package com.paranmanzang.groupservice.service.impl;

import com.paranmanzang.groupservice.model.domain.ErrorField;
import com.paranmanzang.groupservice.model.domain.GroupModel;
import com.paranmanzang.groupservice.model.domain.GroupResponseModel;
import com.paranmanzang.groupservice.model.entity.Category;
import com.paranmanzang.groupservice.model.entity.Group;
import com.paranmanzang.groupservice.model.repository.CategoryRepository;
import com.paranmanzang.groupservice.model.repository.GroupRepository;
import com.paranmanzang.groupservice.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {
    private GroupRepository groupRepository;
    private CategoryRepository categoryRepository; //group추가할때검사

    public GroupServiceImpl(GroupRepository groupRepository, CategoryRepository categoryRepository) {
        this.groupRepository = groupRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Group> groupList() {
        return groupRepository.findAll();
    }

    public Group oneGroup(Long groupId) {
        return groupRepository.getGroupById(groupId);
    }

    public List<Group> groupsByCategory(String categoryId) {
        return groupRepository.findAllByCategoryName(categoryId);
    }

    public List<Object> groupsByUserNickname(String userNickname) {
        try {
            List<Group> groups = groupRepository.findAllByNickname(userNickname);
            if (groups.isEmpty()) {
                List<ErrorField> errorFields = new ArrayList<>();
                errorFields.add(new ErrorField(userNickname, "참여하신 소그룹이 존재하지 않습니다."));
                return Collections.singletonList(errorFields);
            }
            return groups.stream()
                    .map(GroupResponseModel::fromEntity)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return Collections.singletonList(new ErrorField(userNickname, "참여하신 소그룹이 존재하지 않습니다."));
        }
    }

    public Boolean duplicatename(String groupname) {
        return groupRepository.existsByName(groupname);
    }

    //    public Boolean duplicategroupname_OrElse(String categoryname) {
//        Category cate = categoryRepository.findByName(categoryname).orElse(
//                categoryRepository.save(new Category(categoryname)));
//    }
    @Transactional
    public Object addGroup(GroupModel groupModel) {
        //기존 카테고리의 경우
        if (duplicatename(groupModel.getGroupname())) {
            return new ErrorField(groupModel.getGroupname(),
                    "중복되는 그룹명입니다.");
        }
        if (categoryRepository.existsByName(groupModel.getGroupconcept())) {
            Category whichCategory = categoryRepository.findByName(groupModel.getGroupconcept());
            groupModel.setCategoryName(whichCategory.getName());
        } else {//새 카테고리 추가의 경우
            categoryRepository.save(Category.builder().name(groupModel.getCategoryName()).build());
        }
        return groupRepository.save(groupModel.toEntity()) == null ? Boolean.FALSE : Boolean.TRUE;
    }
    //예외처리
//    public User findUserErBlock(String nickname){
//        User temp = userRepositoryForTest.findByNickname(nickname)
//                .orElseThrow(()->
//                        new IllegalArgumentException("잘못된 사용자명입니다."));
//        return temp;
//    }

    public Object enableGroup(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isPresent()) {
            Group groupToEnable = optionalGroup.get();
            if (groupToEnable.isEnabled()) {
                return new ErrorField(groupId, "이미 관리자 승인된 group입니다.");
            } else {
                groupToEnable.setEnabled(true);
                groupRepository.save(groupToEnable);
                return groupToEnable.isEnabled() ? Boolean.TRUE : Boolean.FALSE;
            }
        } else {
            return new ErrorField(groupId, "group이 존재하지 않습니다.");
        }
    }

    public Object enableCancelGroup(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        if (optionalGroup.isPresent()) {
            Group groupToEnable = optionalGroup.get();
            if (!groupToEnable.isEnabled()) {
                return new ErrorField(groupId, "이미 관리자 승인 취소된 group입니다.");
            }
            groupToEnable.setEnabled(false);
            groupRepository.save(groupToEnable);
            return !groupToEnable.isEnabled() ? Boolean.TRUE : Boolean.FALSE;
        } else {
            return new ErrorField(groupId, "group이 존재하지 않습니다.");
        }
    }

    public Object addLeader(Long groupId, String nickname) {
        if (nickname.isEmpty() || groupId == null) {
            return new ErrorField("Error", "다시 입력해주세요.");
        }
        Optional<Group> optgroupToEnable = groupRepository.findById(groupId);
        if (optgroupToEnable.isPresent()) {
            Group grouptoModify = optgroupToEnable.get();
            if (!grouptoModify.getNickname().equals(nickname)) {
                grouptoModify.setNickname(nickname);
//                return grouptoModify;
                return true;
            } else {
                return new ErrorField(nickname, "이미 그룹장입니다.");
            }

        } else {
            return new ErrorField(groupId, "group이 존재하지 않습니다.");
        }
//        Group groupToEnable = groupRepository.findById(groupId)
//                .orElseThrow(()->
//                        new IllegalArgumentException("소모임장 수정 실패")
//        );
//
//        User newUser = userRepositoryForTest.findByNickname(nickname)
//                .orElseThrow(()->
//                        new IllegalArgumentException("잘못된 사용자명입니다."));
    }

    public Object deleteGroup(Long groupId) {
        Optional<Group> optgroupTodelete = groupRepository.findById(groupId);
        if (optgroupTodelete.isPresent()) {
            groupRepository.deleteById(groupId);
            return Boolean.TRUE;
        } else {
            return new ErrorField(groupId, "group이 존재하지 않습니다.");
        }
    }
}
