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

        return new CommentResponse(saved.getId(), saved.getContent(), user.getUsername(), saved.getCreatedAt());
    }
}
