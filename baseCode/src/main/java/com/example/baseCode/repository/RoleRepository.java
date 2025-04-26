package com.example.baseCode.repository;
import com.example.baseCode.entity.Permission;
import com.example.baseCode.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    @Query(value = "SELECT p.* FROM permission p JOIN role_permission rp ON p.name = rp.permission_name WHERE rp.role_name = :roleName", nativeQuery = true)
    Set<Permission> findPermissionsByRoleName(@Param("roleName") String roleName);
    @Modifying
    @Query(value = "DELETE FROM role_permission WHERE role_name = :roleName", nativeQuery = true)
    void deleteRolePermissions(@Param("roleName") String roleName);
}
