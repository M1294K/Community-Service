package com.community.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String content;
    private String authorUsername;
    private LocalDateTime createdAt;
}
