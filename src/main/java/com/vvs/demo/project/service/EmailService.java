package com.vvs.demo.project.service;

import com.vvs.demo.project.model.EmailDetails;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);
}
