package com.dto;

import com.entity.Employee;
import com.entity.Project;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private int commentId;
    private String content;
    private int point;
    private String createTime;
    private String updateTime;
    private boolean status;
    private String nameEmployee;
    private String nameProject;
    @JsonIgnore
    private Employee employee;
    @JsonIgnore
    private Project project;
}
