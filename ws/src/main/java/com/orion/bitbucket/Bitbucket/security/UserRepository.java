package com.orion.bitbucket.Bitbucket.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Administrator,Long> {
    Administrator findByUsername(String username);
}