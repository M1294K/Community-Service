package com.community.controller;

import com.community.dto.comment.CommentRequest;
import com.community.dto.comment.CommentResponse;
import com.community.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.community.util.JwtUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Autowired
    private JwtUtil jwtUtil;

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long postId,
                                                         @RequestBody CommentRequest request,
                                                         @RequestHeader("Authorization") String token) {
        CommentResponse response = commentService.createComment(postId, request, token);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentRequest request,
            @RequestHeader("Authorization") String token
    ){
        String email = jwtUtil.extractEmail(token.substring(7));
        CommentResponse response = commentService.updateComment(postId, commentId, request, email);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestHeader("Authorization") String token
    ){
        String email = jwtUtil.extractEmail(token.substring(7));
        commentService.deleteComment(postId, commentId, email);
        return ResponseEntity.noContent().build(); // 204 응답
    }
    @DeleteMapping("/admin/{commentId}")
    public ResponseEntity<String> forceDeleteComment(@PathVariable Long commentId, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        String role = jwtUtil.extractRole(token.substring(7));
        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한 없음");
        }

        commentService.forceDeleteComment(commentId);
        return ResponseEntity.ok("댓글 강제 삭제 완료");
    }

}
