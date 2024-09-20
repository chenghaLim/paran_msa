    package com.paranmanzang.userservice.service.impl;


    import com.paranmanzang.userservice.model.domain.user.DeclarationPostModel;
    import com.paranmanzang.userservice.model.entity.DeclarationPosts;
    import com.paranmanzang.userservice.model.entity.User;
    import com.paranmanzang.userservice.model.repository.DeclarationPostRepository;
    import com.paranmanzang.userservice.model.repository.UserRepository;
    import com.paranmanzang.userservice.service.DeclarationPostService;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class DeclarationPostServiceImpl implements DeclarationPostService {

        private final UserRepository userRepository;
        private final DeclarationPostRepository declarationPostRepository;

        public DeclarationPostServiceImpl(DeclarationPostRepository declarationPostRepository, UserRepository userRepository) {
            this.declarationPostRepository = declarationPostRepository;
            this.userRepository = userRepository;
        }

        // 신고 게시글 작성
        public Boolean createDPost(DeclarationPostModel declarationPostModel) {
            Optional<User> userOptional = userRepository.findById(declarationPostModel.getUserId());
            if (userOptional.isEmpty()) {
                System.out.println("실패");
                return false; // 사용자가 존재하지 않을 경우
            }
            User user = userOptional.get();

            DeclarationPosts createPost = declarationPostRepository.save(DeclarationPosts.builder()
                    .title(declarationPostModel.getTitle())
                    .content(declarationPostModel.getContent())
                    .target(declarationPostModel.getTarget())
                    .user(user) // User 설정
                    .build());

            return createPost != null;
        }



        //신고 게시글 조회 (관리자 이거나 신고자 본인만)
        public List<DeclarationPosts> getDPost(Long userId) {
            // 사용자 정보 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

            // 관리자인 경우
            if (user.getRole().equals("ROLE_ADMIN")) {
                return declarationPostRepository.findAll();
            }
            // 본인이 신고한 게시글만 조회
            return declarationPostRepository.findByUserId(userId);
        }

        //신고 게시글 상세 조회 (관리자 이거나 신고자 본인만)
        public DeclarationPosts getPostDetail(Long postId, Long userId) {
            // 사용자 정보 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));
            // 게시글 정보 조회
            DeclarationPosts post = declarationPostRepository.findById(postId)
                    .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
            // 관리자인 경우 또는 해당 게시글 작성자인 경우에만 조회 허용
            if (user.getRole().equals("ROLE_ADMIN") || post.getUser().getId().equals(userId)) {
                return post;
            }
            throw new SecurityException("권한이 없습니다.");
        }

        //신고 게시글 접수 상태 변경(boolean값을 받아서 수락이면 카운트 메서드 진행 후 삭제 거절이면 그냥 삭제)
        public boolean deleteDPost(boolean check, Long target, Long id){

            if(check){
                User user = userRepository.findById(target)
                                .orElseThrow(()->new IllegalArgumentException("사용자가 존재하지 않습니다."));
                //신고 당한사람 카운트 추가
                user.setDeclarationCount(user.getDeclarationCount()+1);
                if(user.getDeclarationCount()>=5){
                    userRepository.delete(user);
                }
                declarationPostRepository.deleteById(id);
                return true;
            }
            else{
                declarationPostRepository.deleteById(id);
                return true;
            }
        }


    }
