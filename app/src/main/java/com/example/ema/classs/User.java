package com.example.ema.classs;

public class User {
    private String id;
    private String darga;
    private String name;
    private String jobTitle;
    private String email;

    public User() {

    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDarga() {
        return darga;
    }

    public void setDarga(String darga) {
        this.darga = darga;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
