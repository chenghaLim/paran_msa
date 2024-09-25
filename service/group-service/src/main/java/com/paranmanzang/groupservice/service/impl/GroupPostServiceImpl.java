package com.paranmanzang.groupservice.service.impl;


import com.paranmanzang.groupservice.enums.GroupPostCategory;
import com.paranmanzang.groupservice.model.domain.GroupPostModel;
import com.paranmanzang.groupservice.model.domain.GroupPostResponseModel;
import com.paranmanzang.groupservice.model.repository.GroupPostRepository;
import com.paranmanzang.groupservice.service.GroupPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupPostServiceImpl implements GroupPostService {
    private final GroupPostRepository groupPostRepository;

    public List<GroupPostResponseModel> findAll() {
        return groupPostRepository.findAll().stream()
                .map(GroupPostResponseModel::fromEntity).collect(Collectors.toList());
    }

    public GroupPostResponseModel findById(Long boardId) {
        return GroupPostResponseModel.fromEntity(groupPostRepository.getGroupPostById(boardId));
    }

    public Page<GroupPostResponseModel> findByGroupId(Long groupId, Pageable pageable) {
        return groupPostRepository.findGroupPostByGroupId(groupId,pageable);
    }

    public Boolean savePost(GroupPostModel groupPostModel) {
        return groupPostRepository.save(groupPostModel.toEntity()) != null ? Boolean.TRUE : Boolean.FALSE;
    }

    @Transactional
    public Object updatePost(GroupPostModel groupPostModel) {
        return groupPostRepository.findById(groupPostModel.getBoardId())
                .map(boardtoModify -> {
                    boardtoModify.setTitle(groupPostModel.getTitle());
                    boardtoModify.setContent(groupPostModel.getContent());
                    return groupPostRepository.save(boardtoModify) != null ? Boolean.TRUE : Boolean.FALSE;
                })
                .orElse(Boolean.FALSE);
    }


    public Object deletePost(Long boardId) {
        return groupPostRepository.findById(boardId)
                .map(board -> {
                    groupPostRepository.deleteById(boardId);
                    return Boolean.TRUE;
                })
                .orElse(Boolean.FALSE);
    }

    @Override
    public Object updateViewCount(Long postId) {
        return groupPostRepository.findById(postId)
                .map(post -> {
                    post.setViewCount(post.getViewCount() + 1);
                    groupPostRepository.save(post);
                    return Boolean.TRUE;
                })
                .orElse(Boolean.FALSE);
    }

}
