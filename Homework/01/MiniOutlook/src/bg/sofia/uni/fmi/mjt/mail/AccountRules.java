package bg.sofia.uni.fmi.mjt.mail;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AccountRules {

    private Account account;
    private Map<Integer, RuleDefinitionConverter> accountRules;

    public AccountRules(Account account) {

        this.account = account;
        this.accountRules = new TreeMap<>();

    }

    public Account getAccount() {

        return this.account;
    }

    public Map<Integer, RuleDefinitionConverter> getAccountRules() {

        return this.accountRules;
    }

    public void addAccountRule(RuleDefinitionConverter toAdd, Integer priority) {

        this.accountRules.put(priority, toAdd);
    }

    public void eraseAccountRule(String ruleDefinition, int priority) {

        RuleDefinitionConverter convert = new RuleDefinitionConverter();
        convert.convertToRuleDefinition(ruleDefinition);

        Set<Map.Entry<Integer, RuleDefinitionConverter>> searchedDirectoryAsSet = this.accountRules.entrySet();

        for (Map.Entry<Integer, RuleDefinitionConverter> currentEntry : searchedDirectoryAsSet) {

            if (currentEntry.getKey() == priority && currentEntry.getValue().equals(convert)) {

                this.eraseAccountRuleWithRuleDefinitionConverter(convert, priority);
            }
        }
    }

    public void eraseAccountRuleWithRuleDefinitionConverter(RuleDefinitionConverter toErase, Integer priority) {

        this.accountRules.remove(priority, toErase);
    }
}
