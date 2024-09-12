package com.demo.Model;

public class PushNotificationRequest {
    private String title;
    private String message;
    private String fcmToken;

    public PushNotificationRequest(String title, String message, String fcmToken) {
        this.title = title;
        this.message = message;
        this.fcmToken = fcmToken;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
