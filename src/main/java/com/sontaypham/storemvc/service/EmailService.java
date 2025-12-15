package com.sontaypham.storemvc.service;

import com.sontaypham.storemvc.dto.response.email.EmailResponse;
import com.sontaypham.storemvc.model.EmailDetails;

public interface EmailService {
    void sendTextEmail(EmailDetails emailDetails);
    void sendTemplateEmail(EmailDetails emailDetails);
}
