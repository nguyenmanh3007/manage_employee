package com.dto;

import com.entity.Employee;
import com.entity.Project;
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
    private Employee employee;
    private Project project;
}
