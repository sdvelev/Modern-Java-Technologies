package bg.sofia.uni.fmi.mjt.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;


public class MailMetadataConverter {

    private String sender;
    private String subject;
    private Set<String> recipients;
    private LocalDateTime received;

    public MailMetadataConverter() {

        this.sender = "";
        this.subject = "";
        this.recipients = new HashSet<>();
        this.received = null;
    }

    public void convertToMailMetadata(String mailMetadata) {

        try (BufferedReader bufferedReader = new BufferedReader(new StringReader(mailMetadata))) {

            String currentLine;

            while ((currentLine = bufferedReader.readLine()) != null) {

                currentLine = currentLine.strip();
                processCurrentLine(currentLine);
            }
        } catch (IOException e) {

            throw new RuntimeException("There is a problem in reading from string with mailMetadata", e);
        }

    }

    public void setSender(String sender) {

        this.sender = sender;
    }

    public String getSender() {

        return sender;
    }

    public String getSubject() {

        return subject;
    }

    public Set<String> getRecipients() {

        return recipients;
    }

    public LocalDateTime getReceived() {

        return received;
    }

    @Override
    public String toString() {

        String stringRepresentationRecipients = "";

        for (String recipient : recipients) {

            stringRepresentationRecipients = stringRepresentationRecipients.concat(recipient + ", ");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedReceived = this.received.format(formatter);

        return "sender: " + this.sender + System.lineSeparator() +
            "subject: " + this.subject + System.lineSeparator() +
            "recipients: " + stringRepresentationRecipients + System.lineSeparator() +
            "received: " + formattedReceived + System.lineSeparator();
    }

    private static String processRecipients(String currentLine) {

        return currentLine.replaceFirst("recipients:", "").strip().replace(",", "");
    }

    private static String processSender(String currentLine) {

        return currentLine.replaceFirst("sender:", "").strip();
    }

    private static String processSubject(String currentLine) {

        return currentLine.replaceFirst("subject:", "").strip();
    }

    private static String processReceived(String currentLine) {

        return currentLine.replaceFirst("received:", "").strip();
    }
    private void processCurrentLine(String currentLine) {

        if (currentLine.startsWith("sender:")) {

            setSender(processSender(currentLine));
        }
        else if (currentLine.startsWith("subject:")) {

            setSubject(processSubject(currentLine));
        }
        else if (currentLine.startsWith("received:")) {

            DateTimeFormatter toFormatFromString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            setReceived(LocalDateTime.parse(processReceived(currentLine), toFormatFromString));
        }
        else if (currentLine.startsWith("recipients:")) {

            String[] toAddRecipients = processRecipients(currentLine).split(" ");
            for (String currentRecipient : toAddRecipients) {

                addRecipients(currentRecipient);
            }
        }
    }

    private void setSubject(String subject) {

        this.subject = subject;
    }

    private void addRecipients(String recipient) {

        this.recipients.add(recipient);
    }

    private void setReceived(LocalDateTime received) {

        this.received = received;
    }
}
