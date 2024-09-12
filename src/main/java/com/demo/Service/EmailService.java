package com.demo.Service;

import org.springframework.stereotype.Service;

import com.demo.Model.UserModel;

@Service
public interface EmailService {

	String sendOtpMail(UserModel model);

}
