package org.project.Accounts;

public interface User {
    public boolean validateEmail(String email);
    public int findValidPasswords(String password);
}
