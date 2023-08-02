package com.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="Employee")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="EmployeeId")
    private int employeeId;
    @Column(name="Code",unique = true, nullable = false)
    private int code;
    @Column(name="Username",unique = true, nullable = false)
    private String userName;
    @Column(name="Password", nullable = false)
    @JsonIgnore
    private String password;
    @Column(name="Created")
    @JsonFormat(pattern = "dd/MM/yyyy hh:mm:ss")
    private String created;
    @Column(name="Email", unique = true, nullable = false)
    private String email;
    @Column(name="Phone")
    private String phone;
    @Column(name="TimeCheckin")
    private String timeCheckin;
    @Column(name="TimeCheckout")
    private String timeCheckout;
    @Column(name="EmployeeStatus")
    private boolean employeeStatus;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="Employee_Role",joinColumns = @JoinColumn(name="EmployeeId"),inverseJoinColumns = @JoinColumn(name="RoleId"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Roles> listRoles = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "employee",cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<Confirm> confirms=new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "employee",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Assignment> assignments=new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER,mappedBy = "employee",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Comment> comments=new HashSet<>();

    public Employee(int code, String userName, String password
            , String email,String phone) {
        this.code = code;
        this.userName = userName;
        this.password = password;
        this.phone=phone;
        this.email = email;
    }

    public Employee(int code, String userName, String phone, String timeCheckin) {
        this.code = code;
        this.userName = userName;
        this.phone = phone;
        this.timeCheckin = timeCheckin;
    }
}
