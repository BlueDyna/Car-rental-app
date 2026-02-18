package be.ucll.se.aaron_abbey_backend.mail;


import be.ucll.se.aaron_abbey_backend.model.Notification.NotificationType;

public class MailTemplate {

    private String html;
    private String text;
    

    public MailTemplate() {
    }

    public MailTemplate(String htmlTemplate, String text) {
        this.html = htmlTemplate;
        this.text = text;
    }

    private static MailTemplate createMailTemplate(String title, String text) {

        String cssStyle = "<style>" +
                "body {border: 1px solid red;}" +
                "h1 { color: #FF735C; }" +
                "main {background-color: #F8F8F8}" +
                "p { font-size: 16px; }" +
                "</style>";

        String html = String.format(
                "<html><head>%s</head><body><header><h1>%s</h1></header><main><p>%s</p></main></body></html>",
                cssStyle, title, text);

        return new MailTemplate(html, text);
    }

    public static MailTemplate mailToOwner(NotificationType type, String ownerName, String renterName, String car,
            String licensePlate,
            String startDate, String endDate) {
        String title = "";
        String text = "";
        if (type == NotificationType.CONFIRMATION) {
            title = "You have a new rental!";
            text = String.format(
                    "Dear %s.Your %s with license plate %s has been rented out by %s from %s until %s.", ownerName,
                    car, licensePlate, renterName, startDate, endDate);
        } else if (type == NotificationType.CANCELATION) {
            title = "Your rental has been cancelled!";
            text = String.format(
                    "Dear %s. The rent of your %s with license plate %s has been cancelled.", ownerName,
                    car, licensePlate);
        } else if (type == NotificationType.PENDING) {
            title = "You have a new rental request!";
            text = String.format(
                    "Dear %s. %s with license plate %s has been requested by %s from %s until %s. Got to the website to confirm or deny the request.", ownerName,
                    car, licensePlate, renterName, startDate, endDate);
        }

        return createMailTemplate(title, text);
    }

    public static MailTemplate mailToRenter(NotificationType type, String renterName, String car,
            String licensePlate,
            String startDate, String endDate) {
        String title = "";
        String text = "";
        if (type == NotificationType.CONFIRMATION) {
            title = "You have a new rental!";
            text = String.format(
                    "Dear %s. Your rent request of the %s with license plate %s from %s until %s has been confirmed.",
                    renterName,
                    car, licensePlate, startDate, endDate);
        } else if (type == NotificationType.CANCELATION) {
            title = "Your rental has been cancelled!";
            text = String.format(
                    "Dear %s. The rent of your %s with license plate %s has been cancelled.", renterName,
                    car, licensePlate);
        } else if (type == NotificationType.PENDING) {
            title = "Your request is being processed!";
            text = String.format(
                    "Dear %s. The rent of your %s with license plate %s has been request for now you have to do nothing. We will send you a mail when we know more.", renterName,
                    car, licensePlate);
        } 

        return createMailTemplate(title, text);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    public String getText() {
        return text;
    }
}
