package com.demo.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.demo.Exception.NotFoundException;
import com.demo.Model.FCMmodel;
import com.demo.Model.PushNotificationRequest;
import com.demo.Repository.FCMTokenRepo;
import com.google.firebase.messaging.*;

@Service
public class FCMService {

	@Autowired
	private FCMTokenRepo fCMTokenRepoapi;

	@Autowired
	private FirebaseMessaging firebaseMessaging;

	// ========================================================================================================================

	public void saveToken(int userId, String token) throws NotFoundException {

		FCMmodel existingToken = fCMTokenRepoapi.findByUserId(userId);

		if (existingToken == null) {

			FCMmodel fcMmodel = new FCMmodel();
			fcMmodel.setUserId(userId);
			fcMmodel.setFcmtoken(token);
			fCMTokenRepoapi.save(fcMmodel);
		} else {
			existingToken.setFcmtoken(token);
			fCMTokenRepoapi.save(existingToken);
		}
	}

	// ========================================================================================================================

	public List<FCMmodel> getallTokrn() throws NotFoundException {

		List<FCMmodel> allToken = fCMTokenRepoapi.findAll();

		return allToken;

	}

	// ========================================================================================================================

	public void sendNotification(PushNotificationRequest token) throws NotFoundException {

		Notification notification = Notification.builder().setTitle(token.getTitle()).setBody(token.getMessage())
				.build();

		Message message = Message.builder().setNotification(notification).setToken(token.getFcmToken()).build();

		try {
			String response = FirebaseMessaging.getInstance().send(message);
			System.out.println("Notification sent successfully: " + response);
		} catch (FirebaseMessagingException e) {
			if ("UNREGISTERED".equals(e.getErrorCode())) {
				System.err.println("The FCM token is unregistered. Removing from database.");
				// Remove the token from your database
//	            FCMTokenRepoapi.deleteByToken(token.getFcmToken());
			} else {
				System.err.println("Error sending notification: " + e.getMessage());
				throw new RuntimeException(e);
			}
		}
	}

	// ========================================================================================================================

	// Send notification to multiple devices

	public void sendNotificationToMultipleDevices(String title, String body, List<String> tokens)
			throws FirebaseMessagingException {
		if (tokens == null || tokens.isEmpty()) {
			System.out.println("No tokens provided");
			return;
		}

		Notification notification = Notification.builder().setTitle(title).setBody(body).build();

		MulticastMessage multicastMessage = MulticastMessage.builder().addAllTokens(tokens)
				.setNotification(notification).build();

		// Send the message to multiple devices

		int successCount = firebaseMessaging.sendMulticast(multicastMessage).getSuccessCount();
		System.out.println("Sent message to " + successCount + " devices");
	}

	// ========================================================================================================================

	public static void sendWebNotification(String webToken) {

		System.out.println("web");

		Notification notification = Notification.builder().setTitle("My app").setBody("1234").build();

		Message message = Message.builder().setNotification(notification).setToken(webToken).build();

		try {
			String response = FirebaseMessaging.getInstance().send(message);
			System.out.println("Successfully sent message: " + response);
		} catch (FirebaseMessagingException e) {
			System.out.println("FirebaseMessagingException: " + e.getMessage());
			e.printStackTrace(); // Print stack trace for detailed error information
		} catch (Exception e) {
			System.out.println("Error sending message: " + e.getMessage());
			e.printStackTrace(); // Print stack trace for detailed error information
		}
	}

}
