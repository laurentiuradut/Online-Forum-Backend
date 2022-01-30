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
@Document(collection = "posts")
public class Post {
    @Id
    private String postId;

    @NotBlank
    @Size(max = 20)
    private String postName;
    @Nullable
    private String url;
    @Nullable
    private String description;
    private User user;
    private Category category;

    private Instant createdDate;
    private Integer voteCount = 0;

}
