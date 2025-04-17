package org.project.Accounts;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Accounts implements User {
    private String name;
    private int age;
    private String username;
    private String email;
    private String password;
public Accounts(String name, int age, String username, String email, String password) {
    this.name = name;
    this.age = age;
    this.username = username;
    this.email = email;
    this.password = password;
}
    public boolean validateEmail(String email)
    {
        String regex = "^[a-zA-Z0-9](?!.*\\.\\.)[a-zA-Z0-9._-]*[a-zA-Z0-9]@[a-zA-Z0-9]+([.-][a-zA-Z0-9]+)*\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public int findValidPasswords(String password) {
        String regex = "(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])^(?!.*\\s).{8,}$";
        Pattern pattern = Pattern.compile(regex);

        String[] words = password.split("\\s+");
        int validPasswordCount = 0;

        for (String word : words) {
            if (pattern.matcher(word).matches()) {
                validPasswordCount++;
            }
        }

        return validPasswordCount;
    }

    public String getUsername() {
    return username;
    }

    public String getEmail() {
    return email;
    }

    public String getPassword() {
    return password;
    }

    public int getAge() {
    return age;
    }

    public String getName() {
    return name;
    }


}
