package com.paranmanzang.userservice.service;

import com.paranmanzang.userservice.model.domain.DeclarationPostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface DeclarationPostService {
    Object createDPost(DeclarationPostModel declarationPostModel);
    boolean deleteDPost(Long id);
    Page<DeclarationPostModel> getDPostAdmin(Pageable pageable);
    Page<DeclarationPostModel> getDPost(String nickname, Pageable pageable);
    //Page<AdminPostModel> getAPost(Pageable pageable);
    Object getPostDetail(Long postId);

}
