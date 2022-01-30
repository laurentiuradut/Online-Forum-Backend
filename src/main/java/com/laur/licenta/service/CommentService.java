package com.laur.licenta.service;

import com.laur.licenta.dto.CommentsDto;
import com.laur.licenta.exceptions.PostNotFoundException;
import com.laur.licenta.models.Comment;
import com.laur.licenta.models.Post;
import com.laur.licenta.models.User;
import com.laur.licenta.repository.CommentRepository;
import com.laur.licenta.repository.PostRepository;
import com.laur.licenta.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private CommentRepository commentRepository;



    public void save(CommentsDto commentsDto){
        try {
            Post post = postRepository.findByPostId(commentsDto.getPostId()).orElseThrow(()->new PostNotFoundException("No post found"));
            User currentUser = authService.getCurrentUser();
            Comment comment =dtoToComment(commentsDto,post,currentUser);
            commentRepository.save(comment);
        }
        catch(PostNotFoundException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Post Not Found",exception);
        }

    }

    public List<CommentsDto> getAllCommentsForPost(String postId){
        try {
            Post post=postRepository.findByPostId(postId).orElseThrow(()->new PostNotFoundException("No post found"));

            return commentRepository.findByPost(post).stream().map(this::CommentToDto).collect(Collectors.toList());

        }

        catch(PostNotFoundException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Post Not Found",exception);
        }
    }

    public List<CommentsDto> getAllCommentsForUser(String username){
        try {
            User user=userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("No user found"+username));
            return commentRepository.findByUser(user).stream().map(this::CommentToDto).collect(Collectors.toList());
        }
        catch (UsernameNotFoundException exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not Found",exception);
        }
    }


    public CommentsDto CommentToDto(Comment comment){

        CommentsDto dto=new CommentsDto();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setPostId(comment.getPost().getPostId());
        dto.setUsername(comment.getUser().getUsername());
        dto.setDuration(getDuration(comment));

        return dto;
    }

    public Comment dtoToComment(CommentsDto commentsDto,Post post,User user){
        Comment comment=new Comment();
        comment.setId(commentsDto.getId());
        comment.setText(commentsDto.getText());
        comment.setUser(user);
        comment.setPost(post);
        comment.setCreatedDate(java.time.Instant.now());
        return comment;
    }

    String getDuration(Comment comment) {
        Date date= Date.from(comment.getCreatedDate());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");


        return dateFormat.format(date);
    }


}
