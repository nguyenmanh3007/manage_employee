package com.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name ="project")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ProjectId")
    private int projectId;
    @Column(name="Code",unique = true, nullable = false)
    private String code;
    @Column(name="NameProject", nullable = false)
    private String nameProject;
    @Column(name="CreateDay",nullable = false)
    private String createDay;
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "project",cascade = CascadeType.REMOVE)
    private Set<Assignment> assignments=new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "project",cascade = CascadeType.ALL)
    private Set<Comment> comments=new HashSet<>();
}
