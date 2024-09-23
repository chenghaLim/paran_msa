package com.paranmanzang.groupservice.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "books")
public class Book {//API
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false)
    private String publisher;

    private String categoryName;

    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<LikeBooks> like_books;
}
