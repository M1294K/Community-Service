package com.community.dto.post;

import com.community.dto.comment.CommentResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class PostResponse {
    private Long id;
    private String title;
    private String content;
    private String authorUsername;
    private List<CommentResponse> comments;
    private String boardName;


    public PostResponse(Long id, String title, String content, String authorUsername, List<CommentResponse> comments, String boardName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorUsername = authorUsername;
        this.comments = comments;
        this.boardName = boardName;
    }
    //Overloading
    public PostResponse(Long id, String title, String content, String authorUsername, String boardName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorUsername = authorUsername;
        this.comments = List.of(); // 빈 리스트로 초기화
        this.boardName = boardName;
    }
    public PostResponse(Long id, String title, String content, String authorUsername) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorUsername = authorUsername;
        this.comments = List.of(); // 빈 리스트로 초기화
    }

}
