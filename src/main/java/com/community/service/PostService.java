package com.community.service;

import com.community.dto.comment.CommentResponse;
import com.community.dto.post.PostRequest;
import com.community.dto.post.PostResponse;
import com.community.model.Board;
import com.community.model.Comment;
import com.community.model.Post;
import com.community.model.User;
import com.community.repository.BoardRepository;
import com.community.repository.PostRepository;
import com.community.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;  //추가

    public PostResponse createPost(PostRequest postRequest, String email){
        Optional<User> userOptional = userRepository.findOptionalByEmail(email);
        if (userOptional.isEmpty()){
            throw new IllegalArgumentException("User not found");
        }
        User author = userOptional.get();
        //추가: 게시판 찾기
        Board board = boardRepository.findById(postRequest.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(author);
        post.setBoard(board); //추가

        Post savedPost = postRepository.save(post);

        return new PostResponse(savedPost.getId(), savedPost.getTitle(), savedPost.getContent(),
                savedPost.getAuthor().getUsername(), savedPost.getBoard().getName());
    }
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getAuthor().getUsername(),
                        post.getBoard().getName()
                ))
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        List<CommentResponse> comments = post.getComments().stream().map(comment -> new CommentResponse(
                comment.getId(),comment.getContent(),comment.getAuthor().getUsername(), comment.getCreatedAt())).collect(Collectors.toList());

        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getUsername(),comments, post.getBoard().getName());
    }

    public List<PostResponse> getPostsByBoard(Long boardId) {
        List<Post> posts = postRepository.findByBoardId(boardId);
        return posts.stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getAuthor().getUsername()
                ))
                .collect(Collectors.toList());
    }


    public PostResponse updatePost(Long postId, PostRequest postRequest, String email) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!post.getAuthor().getEmail().equals(email)) {
            throw new SecurityException("수정 권한이 없습니다.");
        }

        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        Post updated = postRepository.save(post);

        return new PostResponse(updated.getId(), updated.getTitle(), updated.getContent(), updated.getAuthor().getUsername(), updated.getBoard().getName());
    }
    @Transactional
    public void deletePost(Long postId, String email){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        if (!post.getAuthor().getEmail().equals(email)){
            throw new SecurityException("삭제 권한이 없습니다");
        }
        postRepository.delete(post);
    }

    public void forceDeletePost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        postRepository.delete(post);
    }
}

