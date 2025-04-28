package com.community.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostRequest {
    private String title;
    private String content;
    private Long boardId;  // 추가 : 글이 속할 게시판 id
}
