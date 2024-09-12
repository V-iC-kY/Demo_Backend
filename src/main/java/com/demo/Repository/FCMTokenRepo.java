package com.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.Model.FCMmodel;

@Repository
public interface FCMTokenRepo extends JpaRepository<FCMmodel, Integer> {

	FCMmodel findByUserId(int id);

}
