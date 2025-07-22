package com.springboot.usermanagementsystemapplication.service.impl;

import com.springboot.usermanagementsystemapplication.dto.UserRequestDTO;
import com.springboot.usermanagementsystemapplication.dto.UserResponseDTO;
import com.springboot.usermanagementsystemapplication.exception.UserNotFoundException;
import com.springboot.usermanagementsystemapplication.model.User;
import com.springboot.usermanagementsystemapplication.repository.UserRepository;
import com.springboot.usermanagementsystemapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserResponseDTO createUser(UserRequestDTO dto) {
//        User user = User.builder()
//                .firstName(dto.getFirstName())
//                .lastName(dto.getLastName())
//                .email(dto.getEmail())
//                .build();
//
//        User saved = userRepository.save(user);
//
//        return UserResponseDTO.builder()
//                .id(saved.getId())
//                .fullName(saved.getFirstName() + " " + saved.getLastName())
//                .email(saved.getEmail())
//                .build();

        User user = modelMapper.map(dto, User.class);  // DTO → Entity
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserResponseDTO.class);  // Entity → DTO
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
//        return userRepository.findAll().stream()
//                .map(user -> UserResponseDTO.builder()
//                        .id(user.getId())
//                        .fullName(user.getFirstName() + " " + user.getLastName())
//                        .email(user.getEmail())
//                        .build())
//                .collect(Collectors.toList());
        return userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, UserResponseDTO.class))  // Entity → DTO
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
//        return userRepository.findById(id)
//                .map(user -> UserResponseDTO.builder()
//                        .id(user.getId())
//                        .fullName(user.getFirstName() + " " + user.getLastName())
//                        .email(user.getEmail())
//                        .build())
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return userRepository.findById(id)
                .map(user -> modelMapper.map(user, UserResponseDTO.class))  // Entity → DTO
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }
}