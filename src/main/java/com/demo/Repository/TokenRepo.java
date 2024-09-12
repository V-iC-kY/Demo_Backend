package com.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.Model.TokenModel;

@Repository
public interface TokenRepo extends JpaRepository<TokenModel, Integer> {

	TokenModel findByUserId(int id);

}
