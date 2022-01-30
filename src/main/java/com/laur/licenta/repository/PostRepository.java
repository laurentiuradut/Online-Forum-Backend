package com.laur.licenta.repository;

import com.laur.licenta.models.Category;
import com.laur.licenta.models.Post;
import com.laur.licenta.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends MongoRepository<Post,String> {
    List<Post> findByCategory(Category category);

    Optional<Post> findByPostId(String postId);
    Optional<Post> findByPostName(String postName);
    List<Post> findByUser(User user);
}
