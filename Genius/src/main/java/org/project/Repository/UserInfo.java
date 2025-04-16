//package org.project.Repository;
//
//import com.fasterxml.jackson.annotation.JsonProperty;
//
//public class UserInfo {
//
//    @JsonProperty ("name")
//    public String name;
//
//    @JsonProperty ("age")
//    public int age;
//
//    @JsonProperty ("username")
//    public String username;
//
//    @JsonProperty ("email")
//    public String email;
//
//    @JsonProperty("password")
//    private String password;
//
//    @JsonProperty ("accountType")
//    public String accountType;
//
//    public UserInfo() {};
//
//    public UserInfo(String name, int age, String username, String email, String password, String accountType) {
//        this.name = name;
//        this.age = age;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.accountType = accountType;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public void setAccountType(String accountType) {
//        this.accountType = accountType;
//    }
//
//    public String getAccountType() {
//        return accountType;
//    }
//
//    @Override
//    public String toString() {
//        return "Name: " + name + "\nAge: " + age + "\nUsername: " + username +
//                "\nEmail: " + email + "\nAccount Type: " + accountType + "\n------------------";
//    }
//
//    public boolean isArtist() {
//        return "Artist".equals(accountType);
//    }
//}

package org.project.Repository;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    @JsonProperty("name")
    private String name;

    @JsonProperty("age")
    private int age;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("accountType")
    private String accountType;

    @JsonProperty("verifyed")
    private boolean verified = false;

    public UserInfo() {}

    public UserInfo(String name, int age, String username, String email, String password, String accountType, boolean varifyed) {
        this.name = name;
        this.age = age;
        this.username = username;
        this.email = email;
        this.password = password;
        this.accountType = accountType;
        this.verified = false;

    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getAccountType() { return accountType; }

    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setAccountType(String accountType) { this.accountType = accountType; }


    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nAge: " + age + "\nUsername: " + username +
                "\nEmail: " + email + "\nAccount Type: " + accountType + "\n------------------";
    }

    public boolean isArtist() {
        return "Artist".equals(accountType);
    }
}
