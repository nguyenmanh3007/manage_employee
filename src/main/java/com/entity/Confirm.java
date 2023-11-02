package com.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name="confirm")
@Data
@Builder
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId",nullable = false)
    private Employee employee;

    public Confirm(Employee employee) {
        this.employee = employee;
    }
}
