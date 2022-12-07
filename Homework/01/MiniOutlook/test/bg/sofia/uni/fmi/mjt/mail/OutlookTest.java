package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OutlookTest {


    @Test
    void testAddNewAccountWithEmptyAccountName() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.addNewAccount("", "fmi@uni-sofia.bg"),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testAddNewAccountWithNullAccountName() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.addNewAccount(null, "fmi@uni-sofia.bg"),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testAddNewAccountWithNullEmail() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.addNewAccount("fmi", null),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testAddNewAccountWithBlankEmail() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.addNewAccount("fmi", "        "),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testAddNewAccountSuccessful() {

        Outlook outlook = new Outlook();

        Set<Account> expected = Set.of(outlook.addNewAccount("fmi", "fmi@uni-sofia.bg"));

        Assertions.assertIterableEquals(expected, outlook.getAccounts());
    }

    @Test
    void testAddNewAccountEmailsAreSame() {

        Outlook outlook = new Outlook();

        Set<Account> expected = Set.of(outlook.addNewAccount("fhf", "fmi@uni-sofia.bg"),
            outlook.addNewAccount("fmi", "fmi@uni-sofia.bg"));

        Assertions.assertTrue(expected.containsAll(outlook.getAccounts()) &&
            outlook.getAccounts().containsAll(expected));
    }

    @Test
    void testAddNewAccountNameAlreadyExist() {

        Outlook outlook = new Outlook();

        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");

        Assertions.assertThrows(AccountAlreadyExistsException.class, () ->
                outlook.addNewAccount("fmi", "fmi@gmail.com"),
            "AccountAlreadyExistsException is expected but not thrown.");
    }

    @Test
    void testCreateFolderWithNullAccountName() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.createFolder(null, "/inbox/documents"),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testCreateFolderWithEmptyAccountName() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.createFolder("", "/inbox/documents"),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testCreateFolderWithEmptyPath() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.createFolder("fmi", ""),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testCreateFolderWithNullPath() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.createFolder("fmi", null),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testCreateFolderWithBlankPath() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.createFolder("fmi", "   "),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testCreateFolderSuccessfullyAdded() {

        Outlook outlook = new Outlook();
        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");

        Map<Folder, List<Folder>> searchedDirectories = null;

        for (AccountFolders currentAccountFolders : outlook.getAccountsFolders()) {

            if (currentAccountFolders.getAccount().name().equals("fmi")) {

                searchedDirectories = currentAccountFolders.getDirectories();
                break;
            }
        }

        Map<Folder, List<Folder>> expected = Map.of(new Folder("inbox"),
            List.of(new Folder("documents")), new Folder("sent"), new ArrayList<Folder>(),
            new Folder("documents"), new ArrayList<Folder>());


        Assertions.assertTrue(expected.equals(searchedDirectories),
            "Actual folders and their location is not the same as expected.");
    }

    @Test
    void testCreateFolderSuccessfullyAddedWithMoreLevels() {

        Outlook outlook = new Outlook();
        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");

        outlook.createFolder("fmi", "/inbox/other");

        outlook.createFolder("fmi", "/inbox/documents/fmiDocuments");

        Map<Folder, List<Folder>> searchedDirectories = null;

        for (AccountFolders currentAccountFolders : outlook.getAccountsFolders()) {

            if (currentAccountFolders.getAccount().name().equals("fmi")) {

                searchedDirectories = currentAccountFolders.getDirectories();
                break;
            }
        }

        Map<Folder, List<Folder>> expected = Map.of(new Folder("inbox"),
            List.of(new Folder("documents"), new Folder("other")),
            new Folder("sent"), new ArrayList<Folder>(),
            new Folder("other"), new ArrayList<Folder>(),
            new Folder("documents"), List.of(new Folder("fmiDocuments")),
            new Folder("fmiDocuments"), new ArrayList<>());


        Assertions.assertTrue(expected.equals(searchedDirectories),
            "Actual folders and their location is not the same as expected.");
    }

    @Test
    void testCreateFolderAccountNotFoundException() {

        Outlook outlook = new Outlook();
        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");

        Assertions.assertThrows(AccountNotFoundException.class, () ->
                outlook.createFolder("fhf", "/inbox/documents"),
            "AccountNotFoundException is expected but not thrown.");
    }

    @Test
    void testCreateFolderInvalidPathExceptionWithLeadingPathWrong() {

        Outlook outlook = new Outlook();
        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");

        Assertions.assertThrows(InvalidPathException.class, () ->
                outlook.createFolder("fmi", "/./documents"),
            "InvalidPathException is expected but not thrown.");
    }

    @Test
    void testCreateFolderInvalidPathExceptionWithIntermediateFoldersMissing() {

        Outlook outlook = new Outlook();
        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");

        Assertions.assertThrows(InvalidPathException.class, () ->
                outlook.createFolder("fmi", "/inbox/documents/fmiDocuments"),
            "InvalidPathException is expected but not thrown.");
    }

    @Test
    void testCreateFolderFolderAlreadyExistException() {

        Outlook outlook = new Outlook();
        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");

        Assertions.assertThrows(FolderAlreadyExistsException.class, () ->
                outlook.createFolder("fmi", "/inbox/documents"),
            "FolderAlreadyExistsException is expected but not thrown.");
    }

    @Test
    void testAddRuleWithAccountNameNull() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.addRule(null, "/inbox/documents", "ruleDefinition", 1),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testAddRuleWithFolderPathNull() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.addRule("fmi", null, "ruleDefinition", 1),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testAddRuleWithRuleDefinitionAndAccountNameNull() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.addRule(null, "/inbox/documents", null, 2),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testAddRuleWithPriorityOutOfRange() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.addRule("fmi", "/inbox/documents", "ruleDefinition", 11),
            "IllegalArgumentException is expected but not thrown.");
    }


    @Test
    void testAddRuleAccountNotFoundException() {

        Outlook outlook = new Outlook();
        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");

        Assertions.assertThrows(AccountNotFoundException.class, () ->
                outlook.addRule("fhf", "/inbox/documents", "ruleDefinition", 1),
            "AccountNotFoundException is expected but not thrown.");
    }

    @Test
    void testAddRuleFolderNotFoundException() {

        Outlook outlook = new Outlook();

        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");
        outlook.createFolder("fmi", "/inbox/fmiDocuments");

        Assertions.assertThrows(FolderNotFoundException.class, () ->
                outlook.addRule("fmi", "/inbox/fhfDocuments", "ruleDefinition", 1),
            "FolderNotFoundException is expected but not thrown.");
    }


    @Test
    void testAddRuleSuccessFullyWithoutRecipientsIncludes() {

        Outlook outlook = new Outlook();

        Account fmiAccount = outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");
        Account fhfAccount = outlook.addNewAccount("fhf", "fhf@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");
        outlook.createFolder("fmi", "/inbox/fmiDocuments");

        outlook.createFolder("fhf", "/inbox/documents");
        outlook.createFolder("fhf", "/inbox/fhfDocuments");
        outlook.createFolder("fhf", "/inbox/fhfDocuments/others");

        String ruleDefinition = "subject-includes: Halls" + System.lineSeparator() +
            "     subject-or-body-includes: Hall" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        outlook.addRule("fhf", "/inbox/fhfDocuments/others", ruleDefinition, 1);

        String metaData = "sender: fmi@uni-sofia.bg" + System.lineSeparator() +
            "     subject: Available Halls?" + System.lineSeparator() +
            "     recipients: fhf@uni-sofia.bg" + System.lineSeparator() +
            "     received: 2022-12-08 14:14";

        String mailContent = "I would like to ask if Hall 210 is free on 23.01.2023 from 10 to 15 h?";

        outlook.sendMail("fmi", metaData, mailContent);

        DateTimeFormatter toFormatFromString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Mail mailExpected = new Mail(fmiAccount, Set.of("fhf@uni-sofia.bg"), "Available Halls?",
            mailContent, LocalDateTime.parse("2022-12-08 14:14", toFormatFromString));

        Map<Folder, List<Folder>> searchedDirectories = null;

        for (AccountFolders currentAccountFolders : outlook.getAccountsFolders()) {

            if (currentAccountFolders.getAccount().name().equals("fhf")) {

                searchedDirectories = currentAccountFolders.getDirectories();
                break;
            }
        }

        Set<Map.Entry<Folder, List<Folder>>> searchedDirectoryAsSet = searchedDirectories.entrySet();

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("inbox")) {

                Assertions.assertFalse(currentEntry.getKey().getMails().contains(mailExpected),
                    "Mail must have been moved but it is not.");
                break;
            }
        }

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("others")) {

                Assertions.assertTrue(currentEntry.getKey().getMails().contains(mailExpected),
                    "Mail must have been moved but it is not present according to the rule.");
            }
        }

      /*  Map<Folder, List<Folder>> expected = Map.of(new Folder("inbox"),
            List.of(new Folder("documents")), new Folder("sent"), new ArrayList<Folder>(),
            new Folder("documents"), new ArrayList<Folder>());


        Assertions.assertTrue(expected.equals(searchedDirectories),
            "Actual folders and their location is not the same as expected.");*/

    }

    @Test
    void testAddRuleSuccessFullyWithoutRecipientsIncludesWithSubjectIncludesTwice() {

        Outlook outlook = new Outlook();

        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");
        outlook.addNewAccount("fhf", "fhf@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");
        outlook.createFolder("fmi", "/inbox/fmiDocuments");

        outlook.createFolder("fhf", "/inbox/documents");
        outlook.createFolder("fhf", "/inbox/fhfDocuments");
        outlook.createFolder("fhf", "/inbox/fhfDocuments/others");

        String ruleDefinition = "subject-includes: Halls" + System.lineSeparator() +
            "subject-includes: Another Halls" + System.lineSeparator() +
            "     subject-or-body-includes: Hall" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        Assertions.assertThrows(RuleAlreadyDefinedException.class, () ->
                outlook.addRule("fhf", "/inbox/fhfDocuments/others", ruleDefinition, 1),
            "In ruleDefinition subject-includes is met twice.");
    }

    @Test
    void testAddRuleSuccessFullyWithoutRecipientsIncludesWithSubjectOrBodyIncludesThreeTimes() {

        Outlook outlook = new Outlook();

        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");
        outlook.addNewAccount("fhf", "fhf@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");
        outlook.createFolder("fmi", "/inbox/fmiDocuments");

        outlook.createFolder("fhf", "/inbox/documents");
        outlook.createFolder("fhf", "/inbox/fhfDocuments");
        outlook.createFolder("fhf", "/inbox/fhfDocuments/others");

        String ruleDefinition = "subject-or-body-includes: Halls" + System.lineSeparator() +
            "subject-or-body-includes: Another Halls" + System.lineSeparator() +
            "     subject-or-body-includes: Hall" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        Assertions.assertThrows(RuleAlreadyDefinedException.class, () ->
                outlook.addRule("fhf", "/inbox/fhfDocuments/others", ruleDefinition, 1),
            "In ruleDefinition subject-includes is met twice.");
    }

    @Test
    void testAddRuleSuccessFullyWithoutRecipientsIncludesWithRecipientsIncludesTwice() {

        Outlook outlook = new Outlook();

        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");
        outlook.addNewAccount("fhf", "fhf@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");
        outlook.createFolder("fmi", "/inbox/fmiDocuments");

        outlook.createFolder("fhf", "/inbox/documents");
        outlook.createFolder("fhf", "/inbox/fhfDocuments");
        outlook.createFolder("fhf", "/inbox/fhfDocuments/others");

        String ruleDefinition = "subject-includes: Halls" + System.lineSeparator() +
            "subject-or-body-includes: Another Halls" + System.lineSeparator() +
            "     recipients-includes: fmi@uni-sofia.bg" + System.lineSeparator() +
            "     recipients-includes: fmi@uni-sofia.bg" + System.lineSeparator();

        Assertions.assertThrows(RuleAlreadyDefinedException.class, () ->
                outlook.addRule("fhf", "/inbox/fhfDocuments/others", ruleDefinition, 1),
            "In ruleDefinition subject-includes is met twice.");
    }

    @Test
    void testAddRuleSuccessFullyWithRecipientsIncludes() {

        Outlook outlook = new Outlook();

        Account fmiAccount = outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");
        outlook.addNewAccount("fhf", "fhf@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");
        outlook.createFolder("fmi", "/inbox/fmiDocuments");

        outlook.createFolder("fhf", "/inbox/documents");
        outlook.createFolder("fhf", "/inbox/fhfDocuments");
        outlook.createFolder("fhf", "/inbox/fhfDocuments/others");

        String ruleDefinition = "subject-includes: Halls" + System.lineSeparator() +
            "     recipients-includes: fhf@uni-sofia.bg, fzf@uni-sofia.bg," + System.lineSeparator() +
            "     subject-or-body-includes: Hall" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        outlook.addRule("fhf", "/inbox/fhfDocuments/others", ruleDefinition, 1);

        String metaData = "sender: fmi@uni-sofia.bg" + System.lineSeparator() +
            "     subject: Available Halls?" + System.lineSeparator() +
            "     recipients: fhf@uni-sofia.bg" + System.lineSeparator() +
            "     received: 2022-12-08 14:14";

        String mailContent = "I would like to ask if Hall 210 is free on 23.01.2023 from 10 to 15 h?";

        outlook.sendMail("fmi", metaData, mailContent);

        DateTimeFormatter toFormatFromString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Mail mailExpected = new Mail(fmiAccount, Set.of("fhf@uni-sofia.bg"), "Available Halls?",
            mailContent, LocalDateTime.parse("2022-12-08 14:14", toFormatFromString));

        Map<Folder, List<Folder>> searchedDirectories = null;

        for (AccountFolders currentAccountFolders : outlook.getAccountsFolders()) {

            if (currentAccountFolders.getAccount().name().equals("fhf")) {

                searchedDirectories = currentAccountFolders.getDirectories();
                break;
            }
        }

        Set<Map.Entry<Folder, List<Folder>>> searchedDirectoryAsSet = searchedDirectories.entrySet();

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("inbox")) {

                Assertions.assertFalse(currentEntry.getKey().getMails().contains(mailExpected),
                    "Mail must have been moved but it is not.");
                break;
            }
        }

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("others")) {

                Assertions.assertTrue(currentEntry.getKey().getMails().contains(mailExpected),
                    "Mail must have been moved but it is not present according to the rule.");
            }
        }

      /*  Map<Folder, List<Folder>> expected = Map.of(new Folder("inbox"),
            List.of(new Folder("documents")), new Folder("sent"), new ArrayList<Folder>(),
            new Folder("documents"), new ArrayList<Folder>());


        Assertions.assertTrue(expected.equals(searchedDirectories),
            "Actual folders and their location is not the same as expected.");*/

    }

    @Test
    void testGetMailsFromFolderWithNullAccount() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.getMailsFromFolder(null, "/inbox/documents"),
            "IllegalArgumentException is expected but not thrown.");

    }

    @Test
    void testGetMailsFromFolderWithNullFolderPath() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.getMailsFromFolder("fmi", null),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testGetMailsFromFolderWithBlankFolderPath() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.getMailsFromFolder("fmi", "    "),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testGetMailsFromFolderWithEmptyAccount() {

        Outlook outlook = new Outlook();

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                outlook.getMailsFromFolder("", "/inbox/documents"),
            "IllegalArgumentException is expected but not thrown.");
    }

    @Test
    void testGetMailsFromFolderAccountNotFoundException() {

        Outlook outlook = new Outlook();

        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");

        Assertions.assertThrows(AccountNotFoundException.class, () ->
                outlook.getMailsFromFolder("fhf", "/inbox/documents"),
            "AccountNotFoundException is expected but not thrown.");
    }

    @Test
    void testGetMailsFromFolderFolderNotFoundException() {

        Outlook outlook = new Outlook();

        outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");
        outlook.createFolder("fmi", "/inbox/documents");

        Assertions.assertThrows(FolderNotFoundException.class, () ->
                outlook.getMailsFromFolder("fmi", "/inbox/fmiDocuments"),
            "FolderNotFoundException is expected but not thrown.");
    }

    @Test
    void testGetMailsFromFolderSuccessfully() {

        Outlook outlook = new Outlook();

        Account fmiAccount = outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");
        outlook.createFolder("fmi", "/inbox/documents");
        outlook.createFolder("fmi", "/inbox/fmiDocuments");

        Map<Folder, List<Folder>> searchedDirectories = null;

        for (AccountFolders currentAccountFolders : outlook.getAccountsFolders()) {

            if (currentAccountFolders.getAccount().name().equals("fmi")) {

                searchedDirectories = currentAccountFolders.getDirectories();
                break;
            }
        }

        DateTimeFormatter toFormatFromString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        String mailContent = "I would like to ask if Hall 210 is free on 23.01.2023 from 10 to 15 h?";

        Mail mailOne = new Mail(fmiAccount, Set.of("fhf@uni-sofia.bg"), "Available Halls?",
            mailContent, LocalDateTime.parse("2022-12-08 14:14", toFormatFromString));

        Mail mailTwo = new Mail(fmiAccount, Set.of("fhf@uni-sofia.bg"), "Available Halls?",
            mailContent, LocalDateTime.parse("2022-12-10 17:19", toFormatFromString));

        Mail mailThree = new Mail(fmiAccount, Set.of("fhf@uni-sofia.bg"), "Available Halls?",
            mailContent, LocalDateTime.parse("2022-12-12 11:25", toFormatFromString));


        Set<Map.Entry<Folder, List<Folder>>> searchedDirectoryAsSet = searchedDirectories.entrySet();

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("inbox")) {

                currentEntry.getKey().addMail(mailOne);
                currentEntry.getKey().addMail(mailTwo);
                currentEntry.getKey().addMail(mailThree);
                break;
            }
        }

        Set<Mail> expected = Set.of(mailTwo, mailOne, mailThree);

        Assertions.assertTrue(expected.containsAll(outlook.getMailsFromFolder("fmi", "/inbox")) &&
                outlook.getMailsFromFolder("fmi", "/inbox").containsAll(expected),
            "Actual collection of mails is not the same as the expected.");
    }

    private void helper(String metaData, String ruleDefinition) {

        Outlook outlook = new Outlook();

        Account fmiAccount = outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");
        outlook.addNewAccount("fhf", "fhf@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");
        outlook.createFolder("fmi", "/inbox/fmiDocuments");

        outlook.createFolder("fhf", "/inbox/documents");
        outlook.createFolder("fhf", "/inbox/fhfDocuments");
        outlook.createFolder("fhf", "/inbox/fhfDocuments/others");

        outlook.addRule("fhf", "/inbox/fhfDocuments/others", ruleDefinition, 6);

        String mailContent = "I would like to ask if Hall 210 is free on 23.01.2023 from 10 to 15 h?";

        outlook.sendMail("fmi", metaData, mailContent);

        DateTimeFormatter toFormatFromString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Mail mailExpected = new Mail(fmiAccount, Set.of("fhf@uni-sofia.bg"), "Available Halls?",
            mailContent, LocalDateTime.parse("2022-12-08 14:14", toFormatFromString));

        Map<Folder, List<Folder>> searchedDirectories = null;

        for (AccountFolders currentAccountFolders : outlook.getAccountsFolders()) {

            if (currentAccountFolders.getAccount().name().equals("fhf")) {

                searchedDirectories = currentAccountFolders.getDirectories();
                break;
            }
        }

        Set<Map.Entry<Folder, List<Folder>>> searchedDirectoryAsSet = searchedDirectories.entrySet();

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("inbox")) {

                Assertions.assertTrue(currentEntry.getKey().getMails().contains(mailExpected),
                    "Mail must not have been moved but it is.");
                break;
            }
        }

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("others")) {

                Assertions.assertFalse(currentEntry.getKey().getMails().contains(mailExpected),
                    "Mail must not have been moved but it is present according to the rule.");
            }
        }



      /*  Map<Folder, List<Folder>> expected = Map.of(new Folder("inbox"),
            List.of(new Folder("documents")), new Folder("sent"), new ArrayList<Folder>(),
            new Folder("documents"), new ArrayList<Folder>());


        Assertions.assertTrue(expected.equals(searchedDirectories),
            "Actual folders and their location is not the same as expected.");*/

    }


    @Test
    void testAddRuleMailDoesNotMatchSubjectInclude() {

        String metaData = "subject: Available Halls?" + System.lineSeparator() +
            "     recipients: fhf@uni-sofia.bg" + System.lineSeparator() +
            "     received: 2022-12-08 14:14";

        String ruleDefinition = "subject-includes: Halls, Availabl" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        this.helper(metaData, ruleDefinition);

    }

    @Test
    void testAddRuleMailDoesNotMatchSubjectOrBodyInclude() {

        String metaData = "subject: Available Halls?" + System.lineSeparator() +
            "     recipients: fhf@uni-sofia.bg" + System.lineSeparator() +
            "     received: 2022-12-08 14:14";

        String ruleDefinition = "subject-or-body-includes: Halls, Availabl" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        this.helper(metaData, ruleDefinition);
    }

    @Test
    void testAddRuleMailDoesNotMatchRecipientsInclude() {

        String metaData = "subject: Available Halls?" + System.lineSeparator() +
            "     recipients: fhf@uni-sofia.bg" + System.lineSeparator() +
            "     received: 2022-12-08 14:14";

        String ruleDefinition = "recipients-includes: fhf@uni-sofia.bg., fhf@uni-sofia.bg!" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        this.helper(metaData, ruleDefinition);
    }

    @Test
    void testAddRuleMailDoesNotMatchFrom() {

        String metaData = "subject: Available Halls?" + System.lineSeparator() +
            "     recipients: fhf@uni-sofia.bg" + System.lineSeparator() +
            "     received: 2022-12-08 14:14";

        String ruleDefinition = "recipients-includes: fhf@uni-sofia.bg" + System.lineSeparator() +
            "     from: fmi@uni_sofia.bg" + System.lineSeparator();

        this.helper(metaData, ruleDefinition);
    }


    @Test
    void testSendMailWithTwoRulesWithDifferentPriority() {

        Outlook outlook = new Outlook();

        Account fmiAccount = outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");
        outlook.addNewAccount("fhf", "fhf@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");
        outlook.createFolder("fmi", "/inbox/fmiDocuments");

        outlook.createFolder("fhf", "/inbox/documents");
        outlook.createFolder("fhf", "/inbox/fhfDocuments");
        outlook.createFolder("fhf", "/inbox/fhfDocuments/others");

        String ruleDefinition = "subject-includes: Halls" + System.lineSeparator() +
            "     recipients-includes: fhf@uni-sofia.bg, fzf@uni-sofia.bg," + System.lineSeparator() +
            "     subject-or-body-includes: Hall" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        String ruleDefinitionAnother = "subject-includes: Available" + System.lineSeparator() +
            "     recipients-includes: fhf@uni-sofia.bg, fzf@uni-sofia.bg, fh@uni-sofia.bg" + System.lineSeparator() +
            "     subject-or-body-includes: Hall" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        outlook.addRule("fhf", "/inbox/fhfDocuments", ruleDefinitionAnother, 1);

        outlook.addRule("fhf", "/inbox/fhfDocuments/others", ruleDefinition, 4);

        String metaData = "sender: fmi@uni-sofia.bg" + System.lineSeparator() +
            "     subject: Available Halls?" + System.lineSeparator() +
            "     recipients: fhf@uni-sofia.bg" + System.lineSeparator() +
            "     received: 2022-12-08 14:14";

        String mailContent = "I would like to ask if Hall 210 is free on 23.01.2023 from 10 to 15 h?";

        outlook.sendMail("fmi", metaData, mailContent);

        DateTimeFormatter toFormatFromString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Mail mailExpected = new Mail(fmiAccount, Set.of("fhf@uni-sofia.bg"), "Available Halls?",
            mailContent, LocalDateTime.parse("2022-12-08 14:14", toFormatFromString));

        Map<Folder, List<Folder>> searchedDirectories = null;

        for (AccountFolders currentAccountFolders : outlook.getAccountsFolders()) {

            if (currentAccountFolders.getAccount().name().equals("fhf")) {

                searchedDirectories = currentAccountFolders.getDirectories();
                break;
            }
        }

        Set<Map.Entry<Folder, List<Folder>>> searchedDirectoryAsSet = searchedDirectories.entrySet();

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("inbox")) {

                Assertions.assertFalse(currentEntry.getKey().getMails().contains(mailExpected),
                    "Mail must have been moved but it is not.");
                break;
            }
        }

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("fhfDocuments")) {

                Assertions.assertTrue(currentEntry.getKey().getMails().contains(mailExpected),
                    "Mail must have been moved but it is not present according to the rule.");
            }
        }

    }


    @Test
    void testAddRuleReceiveEmailAndThenAddTwoRules() {

        Outlook outlook = new Outlook();

        Account fmiAccount = outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");
        outlook.addNewAccount("fhf", "fhf@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");
        outlook.createFolder("fmi", "/inbox/fmiDocuments");

        outlook.createFolder("fhf", "/inbox/documents");
        outlook.createFolder("fhf", "/inbox/fhfDocuments");
        outlook.createFolder("fhf", "/inbox/fhfDocuments/others");

        String metaData = "sender: fmi@uni-sofia.bg" + System.lineSeparator() +
            "     subject: Available Halls?" + System.lineSeparator() +
            "     recipients: fhf@uni-sofia.bg" + System.lineSeparator() +
            "     received: 2022-12-08 14:14";

        String mailContent = "I would like to ask if Hall 210 is free on 23.01.2023 from 10 to 15 h?";

        outlook.receiveMail("fhf", metaData, mailContent);

        String ruleDefinition = "subject-includes: Halls" + System.lineSeparator() +
            "     recipients-includes: fhf@uni-sofia.bg, fzf@uni-sofia.bg," + System.lineSeparator() +
            "     subject-or-body-includes: Hall" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        String ruleDefinitionAnother = "subject-includes: Available" + System.lineSeparator() +
            "     recipients-includes: fhf@uni-sofia.bg, fzf@uni-sofia.bg, fh@uni-sofia.bg" + System.lineSeparator() +
            "     subject-or-body-includes: Hall" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        DateTimeFormatter toFormatFromString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Mail mailExpected = new Mail(fmiAccount, Set.of("fhf@uni-sofia.bg"), "Available Halls?",
            mailContent, LocalDateTime.parse("2022-12-08 14:14", toFormatFromString));

        Map<Folder, List<Folder>> searchedDirectories = null;

        for (AccountFolders currentAccountFolders : outlook.getAccountsFolders()) {

            if (currentAccountFolders.getAccount().name().equals("fhf")) {

                searchedDirectories = currentAccountFolders.getDirectories();
                break;
            }
        }

        Set<Map.Entry<Folder, List<Folder>>> searchedDirectoryAsSet = searchedDirectories.entrySet();

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("inbox")) {

                Assertions.assertTrue(currentEntry.getKey().getMails().contains(mailExpected),
                    "Mail must be in inbox but it is not.");
                break;
            }
        }

        outlook.addRule("fhf", "/inbox/fhfDocuments", ruleDefinitionAnother, 4);

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("fhfDocuments")) {

                Assertions.assertTrue(currentEntry.getKey().getMails().contains(mailExpected),
                    "Mail must have been moved but it is not present according to the rule.");
            }
        }

        outlook.addRule("fhf", "/inbox/fhfDocuments/others", ruleDefinition, 1);

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("others")) {

                Assertions.assertFalse(currentEntry.getKey().getMails().contains(mailExpected),
                    "Mail must not have been moved according to the last added rule as it is already moved.");
            }
        }

    }

    @Test
    void testSendMailMailIsInSent() {


        Outlook outlook = new Outlook();

        Account fmiAccount = outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");
        outlook.addNewAccount("fhf", "fhf@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");
        outlook.createFolder("fmi", "/inbox/fmiDocuments");

        outlook.createFolder("fhf", "/inbox/documents");
        outlook.createFolder("fhf", "/inbox/fhfDocuments");
        outlook.createFolder("fhf", "/inbox/fhfDocuments/others");

        String metaData = "sender: fmi@uni-sofia.bg" + System.lineSeparator() +
            "     subject: Available Halls?" + System.lineSeparator() +
            "     recipients: fhf@uni-sofia.bg" + System.lineSeparator() +
            "     received: 2022-12-08 14:14";

        String mailContent = "I would like to ask if Hall 210 is free on 23.01.2023 from 10 to 15 h?";

        outlook.sendMail("fmi", metaData, mailContent);

        DateTimeFormatter toFormatFromString = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        Mail mailExpected = new Mail(fmiAccount, Set.of("fhf@uni-sofia.bg"), "Available Halls?",
            mailContent, LocalDateTime.parse("2022-12-08 14:14", toFormatFromString));

        Map<Folder, List<Folder>> searchedDirectories = null;

        for (AccountFolders currentAccountFolders : outlook.getAccountsFolders()) {

            if (currentAccountFolders.getAccount().name().equals("fmi")) {

                searchedDirectories = currentAccountFolders.getDirectories();
                break;
            }
        }

        Set<Map.Entry<Folder, List<Folder>>> searchedDirectoryAsSet = searchedDirectories.entrySet();

        for (Map.Entry<Folder, List<Folder>> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey().getFolderName().equals("sent")) {

                Assertions.assertTrue(currentEntry.getKey().getMails().contains(mailExpected),
                    "Mail must be in sent but it is not.");
                break;
            }
        }
    }


}
