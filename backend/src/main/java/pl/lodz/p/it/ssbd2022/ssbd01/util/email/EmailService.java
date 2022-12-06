package pl.lodz.p.it.ssbd2022.ssbd01.util.email;

import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.LoggerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd01.interceptors.MailingServiceInterceptor;

import javax.enterprise.context.ApplicationScoped;
import javax.interceptor.Interceptors;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

@Interceptors({
        MailingServiceInterceptor.class,
        LoggerInterceptor.class})
@ApplicationScoped
public class EmailService implements EmailSender {

    private static final String SENDER_EMAIL = "ssbd01auth@gmail.com";
    private static final String SENDER_PASSWORD = "ecwcibibnotudowt";

    @Override
    public void sendEmail(String to, String token, MailTypeEnum mailType, String language) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                    }
                });

        ResourceBundle rb;

        if (language.equals("pl")) {
            Locale.setDefault(new Locale("pl"));
            rb = ResourceBundle.getBundle("messages", Locale.forLanguageTag("pl-PL"));
        } else {
            Locale.setDefault(new Locale("en"));
            rb = ResourceBundle.getBundle("messages", Locale.forLanguageTag("en"));
        }

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(SENDER_EMAIL));
            msg.setRecipients(Message.RecipientType.TO,
                    to);

            msg.setSubject(rb.getString(mailType.getEmailTitle()));
            msg.setSentDate(new Date());
            msg.setText(EmailBuilder.buildMail(rb.getString(mailType.getHtmlTitle()), rb.getString(mailType.getParagraph()), mailType.displayButton, token, rb.getString(mailType.getButtonText()), language),
                    "utf-8", "html");
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


}

