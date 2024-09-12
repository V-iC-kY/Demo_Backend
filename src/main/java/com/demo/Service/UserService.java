package com.demo.Service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.Component.JwtToken;
import com.demo.Exception.NotFoundException;
import com.demo.Model.FCMmodel;
import com.demo.Model.PushNotificationRequest;
import com.demo.Model.TokenModel;
import com.demo.Model.UserModel;
import com.demo.Repository.FCMTokenRepo;
import com.demo.Repository.TokenRepo;
import com.demo.Repository.UserRepo;
import com.google.firebase.messaging.FirebaseMessagingException;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepoapi;

	@Autowired
	EmailService emailService;

	@Autowired
	private JwtToken jwtTokenapi;

	@Autowired
	private TokenRepo tokenRepoapi;

	@Autowired
	private FCMService fcmServiceapi;

	@Autowired
	private FCMTokenRepo FCMTokenRepoapi;

//	========================================================================================================================

	public Map<String, Object> create(UserModel model)
			throws NotFoundException, NoSuchAlgorithmException, IOException, FirebaseMessagingException {

		UserModel userDetails = userRepoapi.findByEmailAndPhoneNumber(model.getEmail(), model.getPhoneNumber());

		if (userDetails == null) {

			String number = "1234567890";
			String otp = "";
			SecureRandom random = new SecureRandom();
			char[] random_otp = new char[5];

			for (int i = 0; i < 4; i++) {
				random_otp[i] = number.charAt(random.nextInt(number.length()));

				otp = otp + random_otp[i];
			}

			java.security.MessageDigest digest = java.security.MessageDigest.getInstance("sha256");

			byte[] array = digest.digest(otp.getBytes());

			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				buffer.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			model.setOtp(otp);

			userRepoapi.save(model);

			String userEmails = model.getEmail();
			UserModel users1 = userRepoapi.findByEmail(userEmails);

//			emailService.sendOtpMail(model);

			String token = jwtTokenapi.generateToken(model);

			if (users1 != null && users1.getEmail().equals(model.getEmail())) {
//				TokenModel tokenList = tokenRepoapi.findByUserId(users1.getId());
//				if (tokenList != null) {
//				fcmServiceapi.sendNotification(tokenApi);
//				}

				model.setOtp(buffer.toString());

				userDetails = model;

				userRepoapi.save(userDetails);

				Map<String, Object> returnUser = new HashMap<>();

				returnUser.put("id :", model.getId());
				returnUser.put("Name", model.getName());
				returnUser.put("Email", model.getEmail());
				returnUser.put("PhoneNumber :", model.getPhoneNumber());
				returnUser.put("Token :", token);

				return returnUser;
			}

		} else {

			if (userDetails.getEmail() != null && userDetails.getEmail().equals(model.getEmail())) {
				throw new NotFoundException("Email Already Exists!");
			} else if (userDetails.getPhoneNumber() != null
					&& userDetails.getPhoneNumber().equals(model.getPhoneNumber())) {
				throw new NotFoundException("Mobile Number Already Exists!");
			}
		}

		throw new NotFoundException("Registration Failed !");

	}

//	========================================================================================================================

	public Map<String, Object> login(UserModel model, FCMmodel fcmModel)
			throws NotFoundException, NoSuchAlgorithmException, IOException {

		UserModel userDetails = userRepoapi.findByEmailAndPhoneNumber(model.getEmail(), model.getPhoneNumber());

		if (userDetails != null) {
			
			System.out.println("service"+fcmModel.getFcmtoken());
			
			fcmServiceapi.saveToken(userDetails.getId(), fcmModel.getFcmtoken());

			String number = "1234567890";
			String otp = "";
			Random random = new Random();
			char[] arr = new char[5];
			for (int i = 0; i < 5; i++) {
				arr[i] = number.charAt(random.nextInt(number.length()));
				otp = otp + arr[i];
			}

			java.security.MessageDigest digest = java.security.MessageDigest.getInstance("sha256");
			byte[] array = digest.digest(otp.getBytes());

			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < array.length; i++) {
				buffer.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}

			userDetails.setOtp(otp);

			userRepoapi.save(userDetails);

			String enteredEmail = model.getEmail();
			String storedEmail = userDetails.getEmail();
			String enteredPhoneNumber = model.getPhoneNumber();
			String storedPhoneNumber = userDetails.getPhoneNumber();

			if (enteredEmail.equals(storedEmail) && (enteredPhoneNumber.equals(storedPhoneNumber))) {
				userRepoapi.save(userDetails);
				UserModel users = userRepoapi.findByPhoneNumber(model.getPhoneNumber());
				emailService.sendOtpMail(users);

				FCMmodel fcmToken = FCMTokenRepoapi.findByUserId(users.getId());

				if (fcmToken != null) {
					PushNotificationRequest req = new PushNotificationRequest("OTP-Login", "your OTP is- " + otp,
							fcmToken.getFcmtoken());
					fcmServiceapi.sendNotification(req);
				}

				userDetails.setOtp(buffer.toString());
				userRepoapi.save(userDetails);
				TokenModel token = tokenRepoapi.findByUserId(userDetails.getId());

				Map<String, Object> returnUser = new HashMap<>();
				returnUser.put("token", token.getToken());
				returnUser.put("id", userDetails.getId());
				returnUser.put("email", userDetails.getEmail());
				returnUser.put("password", userDetails.getPassword());
				returnUser.put("name", userDetails.getName());
				returnUser.put("phoneNumber", userDetails.getPhoneNumber());

				return returnUser;

			}
		} else {

			throw new NotFoundException("Email Or PhoneNumber Not Found !");
		}
		throw new NotFoundException("Registration Failed !");

	}

