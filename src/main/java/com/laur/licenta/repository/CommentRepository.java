package com.laur.licenta.repository;


import com.laur.licenta.models.Comment;
import com.laur.licenta.models.Post;
import com.laur.licenta.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends MongoRepository<Comment,String>
{
    List<Comment> findByPost(Post post);

    List<Comment> findByUser(User user);
}
