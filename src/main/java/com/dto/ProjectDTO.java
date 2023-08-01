package com.dto;


import com.entity.Assignment;
import com.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private int projectId;
    private String code;
    private String nameProject;
    private String createDay;
    private Set<Assignment> assignments=new HashSet<>();
    private Set<Comment> comments=new HashSet<>();
}
