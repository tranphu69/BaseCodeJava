package com.example.baseCode.service;

import com.example.baseCode.dto.request.RoleRequest;
import com.example.baseCode.dto.response.RoleResponse;
import com.example.baseCode.entity.Role;
import com.example.baseCode.repository.PermissionRepository;
import com.example.baseCode.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PermissionRepository permissionRepository;

    public Role create(RoleRequest request){
        var permissions = permissionRepository.findAllById(request.getPermission());
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setPermissions(new HashSet<>(permissions));
        return roleRepository.save(role);
    }

    public List<Role> getList(){
        return roleRepository.findAll();
    }

    @Transactional
    public void delete(String id){
        roleRepository.deleteRolePermissions(id);
        roleRepository.deleteById(id);
    }
}
