package com.demo.Controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.Exception.NotFoundException;
import com.demo.Model.FCMmodel;
import com.demo.Model.LoginRequestModel;
import com.demo.Model.UserModel;
import com.demo.Service.UserService;
import com.google.firebase.messaging.FirebaseMessagingException;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userServiceapi;

//	========================================================================================================================

	@CrossOrigin("*")
	@PostMapping("/create")
	public Map<String, Object> Create(@RequestBody UserModel model)
			throws NotFoundException, NoSuchAlgorithmException, IOException, FirebaseMessagingException {
		try {
			return userServiceapi.create(model);

		} catch (NotFoundException e) {
			throw new NotFoundException(e.getMessage());
		}
	}

//	========================================================================================================================

	@CrossOrigin("*")
	@PostMapping("/login")
	public Map<String, Object> login(@RequestBody LoginRequestModel loginRequest)
			throws NotFoundException, NoSuchAlgorithmException, IOException {

		try {
			UserModel userModel = loginRequest.getUserModel();
			FCMmodel fcmModel = loginRequest.getFcmModel();
			System.out.println("Controller - FCM Token: " + fcmModel.getFcmtoken());

			if (userServiceapi == null) {
				throw new IllegalStateException("UserService is not initialized");
			}

			return userServiceapi.login(userModel, fcmModel);
		} catch (NotFoundException e) {
			System.err.println("Error: " + e.getMessage());
			throw new NotFoundException("Error: " + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Error: " + e.getMessage());
			throw new NoSuchAlgorithmException("Error: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			throw new IOException("Error: " + e.getMessage());
		}
	}

//	========================================================================================================================

	@CrossOrigin("*")
	@PostMapping("/validateOtp")
	public UserModel validateOtp(@RequestBody UserModel model) throws NotFoundException, NoSuchAlgorithmException {
		return userServiceapi.OtpValidate(model);

	}

//	========================================================================================================================

	@CrossOrigin("*")
	@GetMapping("/getAll")
	public List<UserModel> getUser() throws NotFoundException {
		try {
			return userServiceapi.getall();

		} catch (NotFoundException e) {
			throw new NotFoundException(e.getMessage());
		}
	}

//	========================================================================================================================

	@CrossOrigin("*")
	@PutMapping("/update/{userId}")
	public UserModel update(@PathVariable int userId, @RequestBody UserModel model) throws NotFoundException {
		try {
			return userServiceapi.update(userId, model);

		} catch (NotFoundException e) {
			throw new NotFoundException(e.getMessage());
		}

	}

//	========================================================================================================================
	@CrossOrigin("*")
	@DeleteMapping("/delete/{userId}")
	public String delete(@PathVariable int userId) throws NotFoundException {
		try {
			return userServiceapi.Delete(userId);
		} catch (NotFoundException e) {
			throw new NotFoundException(e.getMessage());
		}
	}
}
