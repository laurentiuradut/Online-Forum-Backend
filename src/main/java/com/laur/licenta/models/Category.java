package com.laur.licenta.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "categories")
public class Category {
    @Id
    @Generated
    private String id;

    @NotBlank(message = "Category name is required")
    @Size(max = 20)
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 20)
    private String description;
    private List<Post> posts;

    private User user;

    private Instant createdDate;

}
