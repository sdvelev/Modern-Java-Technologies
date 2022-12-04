package bg.sofia.uni.fmi.mjt.mail;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Folder {

    private String folderName;
    private Set<Mail> mails;

    public Folder(String folderName) {

        this.folderName = folderName;
        this.mails = new HashSet<>();

    }

    public String getFolderName() {

        return this.folderName;
    }

    public Set<Mail> getMails() {

        return this.mails;
    }

    public void addMail(Mail mail) {

        this.mails.add(mail);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Folder folder = (Folder) o;
        return folderName.equals(folder.folderName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(folderName);
    }

   /* @Override
    public String toString() {

        return "Folder{" +
            "folderName='" + folderName + '\'' +
            ", mails=" + mails +
            '}';
    }*/
}
