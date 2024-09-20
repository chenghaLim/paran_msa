    package com.paranmanzang.userservice.service.impl;

    import com.paranmanzang.userservice.model.domain.user.AdminPostModel;
    import com.paranmanzang.userservice.model.entity.AdminPosts;
    import com.paranmanzang.userservice.model.entity.User;
    import com.paranmanzang.userservice.model.repository.AdminPostRepository;
    import com.paranmanzang.userservice.model.repository.UserRepository;
    import com.paranmanzang.userservice.service.AdminPostService;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;
    import java.util.Optional;

    @Service
    @Transactional
    public class AdminPostServiceImpl implements AdminPostService {

        private final AdminPostRepository adminPostRepository;
        private final UserRepository userRepository;

        public AdminPostServiceImpl(AdminPostRepository adminPostRepository, UserRepository userRepository) {
            this.adminPostRepository = adminPostRepository;
            this.userRepository = userRepository;
        }

        // 게시글 작성
        public Boolean createAPost(AdminPostModel adminPostModel) {
            Optional<User> userOptional = userRepository.findById(adminPostModel.getUserId());
            if (userOptional.isEmpty()) {
                System.out.println("실패");
                return false; // 사용자가 존재하지 않을 경우
            }
            User user = userOptional.get();

            AdminPosts savedPost = adminPostRepository.save(AdminPosts.builder()
                    .title(adminPostModel.getTitle())
                    .content(adminPostModel.getContent())
                    .category(adminPostModel.getCategory())
                    .user(user) // User 설정
                    .build());

            return savedPost != null;
        }

        // 게시글 수정
        public boolean updateAPost(Long id, AdminPostModel adminPostModel) {
            return adminPostRepository.findById(id)
                    .map(existingPost -> {
                        existingPost.setTitle(adminPostModel.getTitle());
                        existingPost.setContent(adminPostModel.getContent());
                        existingPost.setCategory(adminPostModel.getCategory());
                        adminPostRepository.save(existingPost);
                        return true;
                    })
                    .orElse(false);
        }

        // 게시글 삭제
        public boolean deleteAPost(Long id) {
            if (adminPostRepository.existsById(id)) {
                adminPostRepository.deleteById(id);
                return !adminPostRepository.existsById(id);
            }
            return false;
        }

        // 게시글 리스트 조회
        public List<AdminPosts> getAPost() {
            return adminPostRepository.findAll();
        }

        // 게시글 상세 조회
        public Optional<AdminPosts> getAPostById(Long id) {
            Optional<AdminPosts> postOptional = adminPostRepository.findById(id);
            postOptional.ifPresent(post -> {
                post.setViewCount(post.getViewCount() + 1); // 조회수 증가
                adminPostRepository.save(post); // 변경 사항 저장
            });
            return postOptional;
        }

        // 조회수 확인
        public Long getViewCount(Long id) {
            return adminPostRepository.findById(id)
                    .map(AdminPosts::getViewCount)
                    .orElse(0L);
        }
    }