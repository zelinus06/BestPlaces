package com.bestplaces.Dto;

import com.bestplaces.Entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Comment comment;
    private String username;
    private String avatarUrl;
}
