package com.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="confirm")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Confirm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;
    @Column(name = "timeCheckIn")
    private String timeCheckIn;
    @Column(name = "timeCheckOut")
    private String timeCheckOut;
    @Column(name = "checkInLate")
    private int checkInLate;
    @Column(name = "checkOutEarly")
    private int checkOutEarly;
    @Column(name = "statusCheckIn")
    private String statusCheckIn;
    @Column(name = "statusCheckOut")
    private String statusCheckOut;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employeeId",nullable = false)
    private Employee employee;
}
