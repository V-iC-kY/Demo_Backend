//package com.demo.Service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingException;
//import com.google.firebase.messaging.Message;
////import com.google.firebase.messaging.MulticastMessage;
//import com.google.firebase.messaging.Notification;
//
//@Service
//public class FirebaseNotificationService {
//
//	private FirebaseMessaging firebaseMessaging;
//
//	@Autowired
//	public FirebaseNotificationService(FirebaseMessaging firebaseMessaging) {
//		this.firebaseMessaging = firebaseMessaging;
//	}
//
//	// Send notification to a single device
//	public void sendNotification(String title, String body, String token) throws FirebaseMessagingException {
//		Notification notification = Notification.builder().setTitle(title).setBody(body).build();
//
//		Message message = Message.builder().setToken(token).setNotification(notification).build();
//
//		// Sending notification to a single device
//		String response = firebaseMessaging.send(message);
//		System.out.println("Sent message to single device: " + response);
//	}
//
//	// Send notification to multiple devices
////	public void sendNotificationToMultipleDevices(String title, String body, List<String> tokens)
////			throws FirebaseMessagingException {
////		if (tokens == null || tokens.isEmpty()) {
////			System.out.println("No tokens provided");
////			return;
////		}
////
////		Notification notification = Notification.builder().setTitle(title).setBody(body).build();
////
////		MulticastMessage multicastMessage = MulticastMessage.builder().addAllTokens(tokens)
////				.setNotification(notification).build();
////
////		int successCount = firebaseMessaging.sendMulticast(multicastMessage).getSuccessCount();
////		System.out.println("Sent message to " + successCount + " devices");
////	}
//
//}
