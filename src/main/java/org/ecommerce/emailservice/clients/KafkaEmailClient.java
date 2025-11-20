package org.ecommerce.emailservice.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ecommerce.emailservice.dtos.EmailDto;
import org.ecommerce.emailservice.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

@Service
public class KafkaEmailClient {

    private final ObjectMapper objectMapper;
    private final String smtpUsername;
    private final String smtpPassword;

    @Autowired
    public KafkaEmailClient(ObjectMapper objectMapper,
                            @Value("${mail.smtp.username}") String smtpUsername,
                            @Value("${mail.smtp.password}") String smtpPassword) {
        this.objectMapper = objectMapper;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
    }

    @KafkaListener(topics = "EMAIL_SIGNUP", groupId = "email-service-group")
    public void sendEmail(String message) {
        // Implementation for sending email via Kafka
        try{
            EmailDto emailDto = objectMapper.readValue(message, EmailDto.class);

            System.out.println("TLSEmail Start");
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(smtpUsername, smtpPassword);
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, smtpUsername, emailDto.getTo(), emailDto.getSubject(), emailDto.getBody());

        } catch (Exception exception){
            throw new RuntimeException(exception);
        }
    }
}
