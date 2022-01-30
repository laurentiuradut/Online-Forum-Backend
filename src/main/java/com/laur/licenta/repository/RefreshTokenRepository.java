package com.laur.licenta.repository;


import com.laur.licenta.models.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken,String> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByToken(String token);

}
