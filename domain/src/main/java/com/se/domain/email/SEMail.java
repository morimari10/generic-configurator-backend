package com.se.domain.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Email data for sending.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SEMail {

    private static final int WIDTH_ABBREVIATE = 4;

    private String toEmail;
    private String replyEmail;
    private String senderName;
    private String subject;
    private String mailBody;
    private Attachment attachment;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("toEmail", StringUtils.abbreviate(toEmail, WIDTH_ABBREVIATE))
                .append("replyEmail", StringUtils.abbreviate(replyEmail, WIDTH_ABBREVIATE))
                .append("senderName", StringUtils.abbreviate(senderName, WIDTH_ABBREVIATE))
                .append("subject", StringUtils.abbreviate(subject, WIDTH_ABBREVIATE))
                .append("mailBody", StringUtils.abbreviate(mailBody, WIDTH_ABBREVIATE))
                .toString();
    }
}
