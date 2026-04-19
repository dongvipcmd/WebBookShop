package com.example.demowebshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

//    private String name;
//
//    private String gender;
//
//    private String address;
//
//    private LocalDate dob;
//
//    private String phoneNumber;
//
//    private String email;

}
