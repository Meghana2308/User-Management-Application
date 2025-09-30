package com.springboot.usermanagementsystemapplication.service.impl;

import com.springboot.usermanagementsystemapplication.aop.TrackExecutionTime;
import com.springboot.usermanagementsystemapplication.dto.OrderDTO;
import com.springboot.usermanagementsystemapplication.dto.UserRequestDTO;
import com.springboot.usermanagementsystemapplication.dto.UserResponseDTO;
import com.springboot.usermanagementsystemapplication.exception.UserNotFoundException;
import com.springboot.usermanagementsystemapplication.model.Role;
import com.springboot.usermanagementsystemapplication.model.User;
import com.springboot.usermanagementsystemapplication.repository.UserRepository;
import com.springboot.usermanagementsystemapplication.service.EmailService;
import com.springboot.usermanagementsystemapplication.service.RoleService;
import com.springboot.usermanagementsystemapplication.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final RoleService roleService;

    @Override
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
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
        log.info("Creating user with email: {}", userRequestDTO.getEmail());
        User user = modelMapper.map(userRequestDTO, User.class);  // DTO → Entity

        //  Maintain bidirectional Profile ↔ User link
        if (user.getProfile() != null) {
            user.getProfile().setUser(user);
        }

        //  Handle Many-to-Many: Fetch roles from IDs

        if (userRequestDTO.getRoleIds() != null && !userRequestDTO.getRoleIds().isEmpty()) {
            Set<Role> roles = userRequestDTO.getRoleIds()
                    .stream()
                    .map(roleService::getRoleEntityById)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }


        User savedUser = userRepository.save(user);
        emailService.sendWelcomeEmail(user.getEmail());

        return modelMapper.map(savedUser, UserResponseDTO.class);
    }



//    @TrackExecutionTime
//    @Override
//    public List<UserResponseDTO> getAllUsers() {
    ////        return userRepository.findAll().stream()
    ////                .map(user -> UserResponseDTO.builder()
    ////                        .id(user.getId())
    ////                        .fullName(user.getFirstName() + " " + user.getLastName())
    ////                        .email(user.getEmail())
    ////                        .build())
    ////                .collect(Collectors.toList());
//        log.debug("Fetching all users");
//        return userRepository.findAll().stream()
//                .map(user -> modelMapper.map(user, UserResponseDTO.class))  // Entity → DTO
//                .collect(Collectors.toList());
//    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        log.info("Fetching user by ID: {}", id);

        User user = userRepository.findWithOrdersById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return modelMapper.map(user, UserResponseDTO.class);

    }

    @TrackExecutionTime
    @Override
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        log.debug("Fetching users -> page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        return userRepository.findAll(pageable)
                .map(user -> modelMapper.map(user, UserResponseDTO.class));
    }

//    @Override
//    public UserResponseDTO getUserById(Long id) {
    ////        return userRepository.findById(id)
    ////                .map(user -> UserResponseDTO.builder()
    ////                        .id(user.getId())
    ////                        .fullName(user.getFirstName() + " " + user.getLastName())
    ////                        .email(user.getEmail())
    ////                        .build())
    ////                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
//        log.info("Fetching user by ID: {}", id);
//        return userRepository.findById(id)
//                .map(user -> modelMapper.map(user, UserResponseDTO.class))  // Entity → DTO
//                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
//    }



    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO dto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

//        existingUser.setFirstName(dto.getFirstName());
//        existingUser.setLastName(dto.getLastName());
//        existingUser.setEmail(dto.getEmail());
        // ModelMapper will map only non-id fields (as id is not in DTO)
        modelMapper.map(dto, existingUser);

        User updatedUser = userRepository.save(existingUser);

        return modelMapper.map(updatedUser, UserResponseDTO.class);
    }

    @Override
    public void deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(existingUser);
    }

}