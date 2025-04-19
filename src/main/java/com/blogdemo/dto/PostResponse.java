package com.blogdemo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;

    public PostResponse(Long id, String title, String content, String authorUsername) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorUsername = authorUsername;
    }
}
