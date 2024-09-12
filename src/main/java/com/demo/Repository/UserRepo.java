package com.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.Model.UserModel;

@Repository
public interface UserRepo extends JpaRepository<UserModel, Integer> {

	UserModel findByEmailAndPhoneNumber(String email, String phoneNumber);

	UserModel findByPhoneNumber(String phoneNumber);

	UserModel findByEmail(String email);

}
