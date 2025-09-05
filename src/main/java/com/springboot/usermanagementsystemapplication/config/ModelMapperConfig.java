package com.springboot.usermanagementsystemapplication.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.springboot.usermanagementsystemapplication.dto.UserResponseDTO;
import com.springboot.usermanagementsystemapplication.dto.OrderDTO;
import com.springboot.usermanagementsystemapplication.dto.ProfileDTO;
import com.springboot.usermanagementsystemapplication.model.User;
import com.springboot.usermanagementsystemapplication.model.Order;
import com.springboot.usermanagementsystemapplication.model.Profile;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setAmbiguityIgnored(true);

        // Explicit type map for User → UserResponseDTO
        TypeMap<User, UserResponseDTO> userTypeMap =
                modelMapper.createTypeMap(User.class, UserResponseDTO.class);

        userTypeMap.addMappings(mapper -> {
        });

        // Explicit type map for Order → OrderDTO
        modelMapper.createTypeMap(Order.class, OrderDTO.class);

        // Explicit type map for Profile → ProfileDTO
        modelMapper.createTypeMap(Profile.class, ProfileDTO.class);

        return modelMapper;
    }
}
