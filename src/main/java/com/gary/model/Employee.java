package com.gary.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Employee {
    @Id
    @Column
    private Integer id;
    @Column
    private String name;
    @Column
    private String dept;
    @Column
    private Integer salary;
    @Column
    private Date timestamp;

}
