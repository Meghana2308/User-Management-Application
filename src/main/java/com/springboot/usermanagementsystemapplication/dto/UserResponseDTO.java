package com.springboot.usermanagementsystemapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private ProfileDTO profile;

    private List<OrderDTO> orders;

    private Set<RoleDTO> roles;
}
