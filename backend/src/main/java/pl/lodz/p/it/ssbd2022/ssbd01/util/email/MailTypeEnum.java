package pl.lodz.p.it.ssbd2022.ssbd01.util.email;

public enum MailTypeEnum {

    VERIFICATION_MAIL(
            "VerificationMail-Title",
            "VerificationMail-HTMLTitle",
            "VerificationMail-Text",
            true,
            "VerificationMail-Button"),
    ACCOUNT_CONFIRMED_MAIL(
            "AccountConfirmedMail-Title",
            "AccountConfirmedMail-HTMLTitle",
            "AccountConfirmedMail-Text",
            false,
            "AccountConfirmedMail-Button"),
    ACCOUNT_DELETED_MAIL(
            "AccountDeletedMail-Title",
            "AccountDeletedMail-HTMLTitle",
            "AccountDeletedMail-Text",
            false,
            "AccountDeletedMail-Button"),

    ACCOUNT_ACTIVATE_MAIL(
            "AccountActivationMail-Title",
            "AccountActivationMail-HTMLTitle",
            "AccountActivationMail-Text",
            false,
            "AccountActivationMail-Button"),

    ACCOUNT_DEACTIVATE_MAIL(
            "AccountDeactivateMail-Title",
            "AccountDeactivateMail-HTMLTitle",
            "AccountDeactivateMail-Text",
            false,
            "AccountDeactivateMail-Button"),
    RESET_PASSWORD_MAIL(
            "ResetPasswordMail-Title",
            "ResetPasswordMail-HTMLTitle",
            "ResetPasswordMail-Text",
            true,
            "ResetPasswordMail-Button"
    );


    String emailTitle;
    String htmlTitle;
    String paragraph;
    boolean displayButton;
    String buttonText;

    public String getEmailTitle() {
        return emailTitle;
    }

    public String getHtmlTitle() {
        return htmlTitle;
    }

    public String getParagraph() {
        return paragraph;
    }

    public String getButtonText() {
        return buttonText;
    }

    MailTypeEnum(String emailTitle, String htmlTitle, String paragraph, boolean displayButton, String buttonText) {
        this.emailTitle = emailTitle;
        this.htmlTitle = htmlTitle;
        this.paragraph = paragraph;
        this.displayButton = displayButton;
        this.buttonText = buttonText;
    }

}

