package bg.sofia.uni.fmi.mjt.mail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AccountFolders {

    private Account account;
    private Map<Folder, List<Folder>> directories;

    public AccountFolders(Account account) {

        this.account = account;
        this.directories = new HashMap<>();
        this.directories.put(new Folder("inbox"), new ArrayList<>());
        this.directories.put(new Folder("sent"), new ArrayList<>());

    }

    public Account getAccount() {

        return this.account;
    }

    public Map<Folder, List<Folder>>  getDirectories() {

        return this.directories;
    }

    public void addNewFolder(String beforeLastFolder, String lastFolder) {

        Folder folderToAdd = new Folder(lastFolder);
        Folder folderBeforeToAdd = new Folder(beforeLastFolder);

        this.directories.get(folderBeforeToAdd).add(folderToAdd);
        this.directories.put(folderToAdd, new ArrayList<>());
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountFolders that = (AccountFolders) o;
        return account.equals(that.account);
    }

    @Override
    public int hashCode() {

        return Objects.hash(account);
    }
}
