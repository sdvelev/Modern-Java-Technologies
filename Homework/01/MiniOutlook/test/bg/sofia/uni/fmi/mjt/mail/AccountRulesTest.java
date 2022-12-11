package bg.sofia.uni.fmi.mjt.mail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountRulesTest {

    @Test
    void testEraseAccountRuleSuccessfully() {

        Outlook outlook = new Outlook();

        Account fmiAccount = outlook.addNewAccount("fmi", "fmi@uni-sofia.bg");

        outlook.createFolder("fmi", "/inbox/documents");
        outlook.createFolder("fmi", "/inbox/fmiDocuments");

        String ruleDefinition = "subject-includes: Halls" + System.lineSeparator() +
            "     recipients-includes: fhf@uni-sofia.bg, fzf@uni-sofia.bg," + System.lineSeparator() +
            "     subject-or-body-includes: Hall" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        String ruleDefinitionAnother = "subject-includes: Available" + System.lineSeparator() +
            "     recipients-includes: fhf@uni-sofia.bg, fzf@uni-sofia.bg, fh@uni-sofia.bg" + System.lineSeparator() +
            "     subject-or-body-includes: Hall" + System.lineSeparator() +
            "     from: fmi@uni-sofia.bg" + System.lineSeparator();

        outlook.addRule("fmi", "/inbox/documents", ruleDefinition, 1);

        outlook.addRule("fmi", "/inbox/fmiDocuments", ruleDefinitionAnother, 2);

        AccountRules accountRules = outlook.getAccountRulesFromAccount(fmiAccount);

        Assertions.assertTrue(accountRules.getAccountRules().containsKey(2));

        accountRules.eraseAccountRule(ruleDefinitionAnother, 2);

        Assertions.assertFalse(accountRules.getAccountRules().containsKey(2));
    }

}
