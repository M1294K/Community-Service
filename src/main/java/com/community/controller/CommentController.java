package com.community.controller;

import com.community.dto.comment.CommentRequest;
import com.community.dto.comment.CommentResponse;
import com.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId,
                                                         @RequestBody CommentRequest request,
                                                         @RequestHeader("Authorization") String token) {
        CommentResponse response = commentService.createComment(postId, request, token);
        return ResponseEntity.ok(response);
    }
}
