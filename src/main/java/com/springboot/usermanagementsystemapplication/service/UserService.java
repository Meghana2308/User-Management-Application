package com.springboot.usermanagementsystemapplication.service;

import com.springboot.usermanagementsystemapplication.dto.UserRequestDTO;
import com.springboot.usermanagementsystemapplication.dto.UserResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface UserService {

    UserResponseDTO createUser(UserRequestDTO dto);


    Page<UserResponseDTO> getAllUsers(Pageable pageable);

    UserResponseDTO getUserById(Long id);

    UserResponseDTO updateUser(Long id, UserRequestDTO dto);

    void deleteUser(Long id);

}
