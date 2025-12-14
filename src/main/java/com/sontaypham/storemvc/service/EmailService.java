package com.sontaypham.storemvc.service;

public interface EmailService {
    void sendTextEmail(Object object);
    void sendTemplateEmail(Object object);
}
