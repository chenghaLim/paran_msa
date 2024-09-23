package com.paranmanzang.groupservice.service.impl;


import com.paranmanzang.groupservice.model.domain.ErrorField;
import com.paranmanzang.groupservice.model.domain.GroupPostModel;
import com.paranmanzang.groupservice.model.domain.GroupPostResponseModel;
import com.paranmanzang.groupservice.model.entity.GroupPost;
import com.paranmanzang.groupservice.model.repository.GroupPostRepository;
import com.paranmanzang.groupservice.service.GroupPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupPostServiceImpl implements GroupPostService {
    private final GroupPostRepository groupPostRepository;

    public List<GroupPostResponseModel> findAll() {
        return groupPostRepository.findAll().stream()
                .map(GroupPostResponseModel :: fromEntity).collect(Collectors.toList());
    }

    public GroupPostResponseModel findById(Long boardId) {
        return GroupPostResponseModel.fromEntity(groupPostRepository.getGroupPostById(boardId));
    }

    public List<GroupPostResponseModel> findBycategoryId(Long cId){
        return groupPostRepository.findAllByPostCategoryId(cId).stream()
                .map(GroupPostResponseModel :: fromEntity).collect(Collectors.toList());
    }
    public List<GroupPostResponseModel> findByGroupId(Long groupId){
        return groupPostRepository.findAllByGroupId(groupId).stream()
                .map(GroupPostResponseModel :: fromEntity).collect(Collectors.toList());
    }

    public Boolean savePost(GroupPostModel groupPostModel) {
        return groupPostRepository.save(groupPostModel.toEntitysaving()) != null ? Boolean.TRUE : Boolean.FALSE;
    }

    @Transactional
    public Object updatePost(GroupPostModel groupPostModel){
        Optional<GroupPost> toUpdate = groupPostRepository.findById(groupPostModel.getBoardId());
        if (toUpdate.isEmpty()) {
            return new ErrorField(groupPostModel.getBoardId(),"게시물이 존재하지 않습니다.");
        }
        GroupPost boardtoModify = toUpdate.get();
        boardtoModify.setTitle(groupPostModel.getUserBoardtitle());
        boardtoModify.setContent(groupPostModel.getUserBoardcontent());
       return groupPostRepository.save(boardtoModify) != null ? Boolean.TRUE : Boolean.FALSE;
    }

    public Object deletePost(Long boardId){
        Optional<GroupPost> optionalGroupPost = groupPostRepository.findById(boardId);
        if (optionalGroupPost.isPresent()){
            GroupPost groupPost = optionalGroupPost.get();
            groupPostRepository.deleteById(groupPost.getId());
            return Boolean.TRUE;
        }else{
            return new ErrorField(boardId,"게시물이 존재하지 않습니다.");
        }
    }


}
