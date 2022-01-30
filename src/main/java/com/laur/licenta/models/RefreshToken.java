package com.laur.licenta.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.Instant;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "refreshToken")
public class RefreshToken {
    @Id
    private String id;
    private String token;
    private Instant createdDate;
}
