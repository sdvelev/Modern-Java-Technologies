package bg.sofia.uni.fmi.mjt.mail;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountFoldersTest {

    @Test
    void testEqualsEqual() {

        Account fmiAccount = new Account("fmi@uni-sofia.bg", "fmi");

        AccountFolders a = new AccountFolders(fmiAccount);
        a.addNewFolder("inbox", "document");

        AccountFolders b = new AccountFolders(fmiAccount);

        Assertions.assertEquals(a, b, "The two Account folders are for same account.");

    }

    @Test
    void testEqualsNotEqual() {

        Account fmiAccount = new Account("fmi@uni-sofia.bg", "fmi");
        Account fhfAccount = new Account("fhf@uni-sofia,bg", "fhf");

        AccountFolders a = new AccountFolders(fmiAccount);
        a.addNewFolder("inbox", "document");

        AccountFolders b = new AccountFolders(fhfAccount);

        Assertions.assertNotEquals(a, b, "The two Account folders are not for the same account.");

    }

}
