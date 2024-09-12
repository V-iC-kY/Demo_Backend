package com.demo.Model;

public class LoginRequestModel {

	private UserModel userModel;
	private FCMmodel fcmModel;

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public FCMmodel getFcmModel() {
		return fcmModel;
	}

	public void setFcmModel(FCMmodel fcmModel) {
		this.fcmModel = fcmModel;
	}

}
