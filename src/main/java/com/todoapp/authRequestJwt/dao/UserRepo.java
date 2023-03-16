package com.todoapp.authRequestJwt.dao;

import com.todoapp.authRequestJwt.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepo extends JpaRepository<UserModel, Long> {

    Optional<UserModel> findByUsername(String username);
}
