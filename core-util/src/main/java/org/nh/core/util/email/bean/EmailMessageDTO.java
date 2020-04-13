package org.nh.core.util.email.bean;

public class EmailMessageDTO {
    private String subject;
    private String content;

    public EmailMessageDTO(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    public EmailMessageDTO() {
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
