package com.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="comment")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="CommentId")
    private int commentId;
    @Column(name = "content",nullable = false)
    private String content;
    @Column(name = "point")
    private int point;
    @Column(name="createTime")
    private String createTime;
    @Column(name="updateTime")
    private String updateTime;
    @Column(name="status")
    private boolean status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Employee employee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Project project;

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", content='" + content + '\'' +
                ", point=" + point +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", status=" + status +
                ", employee=" + employee.getEmployeeId() +
                ", project=" + project.getProjectId() +
                '}';
    }
}
