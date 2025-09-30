package com.springboot.usermanagementsystemapplication.service.impl;

import com.springboot.usermanagementsystemapplication.dto.RoleDTO;
import com.springboot.usermanagementsystemapplication.exception.RoleNotFoundException;
import com.springboot.usermanagementsystemapplication.model.Role;
import com.springboot.usermanagementsystemapplication.repository.RoleRepository;
import com.springboot.usermanagementsystemapplication.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);
        Role saved = roleRepository.save(role);
        return modelMapper.map(saved, RoleDTO.class);
    }

    @Override
    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        return modelMapper.map(role, RoleDTO.class);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO updateRole(Long id, RoleDTO roleDTO) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));

        existingRole.setName(roleDTO.getName());
        existingRole.setDescription(roleDTO.getDescription());

        Role updated = roleRepository.save(existingRole);
        return modelMapper.map(updated, RoleDTO.class);
    }

    @Override
    public void deleteRole(Long id) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        roleRepository.delete(existingRole);
    }

    @Override
    public Role getRoleEntityById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
    }


}