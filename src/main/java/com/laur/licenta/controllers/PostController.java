package com.laur.licenta.controllers;

import com.laur.licenta.dto.PostRequest;
import com.laur.licenta.dto.PostResponse;
import com.laur.licenta.repository.PostRepository;
import com.laur.licenta.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    public PostService postService;
    @Autowired
    public PostRepository postRepository;

    @PostMapping("/create")
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest){
        postService.save(postRequest);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<PostResponse> getAllPost(){
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable String id){
        return postService.getPost(id);
    }

    @GetMapping("by-category/{name}")
    public List<PostResponse> getPostsByCategory(@PathVariable String name){return postService.getPostsByCategory(name);}

    @GetMapping("by-user/{name}")
    public List<PostResponse> getPostsByUsername(@PathVariable String name){return postService.getPostsByUsername(name);}



}