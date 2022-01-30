package com.laur.licenta.models;

import com.mongodb.lang.Nullable;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "comments")
public class Comment {

    @Id
    @Generated
    private String id;

    @NotBlank
    @Size(max = 20)
    private String name;
    @NotBlank
    @Size(max = 255)
    private String text;
    private Instant createdDate;
    private Post post;
    private User user;

}
