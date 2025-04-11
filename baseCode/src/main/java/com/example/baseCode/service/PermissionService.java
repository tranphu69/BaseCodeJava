package com.example.baseCode.service;

import com.example.baseCode.dto.request.PermissionRequest;
import com.example.baseCode.dto.response.PermissionResponse;
import com.example.baseCode.entity.Permission;
import com.example.baseCode.exception.AppException;
import com.example.baseCode.exception.ErrorCode;
import com.example.baseCode.repository.PermissionRepository;
import com.example.baseCode.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private UserRepository userRepository;

    public Permission create(PermissionRequest request){
        if(permissionRepository.existsByName(request.getName()))
            throw new AppException(ErrorCode.PERMISSION_EXSITED);
        Permission permission = new Permission();
        permission.setName(request.getName());
        permission.setDescription(request.getDescription());
        return permissionRepository.save(permission);
    }

    public List<Permission> getList(){
        return permissionRepository.findAll();
    }

    public void delete(String id){
        permissionRepository.deleteById(id);
    }
}
