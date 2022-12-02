package bg.sofia.uni.fmi.mjt.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.Buffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RuleDefinitionConverter {

    private Set<String> subjectIncludes;
    private Set<String> subjectOrBodyIncludes;
    private Set<String> recipientsIncludes;
    private String from;

    public RuleDefinitionConverter() {
        this.subjectIncludes = new HashSet<>();
        this.subjectOrBodyIncludes = new HashSet<>();
        this.recipientsIncludes = new HashSet<>();
        this.from = "";
    }

    private static String processSubjectIncludes(String currentLine) {

        return currentLine.replaceFirst("subject-includes:", "").strip().replace(",", "");
    }

    private static String processSubjectOrBodyIncludes(String currentLine) {

        return currentLine.replaceFirst("subject-or-body-includes:", "").strip().replace(",", "");
    }

    private static String processRecipientsIncludes(String currentLine) {

        return currentLine.replaceFirst("recipients-includes:", "").strip().replace(",", "");
    }

    private static String processFrom(String currentLine) {

        return currentLine.replaceFirst("from:", "").strip();
    }
    private void processCurrentLine(String currentLine) {

        if (currentLine.startsWith("subject-includes:")) {

            String[] toAddSubjectIncludes = processSubjectIncludes(currentLine).split(" ");
            for (String currentSubjectInclude : toAddSubjectIncludes) {

                addSubjectIncludes(currentSubjectInclude);
            }
        }
        else if (currentLine.startsWith("subject-or-body-includes:")) {

            String[] toAddSubjectOrBodyIncludes = processSubjectOrBodyIncludes(currentLine).split(" ");
            for (String currentSubjectOrBodyInclude : toAddSubjectOrBodyIncludes) {

                addSubjectOrBodyIncludes(currentSubjectOrBodyInclude);
            }
        }
        else if (currentLine.startsWith("recipients-includes:")) {

            String[] toAddRecipientsIncludes = processRecipientsIncludes(currentLine).split(" ");
            for (String currentRecipientInclude : toAddRecipientsIncludes) {

                addRecipientsIncludes(currentRecipientInclude);
            }
        }
        else if (currentLine.startsWith("from:")) {

            setSenderEmail(processFrom(currentLine));
        }

    }

    public void convertToRuleDefinition(String ruleDefinition) {

        try (BufferedReader bufferedReader = new BufferedReader(new StringReader(ruleDefinition))) {

            String currentLine = null;

            while ((currentLine = bufferedReader.readLine()) != null) {
                currentLine = currentLine.strip();

                processCurrentLine(currentLine);
            }

        }
        catch (IOException e) {
            throw new RuntimeException("There is a problem in reading from string with ruleDefinition", e);
        }

    }

    private void setSenderEmail(String senderEmail) {
        this.from = senderEmail;
    }

    private void addSubjectIncludes(String subjectInclude) {
        this.subjectIncludes.add(subjectInclude);
    }

    private void addSubjectOrBodyIncludes(String subjectOrBodyInclude) {
        this.subjectOrBodyIncludes.add(subjectOrBodyInclude);
    }

    private void addRecipientsIncludes(String recipientInclude) {
        this.recipientsIncludes.add(recipientInclude);
    }

    public static void main(String[] args) {

        String ruleDefinition = "subject-includes: mjt, izpit, 2022\n" +
            "      subject-or-body-includes: izpit\n" +
            "      from: stoyo@fmi.bg";

        RuleDefinitionConverter a = new RuleDefinitionConverter();
        a.convertToRuleDefinition(ruleDefinition);

    }

}
