package com.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.Exception.NotFoundException;
import com.demo.Model.FCMmodel;
import com.demo.Service.FCMService;

@RestController
@RequestMapping("/api/user")
public class FCMController {

	@Autowired
	private FCMService fcmService;

	// ========================================================================================================================

//	@CrossOrigin("*")
//	@PostMapping("/register")
//	public FCMmodel registerFCMToken(@RequestBody FCMmodel token) throws NotFoundException {
//
//		return fcmService.saveToken(token);
//	}

	// ========================================================================================================================

	@CrossOrigin("*")
	@PostMapping("/getAllTokrn")
	public List<FCMmodel> getalltokrn() throws NotFoundException {

		return fcmService.getallTokrn();

	}

}
