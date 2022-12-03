package bg.sofia.uni.fmi.mjt.mail;

import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.FolderNotFoundException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.InvalidPathException;
import bg.sofia.uni.fmi.mjt.mail.exceptions.RuleAlreadyDefinedException;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Outlook implements MailClient {

    private Set<Account> accounts;
    private Set<AccountFolders> accountsFolders;
    private Set<AccountRules> accountRules;

    public Outlook() {
        this.accounts = new HashSet<>();
        this.accountsFolders = new HashSet<>();
        this.accountRules = new HashSet<>();
    }

    /**
     * Creates a new account in the MailClient
     *
     * @param accountName short name of the account
     * @param email       email of the account
     * @return the created Account
     * @throws IllegalArgumentException      if any of the string parameters is null, empty or blank
     * @throws AccountAlreadyExistsException if account with the same name is already present in the client
     */
    @Override
    public Account addNewAccount(String accountName, String email) {

        validateIsNull(accountName, "accountName");
        validateIsEmpty(accountName, "accountName");
        validateIsBlank(accountName, "accountName");

        validateIsNull(email, "email");
        validateIsEmpty(email, "email");
        validateIsBlank(email, "email");

        Account toAddAccount = new Account(email, accountName);

        validateIsExistingAccount(toAddAccount);

        this.accounts.add(toAddAccount);

        return toAddAccount;
    }

    /**
     * @param accountName name of the account for which the folder is created
     * @param path        full path to the folder. The root folder and the path separator character
     *                    is forward slash ('/')
     * @throws IllegalArgumentException     if any of the string parameters is null, empty or blank
     * @throws AccountNotFoundException     if the account is not present
     * @throws InvalidPathException         if the folder path does not start from the root folder
     *                                      of received mails, or if some intermediate folders do not exist
     * @throws FolderAlreadyExistsException if folder with the same absolute path is already present
     *                                      for the provided account
     */
    @Override
    public void createFolder(String accountName, String path) {

        validateIsNull(accountName, "accountName");
        validateIsEmpty(accountName, "accountName");
        validateIsBlank(accountName, "accountName");

        validateIsNull(path, "path");
        validateIsEmpty(path, "path");
        validateIsBlank(path, "path");

        Account searchedAccount = getAndValidateAccountNotFoundException(accountName);

        AccountFolders searchedAccountFolders = getAccountFoldersFromAccount(searchedAccount);

        processPath(path, searchedAccountFolders);

    }

    /**
     * Creates a new Rule for the current mail client.
     * The following definition is the valid format for rules:
     * subject-includes: <list-of-keywords>
     * subject-or-body-includes: <list-of-keywords>
     * recipients-includes: <list-of-recipient-emails>
     * from: <sender-email>
     *
     * The order is not determined, and the list might not be full. Example:
     * subject-includes: mjt, izpit, 2022
     * subject-or-body-includes: izpit
     * from: stoyo@fmi.bg
     *
     * For subject-includes and subject-or-body-includes rules, if more than one keywords is specified, all must
     * be contained for the rule to match, i.e. it is a conjunction condition. For recipients-includes,
     * it's enough for one listed recipient to match (disjunction condition). For from, it should be exact match.
     *
     * @param accountName    name of the account for which the rule is applied
     * @param folderPath     full path of the destination folder
     * @param ruleDefinition string definition of the rule
     * @param priority       priority of the rule - [1,10], 1 = highest priority
     * @throws IllegalArgumentException    if any of the string parameters is null, empty or blank,
     *                                     or the priority of the rule is not within the expected range
     * @throws AccountNotFoundException    if the account does not exist
     * @throws FolderNotFoundException     if the folder does not exist
     * @throws RuleAlreadyDefinedException if the rule definition contains a rule/condition that already exists
     */
    @Override
    public void addRule(String accountName, String folderPath, String ruleDefinition, int priority) {

        validateIsNull(accountName, "accountName");
        validateIsEmpty(accountName, "accountName");
        validateIsBlank(accountName, "accountName");

        validateIsNull(folderPath, "folderPath");
        validateIsEmpty(folderPath, "folderPath");
        validateIsBlank(folderPath, "folderPath");

        validateIsNull(ruleDefinition, "ruleDefinition");
        validateIsEmpty(ruleDefinition, "ruleDefinition");
        validateIsBlank(ruleDefinition, "ruleDefinition");

        Account searchedAccount = getAndValidateAccountNotFoundException(accountName);

        AccountFolders searchedAccountFolders = getAccountFoldersFromAccount(searchedAccount);

        if (!isExistingPath(folderPath, searchedAccountFolders)) {
            throw new FolderNotFoundException("Folder does not exist");
        }

        Set<Folder> folders = searchedAccountFolders.getDirectories().keySet();

        RuleDefinitionConverter ruleDefinitionConverter = new RuleDefinitionConverter();
        ruleDefinitionConverter.convertToRuleDefinition(ruleDefinition);
        ruleDefinitionConverter.setDestinationPath(folderPath);

        AccountRules searchedAccountRules = getAccountRulesFromAccount(searchedAccount);

        searchedAccountRules.addAccountRule(ruleDefinitionConverter, priority);

        // TODO We do not have to execute all rules but only the currently added rule
        this.executeAccountRules(searchedAccountRules, searchedAccountFolders);

       /* for (Folder currentFolder : folders) {

            for (Mail currentMail : currentFolder.getMails()) {

                if (isForMoving(currentMail, ruleDefinitionConverter)) {
                    //Move
                }
            }
        }*/



    }

    private void executeAccountRules(AccountRules searchedAccountRules, AccountFolders searchedAccountFolders) {

        Collection<RuleDefinitionConverter> values = searchedAccountRules.getAccountRules().values();

        Set<Folder> folders = searchedAccountFolders.getDirectories().keySet();

        for (RuleDefinitionConverter currentRule : values) {

            for (Folder currentFolder : folders) {

                for (Mail currentMail : currentFolder.getMails()) {

                    if (isForMoving(currentMail, currentRule)) {
                        //Move
                        //searchedAccountFolders.getDirectories().get(currentFolder).remove(currentMail);

                        validatePathStartsFromRoot(currentRule.getDestinationPath());

                        Map<Folder, List<Folder>> directories = searchedAccountFolders.getDirectories();

                        String[] followingFolders = currentRule.getDestinationPath().split("/");

                        String lastDirectory = followingFolders[followingFolders.length - 1];
                        Folder lastDirectoryFolder = new Folder(lastDirectory);

                        for (Folder folder : folders) {

                            if (folder.equals(currentFolder)) {
                                folder.getMails().remove(currentMail);
                            }

                            if (folder.equals(lastDirectoryFolder)) {
                                folder.addMail(currentMail);
                            }
                        }

                    }
                }
            }
        }


    }

    private boolean isForMoving(Mail mail, RuleDefinitionConverter ruleDefinitionConverter) {


        for (String currentSubjectInclude : ruleDefinitionConverter.getSubjectsIncludes()) {

            if (!mail.subject().contains(currentSubjectInclude)) {
                return false;
            }

        }

        for (String currentSubjectOrBodyInclude : ruleDefinitionConverter.getSubjectsOrBodyIncludes()) {

            if (!mail.subject().contains(currentSubjectOrBodyInclude) && !mail.body()
                .contains(currentSubjectOrBodyInclude)) {
                return false;
            }

        }

        boolean isMet = false;

        for (String currentRecipientInclude : ruleDefinitionConverter.getRecipientsIncludes()) {

            if (mail.recipients().contains(currentRecipientInclude)) {
                isMet = true;
            }

        }

        if (!isMet) {
            return false;
        }

        if (!mail.sender().emailAddress().equals(ruleDefinitionConverter.getFrom())) {
            return false;
        }
        return  true;
    }

    private boolean isExistingPath(String folderPath, AccountFolders accountFolders) {

        Map<Folder, List<Folder>> directories = accountFolders.getDirectories();

        String[] followingFolders = folderPath.split("/");

        for (int i = 1; i < followingFolders.length - 1; i++) {

            if (directories.containsKey(new Folder(followingFolders[i]))) {

                boolean isFound = false;

                for (Folder neighbour : directories.get(new Folder(followingFolders[i]))) {


                    if (neighbour.getFolderName().equals(followingFolders[i + 1])) {
                        isFound = true;
                    }
                }

                if (!isFound) {
                    throw new FolderNotFoundException("Folder does not exist");
                }
            }
            else {
                return false;
            }
        }
        return true;
    }

    /**
     * The mail metadata has the following format (we always expect valid format of the mail metadata,
     * no validations are required):
     * sender: <sender-email>
     * subject: <subject>
     * recipients: <list-of-emails>
     * received: <LocalDateTime> - in format yyyy-MM-dd HH:mm
     * <p>
     * The order is not determined and the list might not be full. Example:
     * sender: testy@gmail.com
     * subject: Hello, MJT!
     * recipients: pesho@gmail.com, gosho@gmail.com,
     * received: 2022-12-08 14:14
     *
     * @param accountName  the recipient account
     * @param mailMetadata metadata, including the sender, all recipients, subject, and receiving time
     * @param mailContent  content of the mail
     * @throws IllegalArgumentException if any of the parameters is null, empty or blank
     * @throws AccountNotFoundException if the account does not exist
     */
    @Override
    public void receiveMail(String accountName, String mailMetadata, String mailContent) {

        validateIsNull(accountName, "accountName");
        validateIsEmpty(accountName, "accountName");
        validateIsBlank(accountName, "accountName");

        validateIsNull(mailMetadata, "mailMetadata");
        validateIsEmpty(mailMetadata, "mailMetadata");
        validateIsBlank(mailMetadata, "mailMetadata");

        validateIsNull(mailContent, "mailContent");
        validateIsEmpty(mailContent, "mailContent");
        validateIsBlank(mailContent, "mailContent");

        Account searchedAccount = getAndValidateAccountNotFoundException(accountName);

        AccountFolders searchedAccountFolders = getAccountFoldersFromAccount(searchedAccount);

        MailMetadataConverter mailMetadataConverter = new MailMetadataConverter();
        mailMetadataConverter.convertToMailMetadata(mailMetadata);

        String senderAccountName = getAccountNameByEmail(mailMetadataConverter.getSender());

        Mail toAdd = new Mail(new Account(mailMetadataConverter.getSender(), senderAccountName),
            mailMetadataConverter.getRecipients(), mailMetadataConverter.getSubject(), mailContent,
            mailMetadataConverter.getReceived());

        Set<Folder> folders = searchedAccountFolders.getDirectories().keySet();

        for (Folder folder : folders) {
            if (folder.getFolderName().equals("inbox")) {
                folder.addMail(toAdd);
                break;
            }
        }

        AccountRules searchedAccountRules = getAccountRulesFromAccount(searchedAccount);

        this.executeAccountRules(searchedAccountRules, searchedAccountFolders);

    }

    private String getAccountNameByEmail(String email) {

        for (Account currentAccount : this.accounts) {

            if (currentAccount.emailAddress().equals(email)) {
                return currentAccount.name();
            }
        }
        return null;
    }

    /**
     * Returns a collection of all mails in the provided folder
     *
     * @param account    name of the selected account
     * @param folderPath full path of the folder
     * @return collections of mails available in the folder
     * @throws IllegalArgumentException if any of the parameters is null, empty or blank
     * @throws AccountNotFoundException if the account does not exist
     * @throws FolderNotFoundException  if the folder does not exist
     */
    @Override
    public Collection<Mail> getMailsFromFolder(String account, String folderPath) {

        validateIsNull(account, "account");
        validateIsEmpty(account, "account");
        validateIsBlank(account, "account");

        validateIsNull(folderPath, "folderPath");
        validateIsEmpty(folderPath, "folderPath");
        validateIsBlank(folderPath, "folderPath");

        Account searchedAccount = getAndValidateAccountNotFoundException(account);

        AccountFolders searchedAccountFolders = getAccountFoldersFromAccount(searchedAccount);

        if (!isExistingPath(folderPath, searchedAccountFolders)) {
            throw new FolderNotFoundException("Folder does not exist");
        }

        Set<Mail> result = new HashSet<>();

        Set<Folder> folders = searchedAccountFolders.getDirectories().keySet();

        String[] followingFolders = folderPath.split("/");

        String lastDirectory = followingFolders[followingFolders.length - 1];
        Folder lastDirectoryFolder = new Folder(lastDirectory);

        for (Folder folder : folders) {

            if (folder.equals(lastDirectoryFolder)) {
                result.addAll(folder.getMails());
            }
        }

        return result;
    }

    /**
     * Sends an email. This stores the mail into the sender's "/sent" folder.
     * For each recipient in the recipients email list in the metadata, if an account with this email exists,
     * a {@code receiveMail()} for this account, mail metadata and mail content is called.
     * If an account with the specified email does not exist, it is ignored.
     *
     * @param accountName  name of the sender
     * @param mailMetadata metadata of the mail. "sender" field should be included automatically
     *                     if missing or not correctly set
     * @param mailContent  content of the mail
     * @throws IllegalArgumentException if any of the parameters is null, empty or blank
     */
    @Override
    public void sendMail(String accountName, String mailMetadata, String mailContent) {

        validateIsNull(accountName, "accountName");
        validateIsEmpty(accountName, "accountName");
        validateIsBlank(accountName, "accountName");

        validateIsNull(mailMetadata, "mailMetadata");
        validateIsEmpty(mailMetadata, "mailMetadata");
        validateIsBlank(mailMetadata, "mailMetadata");

        validateIsNull(mailContent, "mailContent");
        validateIsEmpty(mailContent, "mailContent");
        validateIsBlank(mailContent, "mailContent");

        Account searchedAccount = getAndValidateAccountNotFoundException(accountName);
        AccountFolders searchedAccountFolders = getAccountFoldersFromAccount(searchedAccount);

        Set<Folder> folders = searchedAccountFolders.getDirectories().keySet();

        MailMetadataConverter mailMetadataConverter = new MailMetadataConverter();
        mailMetadataConverter.convertToMailMetadata(mailMetadata);

        mailMetadataConverter.setSender(searchedAccount.emailAddress());

        Mail toAdd = new Mail(searchedAccount, mailMetadataConverter.getRecipients(),
            mailMetadataConverter.getSubject(), mailContent, mailMetadataConverter.getReceived());

        for (Folder folder : folders) {

            if (folder.equals(new Folder("sent"))) {
                folder.getMails().add(toAdd);
            }
        }


        for (String recipient : mailMetadataConverter.getRecipients()) {

            Account saveAccount = null;

            boolean isFound = false;
            for (Account currentAccount : this.accounts) {
                if (currentAccount.emailAddress().equals(recipient)) {
                    saveAccount = currentAccount;
                    isFound = true;
                    break;
                }
            }

            if (!isFound) {
                continue;
            }

            this.receiveMail(saveAccount.name(), mailMetadataConverter.toString(), mailContent);
        }

    }

    private void validateIsNull(String parameter, String nameOfParameter) {
        if (parameter == null) {
            throw new IllegalArgumentException(nameOfParameter + " is null");
        }
    }

    private void validateIsEmpty(String parameter, String nameOfParameter) {
        if (parameter.isEmpty()) {
            throw new IllegalArgumentException(nameOfParameter + " is empty");
        }
    }

    private void validateIsBlank(String parameter, String nameOfParameter) {
        if (parameter.isBlank()) {
            throw new IllegalArgumentException(nameOfParameter + " is blank");
        }
    }

    private void validateIsExistingAccount(String accountName) {

        if (this.accounts.contains(new Account("", accountName))) {
            throw new AccountAlreadyExistsException("There is already an account with that name");
        }
    }

    private void validateIsExistingAccount(Account account) {

        if (this.accounts.contains(account)) {
            throw new AccountAlreadyExistsException("There is already an account with that name");
        }
    }

    private Account getAndValidateAccountNotFoundException(String accountName) {

        Account searchedAccount = new Account("", accountName);

        if (this.accounts.contains(searchedAccount)) {
            for (Account currentAccount: this.accounts) {
                if (currentAccount.equals(searchedAccount)) {
                    return currentAccount;
                }
            }
        }

        throw new AccountNotFoundException("There is not such an account");
    }

    private void validatePathStartsFromRoot(String path) {

        if (!path.startsWith("/inbox/")) {
            throw new InvalidPathException("The path does not start with /inbox/");
        }
    }

    private void validateInvalidPathIntermediateFoldersMissing(boolean isFound, String missing) {

        if (!isFound) {
            throw new InvalidPathException("Intermediate folder" + missing + "is missing");
        }
    }

    private void validateAndProcessFolderCreation(Map<Folder, List<Folder>> directories, String[] followingFolders,
                                            AccountFolders accountFolders) {

        String beforeLastDirectory = followingFolders[followingFolders.length - 2];
        Folder beforeLastDirectoryFolder = new Folder(beforeLastDirectory);
        String lastDirectory = followingFolders[followingFolders.length - 1];
        Folder lastDirectoryFolder = new Folder(lastDirectory);

        if (directories.get(beforeLastDirectoryFolder).contains(lastDirectoryFolder)) {
            throw new FolderAlreadyExistsException("Folder with the same absolute path is already present for the " +
                "provided account");
        }

        accountFolders.addNewFolder(beforeLastDirectory, lastDirectory);
    }

    private void processPath(String path, AccountFolders accountFolders) {

        validatePathStartsFromRoot(path);

        Map<Folder, List<Folder>> directories = accountFolders.getDirectories();

        String[] followingFolders = path.split("/");

        for (int i = 1; i < followingFolders.length - 2; i++) {

            if (directories.containsKey(followingFolders[i])) {

                boolean isFound = false;

                for (Folder neighbour : directories.get(followingFolders[i])) {


                    if (neighbour.getFolderName().equals(followingFolders[i + 1])) {
                        isFound = true;
                    }
                }

                validateInvalidPathIntermediateFoldersMissing(isFound, followingFolders[i + 1]);
            }
        }

        validateAndProcessFolderCreation(directories, followingFolders, accountFolders);
    }

    private AccountFolders getAccountFoldersFromAccount(Account account) {

        for (AccountFolders currentAccountFolders : this.accountsFolders) {

            if (currentAccountFolders.getAccount().equals(account)) {
                return currentAccountFolders;
            }
        }

        AccountFolders newToAdd = new AccountFolders(account);
        this.accountsFolders.add(newToAdd);
        return newToAdd;
    }

    private AccountRules getAccountRulesFromAccount(Account account) {

        for (AccountRules currentAccountRules : this.accountRules) {

            if (currentAccountRules.getAccount().equals(account)) {
                return currentAccountRules;
            }
        }

        AccountRules newToAdd = new AccountRules(account);
        this.accountRules.add(newToAdd);
        return newToAdd;
    }

    public static void main(String[] args) {
        Outlook o = new Outlook();



        Account r = o.addNewAccount("rnd", "a@abv.bg");
        Account s = o.addNewAccount("sft", "b@abv.bg");

        o.createFolder("rnd", "/inbox/documents");

        o.createFolder("sft", "/inbox/documents");

        Mail a = new Mail(r, Set.of("b@abv.bg"), "Java", "Hello, Java!", LocalDateTime.now());

        String mailMetadata = "sender: a@abv.bg\n" +
            "     subject: Java\n" +
            "     recipients: b@abv.bg,\n" +
            "     received: 2022-12-08 14:14\n";

        o.sendMail("rnd", mailMetadata, "Hello, Java");
       // o.receiveMail("sft", mailMetadata, "Hello, Java!");

        String ruleDefinition = "subject-includes: Java\n" +
            "     recipients-includes: b@abv.bg\n" +
            "     from: a@abv.bg";

        o.addRule("sft", "/inbox/documents", ruleDefinition, 6);

        System.out.println(o.getMailsFromFolder("sft", "/inbox"));
        System.out.println(o.getMailsFromFolder("sft", "/inbox/documents"));



    }
}
