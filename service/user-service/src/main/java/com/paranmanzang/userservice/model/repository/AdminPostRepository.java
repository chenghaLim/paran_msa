
package com.paranmanzang.userservice.model.repository;

import com.paranmanzang.userservice.model.entity.AdminPosts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminPostRepository extends JpaRepository<AdminPosts, Long>{
}