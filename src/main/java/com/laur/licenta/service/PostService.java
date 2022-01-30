package com.laur.licenta.service;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.laur.licenta.dto.PostRequest;
import com.laur.licenta.dto.PostResponse;
import com.laur.licenta.exceptions.CategoryNotFoundException;
import com.laur.licenta.exceptions.GenericException;
import com.laur.licenta.exceptions.IdNotFoundException;
import com.laur.licenta.models.*;
import com.laur.licenta.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.util.DateUtils;
import org.ocpsoft.prettytime.PrettyTime;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;
import static com.laur.licenta.models.VoteType.UPVOTE;
import static com.laur.licenta.models.VoteType.DOWNVOTE;

@Service
@AllArgsConstructor
public class PostService {
    @Autowired
    public CategoryRepository categoryRepository;
    @Autowired
    public PostRepository postRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public AuthService authService;
    @Autowired
    public CommentRepository commentRepository;
    @Autowired
    public VoteRepository voteRepository;

    public PostResponse PostToDto(Post post){
        PostResponse dto=new PostResponse();
        dto.setPostId(post.getPostId());
        dto.setCategoryName(post.getCategory().getName());
        dto.setUserName(post.getUser().getUsername());
        dto.setPostName(post.getPostName());
        dto.setDescription(post.getDescription());
        dto.setVoteCount(post.getVoteCount());
        dto.setCommentCount(commentCount(post));
        dto.setDuration(getDuration(post));
        dto.setDownVote(isPostDownVoted(post));
        dto.setUpVote(isPostUpVoted(post));

        return dto;
    }

    Integer commentCount(Post post){return commentRepository.findByPost(post).size();
    }


    String getDuration(Post post) {
        Date date= Date.from(post.getCreatedDate());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");


        return dateFormat.format(date);
    }

     boolean isPostUpVoted(Post post) {
        return checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return checkVoteType(post, DOWNVOTE);
    }


    private boolean checkVoteType(Post post, VoteType voteType) {
        if (authService.isLoggedIn()) {
            Optional<Vote> voteForPostByUser =
                    voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,
                            authService.getCurrentUser());
            return voteForPostByUser.filter(vote -> vote.getVoteType().equals(voteType))
                    .isPresent();
        }
        return false;
    }

    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream().
                        map(this::PostToDto).collect(Collectors.toList());
    }

    public PostResponse getPost(String id) {
        try {
            Post post = postRepository.findByPostId(id)
                    .orElseThrow(() -> new IdNotFoundException("No post found with id - " + id));
            return PostToDto(post);
        }
        catch (IdNotFoundException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not Found",exception);
        }
    }

    public List<PostResponse> getPostsByCategory(String name){

            Category category=categoryRepository.findByName(name)
                    .orElseThrow(() -> new GenericException("No category found with name - " + name));
            List<Post> posts =postRepository.findByCategory(category);
            return posts.stream().map(this::PostToDto).collect(Collectors.toList());

    }

    public List<PostResponse> getPostsByUsername(String username){

            User user=userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("No category found with name - " +username));
            List<Post> posts =postRepository.findByUser(user);
            return posts.stream().map(this::PostToDto).collect(Collectors.toList());
    }



    public void save(PostRequest postRequest) {
        try {
            Category category = categoryRepository.findByName(postRequest.getCategoryName()).orElseThrow(() -> new CategoryNotFoundException(postRequest.getCategoryName()));
            User currentUser = authService.getCurrentUser();
            postRepository.save(dtoTOPost(postRequest, category, currentUser));
        }
        catch (CategoryNotFoundException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not Found",exception);
        }
    }

    public Post dtoTOPost(PostRequest postRequest, Category category, User user)
    {
        Post post =new Post();
        post.setDescription(postRequest.getDescription());
        post.setPostName(postRequest.getPostName());
        post.setCategory(category);
        post.setUser(user);
        post.setVoteCount(post.getVoteCount());
        post.setCreatedDate(java.time.Instant.now());


        return post;
    }
}
