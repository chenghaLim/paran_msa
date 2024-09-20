package com.paranmanzang.userservice.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String tel;

    @Column
    private int declarationCount;

    //true면 로그인가능 false면 로그인 불가능
    @ColumnDefault("true")
    @Column(nullable = false)
    private boolean state;

    @Column
    private LocalDateTime logoutAt;

    //

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<LikeRooms> likeRooms;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<DeclarationPosts> declarationPosts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<AdminPosts> adminPosts;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Friends> friends;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Friends> send_friends;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<LikePosts> listLikePosts;
}