//	========================================================================================================================

//	public String sendMail(userModel model)
//			throws NoSuchAlgorithmException, UnsupportedEncodingException, NotFoundException {
//
//		String numbers = "0123456789";
//		String x = "";
//		Random rndm_method = new Random();
//		char[] otp = new char[4];
//		for (int i = 0; i < 4; i++) {
//			otp[i] = numbers.charAt(rndm_method.nextInt(numbers.length()));
//			x = x + otp[i];
//		}
//
//		java.security.MessageDigest md = java.security.MessageDigest.getInstance("sha256");
//		byte[] array = md.digest(x.getBytes());
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < array.length; ++i) {
//			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
//		}
//		userModel users = userapi.findByPhoneNumber(model.getPhoneNumber());
//		if (users != null) {
//			TokenModel tokenList = tokenrepo.findByUserId(users.getId());
//			users.setOtp(x);
//			String userEmail = users.getEmail();
//			String modelEmail = model.getEmail();
//			if (userEmail.equals(modelEmail)) {
//				userapi.save(users);
//				userModel users1 = userapi.findByPhoneNumber(model.getPhoneNumber());
//				EmailServiceApi.sendOtpMail(users1);
//				if (tokenList != null) {
//					PushNotificationRequest req = new PushNotificationRequest("OTP-Login", "your OTP is- " + x,
//							tokenList.getToken());
//					pushNotificationServices.sendPushNotificationToToken(req);
//				}
//
//				users.setOtp(sb.toString());
//				userapi.save(users);
////	    		triggerEmail(users);
//				String token = jwtUtil.generateToken(users, null);
//				System.out.println(token);
//				return token;
//			} else {
//				throw new NotFoundException(" Invalid Mail ");
//			}
//
//		} else {
//			throw new NotFoundException(" Mobile Number Not Registered,Contact Your Admin For Registration ");
//		}
//
//	}

//	========================================================================================================================

	public UserModel OtpValidate(UserModel model) throws NotFoundException, NoSuchAlgorithmException {

		UserModel user = userRepoapi.findByEmail(model.getEmail());

		if (user != null) {

			String table_otp = user.getOtp();

			String otp = model.getOtp();

			java.security.MessageDigest digest = java.security.MessageDigest.getInstance("sha256");

			byte[] array = digest.digest(otp.getBytes());

			StringBuffer buffer = new StringBuffer();

			for (int i = 0; i < array.length; i++) {
				buffer.append(Integer.toHexString((array[i] & 0xFF) | 0100).substring(1, 3));
			}

			if (table_otp.equals(buffer.toString())) {
				return user;
			} else {
				throw new NotFoundException("Invalid OTP ");
			}
		} else {
			throw new NotFoundException("User Not Found !");
		}

	}

//	========================================================================================================================

	public List<UserModel> getall() throws NotFoundException {

		List<UserModel> allUsers = userRepoapi.findAll();

		return allUsers;

	}

//	========================================================================================================================

	public UserModel update(int userId, UserModel model) throws NotFoundException {

		UserModel details = userRepoapi.findById(userId).orElse(null);

		details.setName(model.getName());
		details.setPhoneNumber(model.getPhoneNumber());

		return details;

	}

//	========================================================================================================================

	public String Delete(int userId) throws NotFoundException {

		UserModel user = userRepoapi.findById(userId).orElse(null);

		if (user != null) {
			userRepoapi.delete(user);

			TokenModel model = tokenRepoapi.findByUserId(user.getId());

			tokenRepoapi.delete(model);

			return "User Deleted Successfully...!";
		} else {
			return "User Not Found !";
		}

	}

}
