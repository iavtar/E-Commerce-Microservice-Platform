package com.iavtar.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iavtar.authservice.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

	Role findRoleByName(String name);

}
