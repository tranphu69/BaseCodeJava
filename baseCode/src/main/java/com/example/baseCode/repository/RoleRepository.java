package com.example.baseCode.repository;

import com.example.baseCode.dto.response.PermissionResponse;
import com.example.baseCode.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    @Query("SELECT r.permissions FROM Role r WHERE r.name = :roleName")
    Set<PermissionResponse> findPermissionsByRoleName(@Param("roleName") String roleName);
}
