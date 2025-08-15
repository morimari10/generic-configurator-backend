package com.se.mail.service;

import com.se.domain.email.SEMail;

/**
 * Service for sending the emails.
 */
public interface MailService {

    /**
     * Sends an email.
     *
     * @param mail email data
     */
    void sendMail(SEMail mail);
}
