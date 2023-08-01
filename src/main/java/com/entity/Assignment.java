package com.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name="assignment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private Long id;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "EmployeeId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Employee employee;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ProjectId")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Project project;
    @Column(name="RoleProject")
    @Enumerated(EnumType.STRING)
    private ERoleProject roleProject;
}
