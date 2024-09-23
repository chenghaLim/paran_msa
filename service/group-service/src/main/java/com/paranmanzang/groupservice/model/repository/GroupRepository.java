package com.paranmanzang.groupservice.model.repository;

import com.paranmanzang.groupservice.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Override
    List<Group> findAll();

    Group getGroupById(Long groupId);

    List<Group> findAllByCategoryName(String categoryName);

    List<Group> findAllByNickname(String userNickname);

    boolean existsByName(String name);//적절할까?

    void deleteById(Long groupId);

    Optional<?> findByName(String name);

}
