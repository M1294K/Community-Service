package com.blogdemo.service;

import com.blogdemo.dto.PostRequest;
import com.blogdemo.dto.PostResponse;
import com.blogdemo.model.Post;
import com.blogdemo.model.User;
import com.blogdemo.repository.PostRepository;
import com.blogdemo.repository.UserRepository;
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

    public Post createPost(PostRequest postRequest, String email){
        Optional<User> userOptional = userRepository.findOptionalByEmail(email);
        if (userOptional.isEmpty()){
            throw new IllegalArgumentException("User not found");
        }
        User author = userOptional.get();
        Post post = new Post();
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
        post.setAuthor(author);
        return postRepository.save(post);
    }
    public List<PostResponse> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> new PostResponse(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getAuthor().getUsername()
                ))
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        return new PostResponse(post.getId(), post.getTitle(), post.getContent(), post.getAuthor().getUsername());
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

        return new PostResponse(updated.getId(), updated.getTitle(), updated.getContent(), updated.getAuthor().getUsername());
    }


}

