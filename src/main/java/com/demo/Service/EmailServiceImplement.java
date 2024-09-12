package com.demo.Service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.demo.Model.UserModel;

@Service
public class EmailServiceImplement implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")

	private String sender;

	@Override
	public String sendOtpMail(UserModel model) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();

			Date date = new Date();

			mailMessage.setFrom(sender);

			mailMessage.setTo(model.getEmail());

			mailMessage.setSentDate(date);

			mailMessage.setText("PHONENUMBER: " + model.getPhoneNumber() + '\n' + "EMAIL: " + model.getEmail() + '\n'
					+ "OTP: " + model.getOtp() + '\n' + "\n\n\n\n");

			javaMailSender.send(mailMessage);
			return "Mail Sent Successfully...";
		} catch (Exception e) {
			return "Error while Sending Mail";
		}
	}

}
