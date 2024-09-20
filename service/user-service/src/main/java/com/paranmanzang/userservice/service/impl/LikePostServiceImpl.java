package com.paranmanzang.userservice.service.impl;/*
package com.paranmanzang.userservice.service.user.impl;

import com.paranmanzang.userservice.model.domain.user.LikePostModel;
import com.paranmanzang.userservice.model.domain.user.LikeRoomModel;
import com.paranmanzang.userservice.model.entity.group.GroupPost;
import com.paranmanzang.userservice.model.entity.user.LikePosts;
import com.paranmanzang.userservice.model.entity.user.LikeRooms;
import com.paranmanzang.userservice.model.entity.user.User;
import com.paranmanzang.userservice.model.repository.user.LikePostRepository;
import com.paranmanzang.userservice.model.repository.user.UserRepository;
import com.paranmanzang.userservice.service.user.LikePostService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikePostServiceImpl implements LikePostService {
    private final LikePostRepository likePostRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikePostServiceImpl(LikePostRepository likePostRepository,UserRepository userRepository, PostRepository postRepository){
        this.likePostRepository = likePostRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    @Override
    public boolean add(LikePostModel likePostModel) {
        Long userId = likePostModel.getUserId();
        Long postId = likePostModel.getPostId();

        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<GroupPost> optionalPost = postRepository.findById(postId);

        if(optionalUser.isPresent() && optionalPost.isPresent()) {
            throw new RuntimeException("사용자나 방의 정보가 잘못되었습니다.");
        }

        if (likePostRepository.existsByUserIdAndPostId(userId, postId)) {
            System.out.println("test");
            return false; // 이미 좋아요를 눌렀으면 false 반환
        }

        LikePosts likePosts = new LikePosts();
        likePosts.setPostId(postId);
        likePosts.setUser(optionalUser.get());
        likePostRepository.save(likePosts);
        return true;
    }

    @Override
    public boolean remove(LikePostModel likePostModel) {
        LikePosts likePosts = likePostRepository.findByUserIdAndPostId(likePostModel.getUserId(), likePostModel.getPostId());
        if (likePosts != null) {
            likePostRepository.deleteByUserIdAndPostId(likePostModel.getUserId(), likePostModel.getPostId());
            return true;
        }
        return false;
    }

    @Override
    public boolean removeLikeById(Long id) {
        likePostRepository.deleteById(id);
        System.out.println(!likePostRepository.existsById(id));
        return !likePostRepository.existsById(id);
    }

    @Override
    public List<LikePostModel> findAll(long userId) {
        List<LikePosts> likePosts = likePostRepository.findByUserId(userId);
        return likePosts.stream()
                .map(likePost -> new LikePostModel(likePost.getId(),
                        likePost.getPostId(),
                        likePost.getUser().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public LikePostModel existsByUserIdAndPostId(Long userId, Long postId) {
        LikePosts likePosts = likePostRepository.findByUserIdAndPostId(userId, postId);
        if(likePosts != null) {
            System.out.println(new LikePostModel(likePosts.getId(), likePosts.getPostId(),likePosts.getUser().getId()));
            return new LikePostModel(likePosts.getId(), likePosts.getPostId(),likePosts.getUser().getId());
        }
        System.out.println("null");
        return null;
    }
}

*/
