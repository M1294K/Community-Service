package com.community.service;

import com.community.dto.comment.CommentRequest;
import com.community.dto.comment.CommentResponse;
import com.community.model.Comment;
import com.community.model.Post;
import com.community.model.User;
import com.community.repository.CommentRepository;
import com.community.repository.PostRepository;
import com.community.repository.UserRepository;
import com.community.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    //댓글 작성
    @Transactional
    public CommentResponse createComment(Long postId, CommentRequest request, String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        User user = userRepository.findOptionalByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(user);
        comment.setPost(post);

        Comment saved = commentRepository.save(comment);

        return new CommentResponse(saved.getId(), saved.getContent(), saved.getAuthor().getUsername(), saved.getCreatedAt());
    }
    //댓글 수정
    @Transactional
    public CommentResponse updateComment(Long postId, Long commentId, CommentRequest request, String email){
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        if (!comment.getPost().getId().equals(postId)){
            throw new IllegalArgumentException("댓글이 해당 게시글에 속하지 않습니다.");
        }
        if (!comment.getAuthor().getEmail().equals(email)){
            throw new SecurityException("댓글 수정 권한이 없습니다.");
        }
        comment.setContent(request.getContent());
        Comment updated = commentRepository.save(comment);

        return new CommentResponse(updated.getId(), updated.getContent(), updated.getAuthor().getUsername(), updated.getCreatedAt());
    }
    //댓글 삭제
    @Transactional
    public void deleteComment(Long postId, Long commentId, String email){
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (!comment.getPost().getId().equals(postId)){
            throw new IllegalArgumentException("댓글이 해당 게시글에 속하지 않습니다.");
        }

        if (!comment.getAuthor().getEmail().equals(email)){
            throw new SecurityException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
    public void forceDeleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));
        commentRepository.delete(comment);
    }

}
