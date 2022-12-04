package bg.sofia.uni.fmi.mjt.mail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountFoldersTest {

    @Test
    void testEquals() {

        Outlook outlook = new Outlook();

        Account fmiAccount = new Account("fmi@uni-sofia.bg", "fmi");

        AccountFolders a = new AccountFolders(fmiAccount);
        a.addNewFolder("inbox", "document");

        AccountFolders b = new AccountFolders(fmiAccount);

        Assertions.assertEquals(a, b, "The two Account folders must be equal.");

    }

}
