package com.blogdemo.repository;

import com.blogdemo.model.Post;
import com.blogdemo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PostRepository extends JpaRepository<Post, Long>{

}
