package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class RuleDefinitionConverter {

    private Set<String> subjectIncludes;
    private boolean subjectIncludesAlreadyDefined;
    private Set<String> subjectOrBodyIncludes;
    private boolean subjectOrBodyIncludesAlreadyDefined;
    private Set<String> recipientsIncludes;
    private boolean recipientsIncludesAlreadyDefined;
    private String from;
    private boolean fromAlreadyDefined;
    private String destinationPath = "";

    public RuleDefinitionConverter() {

        this.subjectIncludes = new HashSet<>();
        this.subjectOrBodyIncludes = new HashSet<>();
        this.recipientsIncludes = new HashSet<>();
        this.from = "";

        this.subjectIncludesAlreadyDefined = false;
        this.subjectOrBodyIncludesAlreadyDefined = false;
        this.recipientsIncludesAlreadyDefined = false;
        this.fromAlreadyDefined = false;
    }

    public Set<String> getSubjectsIncludes() {

        return this.subjectIncludes;
    }

    public Set<String> getSubjectsOrBodyIncludes() {

        return this.subjectOrBodyIncludes;
    }

    public Set<String> getRecipientsIncludes() {

        return this.recipientsIncludes;
    }

    public String getFrom() {

        return this.from;
    }

    public void setDestinationPath(String destinationPath) {

        this.destinationPath = destinationPath;
    }

    public String getDestinationPath() {

        return this.destinationPath;
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

    private void processSubjectIncludeAlreadyDefined() {

        if (this.subjectIncludesAlreadyDefined) {

            throw new RuleAlreadyDefinedException("Rule is invalid");
        }
    }

    private void processSubjectOrBodyIncludeAlreadyDefined() {

        if (this.subjectOrBodyIncludesAlreadyDefined) {

            throw new RuleAlreadyDefinedException("Rule is invalid");
        }
    }

    private void processRecipientsIncludeAlreadyDefined() {

        if (this.recipientsIncludesAlreadyDefined) {

            throw new RuleAlreadyDefinedException("Rule is invalid");
        }
    }

    private void processFromAlreadyDefined() {

        if (this.fromAlreadyDefined) {

            throw new RuleAlreadyDefinedException("Rule is invalid");
        }
    }

    private void processCurrentLine(String currentLine) {

        if (currentLine.startsWith("subject-includes:")) {

            processSubjectIncludeAlreadyDefined();
            this.subjectIncludesAlreadyDefined = true;

            String[] toAddSubjectIncludes = processSubjectIncludes(currentLine).split(" ");
            for (String currentSubjectInclude : toAddSubjectIncludes) {

                addSubjectIncludes(currentSubjectInclude);
            }
        }
        else if (currentLine.startsWith("subject-or-body-includes:")) {

            processSubjectOrBodyIncludeAlreadyDefined();
            this.subjectOrBodyIncludesAlreadyDefined = true;

            String[] toAddSubjectOrBodyIncludes = processSubjectOrBodyIncludes(currentLine).split(" ");

            for (String currentSubjectOrBodyInclude : toAddSubjectOrBodyIncludes) {

                addSubjectOrBodyIncludes(currentSubjectOrBodyInclude);
            }
        }
        else if (currentLine.startsWith("recipients-includes:")) {

            processRecipientsIncludeAlreadyDefined();
            this.recipientsIncludesAlreadyDefined = true;

            String[] toAddRecipientsIncludes = processRecipientsIncludes(currentLine).split(" ");
            for (String currentRecipientInclude : toAddRecipientsIncludes) {

                addRecipientsIncludes(currentRecipientInclude);
            }
        }
        else if (currentLine.startsWith("from:")) {

            processFromAlreadyDefined();
            this.fromAlreadyDefined = true;
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

    /*public static void main(String[] args) {

        String ruleDefinition = "subject-includes: mjt, izpit, 2022" + System.lineSeparator() +
            "      subject-or-body-includes: izpit" + System.lineSeparator() +
            "      from: stoyo@fmi.bg";

        RuleDefinitionConverter a = new RuleDefinitionConverter();
        a.convertToRuleDefinition(ruleDefinition);

    }*/

}
