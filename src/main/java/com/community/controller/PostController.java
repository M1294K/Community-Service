package com.community.controller;

import com.community.dto.board.BoardRequest;
import com.community.dto.post.PostRequest;
import com.community.dto.post.PostResponse;

import com.community.model.Post;
import com.community.service.BoardService;
import com.community.service.PostService;
import com.community.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;


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

        PostResponse createdPost = postService.createPost(postRequest, email);
        return ResponseEntity.ok(createdPost);
    }


    //글 조회
    @GetMapping
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    //글 상세 조회 <- comment 까지 담아서 출력해줄 예정
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long postId) {
        PostResponse response = postService.getPostById(postId);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/board/{boardId}")
    public ResponseEntity<List<PostResponse>> getPostsByBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(postService.getPostsByBoard(boardId));
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

    @DeleteMapping("/admin/{postId}")
    public ResponseEntity<String> forceDeletePost(@PathVariable Long postId, @RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        String role = jwtUtil.extractRole(token.substring(7)); // 추출 로직 필요
        if (!role.equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("권한 없음");
        }

        postService.forceDeletePost(postId);
        return ResponseEntity.ok("게시글 강제 삭제 완료");
    }



}
