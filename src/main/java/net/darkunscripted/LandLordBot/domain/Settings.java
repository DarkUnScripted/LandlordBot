package net.darkunscripted.LandLordBot.domain;

import java.util.List;

public class Settings {
    private String prefix;
    private List<String> admins;
    private String fileDirectory;

    public Settings() {

    }

    public String getPrefix() {
        return prefix;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public String getFileDirectory() {
        return fileDirectory;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }

    public void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }
}
