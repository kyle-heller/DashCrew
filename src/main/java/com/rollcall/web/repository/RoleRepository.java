package com.rollcall.web.repository;

import com.rollcall.web.models.Role;
import com.rollcall.web.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
