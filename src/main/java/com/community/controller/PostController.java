package com.community.controller;

import com.community.dto.post.PostRequest;
import com.community.dto.post.PostResponse;

import com.community.model.Post;
import com.community.service.PostService;
import com.community.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> createPost(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody PostRequest postRequest
    ) {
        // "Bearer {token}" 형식에서 토큰만 추출
        String token = authorizationHeader.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(token);

        Post createdPost = postService.createPost(postRequest, email);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }



    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequest postRequest,
            @RequestHeader("Authorization") String token
    ) {
        String email = jwtUtil.extractEmail(token.substring(7));
        PostResponse response = postService.updatePost(postId, postRequest, email);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId, @RequestHeader("Authorization") String token){
        String email = jwtUtil.extractEmail(token.substring(7));
        postService.deletePost(postId, email);
        return ResponseEntity.ok("게시물이 성공적으로 삭제되었습니다.");
    }

}
