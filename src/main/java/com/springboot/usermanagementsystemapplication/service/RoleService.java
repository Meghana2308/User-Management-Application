package com.springboot.usermanagementsystemapplication.service;

import com.springboot.usermanagementsystemapplication.dto.RoleDTO;
import com.springboot.usermanagementsystemapplication.model.Role;
import java.util.List;

public interface RoleService {
    RoleDTO createRole(RoleDTO roleDTO);
    RoleDTO getRoleById(Long id);
    List<RoleDTO> getAllRoles();
    RoleDTO updateRole(Long id, RoleDTO roleDTO);
    void deleteRole(Long id);
    Role getRoleEntityById(Long id);

}