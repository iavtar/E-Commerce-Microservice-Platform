package com.iavtar.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iavtar.authservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findUserByUsername(String username);



}
