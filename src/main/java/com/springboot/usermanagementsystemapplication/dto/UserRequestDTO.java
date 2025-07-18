package com.springboot.usermanagementsystemapplication.dto;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {


    private String firstName;
    private String lastName;
    private String email;

}
