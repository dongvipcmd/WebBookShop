package com.example.demowebshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String role;

    private String name;

    private String gender;

    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private String phoneNumber;

    private String email;

}
