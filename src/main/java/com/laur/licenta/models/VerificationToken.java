package com.laur.licenta.models;

import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@NoArgsConstructor
@Getter
@Setter
@Document(collection="VerificationToken")
public class VerificationToken {

    @Id
    @Generated
    private String id;
    private String token;
    private User user;
    private Instant expiryDate;
}
