package com.springboot.usermanagementsystemapplication.config;

import com.springboot.usermanagementsystemapplication.dto.UserResponseDTO;
import com.springboot.usermanagementsystemapplication.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setAmbiguityIgnored(true);

//        modelMapper.typeMap(User.class, UserResponseDTO.class)
//                .addMappings(mapper -> mapper.map(
//                        src -> src.getFirstName() + " " + src.getLastName(),
//                        UserResponseDTO::setFullName));
//
       return modelMapper;
    }

}
